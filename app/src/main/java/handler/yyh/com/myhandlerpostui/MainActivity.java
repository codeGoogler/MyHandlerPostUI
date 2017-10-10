package handler.yyh.com.myhandlerpostui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends Activity {
    private Unbinder unbinder;
    //非静态内部类会隐式持有外部类的引用 ,防止内存泄漏值Handler

    @BindView(R.id.iv_result)
    ImageView iv_result;

    private  Handler mHandler  = new  Handler() {
        @Override
        public void handleMessage(Message msg) {
            iv_result.setImageResource(R.drawable.iv_builty);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_onclick01,R.id.btn_onclick02,R.id.btn_onclick03,R.id.btn_onclick04,R.id.iv_result,})
    public void onCLick(View view){
        switch (view.getId()){
            case R.id.btn_onclick01://第二点
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(100);
                    }
                });
                break;
            case R.id.btn_onclick02://第一种方式
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    iv_result.setImageResource(R.drawable.iv_builty2);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.btn_onclick03://第三种方式：利用runOnUiThread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            iv_result.setImageResource(R.drawable.iv_builty3);
                        }
                    });
                break;
            case R.id.btn_onclick04://第四种方式：利用View本身的异步线程任务去更新UI
                iv_result.post(new Runnable() {
                    @Override
                    public void run() {
                        iv_result.setImageResource(R.drawable.iv_builty4);
                    }
                });
                break;
            case  R.id.iv_result:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "你要离开我吗", Toast.LENGTH_SHORT).show();
                        iv_result.setImageResource(R.mipmap.ic_launcher);
                    }
                });

                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        mHandler.removeCallbacksAndMessages(null);
    }
}
