package com.zhao.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
	private ServletContext context;

	private WebServer() {
		context = ServletContext.getServletContext();
	}

	// 启动web服务器
	public static void main(String args[]) throws Exception{
		WebServer server = new WebServer();
		try {
			server.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 监听客户端socket连接
	 * 
	 * @throws IOException
	 */
	private void start() throws IOException {
		ServerSocket serverSocket = new ServerSocket(context.getServerListenerPort());
		// 轮询监听客户端请求
		while (true) {
			Socket socket = serverSocket.accept();
			// 启动1个线程去处理客户端请求
			new Thread(new ServerHandler(socket,context)).start();
		}
	}

}