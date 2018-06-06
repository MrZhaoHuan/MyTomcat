package com.zhao.core.servlet.impl;

import com.zhao.core.ServletContext;
import com.zhao.core.http.ServletRequest;
import com.zhao.core.http.ServletResponse;
import com.zhao.core.http.impl.HttpServletRequest;
import com.zhao.core.http.impl.HttpServletResponse;
import com.zhao.core.log.ConsoleLog;
import com.zhao.core.servlet.Servlet;

public abstract class HttpServlet implements Servlet{
	private static final String GET = "GET";
    private static final String POST = "POST";
    
    public ConsoleLog log  =  ConsoleLog.getLog(getClass());
    private ServletContext context;
    
    public void setContext(ServletContext context) {
		this.context = context;
	}
    
    public ServletContext getContext() {
		return context;
	}
    public HttpServlet() {
    	
	}
    
	@Override
	public void init() { 
		log.info("------init()------空实现");
	}
	
	@Override
	public void service(ServletRequest request,ServletResponse response) {
		  HttpServletRequest httpReq =  (HttpServletRequest)request;
		  HttpServletResponse httpResp =  (HttpServletResponse)response;
			  
		  String method = httpReq.getRequestMethod();
		  
		  if(method.equals(GET)) {
			    doGet(httpReq,httpResp);
		  }else if(method.equals(POST)) {
			    doPost(httpReq,httpResp);
		  }
	}

	@Override
	public void destroy() {
		log.info("------destroy()------空实现");
	}
	
	
	protected abstract void doGet(HttpServletRequest httpReq,HttpServletResponse httpResp);
	
	
	protected abstract void doPost(HttpServletRequest httpReq,HttpServletResponse httpResp);

}
