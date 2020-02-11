package tt.okhttp.bean;

import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URL;

public class HttpUrl {

    public String host;
    public int port;
    public String protocol;
    public String file;

    /**
     * scheme://host:port/path?query#fragment
     * @param urlStr
     */
    public HttpUrl(String urlStr)  {
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        host = url.getHost();
        port = url.getPort();
        port = port == -1 ? url.getDefaultPort() : port;
        protocol = url.getProtocol();
        file = url.getFile();
        file = TextUtils.isEmpty(file) ? "/" : file;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getFile() {
        return file;
    }

}
