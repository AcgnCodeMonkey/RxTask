package com.xujl.rxtask;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.xujl.task.RxExecutor;

/**
 * <pre>
 *     @author : xujl
 *     e-mail : 597355068@qq.com
 *     time   : 2018/7/19
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MyApplication extends Application {
    private RefWatcher refWatcher;

    @Override
    public void onCreate () {
        super.onCreate();
        Log.e("cpu核心数：", "" + Runtime.getRuntime().availableProcessors());
        RxExecutor.getInstance().init(8);
        refWatcher = setupLeakCanary();
    }

    private RefWatcher setupLeakCanary () {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install(this);
    }

    public static RefWatcher getRefWatcher (Context context) {
        MyApplication leakApplication = (MyApplication) context.getApplicationContext();
        return leakApplication.refWatcher;
    }
}
