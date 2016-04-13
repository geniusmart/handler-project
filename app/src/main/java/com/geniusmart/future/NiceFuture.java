package com.geniusmart.future;

import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class NiceFuture {

    public static final String TAG = "geniusmart";

    private Callable<Boolean> mCallable;
    private FutureTask<Boolean> mFuture;

    public NiceFuture() {
        mCallable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                Log.i(TAG, System.currentTimeMillis() + "");
                Thread.sleep(5000);
                Log.i(TAG, System.currentTimeMillis() + "");
                return true;
            }
        };

        mFuture = new FutureTask<>(mCallable);
    }

    public void execute() {
        Log.i(TAG, "start future Thread");
        new Thread(mFuture).start();
    }

    /**
     * 取消任务
     *
     * @param mayInterruptIfRunning TODO:参数作用何在？
     */
    public void cancle(boolean mayInterruptIfRunning) {
        mFuture.cancel(mayInterruptIfRunning);
    }
}
