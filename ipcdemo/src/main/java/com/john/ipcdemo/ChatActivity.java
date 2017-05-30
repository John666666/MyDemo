package com.john.ipcdemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.john.ipcdemo.util.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ChatActivity extends Activity {
    private static final String TAG = "ChatActivity";
    private TextView messageBox;
    private ScrollView messageScroll;
    private EditText inputBox;
    private Button btnSend;
    private Socket socket;
    private PrintWriter writer;

    public static final int MESSAGE_TYPE_SEND = 1;
    public static final int MESSAGE_TYPE_RECEIVE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messageScroll = (ScrollView) findViewById(R.id.sv_message_scroll);
        messageBox = (TextView) findViewById(R.id.tv_message_box);
        inputBox = (EditText) findViewById(R.id.et_input);
        btnSend = (Button) findViewById(R.id.btn_send);

        messageBox.setMovementMethod(new ScrollingMovementMethod());

        btnSend.setOnClickListener((v) -> {
            sendMessage();
        });

        inputBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_ENTER) {
                    if(event.getAction() == KeyEvent.ACTION_UP) {
                        //回车发送消息
                        btnSend.callOnClick();
                    }
                    //屏蔽回车
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(socket != null && socket.isConnected()) {
            return;
        }
        new Thread(() -> {
            BufferedReader reader = null;
            try {
                socket = new Socket();
                socket.connect(new InetSocketAddress("127.0.0.1", 8848), 2000);
                LogUtil.i(TAG, "连接到服务器");
                writer = new PrintWriter(socket.getOutputStream(), true);
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                while (true) {
                    String receiveMsg = reader.readLine();
                    if(receiveMsg == null) {
                        LogUtil.i(TAG, "连接已断开");
                        break;
                    }
                    LogUtil.i(TAG, "receive from server: " + receiveMsg);
                    runOnUiThread(() -> {
                        append2MessageBox(MESSAGE_TYPE_RECEIVE, receiveMsg);
                    });
                }
            } catch (IOException e) {
                LogUtil.e(TAG, "Socket连接出错", e);
            }

            if(writer != null) {
                writer.close();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                    socket = null;
                } catch (IOException e) {
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void sendMessage() {
        if (socket == null) {
            LogUtil.w(TAG, "socket未连接");
            return;
        }

        String input = inputBox.getText().toString();
        if (TextUtils.isEmpty(input)) {
            Toast.makeText(this, "不能发送空消息!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (writer == null) {
            LogUtil.w(TAG, "无法写入Socket");
            return;
        }
        writer.println(input);
        LogUtil.i(TAG, "send to server: " + input);
        append2MessageBox(MESSAGE_TYPE_SEND, input);
    }

    /**
     * 追加消息到消息框中
     *
     * @param type    消息类型
     * @param message
     */
    private void append2MessageBox(int type, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        StringBuilder sb = new StringBuilder(messageBox.getText());
        if (type == MESSAGE_TYPE_SEND) {
            sb.append("client:");
            inputBox.getText().clear();
        } else if (type == MESSAGE_TYPE_RECEIVE) {
            sb.append("server:");
        }
        sb.append(message);
        sb.append("\n");
        messageBox.setText(sb.toString());
    }
}
