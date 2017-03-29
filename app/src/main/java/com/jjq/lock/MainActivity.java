package com.jjq.lock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private LockView lockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lockView = (LockView) findViewById(R.id.lockview);

        lockView.setOnLockViewClickListener(new LockView.LockViewClickListener() {
            @Override
            public void clickRight() {
                Toast.makeText(MainActivity.this, "click right", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clickLeft() {
                Toast.makeText(MainActivity.this, "click left", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clickHome() {
                Toast.makeText(MainActivity.this, "click home", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
