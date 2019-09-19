package com.qklt.service;

import com.qklt.domain.Message;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ClockService {

    private static Map<String, Socket> clocks = new HashMap<>();
    private static final int CLOCK_NUM = 1; // 闹钟的数量

    static {
        System.out.println("初始化");
        for(int i = 0; i < CLOCK_NUM; i ++) {
            new Thread(ClockService::getConnection).start();
        }
    }

    public Set getIpSet(){
        return clocks.keySet();
    }

    // 等待连接
    private static void getConnection(){
        Socket socket;
        ServerSocket serS = null;
        String ipAdress;
        try {
            serS = new ServerSocket(8086); // 自设端口
            socket = serS.accept();
            ipAdress = socket.getInetAddress().getHostAddress();
            System.out.println("已连接,ip地址为 " + ipAdress);
            clocks.put(ipAdress, socket);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(serS != null){
                try {
                    serS.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 计时
    public boolean timing(Message message){

        if(clocks.get(message.getIp()) == null){
            return false;
        }

        new Thread(() -> {
            long time = message.getTime();
            System.out.println("开始计时,倒数" + time + "秒");
            try {
                while(true){
                    Thread.sleep(1000);
                    time--;
                    if(time == 0){
                        ring(message.getIp());
                        System.out.println("闹铃");
                        break;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        return true;
    }

    // 响闹
    private void ring(String clockIp){
        Socket socket = clocks.get(clockIp);
        PrintStream ps = null;
        try {
            // 向指定闹钟发送响铃指令
            ps = new PrintStream(socket.getOutputStream());
            System.out.println("已发送");
            ps.println("0");
            ps.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
