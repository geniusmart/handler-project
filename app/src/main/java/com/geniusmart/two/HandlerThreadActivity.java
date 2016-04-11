package com.geniusmart.two;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.geniusmart.R;
import com.geniusmart.one.Employee;


public class HandlerThreadActivity extends Activity {

    public static final String TAG = "geniusmart";

    //团长你好
    HandlerThread mTuanZhangThread;
    //政委你好
    Handler mZhengWeiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);

        mTuanZhangThread = new HandlerThread("强项目1团长");
        mTuanZhangThread.start();//团长启动了项目

        //团长选拔了助理小春
        Looper looper = mTuanZhangThread.getLooper();
        //团长指派助理小春协助政委完成工作，通过构造器传参
        mZhengWeiHandler = new Handler(looper){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case Employee.CODER:
                        Log.i(TAG,"情况已知悉，上有政策下有对策，功能适当删减，" +
                                "Leader最关注的功能保证质量。");
                        break;
                }
            }
        };

        //程序猿开始工作
        new CoderThread().start();
    }

    /**
     * 程序猿
     */
    class CoderThread extends Thread {

        @Override
        public void run() {
            Message message = Message.obtain();
            message.what = Employee.CODER;
            message.obj = "这个迭代版本一周是不可能完成的，加班换来的都是坏味道的代码";
            //程序猿向政委反馈问题
            mZhengWeiHandler.sendMessage(message);
        }
    }
}
