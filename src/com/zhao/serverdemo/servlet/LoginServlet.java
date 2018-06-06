package com.zhao.serverdemo.servlet;

import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.zhao.core.http.impl.HttpServletRequest;
import com.zhao.core.http.impl.HttpServletResponse;
import com.zhao.core.servlet.impl.HttpServlet;

public class LoginServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest httpReq, HttpServletResponse httpResp) {
		doPost(httpReq, httpResp);
	}

	@Override
	protected void doPost(HttpServletRequest httpReq, HttpServletResponse httpResp) {
		
		try {
			String name = URLDecoder.decode(httpReq.getParameter("name"),"UTF-8");
			String pwd = URLDecoder.decode(httpReq.getParameter("pwd"),"UTF-8");
			
			httpResp.setContentType("text/html;charset=utf-8");
			httpResp.getWriter().write("谢谢您！！"+ name + "<br>您的密码是:"+ pwd);
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
		
}