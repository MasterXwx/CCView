package com.weex.wdialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.weex.wdialog_lib.WaveView;

public class MainActivity extends AppCompatActivity {

    WaveView waveView;
    Button add;
    private int offset = 10;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        waveView = findViewById(R.id.wave);
        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waveView.startAnim(true);
            }
        });
    }
}
