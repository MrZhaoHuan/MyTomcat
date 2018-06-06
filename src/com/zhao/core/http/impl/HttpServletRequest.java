package com.zhao.core.http.impl;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.zhao.core.http.ServletRequest;

public class HttpServletRequest implements ServletRequest {
	private String requestLine;
	private Map<String, String> requestHead;
	private String requestMethod;
	private String uri;
	private String contentType;
	private String charEncoding = "ISO8859-1"; //默认编码格式
	private int contentLenth;
	private Map<String, String> parameterMap = new HashMap<>();
	private Map<String, Object> attri = new HashMap<>();
	
	public void setEntity(byte[] entiry) {
		try {
			String entityStr = new String(entiry,charEncoding);
			parseParamText(entityStr);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public HttpServletRequest() {
	}

	public HttpServletRequest(String requestLine, Map<String, String> requestHead) {
		this.requestLine = requestLine;
		this.requestHead = requestHead;
		parseRequestLine(requestLine);
		parseRequestHead(requestHead);
	}

	private void parseRequestHead(Map<String, String> requestHead) {
		String length = requestHead.get("Content-Length");
		this.contentLenth = null==length?0:Integer.parseInt(length.substring(1));
		this.contentType = requestHead.get("Content-Type");
		if(this.contentType!=null) {
			String[] contentTypeArr = this.contentType.split(";");
			if(contentTypeArr.length>1) {
				 setCharEncoding(contentTypeArr[1]);
			}
		}
	}

	public void setCharEncoding(String charEncoding) {
		this.charEncoding = charEncoding;
	}
	public String getCharEncoding() {
		return charEncoding;
	}
	private void parseRequestLine(String requestLine) {
		String[] requestLineArr = requestLine.split(" ");
		this.requestMethod = requestLineArr[0];
		String[] queryUriArr = requestLineArr[1].split("\\?");
		this.uri = queryUriArr[0];
		if (requestMethod.equals("GET")) {
			// [name=zhao,pwd=123]
			if(queryUriArr.length>1) {
					parseParamText(queryUriArr[1]);
			}
		}
	}

	private void parseParamText(String param) {
		String[] parameterArr = param.split("&");
		for (String kv : parameterArr) {
				String[] k_v = kv.split("=");
				if (parameterMap.containsKey(k_v[0])) {
					parameterMap.put(k_v[0], parameterMap.get(k_v[0]) + ( k_v.length>1?("," + k_v[1]):"") );
				} else {
					parameterMap.put(k_v[0],k_v.length>1?k_v[1]:"");
				}
		}
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	@Override
	public Object getAttribute(String name) {
		return attri.get(name);
	}

	@Override
	public int getContentLength() {
		return this.contentLenth;
	}

	@Override
	public String getContentType() {
		return this.contentType;
	}

	@Override
	public String getParameter(String name) {
		return   parameterMap.get(name);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> map = new HashMap<>();
		for (String paramKey : parameterMap.keySet()) {
			map.put(paramKey, parameterMap.get(paramKey).split(","));
		}
		return map;
	}

	@Override
	public void setAttribute(String name, Object o) {
		attri.put(name, o);
	}

	public String getUri() {
		return uri;
	}

	@Override
	public void removeAttribute(String name) {
		attri.remove(name);
	}

	@Override
	public String getHeader(String name) {
		return requestHead.get(name);
	}

}