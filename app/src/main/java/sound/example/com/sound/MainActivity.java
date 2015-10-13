package sound.example.com.sound;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends Activity {

    private MediaRecorder mRecorder = null;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startRecording();
                Log.d("RECORDING", " started");
                handler.postDelayed(this, 5000);
            }
        };

        Runnable runnable_ = new Runnable() {
            @Override
            public void run() {
                stopRecording();
                Log.d("RECORDING", " stopped");
                handler.postDelayed(this, 10000);
            }
        };

        try {
            handler.postDelayed(runnable, 5000);
            handler.postDelayed(runnable_, 10000);
        }catch (Exception e){
            Log.d("RECORDING", " Corrupted!");
        }
    }

    public void startRecording(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date());

        String outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording_" + timestamp + ".3gp";

        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(outputFile);

            try {
                mRecorder.prepare();
                mRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopRecording(){
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
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
}
