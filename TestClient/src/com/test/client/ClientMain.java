package com.test.client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.test.client.models.RequestParameters;
import com.test.client.models.RequestParameters.RequestType;




/**
 *
 * @author georgios.savvidis
 */
public class ClientMain {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

    	String filePath = System.getProperty("user.dir") + "/res/AdoraSvitak_2010.wav";
    	Path path = Paths.get(filePath);
    	
    	byte[] voiceSample = null;

    	try{
    		voiceSample = Files.readAllBytes(path);
    		
    	} catch (IOException e){
    		e.printStackTrace();
    	}

    	RequestParameters params = new RequestParameters("AdoraSvitak", voiceSample, System.currentTimeMillis(), RequestType.CREATE_USER);
    	
    	RequestSender sender = new RequestSender();
    	sender.sendRequest( params );
    }
}