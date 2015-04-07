package com.tieto.systemmanagement.app.tools;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jinpei on 31/03/15.
 * Unit of thread pool management.
 */
public class AppListThreadPoolUtil {

    private static final int CORE_POOL_SIZE = 1;
    private static final int CORE_POOL_MAX = 20;
    private static final int KEEP_ALIVE_TIME = 20000;
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
    private static final int CORE_POOL_CACHE_SIZE = 50;

    private static BlockingQueue<Runnable> workQueue;
    private static RejectedExecutionHandler mRejectedExecutionHandler;

    private static ThreadPoolExecutor mThreadPoolExecutor;

    static {
        workQueue = new ArrayBlockingQueue<Runnable>(CORE_POOL_CACHE_SIZE);

        mRejectedExecutionHandler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

            }
        };

        mThreadPoolExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, CORE_POOL_MAX, KEEP_ALIVE_TIME, TIME_UNIT, workQueue, mRejectedExecutionHandler);
    }

    public static void executeRunnable(Runnable task) {
        mThreadPoolExecutor.execute(task);
    }
}
