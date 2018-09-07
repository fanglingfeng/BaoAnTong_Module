package com.tjsoft.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolManager {
	private static ThreadPoolManager mInstance = new ThreadPoolManager();
    private static final String TAG = "ThreadPoolManager";

    /**
     * 线程池的大小
     */
    private int poolSize = 5;

    /**
     * 线程池
     */
    private ExecutorService threadPool = Executors.newFixedThreadPool(poolSize);

    /**
     * 单任务线程池
     */
    private ExecutorService singlehreadPool = Executors
            .newSingleThreadExecutor();


    private ThreadPoolManager() {

    }

    private static class Holder {
        static final ThreadPoolManager INSTANCE = new ThreadPoolManager();
    }

    public static ThreadPoolManager getInstance() {
        return mInstance;
    }

    /**
     * 向任务队列中添加任务
     *
     * @param task
     */
    public void addAsyncTask(Runnable task) {

        if (!threadPool.isShutdown()) {
            threadPool.execute(task);
        }else {
            threadPool = Executors.newFixedThreadPool(poolSize);
            threadPool.execute(task);
        }
    }

    /**
     * 向单线程任务队列中添加任务
     *
     * @param task
     */
    public void addSingleAsyncTask(Runnable task) {
        if (!singlehreadPool.isShutdown()) {
            singlehreadPool.execute(task);
        } else {
            singlehreadPool = Executors
                    .newSingleThreadExecutor();
            singlehreadPool.execute(task);
        }
    }

    /**
     * 结束轮询，关闭线程池
     */
    public void stop() {
        threadPool.shutdown();
        singlehreadPool.shutdown();
    }
    
    /**
     * 结束轮询，关闭线程池
     */
    public void stopNow() {
        threadPool.shutdownNow();
        singlehreadPool.shutdownNow();
    }
    
}
