package com.zhao.core.http;

import java.lang.reflect.Method;

import com.zhao.core.ServletContext;
import com.zhao.core.http.impl.HttpServletRequest;
import com.zhao.core.http.impl.HttpServletResponse;
import com.zhao.core.servlet.Servlet;

public class DefaultDispatcherHandler {
	private Servlet defaultServlet;

	private DefaultDispatcherHandler() {
	}

	/**
	 * 获得派发处理类(只支持完整路径匹配)
	 * 
	 * @param request
	 * @param context
	 * @return
	 */
	private String getDispatcherClass(HttpServletRequest request, ServletContext context) {
		String uri = request.getUri().substring(1);//  ""   项目名     项目名/资源名称
		if(!"".equals(context.getProjectName()) && !uri.equals("")) {
			String[] project_resource =  uri.split("/");
			if(context.getProjectName().equals(project_resource[0]) && project_resource.length>1) {
			  String resource_name = 	project_resource[1].split("\\?")[0]; //   login
			  System.out.println(resource_name);
			  String servletClassName = context.getServletMapping().get(resource_name);
			  System.out.println(context.getServletMapping());
			  
			  if(servletClassName!=null) {
				  return servletClassName;
			  }
			}
		}
		return context.getDefaultDispatcherClass();
	}

	public void process(ServletRequest request, ServletResponse response, ServletContext context) {
		// 请求派发
		HttpServletRequest httpReq = (HttpServletRequest) request;
		HttpServletResponse httpRes = (HttpServletResponse) response;

		String  clzName = getDispatcherClass(httpReq, context);
		try {
			Class clz =  Class.forName(clzName);
			Object servlet = clz.newInstance();
			Method service = clz.getMethod("service",ServletRequest.class,ServletResponse.class);
			Method setContext = clz.getMethod("setContext",ServletContext.class);
			
			setContext.invoke(servlet,context);
			service.invoke(servlet,request,response);
			
			//刷新流
			if(httpRes.isUseOut()) {
				httpRes.getOutput().flushData();
			}else {
				httpRes.getWriter().flushData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
}