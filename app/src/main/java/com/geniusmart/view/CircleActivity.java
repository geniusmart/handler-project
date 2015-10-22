package com.geniusmart.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.geniusmart.R;

public class CircleActivity extends Activity implements View.OnClickListener{

    public static final String TAG = "CircleActivity";

    CircleView circleView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        circleView = (CircleView) findViewById(R.id.circle_view);

        textView = (TextView) findViewById(R.id.textview);
        Button translationBtn = (Button)findViewById(R.id.translation);
        Button printBtn = (Button)findViewById(R.id.print);
        Button scrollBtn = (Button)findViewById(R.id.scroll);

        translationBtn.setOnClickListener(this);
        printBtn.setOnClickListener(this);
        scrollBtn.setOnClickListener(this);

        //TODO 问题：measure过程和生命周期不是同步执行的？那么分别是在哪个线程中执行的？？
        circleView.post(new Runnable() {
            @Override
            public void run() {

            }
        });

    }

    private void print(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(circleView.getMeasuredWidth() + "->getMeasuredWidth").append("\n")
                .append(circleView.getMeasuredHeight() + "->getMeasuredHeight").append("\n")
                .append(circleView.getX() + "->getX").append("\n")
                .append(circleView.getY() + "->getY").append("\n")
                .append(circleView.getLeft() + "->getLeft").append("\n")
                .append(circleView.getScrollX() + "->getScrollX").append("\n")
                .append(circleView.getTranslationX() + "->getTranslationX");
        textView.setText(stringBuffer.toString());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.translation:
                ObjectAnimator.ofFloat(circleView, "translationX", -30f).setDuration(1000).start();
                break;
            case R.id.print:
                print();
                break;
            case R.id.scroll:
                circleView.scrollTo(50, 50);
                break;
        }
    }
}
