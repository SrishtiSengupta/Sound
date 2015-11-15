package sound.example.com.sound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;

import sound.example.com.sound.GoogleDrive.CreateFolderActivity;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //creates 'Recordings' folder in external storage for Parse
        String folder = "Recordings";
        File f = new File(Environment.getExternalStorageDirectory(),
                folder);
        if (!f.exists()) {
            f.mkdirs();
        }

        //Parse Test
        ParseUser.enableAutomaticUser();
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

        Toast.makeText(this, testObject.getString("foo"), Toast.LENGTH_SHORT)
                .show();
    }

    // Method to start the service
    public void startService(View view) {
        startService(new Intent(getBaseContext(), AmbientSoundService.class));
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

}
