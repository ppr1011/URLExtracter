package business;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import downloader.CurrentPage;
import downloader.DownloadTool;

public class JavOnline {
	public static String baseUrl = "https://watchjavonline.com/category/hdfhd/";
	public static String folder = "hd";
	public static String downloadUrl = "F:/XMPCache/Javonline/"+folder+"/downloadUrl.txt";
	public static String onlineUrl = "F:/XMPCache/Javonline/"+folder+"/onlineUrl.txt";
	/**
	 * ���չʾҳ�������url
	 * @param url
	 * @return
	 * @throws ParserException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static Set<String> getUrlFromPage(String url) throws ParserException, MalformedURLException, IOException{
		Set<String> urlSet = new TreeSet<String>();
		CurrentPage cp = new CurrentPage(url);
		NodeFilter filter = DownloadTool.getFilterByAttribute("class","post-headline");		
		NodeList nodes = DownloadTool.getNodesFromCurrentPage(cp, filter);
		if(nodes!=null){
			urlSet = DownloadTool.getUrlSetFromNodeList(nodes);
		}
		else{
			System.out.println("��ҳ���޵�ǰԪ��");
		}
		return (TreeSet<String>)urlSet;
	}
	

	
	 
	/**
	 * ��û��չʾҳ�������url��Ӧ����Դ��Ƶ����
	 * @param url
	 * @return
	 * @throws ParserException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static Set<String> getResourceUrlSetFromPage(String url) throws ParserException, MalformedURLException, IOException{
		Set<String> urlSet = getUrlFromPage(url);
		Set<String> resourceUrlSet = new TreeSet<String>();
		for(String baseUrl:urlSet){
			String resourceUrl = getResourceUrl(baseUrl);			
			System.out.println("resourceUrl:"+resourceUrl);
			resourceUrlSet.add(resourceUrl);			
		}
		return resourceUrlSet;		
	}
	

	
	/**
	 * ��õ�������ҳ�����Դ��Ƶ����
	 * @param url
	 * @return
	 * @throws ParserException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static String getResourceUrl(String url) throws ParserException, MalformedURLException, IOException{
		Set<String> urlSet = new TreeSet<String>();
		NodeFilter filter  = DownloadTool.getFilterByTagName("iframe"); 
		CurrentPage cp = new CurrentPage(url);
		NodeList nodes = DownloadTool.getNodesFromCurrentPage(cp, filter);		
		if(nodes!=null){
			urlSet = DownloadTool.getUrlSetFromNodeList(nodes);
		}
		else{
			System.out.println("iframeΪ��");
		}
		if(!urlSet.isEmpty()){
			Iterator<String> iterator = urlSet.iterator();		
			String resourceUrl = iterator.next();
			if(resourceUrl==null||resourceUrl.isEmpty()){
				return "��ҳ������Դ:"+url;
			}
			else{
				return resourceUrl;
			}
		}
		else
			return "��ҳ������Դ:"+url;
	}
	
	
	
	/*
	public static Set<String> getRealUrlFromResourceUrlSet(Set<String> resourceUrlSet,String prefix,String suffix,String regex){
		Set<String> realUrlSet = new TreeSet<String>();
		for(String resourceUrl:resourceUrlSet){			
			String resourceName = resourceUrl.replace(regex, "");
			String realUrl = prefix + resourceName + suffix;
			System.out.println(realUrl);
			realUrlSet.add(realUrl);			
			
		}
		return realUrlSet;
	}
	*/
	/**
	 * ��Դ��Ƶ�����µ���ʵ����
	 * @param url
	 * @return
	 * @throws ParserException
	 * @throws MalformedURLException
	 * @throws IOException
	*/
	public static String getRealUrl(String url) throws ParserException, MalformedURLException, IOException{		
        String regex = "\"streamurl\">[^<]+";
        String replace = "\"streamurl\">";
		String streamUrl=DownloadTool.getContentFromSourcePageBySelenium(url, regex, replace);		
		return "https://openload.co/stream/"+streamUrl+"?mime=true";
	}
	
	
	/**
	 * ��û��չʾҳ�������url��Ӧ����ʵ��Ƶ����
	 * @param url
	 * @return
	 * @throws ParserException
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static Set<String> getRealUrlSetFromPage(String url) throws ParserException, MalformedURLException, IOException{
		Set<String> urlSet = getResourceUrlSetFromPage(url);
		
		Set<String> realUrlSet = new TreeSet<String>();
		for(String baseUrl:urlSet){
			String realUrl = getRealUrl(baseUrl);
			System.out.println("realUrl:"+realUrl);
			//cdn�ض���
			realUrlSet.add(realUrl);
		}				
		return realUrlSet;
	}
	
	
	public static void startGetUrl(String folder,String baseUrl,int pageNum,int frontPage) throws IOException{
		
		
		//Runnable a = new JavOnlineTask(folder, baseUrl, pageNum,frontPage);
		//Thread threada = new Thread(a);
		//threada.start();
		
		getRediretUrlTask task = new getRediretUrlTask("F:\\XMPCache\\Javonline\\mywife\\downloadUrl.txt");
		new Thread(task).start();
	}
	
}
