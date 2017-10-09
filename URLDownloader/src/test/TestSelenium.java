package test;


import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import downloader.DownloadTool;



public class TestSelenium {
	
	
	
	//@Test
	public void testSelenium(){
		String url = "https://openload.co/embed/8HB_Rbhnkyw/STAR-810HD.mp4";
        String regex = "\"streamurl\">[^<]+";
        String replace = "\"streamurl\">";
        String keyLink = DownloadTool.getContentFromSourcePageBySelenium(url,regex,replace);
        System.out.println(keyLink);
	}
}
