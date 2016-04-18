package com.geniusmart.future;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.geniusmart.R;

public class FutureActivity extends AppCompatActivity {

    private static final String TAG = "FutureActivity";
    private FutureApi mFutureApi;
    private SimpleAsyncTask mSimpleAsyncTask;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);
        mTextView = (TextView) findViewById(R.id.textView);
        mFutureApi = new FutureApi();
    }

    public void execute(View view) {
        mFutureApi.execute();
    }

    public void executeForResult(View view) {
        mFutureApi.executeForResult();
    }

    public void cancle(View view) {
        if (mFutureApi != null) {
            mFutureApi.cancle(true);
        }
    }

    public void executeSimpleAsyncTask(View view) {

        /*//在子线程中创建和执行AsyncTask
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSimpleAsyncTask = new MySimpleAsyncTask();
                mSimpleAsyncTask.execute("task1");
            }
        }).start();*/
        mSimpleAsyncTask = new MySimpleAsyncTask();
        mSimpleAsyncTask.execute("task1");
    }

    public void cancelSimpleAsyncTask(View view) {
        if (mSimpleAsyncTask != null) {
            mSimpleAsyncTask.cancel(true);
        }
    }

    public void executeFutureApi(View view) {
        mFutureApi.executeFutureAndCallable();
    }

    private class MySimpleAsyncTask extends SimpleAsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected String doInBackground(String param) {
            Log.i(TAG, param + "--doInBackground--start");
            for (int i = 0; i <= 666666666; i++) {
                if (i % 100000 == 0) {
                    publishProgress(String.valueOf(i));
                }
            }
            if (!isCancelled()) {
                Log.i(TAG, param + "--doInBackground--finish");
            }
            return "finish";
        }

        @Override
        protected void onCancelled() {
            mTextView.setText("onCancelled");
            Log.i(TAG, "onCancelled");
        }

        @Override
        protected void onProgressUpdate(String value) {
            mTextView.setText("onProgressUpdate--" + value);
            Log.i(TAG, "onProgressUpdate--" + value);
        }

        @Override
        protected void onPostExecute(String result) {
            mTextView.setText("onPostExecute-" + result);
            Log.i(TAG, "onPostExecute-" + result);
        }
    }
}
