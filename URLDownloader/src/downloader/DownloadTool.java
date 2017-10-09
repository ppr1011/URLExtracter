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
	 * ��̬�飬ִֻ��һ��
	 */
	static{
		System.setProperty("webdriver.gecko.driver", "F:\\Selenium\\geckodriver.exe");
		System.setProperty("webdriver.firefox.bin","D:\\Program Files (x86)\\Mozilla Firefox\\firefox.exe");		
		driver = new FirefoxDriver();
		driver.manage().timeouts().pageLoadTimeout(300,TimeUnit.SECONDS);
	}
	
	
	/**
	 * ���ݱ�ǩ��������������ֵ���������
	 * @param attName
	 * @param attValue
	 * @return
	 */
	public static NodeFilter getFilterByAttribute(String attName,String attValue){
		NodeFilter attFilter = new HasAttributeFilter(attName,attValue);
		return attFilter;
	}
	
	/**
	 * ���ݱ�ǩ���������ݴ��������
	 * @param text
	 * @return
	 */
	public static NodeFilter getFilterByTextString(String text){
		return new StringFilter(text);
	}
	
	
	/**
	 * ���ݱ�ǩ�����������
	 * @param tagName
	 * @return
	 */
	public static NodeFilter getFilterByTagName(String tagName){
		return new TagNameFilter(tagName);
	}
	
	
	/**
	 * ���˳���ǩ
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
			System.out.println("û���ҵ�ƥ��Ľڵ�");
			return null;
		}
		
	}
	
	/**
	 * �ӱ�ǩ��ƥ��url
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
	 * �ӱ�ǩ��ƥ������
	 * @param node
	 * @return
	 */
	public static String getAttValueFromTag(Node node,String attName){
		String nodetext = node.toHtml();		
		String regex = attName+"[\\s]*=[\\s]*[\"].*[\"]";  //ƥ������src="cat.jpg",title="cat" ������
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
	 * ȡ�õ�ǰtag�����°���ĳ�����Ե���������ֵ
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
	 * ��ȡ��ǰ�ڵ��б��url����
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
            	System.out.println("��ȡurl�ڵ�ʧ��");
            }
        }  
        return urlSet;
    }
	
	/**
	 * ʹ��Selenuim�Զ������Թ��� ģ���������ȡԴ���룬����������ʽ��ȡ���ݣ����ÿ��ַ������replace���ַ���
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
        	if(replace == ""){   //Ϊ��ֱ�ӷ�������ƥ���ַ���
        		content = base;
        	}
        	else{
        		content = base.replace(replace, "");
        	}
        	
        }        
        return content;
	}	
	
	

	
}
