package com.zhao.core.servlet;

import com.zhao.core.http.ServletRequest;
import com.zhao.core.http.ServletResponse;

public interface Servlet {
	 void init();
	 void service(ServletRequest request,ServletResponse response);
	 void destroy();
}