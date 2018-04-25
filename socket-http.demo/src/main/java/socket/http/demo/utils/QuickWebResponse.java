package socket.http.demo.utils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class QuickWebResponse {
	/**
	 * 最大下载的文件的大小，
	 * 目前定义为10M
	 */
	public final int max_download_length = 10 * 1024 * 1024;
	/**
	 * 向服务器端发送请求后，
	 * ResponseUri有可能会发生变化,
	 * 如果发生了变化则得不到具体的内容，
	 * 需要重新读取新的URL
	 */
	public URI redirectUri = null;
	/**
	 * 当前请求地址
	 */
	public URI requestUri = null;

	/**
	 * 客户端设置KeepAlive后，
	 * 服务器有可能不支持，
	 * 发送请求后这里会获得服务器端是否支持KeepAlive
	 */
	private boolean keepAlive;
	/**
	 * 服务器端返回的ContentType
	 */
	private String contentType;
	/**
	 * 服务器端返回的ContentLength
	 */
	private int contentLength;
	private WebHeaderCollection headers;
	private String header;
	/**
	 * 以秒为单位的超时时间
	 */
	private int timeout = 0;
	/**
	 * 是否已发送了请求
	 */
	private boolean isrequestsend = false;
	/**
	 * 是否已读取了包头
	 */
	private boolean isheaderparsed = false;
	private Socket socket;

	/**
	 * 发送请求后服务器响应的是否支持KeepLive
	 */
	public boolean isKeepAlive() {
		return keepAlive;
	}

	/**
	 * Mime内容的字符串
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * 本次读取的有效内容长度
	 */
	public int getContentLength() {
		return contentLength;
	}

	/**
	 * 服务器返回的Http头信息
	 */
	public String getHeader() {
		return header;
	}

	/**
	 * 当前socket是否还在连接状态
	 */
	public boolean IsSocketConnected() {
		return (this.socket != null && this.socket.isConnected());
	}

	/**
	 * @param TimeoutSeconds
	 *            发送和接收的超时时间秒数
	 */
	public QuickWebResponse(int TimeoutSeconds) {
		this.timeout = TimeoutSeconds;
		this.redirectUri = null;
		this.requestUri = null;
	}

	/**
	 * 发送连接，
	 * 并且接收包头分析,
	 * 只能在连接正常并且keepLive,
	 * 并获取了包头的情况下调用。 
	 * 并且不能调用两次
	 */
	public void ConnectAndGetHeader(QuickWebRequest request)
			throws IOException, URISyntaxException {
		this.isrequestsend = false;
		this.requestUri = request.requestUri;
		this.redirectUri = null;
		this.socket = new Socket();

		// 一般www都是80端口
		int port = this.requestUri.getPort() == -1 ? 80 : this.requestUri
				.getPort();
		SocketAddress address = new InetSocketAddress(
				this.requestUri.getHost(), port);
		if (this.timeout > 0) {
			this.socket.setSoTimeout(this.timeout * 1000);
			this.socket.connect(address, this.timeout * 1000);
		} else {
			this.socket.connect(address);
		}

		// 发送请求
		String path = this.requestUri.getRawPath().equals("") ? "/"
				: this.requestUri.getRawPath();
		String query = this.requestUri.getQuery() == null ? "" : "?"
				+ this.requestUri.getQuery();
		// 经测试，http协议头顺序应该为以下顺序
		// 正确的头 GET / HTTP/1.1\r\nAccept: */*\r\nAccept-Language:
		// zh-cn\r\nUA-CPU: x86\r\nUser-Agent: Mozilla/4.0 (compatible; MSIE
		// 7.0; Windows NT 5.1)\r\nHost: www.sina.com.cn\r\n\r\n
		// 错误的头 GET /
		// HTTP/1.1\r\nAccept-Language:zh-cn\r\nHost:www.sina.com.cn\r\nUser-Agent:Mozilla/4.0
		// (compatible; MSIE 7.0; Windows NT
		// 5.1)\r\nConnection:Keep-Alive\r\nUA-CPU:x86\r\nAccept:*/*\r\n
		request.header = request.Method + " " + path + query + " HTTP/1.1\r\n"
				+ request.headers.toString();
		// OutputStream outStream = this.socket.getOutputStream();
		// 不使用PrintWriter是为了防止乱码，这里使用字节数组
		DataOutputStream writer = new DataOutputStream(
				this.socket.getOutputStream());
		writer.write(request.header.getBytes("US-ASCII"));
		writer.flush();
		this.isrequestsend = true;

		// 接收包头
		ReceiveHeader();
	}

	/**
	 * 接收包头
	 * 
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private void ReceiveHeader() throws IOException, URISyntaxException {
		this.redirectUri = null;
		this.isheaderparsed = false;
		this.header = "";
		this.headers = new WebHeaderCollection();

		// InputStream inStream = this.socket.getInputStream();
		DataInputStream reader = new DataInputStream(
				this.socket.getInputStream());
		byte[] bytes = new byte[10];
		while (reader.read(bytes, 0, 1) > 0) {
			this.header += new String(bytes, 0, 1, "US-ASCII");
			if (new String(bytes, 0, 1).equals("\n")
					&& this.header.endsWith("\r\n\r\n"))
				break;

			// 防止读取的http头过大,超过100k的头明显有问题
			if (this.header.length() > 100 * 1024) {
				break;
			}
		}

		// 获取的响应头格式
		// HTTP/1.0 200 OK\r\nLast-Modified: Sun, 26 Aug 2012 15:29:15
		// GMT\r\nAccept-Ranges: bytes\r\nX-Powered-By:
		// mod_xlayout/rc2\r\nContent-Type: text/html\r\nDate: Sun, 26 Aug 2012
		// 15:31:27 GMT\r\nServer: Apache\r\nExpires: Sun, 26 Aug 2012 15:32:27
		// GMT\r\nCache-Control: max-age=60\r\nVary: Accept-Encoding\r\nAge:
		// 24\r\nContent-Length: 667008\r\nX-Cache: HIT from
		// xd33-76.sina.com.cn\r\nConnection: close\r\n\r\n
		// 获取Http头
		Pattern p = Pattern.compile("[^\\r\\n]+", Pattern.MULTILINE);
		// java的trim方法带有去掉尾部\r\n的功能
		String matchheader = this.header.trim();
		Matcher matchers = p.matcher(matchheader);
		int mcount = 0;
		String location = "";
		while (matchers.find()) {
			// 检查重定向问题
			if (mcount == 0)
				location = matchers.group(0);

			String[] strItem = matchers.group(0).split(":", 2);
			if (strItem.length > 1) {
				this.headers.put(strItem[0].trim(), strItem[1].trim());
			}

			mcount++;
			if (mcount > TextHelper.max_match_count)
				break;
		}

		// 检查重定向问题,测试通过
		if (mcount > 0
				&& (location.indexOf(" 302 ") != -1 || location
						.indexOf(" 301 ") != -1)
				&& this.headers.get("Location") != null) {
			// 从Http头中检查是否已经用 "location" 重定向到了新的地址
			if (this.headers.get("Location").toLowerCase()
					.startsWith("http://")
					|| this.headers
							.get("Location")
							.toLowerCase()
							.startsWith(this.requestUri.getHost().toLowerCase())) {
				this.redirectUri = new URI(this.headers.get("Location"));
			} else {
				// 此段代码需要(重点)测试,测试通过
				this.redirectUri = new URI(
						this.requestUri.getScheme()
								+ "://"
								+ this.requestUri.getHost()
								+ (this.headers.get("Location").startsWith("/") ? this.headers
										.get("Location") : "/"
										+ this.headers.get("Location")));
			}
		}

		// 编码信息，类型text/html
		this.contentType = this.headers.get("Content-Type");
		if (this.headers.get("Content-Length") != null) {
			this.contentLength = Integer.parseInt(this.headers
					.get("Content-Length"));
		}

		this.keepAlive = (this.headers.get("Connection") != null
				&& this.headers.get("Connection").toLowerCase()
						.equals("keep-alive") || this.headers
				.get("Proxy-Connection") != null
				&& this.headers.get("Proxy-Connection").toLowerCase()
						.equals("keep-alive"));

		if (this.redirectUri != null
				&& this.redirectUri.getHost() != this.requestUri.getHost()) {
			this.keepAlive = false;
		}
		this.isheaderparsed = true;
	}

	/**
	 * 从socket中获取一个Http块的大小
	 */
	public int GetCunkHead(Socket Insocket, boolean IsStart) throws IOException {
		// skip\r\n
		DataInputStream reader = new DataInputStream(Insocket.getInputStream());
		byte[] RecvBuffer = new byte[32];
		if (!IsStart && reader.read(RecvBuffer, 0, 2) != 2) {
			return 0;
		}

		// 定义字节数组缓冲区，对应c#中MemoryStream
		// 这里也可以尝试ByteBuffer缓冲区操作类:ByteBuffer buffer = ByteBuffer.allocate(64);
		ByteBuffer buffer = ByteBuffer.allocate(64);
		ByteArrayOutputStream streamOut = new ByteArrayOutputStream(64);
		int nBytes, nTotalBytes = 0;
		// 有待测试
		while ((nBytes = reader.read(RecvBuffer, 0, 1)) == 1) {
			streamOut.write(RecvBuffer, 0, nBytes);
			buffer.put(RecvBuffer, 0, nBytes);
			nTotalBytes++;

			byte[] barray = streamOut.toByteArray();
			if (barray.length >= 2 && barray[barray.length - 1] == '\n'
					&& barray[barray.length - 2] == '\r') {
				break;
			}
		}
		byte[] chunkArray = streamOut.toByteArray();
		streamOut.flush();
		streamOut.close();
		if (chunkArray != null && chunkArray.length > 2) {
			String strChunkSize = new String(chunkArray, 0,
					chunkArray.length - 2, "US-ASCII");
			int chunk_size = Integer.parseInt(strChunkSize, 16);
			return chunk_size;
		} else {
			return 0;
		}
	}

	/**
	 * 获取响应的内容，如果没有内容则返回null
	 */
	public byte[] ReadResponse() throws Exception {
		if (!this.isrequestsend) {
			throw new Exception("还没有发送请求，无法继续从连接中读取数据!");
		}

		if (!this.isheaderparsed) {
			throw new Exception("还没有读取Http包头，无法继续从连接中读取数据!");
		}

		// 这里也可以尝试ByteBuffer缓冲区操作类
		ByteArrayOutputStream streamOut = new ByteArrayOutputStream(10240);
		byte[] RecvBuffer = new byte[10240];
		int nBytes, nTotalBytes = 0;
		byte[] retbytes = null;
		try {
			DataInputStream reader = new DataInputStream(
					this.socket.getInputStream());
			if (this.headers.get("Transfer-Encoding") != null
					&& this.headers.get("Transfer-Encoding").toLowerCase().equals("chunked")) {
				int chunk_size = 0;
				chunk_size = GetCunkHead(this.socket, true);
				while (this.IsSocketConnected() && chunk_size > 0) {
					if (RecvBuffer.length < chunk_size) {
						RecvBuffer = new byte[chunk_size];
					}
					// 读取chunk块
					while ((nBytes = reader.read(RecvBuffer, 0, chunk_size)) > 0) {
						streamOut.write(RecvBuffer, 0, nBytes);
						if (nBytes != chunk_size) {
							chunk_size = chunk_size - nBytes;
						} else {
							break;
						}
					}
					// 读取chunk头
					chunk_size = GetCunkHead(this.socket, false);
				}
			} else {
				while (this.IsSocketConnected()
						&& (nBytes = reader.read(RecvBuffer, 0, 10240)) > 0) {
					nTotalBytes += nBytes;
					streamOut.write(RecvBuffer, 0, nBytes);

					// 对于有内容长度的服务器的处理
					if (this.contentLength > 0
							&& nTotalBytes >= this.contentLength) {
						break;
					}

					// 如果内容超过指定大小，则结束下载，并且该连接已经不可重用
					if (nTotalBytes >= max_download_length) {
						this.keepAlive = false;
						break;
					}
				}
			}
		} catch (Exception ex) {
			this.keepAlive = false;
			if (streamOut == null || streamOut.size() == 0) {
				throw ex;
			}
		} finally {
			if (streamOut != null) {
				streamOut.flush();
				streamOut.close();
			}
			if (!this.keepAlive)
				Close();// close response
		}

		retbytes = streamOut.toByteArray();
		this.isrequestsend = false;
		this.isheaderparsed = false;
		return retbytes;
	}

	/**
	 * 在Keep-live的情况下，
	 * 如果应用程序没有退出，
	 * 或者下载的主机没有改变，
	 * 就不需要调用Close,
	 * 使用原有的 tcp连接可以重用已经建立的
	 */
	public void Close() throws IOException {
		if (this.IsSocketConnected()) {
			this.socket.close();
			this.socket = null;
		}
	}
}
