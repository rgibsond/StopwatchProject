package com.example.rgdrys13.stopwatch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //instance data
    TextView count;
    Button start, reset;
    Timer t;
    CounterTask counterTask;

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
                t.scheduleAtFixedRate(counterTask, 0, 1000);
            }
        });
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
