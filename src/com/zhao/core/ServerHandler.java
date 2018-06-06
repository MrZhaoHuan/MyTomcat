package com.zhao.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.zhao.core.http.DefaultDispatcherHandler;
import com.zhao.core.http.ServletRequest;
import com.zhao.core.http.ServletResponse;
import com.zhao.core.http.impl.HttpServletRequest;
import com.zhao.core.http.impl.HttpServletResponse;
import com.zhao.core.log.ConsoleLog;

public class ServerHandler implements Runnable{
	   private ServletRequest request;
	   private ServletResponse response;
	   private ConsoleLog log;
	   private Socket socket;  //socket套接字
	   private ServletContext context;
	   
	   public ServerHandler(Socket socket,ServletContext context) {
		   log = ConsoleLog.getLog(getClass());
		   this.socket = socket;
		   this.context = context;
	   }
	   
	   @Override
	   public void run(){
		try {
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
			String lineText = null;
			String requestLine = null;
			Map<String,String> requestHead = new HashMap<>();
			int i = 0;
			boolean lineEnd = false;
			StringBuilder entity = new StringBuilder();
			boolean readNoOver = true;
			while(readNoOver&&(lineText=reader.readLine())!=null) {
					if(i==0) {
						requestLine = lineText;
					}else {
						if("".equals(lineText)) {
							lineEnd = true;
							//构造request对象
							request = new HttpServletRequest(requestLine,requestHead);
							//判断是否存在请求体
							if( ((HttpServletRequest)request).getRequestMethod().equals("GET") ) {
								 readNoOver = false;
							}else{
								//循环读完请求体
								   int contentLength  = ((HttpServletRequest)request).getContentLength();
								       int end = -1;
								   	   while(readNoOver&&(end=reader.read())!=-1){
								   		    entity.append((char)end);
								   		    //流没结束，但只要读完content-length长度的字节数据后就不读了
								   		    if(entity.toString().getBytes("UTF-8").length>=contentLength) {
								   		    	//post请求体内容已经读取完成
								   		    	byte[] entityArr = new byte[contentLength];
								   		    	byte[] readEntityArr  = entity.toString().getBytes("UTF-8");
								   		    	//截取掉多余的字节数据(如果存在)
								   		    	System.arraycopy(readEntityArr,0, entityArr,0,contentLength);
								   		    	((HttpServletRequest)request).setEntity(entityArr);
								   		    	readNoOver = false;
								   		    }
								   	   }
							}
						}else {
						   if(!lineEnd) {
							   	  String[]  headArr=lineText.split(":");
								  requestHead.put(headArr[0],headArr[1]);
						   }
						}
					}
					i++;
			}
			
			//构造响应对象
			Class clz = Class.forName("com.zhao.core.http.impl.HttpServletResponse");
			Constructor c  = clz.getDeclaredConstructor(OutputStream.class);
			c.setAccessible(true);
			response = (HttpServletResponse)c.newInstance(socket.getOutputStream());
			
			//构造DefaultDispatcherHandler
			Class dispatcherHandler = Class.forName("com.zhao.core.http.DefaultDispatcherHandler");
			Constructor dispatcherC  = dispatcherHandler.getDeclaredConstructor();
			dispatcherC.setAccessible(true);
			Method dispatcherProcess = dispatcherHandler.getDeclaredMethod("process",ServletRequest.class,ServletResponse.class,ServletContext.class);
			dispatcherProcess.invoke(((DefaultDispatcherHandler)dispatcherC.newInstance()),this.request,this.response,this.context);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}finally {
			// 关闭连接    
		    try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}  
		}
		   
	   }
}