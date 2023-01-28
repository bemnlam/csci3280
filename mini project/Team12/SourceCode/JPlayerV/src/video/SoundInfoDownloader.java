package video;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;


public class SoundInfoDownloader {
	
	public String findLyrics(String keyword) {
		return sim2tradLyrics(searchLrcFile(keyword));
	}
	
	// get trad content using regEx
	private String translateLyrics(String input){
		String tradStr = "";
		String magic = "(.+)/></head>(.*?)</td>";
		Pattern pat = Pattern.compile(magic, Pattern.DOTALL);
		Matcher mat = pat.matcher(input);
		
		if(mat.find()) {
			//System.out.println(mat.group(2));
			tradStr = mat.group(2);
		} else {
			System.out.println("not match");
		}
		return tradStr;
	}
	
	public void saveLyrics(String lyrics, String wavFullpath) {
		String lrcFullpath = wavFullpath.replace(".wav", ".lrc");
		PrintWriter writer;
		if(!lyrics.isEmpty()){
			try {
				writer = new PrintWriter(lrcFullpath, "UTF-8");
				writer.write(lyrics);
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("FileNotFoundException");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("UnsupportedEncodingException");
			}
		}
	}
	
	// httpclient test
	private String sim2tradLyrics(String url){
		if(url==""){
			return "";
		}
		
		String lyrics = "";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("http://cdict.info/convert/g2b_url.php");
		ArrayList <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("url", url));//"music.baidu.com/data2/lrc/13839727/13839727.lrc"));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			System.out.println("httpEntity err");
		}
		
        try {
        	CloseableHttpResponse response2 = httpclient.execute(httpPost);
        	//System.out.println("post");
        	System.out.println(response2.getStatusLine());
        	//response2.getEntity().getContent()
        	lyrics = translateLyrics(IOUtils.toString(response2.getEntity().getContent(), "UTF-8"));
        	response2.close();
		} catch (Exception e) {
			System.out.println("lyrics pattern matching err");
		}
        
        try {
			httpclient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return lyrics;
	}
	
	// get lrc link
	private String getLrcFile(String input) {
		String site = "http://music.baidu.com/";
		String link = "";
		String magic = "(.*?)down-lrc-btn(.*?)\'href\':\'(.*?)\'";//\\s{\\s\'href\':\'(.*?)\'\\s}";
		Pattern pat = Pattern.compile(magic, Pattern.DOTALL);
		Matcher mat = pat.matcher(input);

		if(mat.find()){
			//System.out.println(mat.group(3));
			link = site+mat.group(3);
		}else{
			System.out.println("link not match");
		}
		return link;
	}
	
	//search lrc file
	private String searchLrcFile(String keyword) {
		String link="";
		CloseableHttpClient httpclient = HttpClients.createDefault();
		keyword = keyword.replace(" ", "+");
		String site = "http://music.baidu.com/search/lrc?key=" + keyword;
		HttpGet httpGet = new HttpGet(site);
		
        try {
			CloseableHttpResponse response1 = httpclient.execute(httpGet);
			System.out.println(response1.getStatusLine());
			String content = IOUtils.toString(response1.getEntity().getContent());
            // do something useful with the response body
            // and ensure it is fully consumed
            //EntityUtils.consume(entity1);
			link = getLrcFile(content);
            response1.close();
		} catch (Exception e) {
			link = "";
			System.out.println("lrc not search");
		}
        
        return link;
	}
}
