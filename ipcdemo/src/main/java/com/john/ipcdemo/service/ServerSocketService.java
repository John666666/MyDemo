package com.john.ipcdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.john.ipcdemo.util.LogUtil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Random;

public class ServerSocketService extends Service {
    private static final String TAG = "ServerSocketService";
    private ServerSocket serverSocket = null;
    private String[] replyList = {
            "欢迎来到王者荣耀", "你会玩什么游戏呢?", "你是GG还是MM?", "我是万能的服务端，你有什么请求说吧！"
    };

    @Override
    public void onCreate() {
        super.onCreate();
        startServerSocket();
        LogUtil.i(TAG, "启动ServerSocket, wait for connect...");
    }

    private void startServerSocket() {
        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8848);
                Socket socket = null;
                while(true) {

                    /*ServerSocketChannel channel = ServerSocketChannel.open();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        channel.bind(new InetSocketAddress("127.0.0.1", 8848));
                        SocketChannel socket = channel.accept();
                        if (socket != null) {
                            handleSocketChannel(socket);
                        }
                    } else {
                        ServerSocket serverSocket = new ServerSocket(8848);
                        Socket socket = serverSocket.accept();
                        if (socket != null) {
                            handleSocket(socket);
                        }
                    }*/
                    socket = serverSocket.accept();
                    if (socket != null) {
                        handleSocket(socket);
                    }
                }
            } catch (IOException e) {
                LogUtil.e(TAG, "启动ServerSocket出错", e);
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private void handleSocket(Socket socket) {
        LogUtil.i(TAG, "和客户端"+socket.getRemoteSocketAddress()+"建立连接");
        new Thread(() -> {
            BufferedReader reader = null;
            PrintWriter out = null;
            try {
                //回复客户端
                out = new PrintWriter(socket.getOutputStream(), true);
                String welcome = "欢迎来到聊天室^^";
                out.println(welcome);
                while(true) {
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                    String line = reader.readLine();
                    if(line == null) {
                        LogUtil.i(TAG, "客户端断开连接");
                        break;
                    }
                    LogUtil.i(TAG, "[handleSocket] 收到客户端消息: " + line);
                    //回复客户端
                    out = new PrintWriter(socket.getOutputStream(), true);
                    String reply = randomReply();
                    out.println(reply);
                }
            } catch (IOException e) {
                LogUtil.e(TAG, "Socket通信异常", e);
            }
            if(out != null) {
                out.close();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }).start();
    }

    private void handleSocketChannel(SocketChannel socketChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        ByteArrayOutputStream out = null;
        try {
            out = new ByteArrayOutputStream();
            while (socketChannel.read(buffer) != -1) {
                out.write(buffer.array());
                buffer.clear();
            }
            LogUtil.i(TAG, "[handleSocketChannel] 收到客户端消息: " + new String(out.toByteArray(), "UTF-8"));

            //回复消息
            String reply = randomReply();
            buffer.clear();
            buffer.put(reply.getBytes("UTF-8"));
            buffer.flip();
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
        } catch (IOException e) {
            LogUtil.e(TAG, "Socket通信异常");
        } finally {
            if (out != null) {
                try {
                    out.close();
                    socketChannel.close();
                } catch (IOException e) {
                }
            }
        }
    }

    Random random = new Random();

    public String randomReply() {
        int idx = random.nextInt(replyList.length);
        return replyList[idx];
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
