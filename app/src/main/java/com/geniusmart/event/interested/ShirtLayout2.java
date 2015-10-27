package com.geniusmart.event.interested;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class ShirtLayout2 extends FrameLayout {

    public static final String TAG = "ShirtLayout";

    public ShirtLayout2(Context context) {
        super(context);
    }

    public ShirtLayout2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShirtLayout2(Context context, AttributeSet attrs, int defStyleAttr) {
                super(context, attrs, defStyleAttr);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                Log.e(TAG, TAG + "--onInterceptTouchEvent--ACTION_DOWN");
                return true;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, TAG + "--onInterceptTouchEvent--ACTION_UP");
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, TAG + "--onTouchEvent--ACTION_DOWN");
                return true;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, TAG + "--onTouchEvent--ACTION_UP");
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, TAG + "--dispatchTouchEvent--ACTION_DOWN");
                break;
            case MotionEvent.ACTION_UP:
                Log.e(TAG, TAG + "--dispatchTouchEvent--ACTION_UP");
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
