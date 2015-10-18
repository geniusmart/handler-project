package com.geniusmart.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.geniusmart.R;

public class CircleActivity extends Activity {

    public static final String TAG = "CircleActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        final CircleView circleView = (CircleView) findViewById(R.id.circle_view);
        //TODO 问题：measure过程和生命周期不是同步执行的？那么分别是在哪个线程中执行的？？
        Log.e(TAG, circleView.getMeasuredWidth() + "");
        Log.e(TAG, circleView.getMeasuredHeight() + "");
        Log.e(TAG, circleView.getX() + "");
        Log.e(TAG, circleView.getY() + "");
        circleView.post(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, circleView.getMeasuredWidth() + "r");
                Log.e(TAG, circleView.getMeasuredHeight() + "r");
                Log.e(TAG,circleView.getX()+"r");
                Log.e(TAG,circleView.getY()+"r");
            }
        });

    }
}
