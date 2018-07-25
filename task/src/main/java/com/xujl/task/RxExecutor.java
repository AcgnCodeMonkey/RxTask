package com.xujl.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 任务管理类
 * 线程池管理
 */
public class RxExecutor {
    private ExecutorService mExecutor;

    private RxExecutor () {
        super();
    }

    public <T> void executeTask (Task<T> task) {
        final TaskCallable<T> thread = new TaskCallable<>();
        executeTask(thread, task);
    }

    /**
     * @param thread 子线程任务
     * @param task   任务关联对象
     */
    public <T> void executeTask (TaskCallable<T> thread, Task<T> task) {
        thread.setTask(task);
        final Future<T> submit = mExecutor.submit(thread);
        task.setFuture(submit);
    }

    public void executeUiTask (Task task) {
        final Emitter emitter = task.getEmitter();
        if (emitter != null) {
            emitter.runUi();
        }
    }

    /**
     * 使用默认参数初始化线程池
     * 核心线程数为cpu核心数
     */
    public void init () {
        //cpu核心数
        final int processors = Runtime.getRuntime().availableProcessors();
        init(processors);
    }

    /**
     * @param corePoolSize 线程池中核心子线程数量，通常情况下建议不超过cpu核心数
     *                     如果应用需要高并发，建议不要超cpu核心数*2
     */
    public void init (int corePoolSize) {
        mExecutor = Executors.newScheduledThreadPool(corePoolSize, Executors.defaultThreadFactory());
    }

    public void setDebug (boolean isDebug) {
        Logger.sIsDebug = isDebug;
    }

    public static RxExecutor getInstance () {
        return Holder.RX_EXECUTOR;
    }

    private static class Holder {
        private static final RxExecutor RX_EXECUTOR = new RxExecutor();
    }

}
