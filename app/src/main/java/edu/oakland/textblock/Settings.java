package edu.oakland.textblock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.util.Log;
import android.widget.TextView;

public class Settings extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";
    private SeekBar seekBar1;
    private SeekBar seekBar2;
    private TextView Text_Timer;
    private TextView Text_MPH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Text_Timer=(TextView) findViewById(R.id.text1);
        Text_MPH=(TextView) findViewById(R.id.text2);
        seekBar1=(SeekBar) findViewById(R.id.timer);
        seekBar2=(SeekBar) findViewById(R.id.mph);

        //初始化
        seekBar1.setMax(5);
        seekBar1.setProgress(2);
        seekBar1.setOnSeekBarChangeListener(seekBar1Listener);
        Text_Timer.setText("TIMER: " + seekBar1.getProgress());
        seekBar2.setMax(90);
        seekBar2.setProgress(35);
        seekBar2.setOnSeekBarChangeListener(seekBar2Listener);
        Text_MPH.setText("MPH: " + seekBar2.getProgress());
    }

    private OnSeekBarChangeListener seekBar1Listener = new OnSeekBarChangeListener(){

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress,
                                      boolean fromUser) {
            Log.i(TAG,"onProgressChanged");
            Text_Timer.setText("TIMER: " + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Log.i(TAG,"onStartTrackingTouch");
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.i(TAG,"onStopTrackingTouch");
        }
    };

    private OnSeekBarChangeListener seekBar2Listener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            Log.i(TAG,"onProgressChanged");
            Text_MPH.setText("MPH: " + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Log.i(TAG,"onStartTrackingTouch");
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Log.i(TAG,"onStopTrackingTouch");
        }
    };
}
