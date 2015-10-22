package com.geniusmart.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.geniusmart.R;

public class CircleView extends View{

    private int mDefaultColor = Color.RED;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final String TAG = "CircleView";

    private int mLastX;
    private int mLastY;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs,R.attr.circleViewStyle);
        init();
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleView);
        mDefaultColor = typedArray.getColor(R.styleable.CircleView_circle_color,mDefaultColor);
        typedArray.recycle();
        init();
    }

    private void init(){
        mPaint.setColor(mDefaultColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG,"-------------onDraw--------------");
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        width = width - paddingLeft - paddingRight;
        height = height - paddingBottom - paddingTop;
        int radius = Math.min(width,height)/2;
        canvas.drawCircle(paddingLeft + width / 2, paddingTop+ height / 2, radius, mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG,"-------------onMeasure--------------");
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        //支持wrap_content模式
        if(widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(200, 200);
        }else if (widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(200, heightSpecSize);
        }else if(heightMeasureSpec == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize,200);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG,"lastX-->"+mLastX);
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                Log.e(TAG,"deltaX-->"+deltaX);
                this.setTranslationX(getTranslationX()+deltaX);
                this.setTranslationY(getTranslationY()+deltaY);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        mLastX = x;
        mLastY = y;
        return true;
    }
}
