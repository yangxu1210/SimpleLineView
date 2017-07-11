package com.simplelineview.utils;

import android.util.Log;

/**
 * Log统一管理类
 * @author Administrator
 *
 */
public class LogUtil {
	private static boolean isDebug = true ;//开发完毕将isDebug置为false
	
	/**
	 * 打印i级别的log
	 * @param tag
	 * @param msg
	 */
	public static void i(String tag,String msg){
		if(isDebug){
			Log.i(tag, ""+msg);
		}
	}
	/**
	 * 打印e级别的log
	 * @param tag
	 * @param msg
	 */
	public static void e(String tag,String msg){
		try {
			if(isDebug){
				Log.e(tag, ""+msg);
			}
		}catch (Exception e){
			Log.e("msg","Log也会报错？？？");
		}
	}
	
	
	/**
	 * 方便打log
	 * @param object
	 * @param msg
	 */
	public static void i(Object object,String msg){
		if(isDebug){
			Log.i(object.getClass().getSimpleName(), ""+msg);
		}
	}
	
	/**
	 * 方便打log
	 * @param object
	 * @param msg
	 */
	public static void e(Object object,String msg){
		if(isDebug){
			Log.e(object.getClass().getSimpleName(), ""+msg);
		}
	}
	
	/**
	 * 输出系统log和系统异常在同一窗口
	 * @param msg 日志内容
	 */
	public static void show(String msg){
		if (isDebug) {
			Log.i("AndroidRuntime", ""+msg);
		}
	}

	/***
	 * 错误日志
	 * @param content
	 */
	public static void e(String content){
		Log.i("AndroidRuntime",""+content);
	}
}
