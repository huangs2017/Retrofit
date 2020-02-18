package test;

public class Log {

	private static boolean isLogOpen = true;

	public static void i(String msg) {
		if (isLogOpen) {
			android.util.Log.i("OkHttp", msg == null ? "null" : msg);
		}
	}

	public static void e(String msg) {
		if (isLogOpen) {
			android.util.Log.e("OkHttp", msg == null ? "null" : msg);
		}
	}

}
