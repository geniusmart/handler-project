package com.geniusmart.future;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SimpleAsyncTask<Params, Progress, Result> {

    private static final String TAG = "SimpleAsyncTask";
    private static final Executor EXECUTOR = Executors.newCachedThreadPool();

    private WorkerRunnable<Params,Result> mWorker;
    private FutureTask<Result> mFuture;
    private static Handler sHandler;

    private static final int MESSAGE_POST_RESULT = 0x1;
    private static final int MESSAGE_POST_PROGRESS = 0x2;

    /**
     * 是否取消
     */
    private final AtomicBoolean mCancelled = new AtomicBoolean();

    public SimpleAsyncTask() {
        mWorker = new WorkerRunnable<Params,Result>() {
            @Override
            public Result call() throws Exception {
                //有可能正常执行，也有可能执行一半被cancel
                Result result = doInBackground();
                return postResult(result);
            }
        };

        mFuture = new FutureTask<Result>(mWorker);

        //为了与主线程通讯，需持有主线程的mainLooper
        //注：源代码里Handler为懒汉式单例，且线程同步
        sHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Result result = (Result) msg.obj;
                switch (msg.what) {
                    case MESSAGE_POST_PROGRESS:
                        //TODO 注：源码中区分了AsyncTask对象
                        onProgressUpdate(result);
                        break;
                    case MESSAGE_POST_RESULT:
                        finish(result);
                        break;
                }
            }
        };
    }

    public void execute() {
        onPreExecute();
        EXECUTOR.execute(mFuture);
    }

    protected final void publishProgress(Progress value) {
        if (!isCancelled()) {
            sHandler.obtainMessage(MESSAGE_POST_PROGRESS, value).sendToTarget();
        }
    }

    private Result postResult(Result result) {
        Message message = sHandler.obtainMessage(MESSAGE_POST_RESULT, result);
        message.sendToTarget();
        return result;
    }


    /**
     * 取消任务
     * 此方法不让重写，因此定义为final方法
     *
     * @param mayInterruptIfRunning TODO:参数作用何在？
     */
    public final boolean cancel(boolean mayInterruptIfRunning) {
        mCancelled.set(true);
        return mFuture.cancel(mayInterruptIfRunning);
    }

    private void finish(Result result) {
        if (isCancelled()) {
            onCancelled();
        } else {
            onPostExecute(result);
        }
    }

    protected abstract Result doInBackground();

    protected void onPreExecute() {
    }

    protected void onPostExecute(Result result) {
    }

    protected void onProgressUpdate(Result value) {
    }

    protected void onCancelled() {
    }

    public final boolean isCancelled() {
        return mCancelled.get();
    }

    private static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
        Params[] mParams;
    }
}
