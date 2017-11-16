package com.kunion.kcvideo;

import com.kunion.kcvideoapp.KcVideoApp;

public class LogUtil {
	
	public static KcVideoApp globalContext;
	public static void log(String log) {
		System.out.println(log);
		if (globalContext != null)
			globalContext.log(log);
	}
	
	public static void logScreen(String log){
		globalContext.log(log);
	}
}
