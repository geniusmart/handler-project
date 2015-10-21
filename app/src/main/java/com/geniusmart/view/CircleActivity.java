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

        circleView.scrollTo(50,50);

        //TODO 问题：measure过程和生命周期不是同步执行的？那么分别是在哪个线程中执行的？？
        circleView.post(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG, circleView.getMeasuredWidth() + "->getMeasuredWidth");
                Log.e(TAG, circleView.getMeasuredHeight() + "->getMeasuredHeight");
                Log.e(TAG,circleView.getX()+"->getX");
                Log.e(TAG,circleView.getY()+"->getY");
                Log.e(TAG,circleView.getLeft()+"->getLeft");
                Log.e(TAG, circleView.getScrollX() + "->getScrollX");
                Log.e(TAG,circleView.getTranslationX()+"->getTranslationX");
            }
        });

    }
}
