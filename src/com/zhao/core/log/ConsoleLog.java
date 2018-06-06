package com.zhao.core.log;

import static java.lang.System.out;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.System.err;

public final class ConsoleLog {
	private SimpleDateFormat dateFormat;
	private Class sourceClass;

	private ConsoleLog(Class sourceClass) {
		this.sourceClass = sourceClass;
		this.dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	}

	/**
	 * 获取日志器
	 * 
	 * @param clz
	 * @return
	 */
	public static ConsoleLog getLog(Class clz) {
		return new ConsoleLog(clz);
	}

	/**
	 * 打印普通信息——>控制台
	 * 
	 * @param msg
	 */
	public void info(Object msg) {
		out.println(dateFormat.format(new Date()) + " " + sourceClass.getName() + "普通信息:" + msg);
	}

	/**
	 * 打印错误信息——>控制台
	 * 
	 * @param msg
	 */
	public void error(String msg) {
		err.println(dateFormat.format(new Date()) + " " + sourceClass.getName() + "错误信息:" + msg);
	}
}