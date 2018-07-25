package com.xujl.task;

import java.util.concurrent.Future;

public abstract class Task<T> implements RxLife {
    private static final String TAG = "Task";
    private Emitter mEmitter;
    private Future<T> mFuture;
    private boolean isDestroy;
    private ITaskFinish mITaskFinish;
    /**
     * 绑定生命周期时是否需要在
     * 生命周期结束后取消任务
     * 设置为true时（任务可能执行到一半就会结束
     * ，慎重设置！）
     */
    private boolean needCancel;

    public Task () {
        this(false);
    }

    public Task (boolean needCancel) {
        mEmitter = new Emitter(this);
        bindLife(this);
        this.needCancel = needCancel;
    }

    public void run (Emitter emitter) throws Exception {
    }

    /**
     * 生命周期结束后，子线程任务依然会执行
     * 完毕，但不会再回调结果,如果需要及时停止
     * 子线程任务，请在子线程任务中根据方法isDestroy()
     * 加以判断
     */
    @Override
    public void onDestroy () {
        if (getEmitter() == null) {
            return;
        }
        if (mFuture != null && needCancel) {
            mFuture.cancel(true);
        }
        getEmitter().error(new Exception("生命周期结束，中断关联！"));
        recycle();
    }

    private void recycle () {
        isDestroy = true;
        mEmitter = null;
    }

    @Override
    public boolean isDestroy () {
        return isDestroy;
    }

    /**
     * 仅仅执行ui任务，
     * 主线程回调
     */
    public void onlyRunUiTask () {

    }

    /**
     * 当前线程回调，取决于开启任务的线程
     *
     * @param rxLife
     */
    public void bindLife (RxLife rxLife) {

    }

    /**
     * 主线程回调
     *
     * @param object
     */
    public void onNext (Object object) {

    }

    /**
     * 主线程回调
     *
     * @param e
     */
    public void onError (Exception e) {
        if (mITaskFinish != null) {
            mITaskFinish.onFinish(this);
        }
    }

    /**
     * 主线程回调
     */
    public void onFinished () {
        if (mITaskFinish != null) {
            mITaskFinish.onFinish(this);
        }
        recycle();
    }

    public Future<T> getFuture () {
        return mFuture;
    }

    public void setFuture (Future<T> future) {
        mFuture = future;
    }

    public boolean isNeedCancel () {
        return needCancel;
    }

    public void setNeedCancel (boolean needCancel) {
        this.needCancel = needCancel;
    }

    @Override
    final public void setITaskFinish (ITaskFinish iTaskFinish) {
        mITaskFinish = iTaskFinish;
    }

    Emitter getEmitter () {
        return mEmitter;
    }

    @Override
    public boolean equals (Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode () {
        return super.hashCode();
    }
}
