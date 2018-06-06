package com.zhao.core.io;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import com.zhao.core.http.ServletResponse;
import com.zhao.core.http.impl.HttpServletResponse;

public class ServletWriter extends ServletStream {
	private BufferedWriter writer;
	private StringBuilder responseText = new StringBuilder();
	public ServletWriter(OutputStream out,ServletResponse response) {
		super(response);
		try {
			writer = new BufferedWriter(new OutputStreamWriter(out,"UTF-8"));
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public void flushData() {
		try {
			((HttpServletResponse)getResponse()).setContentLenth(responseText.toString().getBytes("UTF-8").length);
			writer.write(getLineAndHead()+responseText.toString()); // 响应体
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				writer.flush();
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	public void write(String msg) {
		responseText.append(msg);
	}

}
