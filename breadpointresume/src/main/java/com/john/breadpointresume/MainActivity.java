package com.john.breadpointresume;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends Activity implements View.OnClickListener{

    private ProgressBar progressBar;
    private Button btnStart;
    private Button btnPause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnStart = (Button) findViewById(R.id.start);
        btnPause = (Button) findViewById(R.id.pause);
        btnStart.setOnClickListener(this);
        btnPause.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
