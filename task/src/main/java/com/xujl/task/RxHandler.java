package com.xujl.task;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * <pre>
 *     @author : xujl
 *     e-mail : 597355068@qq.com
 *     time   : 2018/7/23
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class RxHandler extends Handler {
    private static final String TAG = "RxHandler";
    private WeakReference<Task> mTaskWeakReference;

    RxHandler (Task task) {
        super(Looper.getMainLooper());
        this.mTaskWeakReference = new WeakReference<>(task);
    }

    @Override
    public void handleMessage (Message msg) {
        super.handleMessage(msg);
        if (mTaskWeakReference == null) {
            Log.d(TAG, "Task:  mTaskWeakReference.isDestroy");
            return;
        }
        final Task task = mTaskWeakReference.get();
        if (task == null) {
            Log.d(TAG, "Task:  task.isDestroy");
            return;
        }
        try {
            switch (msg.what) {
                case Emitter.ERROR:
                    task.onError((Exception) msg.obj);
                    break;
                case Emitter.NEXT:
                    task.onNext(msg.obj);
                    break;
                case Emitter.UI:
                    task.onlyRunUiTask();
                    break;
                case Emitter.FINISH:
                    task.onFinished();
                    mTaskWeakReference = null;
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }

    }
}
