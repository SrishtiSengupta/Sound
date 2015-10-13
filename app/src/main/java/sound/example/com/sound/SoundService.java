package sound.example.com.sound;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SoundService extends Service {

    private MediaRecorder mRecorder = null;
    private Handler handler;

    public SoundService() {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Continues running until it is stopped

        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

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

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
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

    public void stopRecording() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

}
