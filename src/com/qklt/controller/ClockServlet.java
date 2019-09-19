package com.qklt.controller;

import com.alibaba.fastjson.JSON;
import com.qklt.domain.Message;
import com.qklt.service.ClockService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ClockServlet extends HttpServlet {

    private static ClockService clockService = new ClockService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter out = resp.getWriter();
        Set clockIp = clockService.getIpSet();
        String clockIpJson = JSON.toJSONString(clockIp);
        out.print(clockIpJson);
        out.flush();

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String time = req.getParameter("time");
        String ipAdress = req.getParameter("ipAdress");
        Message message = new Message();
        PrintWriter out = resp.getWriter();
        Map<String, String> statue = new HashMap<>();
        message.setTime(Long.parseLong(time));
        message.setIp(ipAdress);
        if(clockService.timing(message)){
            statue.put("statue","1");
        } else {
            statue.put("statue","0");
        }
        String statueJson = JSON.toJSONString(statue);
        out.print(statueJson);
        out.flush();

    }
}
