package downloader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.*;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.StringFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class DownloadTool {

	public static WebDriver driver = null;
	
	/*
	 * 静态块，只执行一次
	 */
	static{
		System.setProperty("webdriver.gecko.driver", "F:\\Selenium\\geckodriver.exe");
		System.setProperty("webdriver.firefox.bin","D:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");		
		driver = new FirefoxDriver();
		driver.manage().timeouts().pageLoadTimeout(300,TimeUnit.SECONDS);
	}
	
	
	/**
	 * 根据标签的属性名和属性值创造过滤器
	 * @param attName
	 * @param attValue
	 * @return
	 */
	public static NodeFilter getFilterByAttribute(String attName,String attValue){
		NodeFilter attFilter = new HasAttributeFilter(attName,attValue);
		return attFilter;
	}
	
	/**
	 * 根据标签包含的内容创造过滤器
	 * @param text
	 * @return
	 */
	public static NodeFilter getFilterByTextString(String text){
		return new StringFilter(text);
	}
	
	
	/**
	 * 根据标签名创造过滤器
	 * @param tagName
	 * @return
	 */
	public static NodeFilter getFilterByTagName(String tagName){
		return new TagNameFilter(tagName);
	}
	
	
	/**
	 * 过滤出标签
	 * @param cp
	 * @param filter
	 * @return
	 * @throws ParserException
	 */
	public static NodeList getNodesFromCurrentPage(CurrentPage cp,NodeFilter filter) throws ParserException{
		Parser htmlParser = cp.getHtmlParser();
		NodeList nodes = htmlParser.extractAllNodesThatMatch(filter);
		if(nodes!=null){
			return nodes;
		}
		else{
			System.out.println("没有找到匹配的节点");
			return null;
		}
		
	}
	
	/**
	 * 从标签中匹配url
	 * @param node
	 * @return
	 */
	public static String getUrlFromTag(Node node){
		String nodetext = node.toHtml();
		//String urlRegex = "^(?:https?://)?[\\w]{1,}(?:\\.?[\\w]{1,})+[\\w-_/?&=#%:]*$";
		String urlRegex = "http[^\"]+";

		Pattern p = Pattern.compile(urlRegex,Pattern.CASE_INSENSITIVE); 
		Matcher m = p.matcher(nodetext);
		if(m.find()) { 
			return(m.group(0)); 
		}
		else{
			return null;
		}		
	}
	
	
	/**
	 * 从标签中匹配属性
	 * @param node
	 * @return
	 */
	public static String getAttValueFromTag(Node node,String attName){
		String nodetext = node.toHtml();		
		String regex = attName+"[\\s]*=[\\s]*[\"].*[\"]";  //匹配诸如src="cat.jpg",title="cat" 等属性
		Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE); 
		Matcher m = p.matcher(nodetext);
		if(m.find()) { 
			String att = m.group(0);
			String valueRegex = "\"[^\"]+\"";
			Pattern p_1 = Pattern.compile(valueRegex,Pattern.CASE_INSENSITIVE); 
			Matcher m_1 = p_1.matcher(att);
			if(m_1.find()){
				return m_1.group(0).substring(1, m_1.group(0).length()-2);
			}
			return null;
		}
		else{
			return null;
		}		
	}
	
	
	/**
	 * 取得当前tag链表下包含某个属性的它的属性值
	 */
	public static Set<String> getAttValueByAttName(NodeList nodes,String attName){
		Set<String> attValueSet = new TreeSet<String>();
		for(int i=0;i<nodes.size();i++){
			Node node = (Node) nodes.elementAt(i);
			String value = getAttValueFromTag(node,attName);
			attValueSet.add(value);
		}
		return attValueSet;
	}
	
	
	/**
	 * 获取当前节点列表的url集合
	 * @param nodes
	 * @return
	 */
	public static Set<String> getUrlSetFromNodeList(NodeList nodes){  
		Set<String> urlSet = new TreeSet<String>();
        for (int i = 0; i < nodes.size(); i++) {  
            Node node = (Node) nodes.elementAt(i); 
            String url = getUrlFromTag(node);      
            System.out.println("url:"+url);
            if(!url.isEmpty()){
            	urlSet.add(url);
            }
            else{
            	System.out.println("获取url节点失败");
            }
        }  
        return urlSet;
    }
	
	/**
	 * 使用Selenuim自动化测试工具 模拟浏览器获取源代码，根据正则表达式获取内容，再用空字符串替代replace的字符串
	 * @param url
	 * @param regex
	 * @param replace
	 * @return
	 */
	public static String getContentFromSourcePageBySelenium(String url,String regex,String replace){

		driver.get(url);
        String sourcePage = driver.getPageSource();
        Pattern p = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sourcePage);
        String base = null;
        String content = null;
        if(m.find()){
        	base = m.group(0);
        	if(replace == ""){   //为空直接返回正则匹配字符串
        		content = base;
        	}
        	else{
        		content = base.replace(replace, "");
        	}
        	
        }        
        return content;
	}	
	
	

	
}
