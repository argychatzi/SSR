package com.kth.ssr.network;

import java.io.File;

import com.bitsinharmony.recognito.utils.Logger;
import com.giorgos.agent.ObjectSizeFetcher;
import com.j256.ormlite.logger.LocalLog;
import com.kth.recognitohelper.RecognitoHelper;
import com.kth.recognitohelper.database.models.MatchedRecord;


/**
 *
 * @author georgios.savvidis
 */
public class ServerMain {
    
	private static final String TAG = ServerMain.class.getCanonicalName();
	
    public static void main(String[] args) {
    	
    	//Disable DEBUG and INFO logs that are spit out by the Ormlite library
    	System.setProperty(LocalLog.LOCAL_LOG_LEVEL_PROPERTY, "ERROR");
		
//    	SocketMonitor socketMonitor = new SocketMonitor();
//    	socketMonitor.startListening();
    	
    	testRecognito();
    }
    
    private static void testRecognito(){
//    	Logger.d(TAG, "Test recognito");
    	String giorgos1 = System.getProperty("user.dir") + "/res/giorgos_part1.wav";
    	String giorgos2 = System.getProperty("user.dir") + "/res/giorgos_part2.wav";
    	String katha1 = System.getProperty("user.dir") + "/res/katha_part1.wav";
    	String katha2 = System.getProperty("user.dir") + "/res/katha_part2.wav";

    	String giorgosNoise1 = System.getProperty("user.dir") + "/res/giorgos_noise_part1.wav";
    	String giorgosNoise2 = System.getProperty("user.dir") + "/res/giorgos_noise_part2.wav";
    	String kathaNoise1 = System.getProperty("user.dir") + "/res/katha_noise_part1.wav";
    	String kathaNoise2 = System.getProperty("user.dir") + "/res/katha_noise_part2.wav";
    	
    	
//		Logger.d(TAG, "---> File size: " + ObjectSizeFetcher.getObjectSize(new File(giorgos1)));
//		Logger.d(TAG, "---> Voiceprint size: " + ObjectSizeFetcher.getObjectSize(voicePrint));

    	
    	RecognitoHelper recognitoHelper = new RecognitoHelper(44100);
    	recognitoHelper.createUser("Giorgos", new File(giorgos1));
//    	recognitoHelper.createUser("Katha", new File(kathaNoise1));
    	
//    	MatchedRecord user = recognitoHelper.recognizeAndUpdateUser(new File(kathaNoise2));
    	long timeBefore = System.currentTimeMillis();
//    	MatchedRecord user = recognitoHelper.recognizeAndUpdateUser(new File(System.getProperty("user.dir") + "/raw/AlanRussell_2006_trimmed.wav"));
    	MatchedRecord user = recognitoHelper.recognizeAndUpdateUser(new File(kathaNoise2));
    	Logger.d(TAG, "Finished recognition. Duration = " + ( System.currentTimeMillis() - timeBefore ) );
    	
    	if(user != null){
    		Logger.d(TAG, "Username: " + user.getUser().getUsername());
    		Logger.d(TAG, "Likelihood: " + user.getLikelihood());
    	}
    	
//    	recognitoHelper2.createUser("Adora", new File(filePath));
//    	recognitoHelper2.createUser("AlaindeBotton", new File(filePath2));
    	
//    	RecognitoHelper recognitoHelper = new RecognitoHelper(44100);
//    	recognitoHelper2.createUser("Adora", new File(filePath));
//		recognitoHelper2.createUser("AlaindeBotton", new File( filePath2 ) );
//
//    	RecognitoHelper recognitoHelper = new RecognitoHelper(16000);
//    	MatchedRecord user2 = recognitoHelper2.recognizeAndUpdateUser(new File(filePath2));
//    	
//    	if(user2 != null){
//    		Logger.d(TAG, "Username: " + user2.getUser().getUsername());
//    		Logger.d(TAG, "Likelihood: " + user2.getLikelihood());
//    	}
    	
//    	RecognitoHelper recognitoHelper2 = new RecognitoHelper(44100);
//		recognitoHelper2.createUser("AlaindeBotton", new File( filePath2 ) );
//    	MatchedRecord user2 = recognitoHelper2.recognizeAndUpdateUser( new File(filePath4) );
//    	
//    	if(user2 != null){
//    		Logger.d(TAG, "Username: " + user2.getUser().getUsername());
//    		Logger.d(TAG, "Likelihood: " + user2.getLikelihood());
//    	}    	
    }
}