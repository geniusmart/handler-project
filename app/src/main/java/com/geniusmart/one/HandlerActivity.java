package com.geniusmart.one;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.geniusmart.R;


public class HandlerActivity extends Activity {

    public static final String TAG = "geniusmart";
    private TextView mCTOTextView;

    //CEO老张——主线程或UI线程
    Thread mCEO = Thread.currentThread();

    //老张重金请来的技术总监
    Handler mCTOHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "是否为主线程:" + (mCEO == Thread.currentThread()));

            String line = "\n";
            StringBuffer text = new StringBuffer(mCTOTextView.getText());

            switch (msg.what){
                case Employee.PM:
                    text.append(line).append("处理PM的需求：开发人员全体加班，严格执行996");
                    mCTOTextView.setText(text);
                    break;
                case Employee.CODER:
                    text.append(line).append("处理Coder的需求：将安排人员排查性能问题");
                    mCTOTextView.setText(text);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCTOTextView = (TextView) findViewById(R.id.cto_tv);

        new CoderThread().start();
        new PMThread().start();
    }

    /**
     * 程序猿
     */
    class CoderThread extends Thread {

        @Override
        public void run() {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Message message = Message.obtain();
            message.what = Employee.CODER;
            message.obj = "公司的Android基础框架存在性能问题，请排查。";
            //程序猿向技术总监反馈问题
            mCTOHandler.sendMessage(message);
        }
    }

    /**
     * 产品汪
     */
    class PMThread extends Thread {

        @Override
        public void run() {
            Message message = Message.obtain();
            message.what = Employee.PM;
            message.obj = "开发进度严重滞后于产品计划，请协调。";
            //产品汪考虑了5秒钟，像CTO反馈了目前的产品进度问题
            mCTOHandler.sendMessageDelayed(message,5000);
        }
    }

}
