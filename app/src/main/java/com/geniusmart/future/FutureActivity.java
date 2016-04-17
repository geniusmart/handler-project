package com.geniusmart.future;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.geniusmart.R;

public class FutureActivity extends AppCompatActivity {

    private static final String TAG = "FutureActivity";
    private FutureApi mFutureApi;
    private SimpleAsyncTask mSimpleAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);
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

    private static class MySimpleAsyncTask extends SimpleAsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            super.onPreExecute();
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
            Log.i(TAG, "onCancelled");
        }

        @Override
        protected void onProgressUpdate(String value) {
            Log.i(TAG, "onProgressUpdate--" + value);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "onPostExecute-" + result);
        }
    }

    ;
}
