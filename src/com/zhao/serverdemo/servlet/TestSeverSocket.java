package com.zhao.serverdemo.servlet;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TestSeverSocket {
		public static void main(String[] args) throws Exception{
			
			  ServerSocket serverSocket = new ServerSocket(8080);
			  Socket client =  serverSocket.accept();
			  System.out.println("客户端的请求过来了");
			  
			  OutputStream out  = client.getOutputStream();
			  BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out,"utf-8"));
			  
			  StringBuilder builder = new StringBuilder();
			  
			  //String responText = "我们都是中国人";
			  builder.append("HTTP/1.1 error 404");
			  builder.append(System.lineSeparator());
			  //builder.append("Content-Type:text/html;charset=utf-8");
			  builder.append(System.lineSeparator());
			  //builder.append("Content-Length:");
			  //builder.append(responText.getBytes("utf-8").length);
			 // builder.append(System.lineSeparator());
			  //builder.append(System.lineSeparator());
			  //builder.append(responText);
			  
			  writer.write(builder.toString());
			  writer.flush();
			  writer.close();
			  client.close();
		}
}	