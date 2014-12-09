package com.giorgos.filedownloader;

public class MainClass {

	public static void main(String[] args) {
		String filePath = System.getProperty("user.dir") + "/res/urls.txt";
		System.out.println(filePath);
		FileDownloader.startDownloading(filePath);
	}
}
