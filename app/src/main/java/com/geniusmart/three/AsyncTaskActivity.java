package com.geniusmart.three;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.geniusmart.R;

public class AsyncTaskActivity extends Activity implements View.OnClickListener{

    public static final String TAG = "AsyncTaskActivity";
    MyAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);

        findViewById(R.id.btn_cancle2).setOnClickListener(this);
        findViewById(R.id.btn_cancle1).setOnClickListener(this);
        findViewById(R.id.btn_execute).setOnClickListener(this);

        //myAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_execute:
                myAsyncTask = new MyAsyncTask();
                myAsyncTask.execute();
                break;
            case R.id.btn_cancle1:
                //参数为ture时，允许任务执行一半终止
                myAsyncTask.cancel(true);
                break;
            case R.id.btn_cancle2:
                //参数为ture时，允许任务执行一半终止
                myAsyncTask.cancel(false);
                break;
        }
    }

    class MyAsyncTask extends AsyncTask<String,Integer,String >{

        @Override
        protected void onCancelled() {//4个on开头，和1个do开头
            Log.e(TAG,"onCancelled");
        }

        @Override
        protected void onPostExecute(String s) {
            Log.e(TAG, "onPostExecute");
        }

        @Override
        protected void onPreExecute() {
            Log.e(TAG, "onPreExecute");
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            Log.e(TAG, "onProgressUpdate" + values[0]);
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e(TAG,"doInBackground--start");
            for(int i = 1;i<=100000000;i++){
                if(i % 10000 == 0){
                    publishProgress(i / 10000);
                }
            }
            Log.e(TAG,"doInBackground--end");
            return null;
        }
    }

}
