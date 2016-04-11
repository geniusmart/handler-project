package com.geniusmart.future;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.geniusmart.R;

public class FutureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);
    }

    public void execute(View view) {
        new SuperAsyncTask().execute();
    }
}
