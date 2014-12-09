package com.test.client;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import com.google.gson.Gson;
import com.test.client.models.RequestParameters;


public class RequestSender {
	
	private static final String DEFAULT_HOST = "127.0.0.1";
	private static final int DEFAULT_PORT = 4444;
	
	public void sendRequest(RequestParameters data){
        System.out.println("Send Request");
        Socket clientSocket = null;

        try {
            clientSocket = new Socket(DEFAULT_HOST, DEFAULT_PORT);
            
            OutputStreamWriter outputStream = new OutputStreamWriter(clientSocket.getOutputStream());
            
            Gson gson = new Gson();
            
            outputStream.write( gson.toJson(data) );
            outputStream.flush();
            outputStream.close();
            
            clientSocket.close();
        } catch (UnknownHostException e) {
            System.out.println("Unknown host!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO error!");
            e.printStackTrace();
        }
	}
}
