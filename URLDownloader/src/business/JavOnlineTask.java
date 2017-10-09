package business;

import java.io.IOException;
import java.util.TreeSet;

import org.htmlparser.util.ParserException;

import downloader.UrlDownloader;

public class JavOnlineTask implements Runnable {
	String baseUrl;
	int pageNum;
	int frontPage;
	String downloadUrl;
	String onlineUrl;
	public JavOnlineTask(String folder,String baseUrl,int pageNum,int frontPage){
		downloadUrl = "F:/XMPCache/Javonline/"+folder+"/downloadUrl"+frontPage+"_"+pageNum+".txt";
		onlineUrl = "F:/XMPCache/Javonline/"+folder+"/onlineUrl"+frontPage+"_"+pageNum+".txt";
		this.pageNum = pageNum;
		this.baseUrl = baseUrl;
		this.frontPage = frontPage;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String url = "";
		for(int i=frontPage;i<=pageNum;i++){  //第一次到了i=pageNum,下一次应从pageNum+1开始
			if(i==1){
				url=baseUrl;
			}
			else
				url = baseUrl+"page/"+i+"/";
			TreeSet<String> realUrlSet;
			try {
				realUrlSet = (TreeSet<String>) JavOnline.getRealUrlSetFromPage(url);
				try {
					UrlDownloader.saveUrl(realUrlSet, downloadUrl);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ParserException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
	}

}
