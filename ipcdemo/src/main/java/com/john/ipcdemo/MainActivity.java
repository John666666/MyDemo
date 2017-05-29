package com.john.ipcdemo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Toast;

import com.john.ipcdemo.aidl.BinderPool;
import com.john.ipcdemo.aidl.BinderPoolImpl;
import com.john.ipcdemo.aidl.IBinderPool;
import com.john.ipcdemo.aidl.IHello;
import com.john.ipcdemo.aidl.IMathAdd;
import com.john.ipcdemo.provider.MyProvider;
import com.john.ipcdemo.service.MyMessengerService;
import com.john.ipcdemo.util.LogUtil;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private Messenger messenger;
    private IBinderPool binderPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(MainActivity.this, MyMessengerService.class);
        bindService(intent, messengerConnect, BIND_AUTO_CREATE);
    }

    public void clickHandle(View v) {
        switch (v.getId()) {
            case R.id.btn_query_all_man:
                Toast.makeText(this, "provider查询", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(MyProvider.AUTHORITIES_URI + MyProvider.TABLE_MAN);
                getContentResolver().query(uri, null, null, null, null);
                break;
            case R.id.btn_messenger:
                if (messenger == null) {
                    Toast.makeText(this, "远程Messenger服务未绑定", Toast.LENGTH_SHORT).show();
                    return;
                }
                Message msg = Message.obtain();
                msg.what = 1;
                Bundle bundle = new Bundle();
                bundle.putString("msg", "Hello Messenger");
                msg.obj = bundle;
                try {
                    messenger.send(msg);
                    Toast.makeText(this, "Messenger通信完毕", Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    LogUtil.e(TAG, "Messenger远程通信异常", e);
                }
                break;
            case R.id.btn_math_add:
                new Thread(() -> {
                    IBinder binder = BinderPool.getInstance(MainActivity.this).getBinder(BinderPoolImpl.BIND_MATH_ADD_CODE);
                    if (binder == null) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "未能连接到期望的远程服务", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }
                    try {
                        IMathAdd mathAdd = IMathAdd.Stub.asInterface(binder);
                        int sum = mathAdd.add(4, 5);
                        runOnUiThread(() -> {
                            Toast.makeText(this, "远程服务计算结果：" + sum, Toast.LENGTH_SHORT).show();
                        });
                    } catch (RemoteException e) {
                        LogUtil.e(TAG, "AIDL远程接口调用异常", e);
                    }
                }).start();
                break;
            case R.id.btn_hello:
                new Thread(() -> {
                    IBinder helloBinder = BinderPool.getInstance(MainActivity.this).getBinder(BinderPoolImpl.BIND_HELLO_CODE);
                    if (helloBinder == null) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "未能连接到期望的远程服务", Toast.LENGTH_SHORT).show();
                        });
                        return;
                    }
                    try {
                        IHello hello = IHello.Stub.asInterface(helloBinder);
                        String retStr = hello.sayHello("John");
                        runOnUiThread(() -> {
                            Toast.makeText(this, "远程服务返回结果：" + retStr, Toast.LENGTH_SHORT).show();
                        });
                    } catch (RemoteException e) {
                        LogUtil.e(TAG, "AIDL远程接口调用异常", e);
                    }
                }).start();
                break;
            case R.id.btn_test:
                Toast.makeText(this, "有没有乱码？", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unbindService(messengerConnect);
        } catch (Exception e) {
        }
    }

    ServiceConnection messengerConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service == null) {
                LogUtil.w(TAG, "未获取到远程Messenger");
                return;
            }
            LogUtil.i(TAG, "远程Messenger服务返回binder: " + service);
            messenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.i(TAG, "远程Messenger服务断开");
            messenger = null;
        }
    };
}
