package com.zhao.core.http.impl;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.zhao.core.http.ServletResponse;
import com.zhao.core.io.ServletOutputStream;
import com.zhao.core.io.ServletWriter;

public class HttpServletResponse implements ServletResponse {
	private ServletOutputStream out;
	private ServletWriter writer;
	private boolean useOut = false;
	private boolean useWriter = false;
	private int status = 200;
	private String statusTip = "ok";
	private String contentType = "text/html;charset=utf-8";
	private int contentLenth;
	private Map<String, String> headers = new HashMap<>();

	public void setUseOut(boolean useOut) {
		this.useOut = useOut;
	}

	public boolean isUseOut() {
		return useOut;
	}
    
	public void setUseWriter(boolean useWriter) {
		this.useWriter = useWriter;
	}

	public boolean isUseWriter() {
		return useWriter;
	}

	public void setStatusTip(String statusTip) {
		this.statusTip = statusTip;
	}

	public String getStatusTip() {
		return statusTip;
	}

	public void setContentLenth(int contentLenth) {
		headers.put("Content-Length", contentLenth + "");
	}

	private HttpServletResponse(OutputStream out) {
		this.out = new ServletOutputStream(out, this);
		this.writer = new ServletWriter(out, this);
		headers.put("Content-Type", contentType);
	}

	@Override
	public ServletOutputStream getOutput() {
		if(isUseWriter()) {
			throw new RuntimeException("已经在另外的地方打开了ServletWriter");
		}else {
			setUseOut(true);
		}
		return out;
	}

	@Override
	public ServletWriter getWriter() {
		if(isUseOut()) {
			throw new RuntimeException("已经在另外的地方打开了ServletOutputStream");
		}else {
			setUseWriter(true);
		}
		return writer;
	}

	@Override
	public void setContentType(String contentType) {
		this.setHead("Content-Type", contentType);
	}

	@Override
	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	@Override
	public void addHead(String name, String value) {
		String headValues = headers.get(name);
		if (null != headValues) {
			headers.put(name, headValues + ";" + value);
		} else {
			headers.put(name, value);
		}
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	@Override
	public void setHead(String name, String value) {
		headers.put(name, value);
	}

}