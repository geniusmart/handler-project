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


public class HandlerThreadActivity2 extends Activity {

    public static final String TAG = "geniusmart";

    //老张你好
    Thread mCEO = Thread.currentThread();

    //总监你好
    Handler mCTOHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Employee.ZHEGNWEI:
                    Message message = Message.obtain();
                    message.what = Employee.CTO;
                    message.obj = "表现异常出色，这个月的绩效名额会适当倾斜";
                    //向政委发送消息（实现了主线程->HandlerThread的通讯）
                    mZhengWeiHandler.sendMessage(message);
                    break;
            }
        }
    };

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
                        Message message = Message.obtain();
                        message.what = Employee.ZHEGNWEI;
                        message.obj = "汇报项目组工作进度和团队建设情况";
                        //向CTO发送消息（实现了HandlerThread->主线程通讯）
                        mCTOHandler.sendMessage(message);
                        break;
                    case Employee.CTO:
                        Log.i(TAG, "真是受宠若惊，谢主隆恩！");
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
            message.obj = "上交周报";
            //向政委发送消息
            mZhengWeiHandler.sendMessageDelayed(message,2000);
        }
    }
}
