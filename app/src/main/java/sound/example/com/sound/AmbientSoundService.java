package sound.example.com.sound;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AmbientSoundService extends Service {

    private MediaRecorder mRecorder = null;
    private MediaRecorder storeRecorder = null;
    private double ambient_sound;
    private List<Double> buffer = new ArrayList<Double>();
    private double max_ambient_sound;

    private final Timer t = new Timer();

    public AmbientSoundService() {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Continues running until it is stopped

        Toast.makeText(this, "Ambient Sound Service Started", Toast.LENGTH_LONG).show();
        Log.d("Ambient Sound Service", "STARTED");

        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                /*AMBIENT SOUND*/
                stopRecording();
                start();
                max_ambient_sound = getAmplitude();

                if (max_ambient_sound > 3000) {
                    stop();
                    storeRecordings();
                }

                Log.d("Ambient Sound: ", String.valueOf(max_ambient_sound));

            }
        }, 0, 5000);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Ambient Sound Service", "STOPPED");
        Toast.makeText(this, "Ambient Sound Service Destroyed!", Toast.LENGTH_LONG).show();
    }

    //starts recording for ambient sound calculation
    public void start() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");

            mRecorder.setAudioSamplingRate(480000);

            try {
                mRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();
        }
    }

    //stops recording for ambient sound
    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    //starts recording for storing sound samples
    public void startRecording() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = dateFormat.format(new Date());

        String outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording_" + timestamp + ".3gp";

        if (storeRecorder == null) {
            storeRecorder = new MediaRecorder();
            storeRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            storeRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            storeRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            storeRecorder.setOutputFile(outputFile);

            try {
                storeRecorder.prepare();
                storeRecorder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //stops recording for storing samples
    public void stopRecording() {
        if (storeRecorder != null) {
            storeRecorder.stop();
            storeRecorder.release();
            storeRecorder = null;
        }
    }

    //saves recordings to internal storage
    public void storeRecordings() {
        startRecording();
    }

    public double getAmplitude() {
        if (mRecorder != null) {
            ambient_sound = mRecorder.getMaxAmplitude();
            buffer.add(ambient_sound);
        } else {
            ambient_sound = 0.0;
            buffer.add(ambient_sound);
        }
        Log.d("BUffer size", String.valueOf(buffer.size()));
        buffer.clear();

        return ambient_sound;

    }

}
