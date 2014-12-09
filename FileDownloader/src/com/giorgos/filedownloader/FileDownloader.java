package com.giorgos.filedownloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloader {

	public static void startDownloading(String file) {
		System.out.println("Start downloading");

		try{
			FileReader fileReader = new FileReader(new File(file));
			
			BufferedReader br = new BufferedReader(fileReader);
			
			String line = null;
			// if no more lines the readLine() returns null
			while ((line = br.readLine()) != null) {
				// reading lines until the end of the file
				System.out.println("DownloadAsync: " + line);
				downloadFileAsync(line);
			}
			
			br.close();
			
		} catch (FileNotFoundException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void downloadFileAsync(final String url){
		
		Thread download = new Thread(){
		    public void run(){
		    	try{
		    		System.out.println("Start download");
		    		downloadFile(url);
		    		System.out.println("Download completed!");
		    	} catch(IOException e){
		    		e.printStackTrace();
		    		System.out.println("Download failed!");
		    	}
		    }
		};
		download.start();
	}
	
	private static void downloadFile(String urlString) throws IOException {
		System.out.println("Downloading file...");
		
		String outputFileName = urlString.replace("http://video.ted.com/talks/podcast/audio", "");
		String outFilePath = "res/output/" + outputFileName;

		InputStream inputStream = null;
	    FileOutputStream fileOutputStream = null;
		URL url = new URL(urlString);

	    try {
	        URLConnection urlConn = url.openConnection();//connect

	        inputStream = urlConn.getInputStream();               //get connection inputstream
	        fileOutputStream = new FileOutputStream(outFilePath);   //open outputstream to local file

	        byte[] buffer = new byte[4096];              //declare 4KB buffer
	        int len;

	        //while we have availble data, continue downloading and storing to local file
	        while ((len = inputStream.read(buffer)) > 0) {  
	            fileOutputStream.write(buffer, 0, len);
	        }
	    } finally {
	        try {
	            if (inputStream != null) {
	                inputStream.close();
	            }
	        } finally {
	            if (fileOutputStream != null) {
	                fileOutputStream.close();
	            }
	        }
	    }
	}
}
