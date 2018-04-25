package socket.http.demo.utils;

import java.util.HashMap;
import java.util.Set;

/**
 * http头信息类
 */
public class WebHeaderCollection {
	private HashMap<String, String> headers = null;

	public WebHeaderCollection() {
		this.headers = new HashMap<String, String>();
		this.headers.put("Accept", "*/*");
		this.headers.put("Accept-Language", "zh-cn");
		this.headers.put("UA-CPU", "x86");
		this.headers.put("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
		this.headers.put("Host", "");
		// this.headers.put("Connection", "Keep-Alive");
		// this.headers.put("Proxy-Connection", "Keep-Alive");
		// this.headers.put("Transfer-Encoding", "");
		// this.headers.put("Location", "");
		// this.headers.put("Content-Type", "");
		// this.headers.put("Content-Length", "");
		// this.headers.put("Referer", "");
	}

	public void put(String key, String value) {
		this.headers.put(key, value);
	}

	public String get(String key) {
		if (this.headers.containsKey(key))
			return this.headers.get(key);

		return null;
	}

	public void remove(String key) {
		this.headers.remove(key);
	}

	public String toString() {
		String headstring = "Accept: " + this.headers.get("Accept") + "\r\n"
				+ "Accept-Language: " + this.headers.get("Accept-Language")
				+ "\r\n" + "UA-CPU: " + this.headers.get("UA-CPU") + "\r\n"
				+ "User-Agent: " + this.headers.get("User-Agent") + "\r\n"
				+ "Host: " + this.headers.get("Host") + "\r\n\r\n";
		return headstring;
	}
}
