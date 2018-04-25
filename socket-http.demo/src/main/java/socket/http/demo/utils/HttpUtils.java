package socket.http.demo.utils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtils {

	/**
	 * 规范化并合并URL
	 * 
	 * @baseUri 基本url
	 * @urlpath 访问路径，或完全的url
	 */
	public static URI CombineUrl(URI baseUri, String urlpath) {
		if (urlpath == null || urlpath.isEmpty())
			return null;

		String lowverUrl = urlpath.toLowerCase();
		try {
			if (!lowverUrl.startsWith("http://")
					&& !lowverUrl.startsWith("https://")) {
				return new URI(baseUri.getScheme() + urlpath);
			} else {
				return new URI(urlpath);
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 自动检测内容的charset,通过字符集和html中指定的contentType来获取 如果无法获得字符集，则返回null
	 */
	public static Charset DetectCharset(QuickWebResponse response, byte[] bytes)
			throws UnsupportedEncodingException {
		String regexstr = "(?=text/html|text/xml|application/x-javascript).*?charset=([^\"]+)";// 可能的编码方式见jdk的CharSet类说明ISO-8859-1
		Pattern p = Pattern.compile(regexstr, Pattern.CASE_INSENSITIVE
				| Pattern.MULTILINE);
		String contentType = response.getContentType();
		if (contentType != null) {
			Matcher matchers = p.matcher(contentType);
			if (matchers.find()) {
				String charset = matchers.group(1).toUpperCase();
				Charset encoder = Charset.forName(charset);
				if (encoder != null) {
					return encoder;
				}
			}
		}

		String ascii = new String(bytes, "US-ASCII");
		Matcher matchers1 = p.matcher(ascii);
		Charset currentCharset = null;
		if (matchers1.find()) {
			String charset = matchers1.group(1).toUpperCase();
			try {
				currentCharset = Charset.forName(charset);
			} catch (Exception ex) {
				// currentCharset = null;
			}
		}

		return currentCharset;
	}

	/**
	 * org.apache.commons.lang.StringEscapeUtils 可以代替 对html做Encode编码
	 */
	public static String HtmlEncode(String html) {
		StringBuffer stringbuffer = new StringBuffer();
		int j = html.length();
		for (int i = 0; i < j; i++) {
			char c = html.charAt(i);
			switch (c) {
			case 60:
				stringbuffer.append("&lt;");
				break;
			case 62:
				stringbuffer.append("&gt;");
				break;
			case 38:
				stringbuffer.append("&amp;");
				break;
			case 34:
				stringbuffer.append("&quot;");
				break;
			case 169:
				stringbuffer.append("&copy;");
				break;
			case 174:
				stringbuffer.append("&reg;");
				break;
			case 165:
				stringbuffer.append("&yen;");
				break;
			case 8364:
				stringbuffer.append("&euro;");
				break;
			case 8482:
				stringbuffer.append("&#153;");
				break;
			case 13:
				if (i < j - 1 && html.charAt(i + 1) == 10) {
					stringbuffer.append("<br>");
					i++;
				}
				break;
			case 32:
				if (i < j - 1 && html.charAt(i + 1) == ' ') {
					stringbuffer.append(" &nbsp;");
					i++;
					break;
				}
			default:
				stringbuffer.append(c);
				break;
			}
		}
		return new String(stringbuffer.toString());
	}

}
