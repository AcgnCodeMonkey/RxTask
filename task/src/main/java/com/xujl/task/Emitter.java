package com.xujl.task;


import android.os.Message;

/**
 * 事件发射器
 */
public class Emitter {
    private static final String TAG = "Emitter";
    public static final int ERROR = -1;
    public static final int NEXT = 0;
    public static final int FINISH = 1;
    public static final int UI = 2;
    private RxHandler mHandler;

    Emitter (Task task) {
        super();
        mHandler = new RxHandler(task);
    }


    void clearHandler () {
        mHandler = null;
    }

    private void sendMessage (int code, Object object) {
        if (mHandler == null) {
            return;
        }
        Message message = new Message();
        message.what = code;
        message.obj = object;
        mHandler.sendMessage(message);
    }

    public void error (Exception e) {
        sendMessage(ERROR, e);
    }

    public void next (Object object) {
        sendMessage(NEXT, object);
    }

    public void finish () {
        sendMessage(FINISH, null);
    }

    void runUi () {
        sendMessage(UI, null);
    }


}
