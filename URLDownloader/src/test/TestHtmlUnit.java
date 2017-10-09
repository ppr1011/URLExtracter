package test;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TestHtmlUnit {
	public void main() throws Exception {
		String url = "";
		String str;
		//ʹ��Chrome��ȡ��ҳ
		WebClient webClient = new WebClient(BrowserVersion.CHROME);

		// �򿪵Ļ�������ִ��javaScript/Css
		webClient.getOptions().setJavaScriptEnabled(false);
		webClient.getOptions().setCssEnabled(false);

		// ��ȡҳ��
		HtmlPage page = webClient.getPage(url);

		// ��ȡҳ���XML����
		str = page.asXml();
		System.out.println("Xml:------" + str);
		// ��ȡҳ����ı�
		System.out.println("Text:------" + str);
		
		// �ر�webClient
		webClient.close();
	}
}
