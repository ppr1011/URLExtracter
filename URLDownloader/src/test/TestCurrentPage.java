package test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.TreeSet;

import org.htmlparser.util.ParserException;
import org.junit.Test;

import business.JavOnline;
import downloader.CurrentPage;
import downloader.UrlDownloader;


public class TestCurrentPage {

	//@Test
	public void testGetUrlContentNew() throws IOException, ParserException{
		String baseUrl = "https://watchjavonline.com/category/hdfhd/";
		String folder = "hd";
		String downloadUrl = "F:/XMPCache/Javonline/"+folder+"/downloadUrl.txt";
		String onlineUrl = "F:/XMPCache/Javonline/"+folder+"/onlineUrl.txt";

		String url = "";
		for(int i=1;i<14;i++){  //第一次到了i=13,下一次应从i=14开始
			if(i==1){
				url=baseUrl;
			}
			else
				url = baseUrl+"page/"+i+"/";
			TreeSet<String> realUrlSet = (TreeSet<String>) JavOnline.getRealUrlSetFromPage(url);
			UrlDownloader.saveUrl(realUrlSet, downloadUrl);
		}		
	}
	
	
	public void testGetCurrentPageContentOld() throws ParserException, MalformedURLException, IOException{
		//JavOnline.startGetUrl();		
	}	
	
	public static void main(String[] args){
		String folder = "mywife";
		String baseUrl = "https://watchjavonline.com/category/mywife/";
		int lastPage = 13;
		int frontPage = 1;
		try {
			JavOnline.startGetUrl(folder,baseUrl,lastPage,frontPage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
