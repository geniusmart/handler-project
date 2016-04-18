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

    /**
     * 一个静态的单例对象，所有AsyncTask对象共享的
     */
    private static InternalHandler sHandler = new InternalHandler();

    /**
     * 是否取消
     */
    private final AtomicBoolean mCancelled = new AtomicBoolean();

    public SimpleAsyncTask() {
        mWorker = new WorkerRunnable<Params, Result>() {
            @Override
            public Result call() throws Exception {
                //调用模板方法2-执行后台任务
                Result result = doInBackground(mParams);
                //提交结果给Handler
                return postResult(result);
            }
        };

        //此为线程对象
        mFuture = new FutureTask<>(mWorker);
    }

    public void execute(Params params) {
        mWorker.mParams = params;
        //在线程启动前调用预执行的模板方法，意味着它在调用AsyncTask.execute()的所在线程里执行，如果是在子线程中，则无法处理UI
        //调用模板方法1-预执行
        onPreExecute();
        //执行FutureTask启动线程
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
            //调用模板方法5：终止线程执行
            onCancelled();
        } else {
            //调用模板方法4：线程执行完毕
            onPostExecute(result);
        }
    }

    /**
     * 模板方法1-预执行，应在线程启动之前执行
     */
    protected void onPreExecute() {
    }

    /**
     * 模板方法2-执行后台任务，应该在线程体中调用，即在FutureTask中调用
     */
    protected abstract Result doInBackground(Params params);

    /**
     * 模板方法3-执行进度反馈，应该在Handler中调用
     */
    protected void onProgressUpdate(Progress progress) {
    }

    /**
     * 模板方法4-执行完毕，应该在Handler中调用
     */
    protected void onPostExecute(Result result) {
    }

    /**
     * 模板方法5-终止线程执行，应该在Handler中调用
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
        /**
         * 注：此为android 22之后的写法，构造函数里默认指定mainLooper对象
         * 它的好处是无论Handler在什么时机被实例化，都可以与主线程进行交互
         * 相比之下，之前版本的AsyncTask必须在ActivityThread中执行AsyncTask.init()
         */
        public InternalHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            AsyncTaskResult<?> result = (AsyncTaskResult<?>) msg.obj;
            switch (msg.what) {
                case MESSAGE_POST_RESULT:
                    //调用模板方法4和5，取消或者完成
                    result.mTask.finish(result.mData);
                    break;
                case MESSAGE_POST_PROGRESS:
                    //调用模板方法3-执行进度反馈
                    result.mTask.onProgressUpdate(result.mData);
                    break;
            }
        }
    }

    /**
     * 由于InternalHandler是静态内部类，无法引用外部类SimpleAsyncTask的实例对象，
     * 因此需要将外部类对象作为属性传递进来，所以封装此类
     */
    private static class AsyncTaskResult<Data> {
        final SimpleAsyncTask mTask;
        final Data mData;

        AsyncTaskResult(SimpleAsyncTask task, Data data) {
            mTask = task;
            mData = data;
        }
    }
}
