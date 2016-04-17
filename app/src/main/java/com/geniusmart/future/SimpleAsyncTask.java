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

    private static final Executor EXECUTOR = Executors.newCachedThreadPool();

    private WorkerRunnable<Params, Result> mWorker;
    private FutureTask<Result> mFuture;

    private static final int MESSAGE_POST_RESULT = 0x1;
    private static final int MESSAGE_POST_PROGRESS = 0x2;

    private static InternalHandler sHandler = new InternalHandler();

    /**
     * 是否取消
     */
    private final AtomicBoolean mCancelled = new AtomicBoolean();

    public SimpleAsyncTask() {
        mWorker = new WorkerRunnable<Params, Result>() {
            @Override
            public Result call() throws Exception {
                Result result = doInBackground(mParams);
                return postResult(result);
            }
        };

        mFuture = new FutureTask<>(mWorker);
    }

    public void execute(Params params) {
        mWorker.mParams = params;
        onPreExecute();
        EXECUTOR.execute(mFuture);
    }

    protected final void publishProgress(Progress progress) {
        if (!isCancelled()) {
            AsyncTaskResult<Progress> taskResult = new AsyncTaskResult<>(this, progress);
            sHandler.obtainMessage(MESSAGE_POST_PROGRESS, taskResult).sendToTarget();
        }
    }

    private Result postResult(Result result) {
        AsyncTaskResult<Result> taskResult = new AsyncTaskResult<>(this, result);
        Message message = sHandler.obtainMessage(MESSAGE_POST_RESULT, taskResult);
        message.sendToTarget();
        return result;
    }


    /**
     * 取消任务
     * 此方法不让重写，因此定义为final方法
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

    protected void onPreExecute() {
    }

    /**
     * 此方法应该在线程体中调用，即在FutureTask中调用
     */
    protected abstract Result doInBackground(Params params);

    /**
     * 此方法应该在Handler中调用
     */
    protected void onProgressUpdate(Progress progress) {
    }

    /**
     * 此方法应该在Handler中调用
     */
    protected void onPostExecute(Result result) {
    }

    /**
     * 此方法应该在Handler中调用
     */
    protected void onCancelled() {
    }

    public final boolean isCancelled() {
        return mCancelled.get();
    }

    private static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
        Params mParams;
    }

    private static class InternalHandler extends Handler {
        public InternalHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            AsyncTaskResult<?> result = (AsyncTaskResult<?>) msg.obj;
            switch (msg.what) {
                case MESSAGE_POST_RESULT:
                    result.mTask.finish(result.mData);
                    break;
                case MESSAGE_POST_PROGRESS:
                    result.mTask.onProgressUpdate(result.mData);
                    break;
            }
        }
    }

    private static class AsyncTaskResult<Data> {
        final SimpleAsyncTask mTask;
        final Data mData;

        AsyncTaskResult(SimpleAsyncTask task, Data data) {
            mTask = task;
            mData = data;
        }
    }
}
