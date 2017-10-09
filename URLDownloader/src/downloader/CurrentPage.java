package downloader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.TextExtractingVisitor;

public class CurrentPage {

	public static String currentUrl;
	public static Parser htmlParser;	
	
	
	/**
	 * 
	 * @param url
	 * @throws ParserException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public CurrentPage(String url) throws ParserException, MalformedURLException, IOException{
		currentUrl = url;		
		HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
		urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		urlConnection.setInstanceFollowRedirects(true);
		//InputStream is = urlConnection.getInputStream();
		//BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
		//String ss;
		//while((ss = reader.readLine())!=null) {
		//		System.out.println(ss);
		//	}

		//解决重定向问题
		if(urlConnection.getResponseCode()==301||urlConnection.getResponseCode()==302){
			String newUrl = urlConnection.getHeaderField("Location");
			currentUrl = newUrl;
			urlConnection = (HttpURLConnection) new URL(newUrl).openConnection();
			urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		}
		htmlParser = new Parser(urlConnection);
		//TextExtractingVisitor visitor = new TextExtractingVisitor();  
		//htmlParser.visitAllNodesWith(visitor);  
        //String textInPage = visitor.getExtractedText();
        //System.out.println(textInPage);
		//打印头
		/*Map<String,List<String>> map = urlConnection.getHeaderFields();
		for(Entry<String,List<String>> entry:map.entrySet()){
			System.out.println(entry.getKey());
			List<String> list = entry.getValue();
			for(String string:list){
				System.out.println(string);
			}
			System.out.println();
		}*/
		//System.out.println(getUrlContentNew());
	}
	
	/**
	 * 使用HTML PARSER获取网页源代码
	 * @return
	 * @throws ParserException
	 */
	public static String getUrlContentNew() throws ParserException{
		TextExtractingVisitor visitor = new TextExtractingVisitor();		
		htmlParser.visitAllNodesWith(visitor);
        return visitor.getExtractedText();
	}
	
	
	/**
	 * 使用旧方法获取网页所有内容
	 * @return
	 * @throws IOException
	 */
	public static String getUrlContent(String tUrl) throws IOException{		
		URL url = new URL(tUrl);
		InputStream inputStream = url.openStream();		
		InputStreamReader reader = new InputStreamReader(inputStream);
		BufferedReader buffer = new BufferedReader(reader);		
		String str;
		StringBuilder stringBuilder = new StringBuilder();
		while((str=buffer.readLine())!=null){			
			stringBuilder.append(str);
		}
		return stringBuilder.toString();
	}
	
	public String getCurrentUrl() {
		return currentUrl;
	}

	public void setCurrentUrl(String currentUrl) {
		this.currentUrl = currentUrl;
	}

	public Parser getHtmlParser() {
		return htmlParser;
	}

	public void setHtmlParser(Parser htmlParser) {
		this.htmlParser = htmlParser;
	}
}
