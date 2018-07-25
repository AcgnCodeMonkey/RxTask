package com.xujl.rxtask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * <pre>
 *     @author : xujl
 *     e-mail : 597355068@qq.com
 *     time   : 2018/7/20
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class SplashActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.btn1:
                startActivity(new Intent(this, HandlerActivity.class));
                break;
            case R.id.btn2:
                startActivity(new Intent(this, MainActivity.class));
                break;
            default:

                break;

        }
    }
}
