package com.zhao.core.http;

import java.util.Map;

public interface ServletRequest{

	public Object getAttribute(String name);

	public int getContentLength();

	public String getContentType();

	public String getHeader(String name);
	
	public String getParameter(String name);

	public Map<String, String[]> getParameterMap();

	public void setAttribute(String name, Object o);

	public void removeAttribute(String name);

}