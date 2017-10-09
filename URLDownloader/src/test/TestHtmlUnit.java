package test;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TestHtmlUnit {
	public void main() throws Exception {
		String url = "";
		String str;
		//使用Chrome读取网页
		WebClient webClient = new WebClient(BrowserVersion.CHROME);

		// 打开的话，就是执行javaScript/Css
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);

		// 获取页面
		HtmlPage page = webClient.getPage(url);

		// 获取页面的XML代码
		str = page.asXml();
		System.out.println("Xml:------" + str);
		// 获取页面的文本
		System.out.println("Text:------" + str);
		
		// 关闭webClient
		webClient.close();
	}
}
