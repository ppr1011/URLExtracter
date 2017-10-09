package business;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Set;
import java.util.TreeSet;

import org.htmlparser.util.ParserException;

import downloader.DownloadTool;
import downloader.UrlDownloader;

public class getRediretUrlTask implements Runnable {
	
	String filename;
	Set<String> targetUrlSet = new TreeSet<String>();
	String targetSaveUrl;
	public getRediretUrlTask(String filename) throws IOException{
		this.filename = filename;
		targetSaveUrl = filename.replace("downloadUrl", "targetLink");
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {			
			if(!filename.isEmpty()){
				File file = new File(filename);
				if(!file.exists()){
					if(file.createNewFile()){
						System.out.println("创建文件"+filename+"成功");						
					}
					else{
						System.out.println("创建文件"+filename+"失败，文件可能已经存在");		
					}
				}
				if(file.isFile()){
					FileReader reader = new FileReader(file);
					BufferedReader br=new BufferedReader(reader);				
					String line="";
			        while ((line=br.readLine())!=null) {
			            System.out.println(line);
			            line.replaceAll("\n", "");
						if(line.startsWith("https:")){
							String targetUrl;
							try {
								targetUrl = UrlDownloader.getRediretUrl(line);
								System.out.println(targetUrl);
								targetUrlSet.add(targetUrl);
							} catch (ParserException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
			        }
			        UrlDownloader.saveUrl(targetUrlSet, targetSaveUrl);
				}
				else{
					System.out.println("该路径不指向文件");
				}
			}
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
