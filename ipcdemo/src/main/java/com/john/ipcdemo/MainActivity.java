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

import com.john.ipcdemo.aidl.IMyAIDLTest;
import com.john.ipcdemo.provider.MyProvider;
import com.john.ipcdemo.service.MyMessengerService;
import com.john.ipcdemo.util.LogUtil;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    private Messenger messenger;
    private IMyAIDLTest aidlTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(MainActivity.this, MyMessengerService.class);
//        intent.putExtra(MyMessengerService.BIND_CODE_KEY, MyMessengerService.BIND_MESSENGER_CODE);
//        bindService(intent, messengerConnect, BIND_AUTO_CREATE);

        intent.putExtra(MyMessengerService.BIND_CODE_KEY, MyMessengerService.BIND_AIDL_CODE);
        bindService(intent, aidlConnect, BIND_AUTO_CREATE);
    }

    public void clickHandle(View v) {
        switch (v.getId()) {
            case R.id.btn_query_all_man:
                Toast.makeText(this, "provider查询", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse(MyProvider.AUTHORITIES_URI+MyProvider.TABLE_MAN);
                getContentResolver().query(uri, null, null, null, null);
                break;
            case R.id.btn_messenger:
                if(messenger == null) {
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
            case R.id.btn_aidl:
                if(aidlTest == null) {
                    Toast.makeText(this, "远程AIDL服务未绑定", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    int result = aidlTest.add(1, 5);
                    Toast.makeText(this, "远程AIDL计算结果: "+result, Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    LogUtil.e(TAG, "AIDL远程接口调用异常", e);
                }
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
        try {
            unbindService(aidlConnect);
        } catch (Exception e) {
        }
    }

    ServiceConnection messengerConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if(service == null) {
                LogUtil.w(TAG, "未获取到远程binder");
                return;
            }
            LogUtil.i(TAG, "远程Messenger服务返回binder: "+service);
            messenger = new Messenger(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.i(TAG, "远程Messenger服务断开");
        }
    };

    ServiceConnection aidlConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if(service == null) {
                LogUtil.w(TAG, "未获取到远程binder");
                return;
            }
            LogUtil.i(TAG, "远程AIDL服务返回binder: "+service);
            aidlTest = IMyAIDLTest.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtil.i(TAG, "远程Messenger服务断开");
        }
    };
}
