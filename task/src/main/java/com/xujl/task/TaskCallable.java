package com.xujl.task;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

/**
 * <pre>
 *     @author : xujl
 *     e-mail : 597355068@qq.com
 *     time   : 2018/7/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class TaskCallable<T> implements Callable<T> {
    protected static final String TAG = "TaskThread";
    private WeakReference<Task> task;

    public TaskCallable (Task task) {
        this.task = new WeakReference<>(task);
    }

    public TaskCallable () {
    }

    public void setTask (Task task) {
        this.task = new WeakReference<>(task);
    }

    @Override
    final public T call () throws Exception {
        Logger.d(TAG, "taskId:" + Thread.currentThread().getId());
        if (isTaskNull()) {
            return null;
        }
        try {
            /*优先执行当前子类run(emitter)方法，如果
            当前方法返回false则再去执行Task的run方法
             */
            final boolean isFinish = run(task.get().getEmitter());
            if (!isFinish) {
                task.get().run(task.get().getEmitter());
            }
        } catch (Exception e) {
            Logger.e(TAG, "任务执行出错：", e);
            if (isEmitterNotNull()) {
                task.get().getEmitter().error(e);
            }
        }
        if (isEmitterNotNull()) {
            task.get().getEmitter().finish();
        }
        return null;
    }

    /**
     * 发射器是否不为空
     *
     * @return
     */
    private boolean isEmitterNotNull () {
        return task.get() != null && task.get().getEmitter() != null;
    }

    /**
     * 任务是否存在
     *
     * @return
     */
    private boolean isTaskNull () {
        return task.get() == null;
    }

    /**
     * 子线程方法
     *
     * @param emitter
     * @return
     * @throws Exception
     */
    public boolean run (Emitter emitter) throws Exception {
        return false;
    }


}
