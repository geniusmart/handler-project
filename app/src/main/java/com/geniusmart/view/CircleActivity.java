package com.geniusmart.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.VelocityTrackerCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.TextView;

import com.geniusmart.R;

public class CircleActivity extends Activity implements View.OnClickListener{

    public static final String TAG = "CircleActivity";

    CircleView circleView;
    TextView textView;
    private int num = 1;
    VelocityTracker velocityTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle);
        circleView = (CircleView) findViewById(R.id.circle_view);
        textView = (TextView) findViewById(R.id.textview);
        Button translationBtn = (Button)findViewById(R.id.translation);
        Button printBtn = (Button)findViewById(R.id.print);
        Button scrollToBtn = (Button)findViewById(R.id.scroll_to);
        Button scrollByBtn = (Button) findViewById(R.id.scroll_by);

        translationBtn.setOnClickListener(this);
        printBtn.setOnClickListener(this);
        scrollToBtn.setOnClickListener(this);
        scrollByBtn.setOnClickListener(this);

        int scaledTouchSlop = ViewConfiguration.get(this).getScaledTouchSlop();
        Log.e(TAG,"scaledTouchSlop=" +scaledTouchSlop);

        //TODO 问题：measure过程和生命周期不是同步执行的？那么分别是在哪个线程中执行的？？
        circleView.post(new Runnable() {
            @Override
            public void run() {

            }
        });

        velocityTracker = VelocityTracker.obtain();
    }

    private void print(){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(circleView.getMeasuredWidth() + "->getMeasuredWidth").append("\n")
                .append(circleView.getMeasuredHeight() + "->getMeasuredHeight").append("\n")
                .append(circleView.getX() + "->getX").append("\n")
                .append(circleView.getY() + "->getY").append("\n")
                .append(circleView.getLeft() + "->getLeft").append("\n")
                .append(circleView.getScrollX() + "->getScrollX").append("\n")
                .append(circleView.getTranslationX() + "->getTranslationX").append("\n")
                .append(circleView.getTop() + "->getTop");
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
            case R.id.scroll_to:
                circleView.scrollTo(50 * num, 0);
                num = num * -1;
                break;
            case R.id.scroll_by:
                circleView.scrollBy(50 * num, 0);
                num = num * -1;
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.e(TAG,"x="+event.getX()+",y="+event.getY());
        float begin = 0;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                begin = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float result = event.getX() - begin;
                Log.e(TAG,"x轴滑动距离为："+result);
                break;
        }
        velocityTracker.addMovement(event);
        //计算每1000ms的速度
        velocityTracker.computeCurrentVelocity(1000);
        float xVelocity = velocityTracker.getXVelocity();
        float yVelocity = velocityTracker.getYVelocity();
        Log.e(TAG,"速度="+xVelocity+","+yVelocity);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        velocityTracker.clear();
        velocityTracker.recycle();
        super.onDestroy();
    }
}

/**
 1.相对于父容器的值有：
   x，y，translationX,translationY,Left,Right,Top,Bottom
   x = left + translationX;
   y = top + ttanslationY;

   (translationX,translationY)为相对于父容器的偏移量
   (x,y)为相对于父容器的坐标

 */

/**
   scrollTo:相对于(0,0)
   scrollBy:相对于当前位置
   假设从（0，0）互动到（100，100），则scrollX = （0，0）-（100，100）=（-100，-100）
 */

/**
 滑动类型:
 1.View本身的scrollTo和scrollBy
 2.动画：transationX
 3.改变View的LayoutParams
*/
