package com.john.mydemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.john.mydemo.R;

public class MainActivity extends Activity  implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_sqlite).setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.btn_sqlite:
                intent = new Intent(this, SQLiteActiviy.class);
                startActivity(intent);
                break;
        }
    }
}
