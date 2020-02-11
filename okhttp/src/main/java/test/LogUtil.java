package test;

import android.util.Log;

public class LogUtil {
	
	private static boolean isLogOpen = true;

	public static void i(String msg) {
		if (isLogOpen) {
			Log.i("OkHttp", msg == null ? "null" : msg);
		}
	}

	public static void e(String msg) {
		if (isLogOpen) {
			Log.e("OkHttp", msg == null ? "null" : msg);
		}
	}

}
