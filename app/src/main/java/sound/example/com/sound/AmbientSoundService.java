package sound.example.com.sound;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AmbientSoundService extends Service {

    private MediaRecorder mRecorder = null;
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
                for (int j = 0; j < 1000; j++) {
                    start();
                    max_ambient_sound = getAmplitude();
                }
                stop();
                Log.d("Ambient Sound: ", String.valueOf(max_ambient_sound));
                Log.d("BUffer size", String.valueOf(buffer.size()));

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

    public void start() {
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mRecorder.start();
        }
    }

    public void stop() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double getAmplitude() {
        if (mRecorder != null) {
            ambient_sound = mRecorder.getMaxAmplitude();
            buffer.add(ambient_sound);
        } else {
            ambient_sound = 0.0;
            buffer.add(ambient_sound);
        }
        max_ambient_sound = calculateAverage(buffer);
        return max_ambient_sound;
    }

    private double calculateAverage(List<Double> buffer) {
        Double sum = 0.0;
        if (!buffer.isEmpty()) {
            for (Double i : buffer) {
                sum += i;
            }
            double temp = sum.doubleValue() / buffer.size();
            return temp;
        }
        return sum;
    }
}
