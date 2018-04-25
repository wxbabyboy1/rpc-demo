package socket.http.demo.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 支持使用Http Keep-live功能的连接复用请求对像
 * 可以减少客户端到服务器的连接次数
 * 该对像创建的Request和Response都<font color="red">不是线程安全</font>的
 */
public class QuickWebRequest {
	private QuickWebResponse response;
	private boolean keepalive = true;
	private URI refuri = null;

	public WebHeaderCollection headers;
	public String header;
	public URI requestUri;
	public String Method = "GET";

	/**
	 * 设置引用的URL
	 * 
	 * @param refuri
	 */
	public void setRefuri(URI refuri) {
		this.refuri = refuri;
		if (refuri != null) {
			this.headers.put("Referer", refuri.getPath());
		} else {
			if (this.headers.get("Referer") != null) {
				this.headers.remove("Referer");
			}
		}
	}

	public URI getRefuri() {
		return refuri;
	}

	/**
	 * 构造函数用来创建一个新的web请求对象
	 * 
	 * @param uri
	 *            要下载的页面的Url
	 * @param refuri
	 *            页面的引用页的Url
	 * 
	 */
	private QuickWebRequest(URI uri, URI refuri, boolean bKeepAlive) {
		this.headers = new WebHeaderCollection();
		this.requestUri = uri;
		this.refuri = refuri;
		this.headers.put("Accept", "*/*");
		this.headers.put("Accept-Language", "zh-cn");
		this.headers.put("UA-CPU", "x86");
		this.headers.put("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1)");
		this.headers.put("Host", uri.getHost());
		if (this.keepalive) {
			this.headers.put("Connection", "Keep-Alive");
		}
		this.setRefuri(refuri);
	}

	/**
	 * 创建http请求对象
	 * @param uri
	 * @param refuri
	 * @param AliveRequest
	 * @param bKeepAlive
	 * @return
	 * @throws IOException
	 */
	public static QuickWebRequest create(URI uri, URI refuri,
			QuickWebRequest AliveRequest, boolean bKeepAlive)
			throws IOException {
		if (bKeepAlive && AliveRequest != null && AliveRequest.response != null
				&& AliveRequest.response.isKeepAlive()
				&& AliveRequest.response.IsSocketConnected()
				&& AliveRequest.requestUri.getHost().equals(uri.getHost())) {
			AliveRequest.requestUri = uri;
			AliveRequest.setRefuri(refuri);

			return AliveRequest;
		}

		// 如果原socket还在连接状态,但是主机名发生了变化，导致连接不能复用，需要关闭原socket
		if (AliveRequest != null && AliveRequest.response != null
				&& AliveRequest.response.isKeepAlive()
				&& AliveRequest.response.IsSocketConnected()
				&& AliveRequest.requestUri != null
				&& !AliveRequest.requestUri.getHost().equals(uri.getHost())) {
			AliveRequest.response.Close();
		}

		return new QuickWebRequest(uri, refuri, bKeepAlive);
	}

	/**
	 * 获取服务器响应对象
	 */
	public QuickWebResponse getResponse(int Timeout) throws IOException,
			URISyntaxException {
		// 如果连接不能复用，则重新创建
		if (response == null || !response.IsSocketConnected()) {
			response = new QuickWebResponse(Timeout);
			response.ConnectAndGetHeader(this);
		} else {
			// 连接并获取头信息
			response.ConnectAndGetHeader(this);
		}

		return response;
	}
}
