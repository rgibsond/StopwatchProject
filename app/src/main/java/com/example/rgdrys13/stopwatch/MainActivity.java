package com.example.rgdrys13.stopwatch;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static final String COUNT = "COUNT";
    public static final String BUTTON_ENABLED = "BUTTON_ENABLED";
    //instance data
    TextView count;
    Button start, reset;
    Timer t;
    CounterTask counterTask;
    SoundPool sp;
    int bloop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.count = (TextView) findViewById(R.id.counter_id);
        this.start = (Button) findViewById(R.id.start);

        this.t = new Timer();
        this.counterTask = new CounterTask(0);

        this.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.start.setEnabled(false);
                t.scheduleAtFixedRate(counterTask, 0, 1000);
            }
        });

        // set a listener
        this.count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animator anim = AnimatorInflater.loadAnimator(MainActivity.this, R.animator.animate_count);

                anim.setTarget(MainActivity.this.count);
                anim.start();
                sp.play(bloop, 1f, 1f, 1, 0, 1);
            }
        });

        sp = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        bloop = sp.load(this, R.raw.bloop, 1);
    }

    @Override
    protected void onStart() {
        super.onStart();

        long c= getPreferences(MODE_PRIVATE).getLong(COUNT, 0);
        boolean buttonEnabled= getPreferences(MODE_PRIVATE).getBoolean(BUTTON_ENABLED, true);
        start.setEnabled(buttonEnabled);

        //only do this
        if (!buttonEnabled) {
            counterTask.count=c;

            t.scheduleAtFixedRate(counterTask, 0, 1000);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // save the counter
        getPreferences(MODE_PRIVATE).edit().putLong(COUNT, counterTask.count).apply();
        //save the button state
        getPreferences(MODE_PRIVATE).edit().putBoolean(BUTTON_ENABLED, start.isEnabled()).apply();

    }

    class CounterTask extends TimerTask {

        //instance data
        long count;
        //constructor
        public CounterTask(long i){
            this.count = i;
        }

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.this.count.setText(Long.toString(count++));
                }
            });
        }
    }
}
