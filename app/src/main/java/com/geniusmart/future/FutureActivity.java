package com.geniusmart.future;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.geniusmart.R;

public class FutureActivity extends AppCompatActivity {

    private static final String TAG = "FutureActivity";
    private NiceFuture mNiceFuture = new NiceFuture();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);
    }

    public void execute(View view) {
        mNiceFuture.execute();
    }

    public void cancle(View view) {
        mNiceFuture.cancle(true);
    }

    public void executeSimpleAsyncTask(View view) {
        mSimpleAsyncTask.execute();
    }

    public void cancelSimpleAsyncTask(View view) {
        mSimpleAsyncTask.cancel(true);
    }

    SimpleAsyncTask mSimpleAsyncTask = new SimpleAsyncTask() {

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
            super.onPreExecute();
        }

        @Override
        protected String doInBackground() {

            for (int i = 0; i < 30000; i++) {
                publishProgress("progress" + i);
            }
            Log.i(TAG, "doInBackground");
            return "finish";
        }

        @Override
        protected void onCancelled() {
            Log.i(TAG, "onCancelled");
        }

        @Override
        protected void onProgressUpdate(String value) {
            Log.i(TAG, "onProgressUpdate" + value);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, "onPostExecute" + result);
        }
    };
}
