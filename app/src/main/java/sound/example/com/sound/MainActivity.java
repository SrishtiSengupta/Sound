package sound.example.com.sound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.File;

import sound.example.com.sound.GoogleDrive.CreateFileInFolderActivity;
import sound.example.com.sound.GoogleDrive.CreateFolderActivity;


public class MainActivity extends Activity {

//    // Dropbox app specific settings
//    private static final String APP_KEY = "xxxxxxxxxxxxxxx";
//    private static final String APP_SECRET = "xxxxxxxxxxxxxxx";
//
//    private static final String ACCOUNT_PREFS_NAME = "prefs";
//    private static final String ACCESS_KEY_NAME = "ACCESS_KEY";
//    private static final String ACCESS_SECRET_NAME = "ACCESS_SECRET";
//
//    private static final boolean USE_OAUTH1 = false;
//
//    private Button mSubmit;
//    private boolean mLoggedIn;
//
//    public DropboxAPI<AndroidAuthSession> mDBApi;
//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // We create a new AuthSession so that we can use the Dropbox API.
//        AndroidAuthSession session = buildSession();
//        mDBApi= new DropboxAPI<AndroidAuthSession>(session);
//
////        mDBApi.getSession().startOAuth2Authentication(MainActivity.this);
//
//        mSubmit = (Button)findViewById(R.id.button_dropbox);
//
//        mSubmit.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // This logs you out if you're logged in, or vice versa
//                if (mLoggedIn) {
//                    logOut();
//                }
//                else {
//                    // Start the remote authentication
//                    if (USE_OAUTH1) {
//                        mDBApi.getSession().startAuthentication(MainActivity.this);
//                    } else {
//                        mDBApi.getSession().startOAuth2Authentication(MainActivity.this);
//                    }
//                }
//            }
//        });

        //creates 'Recordings' folder in external storage for Parse
        String folder = "Recordings";
        File f = new File(Environment.getExternalStorageDirectory(),
                folder);
        if (!f.exists()) {
            f.mkdirs();
        }
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

    // Method to create Google Drive Folder
    public void makeFolder(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateFolderActivity.class);
        startActivity(intent);
    }

    // Method to create Google Drive Folder
    public void makeFile(View view) {
        Intent intent = new Intent(getApplicationContext(), CreateFileInFolderActivity.class);
        startActivity(intent);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        AndroidAuthSession session = mDBApi.getSession();
//
//        // The next part must be inserted in the onResume() method of the
//        // activity from which session.startAuthentication() was called, so
//        // that Dropbox authentication completes properly.
//        if (session.authenticationSuccessful()) {
//            try {
//                // Mandatory call to complete the auth
//                session.finishAuthentication();
//
//                // Store it locally in our app for later use
//                storeAuth(session);
//                setLoggedIn(true);
//
//            } catch (IllegalStateException e) {
//                Toast.makeText(MainActivity.this, "Couldn't authenticate with Dropbox!", Toast.LENGTH_SHORT).show();
//                Log.i("TAG", "Error authenticating", e);
//
//            }
//        }
//    }


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

//    /**
//     * Convenience function to change UI state based on being logged in
//     */
//    private void setLoggedIn(boolean loggedIn) {
//        mLoggedIn = loggedIn;
//        if (loggedIn) {
//            mSubmit.setText("Unlink from Dropbox");
//        } else {
//            mSubmit.setText("Link with Dropbox");
//        }
//    }
//
//    private void logOut() {
//        // Remove credentials from the session
//        mDBApi.getSession().unlink();
//
//        // Clear our stored keys
//        clearKeys();
//
//        // Change UI state to display logged out version
//        setLoggedIn(false);
//    }
//
//
//    /**
//     * Keeps the access keys returned from Trusted Authenticator in a local
//     * store, rather than storing user name & password, and re-authenticating each
//     * time (which is not to be done, ever).
//     */
//    private void loadAuth(AndroidAuthSession session) {
//        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
//        String key = prefs.getString(ACCESS_KEY_NAME, null);
//        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
//        if (key == null || secret == null || key.length() == 0 || secret.length() == 0) return;
//
//        if (key.equals("oauth2:")) {
//            // If the key is set to "oauth2:", then we can assume the token is for OAuth 2.
//            session.setOAuth2AccessToken(secret);
//        } else {
//            // Still support using old OAuth 1 tokens.
//            session.setAccessTokenPair(new AccessTokenPair(key, secret));
//        }
//    }
//
//    /**
//     * Keeps the access keys returned from Trusted Authenticator in a local
//     * store, rather than storing user name & password, and re-authenticating each
//     * time (which is not to be done, ever).
//     */
//    private void storeAuth(AndroidAuthSession session) {
//        // Store the OAuth 2 access token, if there is one.
//        String oauth2AccessToken = session.getOAuth2AccessToken();
//        if (oauth2AccessToken != null) {
//            SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
//            SharedPreferences.Editor edit = prefs.edit();
//            edit.putString(ACCESS_KEY_NAME, "oauth2:");
//            edit.putString(ACCESS_SECRET_NAME, oauth2AccessToken);
//            edit.commit();
//            return;
//        }
//        // Store the OAuth 1 access token, if there is one.  This is only necessary if
//        // you're still using OAuth 1.
//        AccessTokenPair oauth1AccessToken = session.getAccessTokenPair();
//        if (oauth1AccessToken != null) {
//            SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
//            SharedPreferences.Editor edit = prefs.edit();
//            edit.putString(ACCESS_KEY_NAME, oauth1AccessToken.key);
//            edit.putString(ACCESS_SECRET_NAME, oauth1AccessToken.secret);
//            edit.commit();
//            return;
//        }
//    }
//
//    private void clearKeys() {
//        SharedPreferences prefs = getSharedPreferences(ACCOUNT_PREFS_NAME, 0);
//        SharedPreferences.Editor edit = prefs.edit();
//        edit.clear();
//        edit.commit();
//    }
//
//    // initialization function
//    private AndroidAuthSession buildSession(){
//        AppKeyPair appKeys = new AppKeyPair(APP_KEY, APP_SECRET);
//        AndroidAuthSession session = new AndroidAuthSession(appKeys);
//        loadAuth(session);
//
//        return session;
//    }

}

