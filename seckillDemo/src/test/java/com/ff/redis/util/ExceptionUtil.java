package com.ff.redis.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author cornyu
 * @version 创建时间：2018年11月11日 下午10:12:01 类说明
 */
public class ExceptionUtil {
	public static String getTrace(Throwable throwable) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		throwable.printStackTrace(writer);
		StringBuffer buffer = stringWriter.getBuffer();
		return buffer.toString();
	}
}
