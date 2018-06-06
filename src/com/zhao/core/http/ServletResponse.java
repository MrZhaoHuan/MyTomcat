package com.zhao.core.http;

import com.zhao.core.io.ServletOutputStream;
import com.zhao.core.io.ServletWriter;

public interface ServletResponse {
	
	public ServletOutputStream getOutput();

	public ServletWriter getWriter();
	
	public void setContentType(String contentType);
	
	public void setStatus(int status);
	
	public void addHead(String name,String value);
	
	public void setHead(String name,String value);

}