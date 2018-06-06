package com.zhao.core.io;

import java.util.Map;

import com.zhao.core.http.ServletResponse;
import com.zhao.core.http.impl.HttpServletResponse;

public abstract class ServletStream {
	private ServletResponse response;

	public ServletStream(ServletResponse response) {
		this.response = response;
	}

	public ServletResponse getResponse() {
		return response;
	}

	/**
	 * 获取响应行和响应头
	 * 
	 * @return
	 */
	public String getLineAndHead() {
		HttpServletResponse httpRes = (HttpServletResponse) response;
		// 响应头
		Map<String, String> mapHeades = httpRes.getHeaders();

		StringBuilder responseLineAndHead = new StringBuilder();
		// 响应行
		responseLineAndHead.append("HTTP/1.1 ");
		responseLineAndHead.append(httpRes.getStatus());
		responseLineAndHead.append(" ");
		responseLineAndHead.append(httpRes.getStatusTip());
		responseLineAndHead.append(System.lineSeparator());

		for (String headName : mapHeades.keySet()) {
			responseLineAndHead.append(headName);
			responseLineAndHead.append(":");
			responseLineAndHead.append(mapHeades.get(headName));
			responseLineAndHead.append(System.lineSeparator());
		}
		responseLineAndHead.append(System.lineSeparator()); //响应头结束
		return responseLineAndHead.toString();
	}

}