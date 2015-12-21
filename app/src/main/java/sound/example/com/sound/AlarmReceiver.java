package sound.example.com.sound;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.FileNotFoundException;

import static sound.example.com.sound.MainActivity.sync;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            sync();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();
        Log.d("DROPBOX", "Syncing");
    }
}
