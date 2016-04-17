package com.geniusmart.future;

import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class FutureApi {

    public static final String TAG = "FutureApi";
    private Callable<String> mCallable;
    private FutureTask<String> mFuture;
    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();

    public void execute() {
        mCallable = new MyCallable();
        mFuture = new FutureTask<>(mCallable);
        new Thread(mFuture).start();
    }

    public void executeForResult() {
        try {
            mCallable = new MyCallable();
            mFuture = new FutureTask<>(mCallable);
            new Thread(mFuture).start();
            String result = mFuture.get();
            Log.d(TAG, "result=" + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void executeFutureAndCallable(){
        mCallable = new MyCallable();
        Future<String> future = mExecutorService.submit(mCallable);
        try {
            String result = future.get();
            Log.d(TAG,result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消任务
     * @param mayInterruptIfRunning
     */
    public void cancle(boolean mayInterruptIfRunning) {
        mFuture.cancel(mayInterruptIfRunning);
    }

    private static class MyCallable implements Callable<String> {
        @Override
        public String call() throws Exception {
            Log.d(TAG, "---Thread start ---");
            Thread.sleep(3000);
            Log.d(TAG, "---Thread finish---");
            return Thread.currentThread().getName();
        }
    }
}
