package com.xujl.rxtask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.leakcanary.RefWatcher;
import com.xujl.task.Emitter;
import com.xujl.task.RxExecutor;
import com.xujl.task.RxHelper;
import com.xujl.task.RxLife;
import com.xujl.task.RxLifeList;
import com.xujl.task.Task;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private TextView tv;
    private RxLifeList mBindLife;
    private ImageView iv;
    private LinearLayout lin1;
    private LinearLayout lin2;
    private Bitmap mBitmap;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        lin1 = findViewById(R.id.lin1);
        lin2 = findViewById(R.id.lin2);
        final MovementMethod movementMethod = ScrollingMovementMethod.getInstance();
        tv.setMovementMethod(movementMethod);
        findViewById(R.id.btn_add).setOnClickListener(this);
        findViewById(R.id.btn_add_count).setOnClickListener(this);
        findViewById(R.id.btn_add_most).setOnClickListener(this);
        findViewById(R.id.btn_add_recycle).setOnClickListener(this);
        findViewById(R.id.btn_add_bind).setOnClickListener(this);
        findViewById(R.id.btn_add_bind2).setOnClickListener(this);
        findViewById(R.id.btn_add_ui).setOnClickListener(this);
        ((RadioGroup) findViewById(R.id.rg)).setOnCheckedChangeListener(this);
        mBindLife = new RxLifeList();
        iv = findViewById(R.id.iv);
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
    }

    @Override
    public void onClick (View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.btn_add:
                RxExecutor.getInstance()
                        .executeTask(new Task() {
                            @Override
                            public void run (Emitter emitter) throws Exception {
                                super.run(emitter);
                                Thread.sleep(1000);
                            }

                            @Override
                            public void onFinished () {
                                super.onFinished();
                                result("单个任务：执行完毕！", R.color.task_color_1);
                            }
                        });
                break;
            case R.id.btn_add_most:
                for (int i = 0; i < 10; i++) {
                    final int taskId = i;
                    RxExecutor.getInstance()
                            .executeTask(new Task() {
                                @Override
                                public void run (Emitter emitter) throws Exception {
                                    super.run(emitter);
                                    Thread.sleep(500);
                                }

                                @Override
                                public void onFinished () {
                                    super.onFinished();
                                    result("批量任务：" + taskId + "执行完毕！", R.color.task_color_2);
                                }
                            });
                }
                break;
            case R.id.btn_add_count:
                RxExecutor.getInstance()
                        .executeTask(new RxHelper.CountDownTask(5 * 1000, 1000) {
                            @Override
                            public void count (long time) {
                                result("倒计时任务，时间：" + time, R.color.task_color_3);
                            }
                        });
                break;

            case R.id.btn_add_recycle:
                RxExecutor.getInstance()
                        .executeTask(new RxHelper.RecycleTask(1000, 3) {
                            @Override
                            public void count (int count) {
                                result("循环任务执行第" + count + "次！", R.color.task_color_4);
                            }
                        });
                break;
            case R.id.btn_add_bind:
                RxExecutor.getInstance()
                        .executeTask(new TestRunnable(),
                                new Task() {
                                    @Override
                                    public void bindLife (RxLife rxLife) {
                                        super.bindLife(rxLife);
                                        mBindLife.add(rxLife);
                                    }

                                    @Override
                                    public void onError (Exception e) {
                                        super.onError(e);
                                        toast(e.getMessage() + "\n解绑handler，不再回调！");
                                    }

                                    @Override
                                    public void onFinished () {
                                        super.onFinished();
                                        tv.setText("handler改变了控件文本");
                                        iv.setImageBitmap(mBitmap);
                                        toast("绑定生命周期的任务执行完毕！");
                                    }
                                });
                break;
            case R.id.btn_add_bind2:
                RxExecutor.getInstance()
                        .executeTask(new TestRunnable(),
                                new Task(true) {
                                    @Override
                                    public void bindLife (RxLife rxLife) {
                                        super.bindLife(rxLife);
                                        mBindLife.add(rxLife);
                                    }

                                    @Override
                                    public void onError (Exception e) {
                                        super.onError(e);
                                        toast(e.getMessage() + "\n解绑handler，不再回调！");
                                    }

                                    @Override
                                    public void onFinished () {
                                        super.onFinished();
                                        tv.setText("handler改变了控件文本");
                                        iv.setImageBitmap(mBitmap);
                                        toast("绑定生命周期的任务执行完毕！");
                                    }
                                });
                break;
            case R.id.btn_add_ui:
                RxExecutor.getInstance()
                        .executeTask(new Task() {
                            @Override
                            public void run (Emitter emitter) throws Exception {
                                super.run(emitter);
                                while (!isDestroy()) {
                                    RxExecutor.getInstance()
                                            .executeUiTask(new Task() {
                                                @Override
                                                public void onlyRunUiTask () {
                                                    super.onlyRunUiTask();
                                                    result("子线程中切换了ui！", R.color.task_color_5);
                                                }
                                            });
                                    Thread.sleep(1000 * 2);
                                }
                            }

                            @Override
                            public void bindLife (RxLife rxLife) {
                                super.bindLife(rxLife);
                                mBindLife.add(rxLife);
                            }
                        });


                break;
            default:

                break;

        }
    }

    private void result (String msg, int colorId) {
        Log.e(TAG, msg);
        SpannableStringBuilder builder = new SpannableStringBuilder("\n" + msg);
        builder.setSpan(new ForegroundColorSpan(getResources().getColor(colorId)), 0, msg.length() + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.append(builder);
        final int scrollAmount = tv.getLayout().getLineTop(tv.getLineCount()) - tv.getHeight();
        if (scrollAmount > 0) {
            tv.scrollTo(0, scrollAmount + 20);
        }
    }

    private void toast (String msg) {
        Log.e(TAG, msg);
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy () {
//        mBitmap.recycle();
        if (mBindLife != null) {
            mBindLife.onDestroy();
        }
        super.onDestroy();
        RefWatcher refWatcher = MyApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }

    @Override
    public void onCheckedChanged (RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb1:
                lin1.setVisibility(View.VISIBLE);
                lin2.setVisibility(View.GONE);
                break;
            case R.id.rb2:
                lin1.setVisibility(View.GONE);
                lin2.setVisibility(View.VISIBLE);
                break;
            default:

                break;

        }
    }
}
