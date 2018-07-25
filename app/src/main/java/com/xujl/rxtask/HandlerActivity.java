package com.xujl.rxtask;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.leakcanary.RefWatcher;

import java.lang.ref.WeakReference;

/**
 * <pre>
 *     @author : xujl
 *     e-mail : 597355068@qq.com
 *     time   : 2018/7/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class HandlerActivity extends Activity implements View.OnClickListener {
    private TextView tv;
    private ImageView iv;
    private Bitmap mBitmap;
    private MyHandler mHandler2;

    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        tv = findViewById(R.id.tv);
        iv = findViewById(R.id.iv);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        findViewById(R.id.btn).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn).setOnClickListener(this);
        mHandler2 = new MyHandler(this);

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            Log.e("Handler", "Message");
            tv.setText("handler改变了控件文本");
            iv.setImageBitmap(mBitmap);
        }
    };

    @Override
    protected void onDestroy () {
        super.onDestroy();
//        mBitmap.recycle();
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.btn:

                new Thread(new Runnable() {
                    @Override
                    public void run () {
                        try {
                            Thread.sleep(TimeConfig.SLEEP_TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler.sendEmptyMessage(0);
                    }
                }).start();
                break;
            case R.id.btn2:
                new Thread(new Runnable() {
                    @Override
                    public void run () {
                        try {
                            Thread.sleep(TimeConfig.SLEEP_TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        mHandler2.sendEmptyMessage(0);
                    }
                }).start();
                break;
            case R.id.btn3:
                new TestThread(new MyHandler(this)).start();
                break;
            case R.id.btn4:
                new TestThread(mHandler).start();
                break;
            default:

                break;

        }

    }

    private static class MyHandler extends Handler {
        private WeakReference<HandlerActivity> mReference;

        public MyHandler (HandlerActivity activity) {
            mReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            Log.e("Handler2", "Message");
            if (mReference.get() == null) {
                return;
            }
            mReference.get().tv.setText("handler2改变了控件文本");
            mReference.get().iv.setImageBitmap(mReference.get().mBitmap);

        }
    }

    private static class TestThread extends Thread {
        private WeakReference<Handler> mReference;

        public TestThread (Handler handler) {
            mReference = new WeakReference<>(handler);
        }

        @Override
        public void run () {
            super.run();
            try {
                Thread.sleep(TimeConfig.SLEEP_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (mReference.get() != null) {
                mReference.get().sendEmptyMessage(0);
            }

        }
    }

}
