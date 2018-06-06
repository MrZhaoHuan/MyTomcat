package com.zhao.core.io;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.zhao.core.http.ServletResponse;
import com.zhao.core.http.impl.HttpServletResponse;

public class ServletOutputStream extends ServletStream {
	private BufferedOutputStream out;
	private ServletResponse response;
	private List<Byte> list = new ArrayList<>();

	public ServletOutputStream(OutputStream out, ServletResponse response) {
		super(response);
		this.out = new BufferedOutputStream(out);
	}

	public void flushData() {
		try {
			((HttpServletResponse) getResponse()).setContentLenth(list.size());
			out.write(getLineAndHead().getBytes("UTF-8"));
			byte[] respData = new byte[list.size()];
			for (int i = 0; i < list.size(); i++) {
				respData[i] = list.get(i);
			}
			out.write(respData); // 响应体
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void print(byte msg) {
		list.add(msg);
	}
}