package tt.okhttp.bean;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RequestBody {

    private final static String CONTENT_TYPE = "application/x-www-form-urlencoded"; // 表单提交 使用urlencoded编码
    Map<String, String> bodyMap = new HashMap<>();

    public RequestBody add(String name, String value) {
        try {
            bodyMap.put(URLEncoder.encode(name, "utf-8"), URLEncoder.encode(value, "utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }


    public String contentType() {
        return CONTENT_TYPE;
    }

    public long contentLength() {
        return body().getBytes().length;
    }

    public String body() {
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : bodyMap.entrySet()) {
            sb.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }
        if (sb.length() != 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

}
