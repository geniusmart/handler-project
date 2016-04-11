package com.geniusmart.future;

import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class SuperAsyncTask {

    Executor mExecutor = Executors.newSingleThreadExecutor();

    Callable<Boolean> mCallable = new Callable<Boolean>() {
        @Override
        public Boolean call() throws Exception {
            Log.e("Genius",System.currentTimeMillis()+"");
            Thread.sleep(5000);
            Log.e("Genius",System.currentTimeMillis()+"");
            return true;
        }
    };
    FutureTask<Boolean> mFuture = new FutureTask<>(mCallable);

    public void execute(){
        mExecutor.execute(mFuture);
    }
}
