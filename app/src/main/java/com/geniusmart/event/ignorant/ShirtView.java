package com.geniusmart.event.ignorant;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ShirtView extends View {

    public static final String TAG = "ShirtView";

    public ShirtView(Context context) {
        super(context);
    }

    public ShirtView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShirtView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, TAG + "--onTouchEvent--ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, TAG + "--onTouchEvent--ACTION_UP");
                break;
        }
        return super.onTouchEvent(event);
    }
}
