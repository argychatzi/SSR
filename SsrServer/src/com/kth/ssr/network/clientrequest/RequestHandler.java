package com.kth.ssr.network.clientrequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.bitsinharmony.recognito.utils.Logger;
import com.kth.recognitohelper.RecognitoHelper;
import com.kth.recognitohelper.database.models.MatchedRecord;
import com.kth.recognitohelper.database.transaction.DBTransactionResult;
import com.kth.ssr.network.models.Request;
import com.kth.ssr.network.models.RequestAlternative;

/**
 * @author georgios.savvidis
 *
 */
public class RequestHandler{
	private static final String TAG = RequestHandler.class.getCanonicalName();
	
	private RecognitoHelper mRecognitoHelper;
	private final static int SAMPLE_RATE = 44100;
	
	
	public RequestHandler(){
		mRecognitoHelper = new RecognitoHelper(SAMPLE_RATE);
	}
	
	public DBTransactionResult handleRequestCreateUser( RequestAlternative params ){
		Logger.d(TAG, "Handle request CreateUser");
		
//		File voiceSample = parseVoiceSample(params);
//		return mRecognitoHelper.createUser(params.getUsername(), params.getVoiceSample());
		return null;
	}
	
	public MatchedRecord handleRequestRecognizeAndUpdateUser( RequestAlternative params ){
		Logger.d(TAG, "Handle request RecognizeAndUpdateUser");
		
//		File voiceSample = parseVoiceSample(params);
		return mRecognitoHelper.recognizeAndUpdateUser(params.getVoiceSample());
	}

	private File parseVoiceSample( Request params ){
		Logger.d(TAG, "Parse voice sample");
		
		String username = ( params.getUsername() != null ) ? params.getUsername() : "unknownUser" ;
		long timestamp = params.getTimestamp();
		
		StringBuilder pathBuilder = new StringBuilder( System.getProperty("user.dir") );
		pathBuilder.append( "/res/" )
		.append( username )
		.append( "_" )
		.append( timestamp )
		.append( ".wav" );
		
		File voiceSample = new File( pathBuilder.toString() );

		try{
			Files.write( voiceSample.toPath(), params.getVoiceSample() );
		} catch (IOException e){
			e.printStackTrace();
		}
		
		return voiceSample;
	}
}