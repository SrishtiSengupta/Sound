package sound.example.com.sound;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.exception.DropboxUnlinkedException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class MainActivity extends Activity {

    // Dropbox app specific settings
    private static final String APP_KEY = "xxxxxxxxxx";
    private static final String APP_SECRET = "xxxxxxxxxxxx";

    private static final String ACCOUNT_PREFS_NAME = "prefs";
    private static final String ACCESS_KEY_NAME = "ACCESS_KEY";
    private static final String ACCESS_SECRET_NAME = "ACCESS_SECRET";

    private static final boolean USE_OAUTH1 = false;
    private boolean tokenExists = false;

    private Button mSubmit;
    private Button logout;

    public DropboxAPI<AndroidAuthSession> mDBApi;

    private Button sync;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // We create a new AuthSession so that we can use the Dropbox API.
        final AndroidAuthSession session = buildSession();

        mDBApi = new DropboxAPI<AndroidAuthSession>(session);
        mSubmit = (Button) findViewById(R.id.button_dropbox);

        //check if token already exists and use that instead of authenticating again (for OAuth2)
        SharedPreferences store = getSharedPreferences(ACCOUNT_PREFS_NAME, Context.MODE_PRIVATE);
        String access_token = store.getString(ACCESS_SECRET_NAME, "missing");

        if (!access_token.equals("missing")) {
            Log.d("Access Token", access_token);
            tokenExists = true;
            session.setOAuth2AccessToken(access_token);
        } else {
            Toast.makeText(MainActivity.this, "Link to Dropbox!", Toast.LENGTH_SHORT).show();
        }

        mSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Start the remote authentication
                if (tokenExists) {
                    Toast.makeText(MainActivity.this, "Dropbox already linked!", Toast.LENGTH_SHORT).show();
                } else {
                    if (USE_OAUTH1) {
                        mDBApi.getSession().startAuthentication(MainActivity.this);
                    } else {
                        mDBApi.getSession().startOAuth2Authentication(MainActivity.this);
                    }
                }
            }
        });

        //creates 'Recordings' folder in external storage
        String folder = "Recordings";
        File f = new File(Environment.getExternalStorageDirectory(),
                folder);
        if (!f.exists()) {
            f.mkdirs();
        }

        sync = (Button) findViewById(R.id.sync_button);
        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "syncing", Toast.LENGTH_SHORT).show();
                try {
                    sync();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        logout = (Button) findViewById(R.id.button_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
                tokenExists = false;
                Toast.makeText(MainActivity.this, "Logged Out!", Toast.LENGTH_SHORT).show();
            }
        });

//
//        //Parse Test
//        ParseUser.enableAutomaticUser();
//        ParseObject testObject = new ParseObject("TestObject");
//        testObject.put("foo", "bar");
//        testObject.saveInBackground();
//
//        Toast.makeText(this, testObject.getString("foo"), Toast.LENGTH_SHORT)
//                .show();
    }

    // Method to start the service
    public void startService(View view) {
        Intent intent = new Intent(getBaseContext(), AmbientSoundService.class);
        startService(intent);
    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), AmbientSoundService.class));
    }

    // Method for syncing files to Dropbox
    public void sync() throws FileNotFoundException {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                String file_path = Environment.getExternalStorageDirectory() + "/Recordings";
//                File tmpFile = new File(file_path, "test.3gp");
                File tmpFile = new File(file_path);
                File[] files = tmpFile.listFiles();
                int numberOfFiles = files.length;
                Log.d("number of files", String.valueOf(numberOfFiles));

                for (int i = 0; i < numberOfFiles; i++) {
                    Log.d("Filename", String.valueOf(files[i]));
                    Log.d("Filename", String.valueOf(files[i]).split("/")[5]);
                }

                for (int i = 0; i < numberOfFiles; i++) {
                    File uploadFile = new File(file_path, String.valueOf(files[i]).split("/")[5]);

                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(uploadFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    try {
                        DropboxAPI.Entry newEntry = mDBApi.putFileOverwrite(String.valueOf(files[i]).split("/")[5], fis, uploadFile.length(), null);
                    } catch (DropboxUnlinkedException e) {
                        Log.e("DbExampleLog", "User has unlinked.");
                    } catch (DropboxException e) {
                        Log.e("DbExampleLog", "Something went wrong while uploading.");
                    }

                }
            }
        });
    }

//    // Method to create Google Drive Folder
//    public void makeFolder(View view) {
//        Intent intent = new Intent(getApplicationContext(), CreateFolderActivity.class);
//        startActivity(intent);
//    }
//
//    // Method to create Google Drive Folder
//    public void makeFile(View view) {
//        Intent intent = new Intent(getApplicationContext(), CreateFileInFolderActivity.class);
//        startActivity(intent);
//    }

    @Override
    protected void onResume() {
        super.onResume();
        AndroidAuthSession session = mDBApi.getSession();

        if (session.authenticationSuccessful()) {
            try {
                // Mandatory call to complete the auth
                session.finishAuthentication();

                // Store it locally in our app for later use
                storeAuth(session);

            } catch (IllegalStateException e) {
                Toast.makeText(MainActivity.this, "Couldn't authenticate with Dropbox!", Toast.LENGTH_SHORT).show();
                Log.i("TAG", "Error authenticating", e);

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void logOut() {
        // Remove credentials from the session
        mDBApi.getSession().unlink();

        // Clear our stored keys
        clearKeys();

    }

    private void loadAuth(AndroidAuthSession session) {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        String key = prefs.getString(ACCESS_KEY_NAME, null);
        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
        if (key == null || secret == null || key.length() == 0 || secret.length() == 0) return;

        if (key.equals("oauth2:")) {
            // If the key is set to "oauth2:", then we can assume the token is for OAuth 2.
            session.setOAuth2AccessToken(secret);
        } else {
            // Still support using old OAuth 1 tokens.
            session.setAccessTokenPair(new AccessTokenPair(key, secret));
        }
    }

    /**
     * Keeps the access keys returned from Trusted Authenticator in a local
     * store, rather than storing user name & password, and re-authenticating each
     * time (which is not to be done, ever).
     */
    private void storeAuth(AndroidAuthSession session) {
        Log.d("storeAuth", "CALLED");

        // Store the OAuth 2 access token, if there is one.
        String oauth2AccessToken = session.getOAuth2AccessToken();
        if (oauth2AccessToken != null) {
            SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(ACCESS_KEY_NAME, "oauth2:");
            edit.putString(ACCESS_SECRET_NAME, oauth2AccessToken);
            edit.commit();
            return;
        }
        // Store the OAuth 1 access token, if there is one.  This is only necessary if
        // you're still using OAuth 1.
        AccessTokenPair oauth1AccessToken = session.getAccessTokenPair();
        if (oauth1AccessToken != null) {
            SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString(ACCESS_KEY_NAME, oauth1AccessToken.key);
            edit.putString(ACCESS_SECRET_NAME, oauth1AccessToken.secret);
            edit.commit();
            return;
        }
    }

    private void clearKeys() {
        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
        SharedPreferences.Editor edit = prefs.edit();
        edit.clear();
        edit.commit();
    }

    // initialization function
    private AndroidAuthSession buildSession() {
        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
        AndroidAuthSession session = new AndroidAuthSession(appKeys);
        loadAuth(session);

        return session;
    }

}

