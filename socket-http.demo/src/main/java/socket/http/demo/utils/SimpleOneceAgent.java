package socket.http.demo.utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleOneceAgent {

	/**
	 * 读取一个指定URL的内容并转换为UTF-8字符串返回
	 */
	public String ReadUrlContent(String url) throws Exception {
		return ReadUrlContent(url, true, true);
	}

	/**
	 * 读取一个指定URL的内容并转换为UTF-8字符串返回
	 */
	public String ReadUrlContent(String url, boolean checkredirect,
			boolean checkHtmlRedurect) throws Exception {
		// 检测是否为http://开头
		Pattern purl = Pattern.compile("^.*://.*$");
		if (!purl.matcher(url).matches()) {
			url = "http://" + url;
		}
		QuickWebRequest request = QuickWebRequest.create(new URI(url), null,
				null, false);
		QuickWebResponse response = request.getResponse(15);

		if (checkredirect) {
			if (response.redirectUri != null) {
				String AbsoluteUri = response.redirectUri.getScheme()
						+ "://"
						+ response.redirectUri.getHost()
						+ (response.redirectUri.getRawPath() != null ? response.redirectUri
								.getRawPath() : "")
						+ (response.redirectUri.getRawQuery() != null ? "?"
								+ response.redirectUri.getRawQuery() : "");
				if (AbsoluteUri != url)
					return ReadUrlContent(AbsoluteUri, false, checkHtmlRedurect);
			}
		}

		byte[] bytes = response.ReadResponse();
		if (bytes == null)
			return null;

		Charset charset = HttpUtils.DetectCharset(response, bytes);
		if (charset == null) {
			// 默认使用gb2312
			charset = Charset.forName("gb2312");
		}

		// 完成到utf-8的转码
		if (!charset.name().toLowerCase().equals("utf-8")) {
			bytes = new String(bytes, charset).getBytes(Charset
					.forName("utf-8"));
			charset = Charset.forName("utf-8");
		}

		String content = new String(bytes, charset);
		if (checkHtmlRedurect) {
			String regexstr = "^<META\\s+HTTP-EQUIV\\s*=\\s*[\"]*Refresh[\"]*\\s+CONTENT=[\"\\s]*\\d+\\s*[;]\\s*URL=(.*?)[\\s\"]*>$";
			Pattern p = Pattern.compile(regexstr, Pattern.CASE_INSENSITIVE);
			Matcher matchers = p.matcher(content);
			if (matchers.find()) {
				String reurl = matchers.group(1);
				if (reurl != null && !reurl.isEmpty() && !reurl.equals(url)) {
					return ReadUrlContent(url, checkredirect, false);
				}
			}
		}

		return content;
	}

}
