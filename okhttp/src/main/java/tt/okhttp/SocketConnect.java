package tt.okhttp;

import android.text.TextUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocketFactory;
import tt.okhttp.bean.HttpUrl;
import tt.okhttp.bean.Request;

// 创建Socket通信
public class SocketConnect {

    Socket socket;
    long lastUsetime;

    public boolean isSameAddress(String host, int port) {
        if (null == socket) {
            return false;
        }
        return TextUtils.equals(socket.getInetAddress().getHostName(), host) && port == socket.getPort();
    }

    public InputStream call(NetDataPipe netDataPipe, Request request) throws IOException {
        createSocket(request);
        //写出请求
        netDataPipe.writeRequest(socket.getOutputStream(), request);
        return socket.getInputStream();
    }

    // 创建socket
    private void createSocket(Request request) throws IOException {
        if (socket == null || socket.isClosed()) {
            HttpUrl url = request.url();
            //需要SSLSocket
            if (url.protocol.equalsIgnoreCase("https")) {
                socket = SSLSocketFactory.getDefault().createSocket();
            } else {
                socket = new Socket();
            }
            socket.connect(new InetSocketAddress(url.host, url.port));
        }
    }

    public void close() {
        if (socket != null) {
            try {
                socket.close();
            } catch (Exception e) {

            }
        }
    }

    public void updateLastUseTime() {
        lastUsetime = System.currentTimeMillis(); // 更新最后使用时间
    }
}
