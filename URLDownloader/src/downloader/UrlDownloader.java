package downloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.net.ssl.HttpsURLConnection;

import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;

public class UrlDownloader {
	
	public String url;
	
	public UrlDownloader(String url){
		this.url = url;
	}
	
	
	/**
     * 从网络Url中下载文件
     * @param urlStr
     * @param fileName
     * @param savePath
     * @throws IOException
     */
    public static void  downLoadFromUrl(String urlStr,String fileName,String savePath) throws IOException{
        URL url = new URL(urlStr);  
        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection(); 
        //设置超时间为3秒
        //conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        
        //得到输入流
        InputStream inputStream = conn.getInputStream();  
        //获取自己数组
        byte[] getData = readInputStream(inputStream);    

        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);    
        FileOutputStream fos = new FileOutputStream(file);     
        fos.write(getData); 
        if(fos!=null){
            fos.close();  
        }
        if(inputStream!=null){
            inputStream.close();
        }


        System.out.println("info:"+url+" download success"); 

    }
    
    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        while((len = inputStream.read(buffer)) != -1) {  
            bos.write(buffer, 0, len);  
        }  
        bos.close();  
        return bos.toByteArray();  
    }  
    
    /**
     * 写入文件,保存url
     * @param urlSet
     * @param filename
     * @throws IOException
     */
    public static void saveUrl(Set<String> urlSet,String filename) throws IOException{
    	File file = new File(filename); 
    	if(!file.exists()){
    		if(file.createNewFile()){
    			System.out.println("创建文件:"+file.getName());
    		}
    		else{
    			System.out.println("创建文件失败");
    		}
    	}
    	if(file.isFile()){
    		FileWriter writer = new FileWriter(file,true);
        	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
			writer.append(df.format(new Date()));
			writer.append('\n');
        	for(String url:urlSet){
        		writer.append(url);
        		writer.append('\n');
        	}
        	writer.close();
        	System.out.println("写入文件"+filename+"成功");
    	}
    	else{
    		System.out.println("该路径不是一个文件夹");
    	}
    }
    
	/**
	 * 获取重定向之后的地址
	 * @param url
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws ParserException 
	 */
	public static String getRediretUrl(String url) throws MalformedURLException, IOException, ParserException{		
		HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
		urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		urlConnection.setInstanceFollowRedirects(true);
		//解决重定向问题
		String newUrl;
		Parser htmlParser;
		if(urlConnection.getResponseCode()==301||urlConnection.getResponseCode()==302){
			newUrl = urlConnection.getHeaderField("Location");			
			urlConnection = (HttpURLConnection) new URL(newUrl).openConnection();
			htmlParser = new Parser(urlConnection);
			return htmlParser.getURL();
		}
		else{
			htmlParser = new Parser(urlConnection);
			return htmlParser.getURL();
		}		
	}    
}
