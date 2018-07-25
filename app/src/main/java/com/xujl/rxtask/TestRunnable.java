package com.xujl.rxtask;

import android.util.Log;

import com.xujl.task.Emitter;
import com.xujl.task.TaskCallable;

/**
 * <pre>
 *     @author : xujl
 *     e-mail : 597355068@qq.com
 *     time   : 2018/7/24
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class TestRunnable extends TaskCallable {

    @Override
    public boolean run (Emitter emitter) throws Exception {
        Thread.sleep(TimeConfig.SLEEP_TIME);
        Log.e("TestRunnable","执行完毕！");
        return true;
    }
}
