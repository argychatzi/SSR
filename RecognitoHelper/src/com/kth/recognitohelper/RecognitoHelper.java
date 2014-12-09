package com.kth.recognitohelper;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.bitsinharmony.recognito.MatchResult;
import com.bitsinharmony.recognito.Recognito;
import com.bitsinharmony.recognito.VoicePrint;
import com.bitsinharmony.recognito.utils.Logger;
import com.giorgos.agent.ObjectSizeFetcher;
import com.javamex.classmexer.MemoryUtil;
import com.javamex.classmexer.MemoryUtil.VisibilityFilter;
import com.kth.recognitohelper.database.exceptions.DummySamplesNotFoundException;
import com.kth.recognitohelper.database.models.DummyUser;
import com.kth.recognitohelper.database.models.MatchedRecord;
import com.kth.recognitohelper.database.models.User;
import com.kth.recognitohelper.database.transaction.DBTransactionResult;
import com.kth.recognitohelper.database.transaction.manager.DBTransactionManager;
import com.kth.recognitohelper.database.transaction.manager.DBTransactionManager.DatabaseType;

/**
 * Created by georgios.savvidis on 26/09/14.
 */
public class RecognitoHelper{
	private static final String TAG = RecognitoHelper.class.getCanonicalName();

    private static final int MINIMUM_LIKELIHOOD_RATIO_TO_UPDATE = 70;

	private Recognito<String> mRecognito;
	private DBTransactionManager mDbTransactionManager;

	private static final String DEFAULT_DUMMY_SAMPLES_PATH = System.getProperty("user.dir") + "/raw";
	
    /**
     * 
     * Call this constructor if this library is used by a Java application
     */
    public RecognitoHelper(float sampleRate){
        mDbTransactionManager = new DBTransactionManager(DatabaseType.TYPE_JDBC_SQLITE);
        initRecognito(sampleRate, DEFAULT_DUMMY_SAMPLES_PATH);
    }

    /**
     * Call this constructor if this library is used by an Android app. 
     * 
     * @param context Should not be null
     */
    public RecognitoHelper(Context context, float sampleRate, String dummySamplesPath){
        mDbTransactionManager = new DBTransactionManager(DatabaseType.TYPE_ANDROID_SQLITE, context);
        initRecognito(sampleRate, dummySamplesPath);
    }

    private void initRecognito(float sampleRate, String dummySamplesPath) throws DummySamplesNotFoundException{    
    	Logger.d(TAG, "InitRecognito");

    	//TODO Uncomment this and fix on standalone
    	//For the first time only create the "dummy_users" table in the DB
    	if( !mDbTransactionManager.dummyUsersCreated() ){
    		Logger.d(TAG, "Create dummyUsers");

    		mRecognito = new Recognito<>(sampleRate);

    		//Get only .wav files
    		FilenameFilter wavExtensionFilter = new FilenameFilter() {
    			public boolean accept(File dir, String name) {
    				return name.toLowerCase().endsWith(".wav");
    			}
    		};
    		File rawFolder = new File( dummySamplesPath );
    		Logger.d(TAG, "Raw folder (expected) location: " + rawFolder.getAbsolutePath());
    		
    		File[] listOfFiles = rawFolder.listFiles( wavExtensionFilter );
    		
    		if( listOfFiles == null ){
    			throw new DummySamplesNotFoundException("The \"raw\" folder with the dummy voice samples does not exist");
    		}
    		    
//    		for(int i=0; i<listOfFiles.length; i++){
    		for(int i=0; i<5; i++){
    			File sample = listOfFiles[i];
    			String username = ( sample.getName().split("_") )[0];
    			
    			Logger.d(TAG, "Creating dummy user: " + username);
    			
    			
    			try{
    				VoicePrint voicePrint = mRecognito.createVoicePrint(username, sample);
    				
    				//TODO check size of voiceprint
    				Logger.d(TAG, "---> File size: " + ObjectSizeFetcher.getObjectSize(sample));
    				Logger.d(TAG, "---> Voiceprint size: " + ObjectSizeFetcher.getObjectSize(voicePrint));
    				Logger.d(TAG, "---> Deep Voiceprint size: " + MemoryUtil.deepMemoryUsageOf(voicePrint, VisibilityFilter.ALL));
    				Logger.d(TAG, "---> Features size: " + ObjectSizeFetcher.getObjectSize(voicePrint.getFeatures()));
    				double[] array = mRecognito.convertFileToDoubleArray(sample);
    				double value = 0;
    				int value2 = 0;
    				double[] value3 = new double[20];
    				boolean value4 = true;
    				Logger.d(TAG, "---> DoubleArray: " + ObjectSizeFetcher.getObjectSize(array));
    				Logger.d(TAG, "---> double: " + ObjectSizeFetcher.getObjectSize(value));
    				Logger.d(TAG, "---> double[20]: " + ObjectSizeFetcher.getObjectSize(value3));
    				Logger.d(TAG, "---> int: " + ObjectSizeFetcher.getObjectSize(value2));
    				Logger.d(TAG, "---> boolean: " + ObjectSizeFetcher.getObjectSize(value4));

    				DummyUser dummyUser = new DummyUser(username, voicePrint);
        			mDbTransactionManager.createDummyUser(dummyUser);

    			}catch (IOException e){
    				e.printStackTrace();
    			}
    			
    			//TODO delete the sample file after creating storing its corresponding voiceprint.
    			//deleteRecording(sample);
    		}    		
    	} else{
    		
    		List<User> storedUsers = mDbTransactionManager.loadAllUsers( true );
    		Logger.d(TAG, "Stored users: " + storedUsers.size());
    		
    		Map<String, VoicePrint> voicePrintsByUserKey = new HashMap<String, VoicePrint>();
    		
    		for(int i=0; i<storedUsers.size(); i++){
    			User user = storedUsers.get(i);
    			voicePrintsByUserKey.put(user.getUsername(), user.getVoicePrint());
    		}
    		
    		//Populate Recognito with all the previously stored voiceprints (if any).
    		mRecognito = new Recognito<String>(sampleRate, voicePrintsByUserKey);
    	}
		
    }

	public DBTransactionResult createUser(String username, File voiceSample){
    	Logger.d(TAG, "Create user: " + username + " File: " + voiceSample.getAbsolutePath() + " file exists: " + voiceSample.exists());
    	
    	DBTransactionResult dbTransactionResult = new DBTransactionResult(DBTransactionResult.RESULT_FAILURE);

    	if( mDbTransactionManager.userExists(username) ){
    		dbTransactionResult.setFailureReason(DBTransactionResult.FAILURE_REASON_USER_ALREADY_EXISTS);
    		    	
    	} else{

    		try{
    			VoicePrint voiceprint = mRecognito.createVoicePrint(username, voiceSample);
    			
    			User newUser = new User(username, voiceprint);
    			dbTransactionResult = mDbTransactionManager.createUser(newUser);
    			
    		}catch(IOException e){
    			e.printStackTrace();
    		}
    	}
		
    	deleteRecording(voiceSample);
		return dbTransactionResult;
	}
	
	public MatchedRecord recognizeAndUpdateUser(VoicePrint voicePrint){
		MatchedRecord matchedRecord = findBestMatch(voicePrint);

		Logger.d(TAG,  "Found best match: " + matchedRecord.getUser().getUsername() + " likelihood: " + matchedRecord.getLikelihood());
		
		updateUser(matchedRecord);
		
//		deleteRecording(voiceSample);
		
		//Currently we're checking the likelihood ratio only in order to update the user's VoicePrint. 
		//Perhaps instead of returning a MatchedRecord, we should return a User. This means that this method should check the likelihood in order to decide
		//if we'll return null or the User.
		
		return matchedRecord;
	}

    public MatchedRecord recognizeAndUpdateUser(File voiceSample){
    	MatchedRecord matchedRecord = findBestMatch(voiceSample);
    	Logger.d(TAG,  "Found best match: " + matchedRecord.getUser().getUsername() + " likelihood: " + matchedRecord.getLikelihood());
    	
    	updateUser(matchedRecord);	
        
		deleteRecording(voiceSample);
        
        //Currently we're checking the likelihood ratio only in order to update the user's VoicePrint. 
        //Perhaps instead of returning a MatchedRecord, we should return a User. This means that this method should check the likelihood in order to decide
        //if we'll return null or the User.
        
        return matchedRecord;
    }
    
    private void updateUser(MatchedRecord matchedRecord){
    	
    	// Update the VoicePrint only if we're sure enough
    	if( matchedRecord != null && matchedRecord.getLikelihood() > MINIMUM_LIKELIHOOD_RATIO_TO_UPDATE ){
    		
			//Update the VoicePrint of the recognized user
			User matchedUser = matchedRecord.getUser();
			VoicePrint updatedVoicePrint = mRecognito.mergeVoiceSample( matchedUser.getUsername(), matchedRecord.getUser().getVoicePrint() );
			matchedUser.setVoicePrint(updatedVoicePrint);
			
			//Update the user model in the DB
			mDbTransactionManager.updateUser(matchedUser);
    	}
    }
    
    private MatchedRecord findBestMatch(File voiceSample){
    	Logger.d(TAG, "Recognize voice");
    	
        try{
            List<MatchResult<String>> matches = mRecognito.identify(voiceSample);

            Logger.d(TAG, "Found matches: " + matches.size());
            
            return findBestMatch(matches);
            
        }catch (IOException e){
            e.printStackTrace();
        }
        
        return null;
    }
    
    private MatchedRecord findBestMatch(VoicePrint voicePrint){
    	Logger.d(TAG, "Recognize voice");
    	
		List<MatchResult<String>> matches = mRecognito.identify(voicePrint);
		
		Logger.d(TAG, "Found matches: " + matches.size());
		
    	return findBestMatch(matches);
    }
    
    private MatchedRecord findBestMatch(List<MatchResult<String>> matches){
    	
    	for(int i=0; i<matches.size(); i++){
    		
    		MatchResult<String> bestMatch = matches.get(i);
    		Logger.d(TAG, "Best match: " + bestMatch.getKey() + " likelihood: " + bestMatch.getLikelihoodRatio());
    		
    		// Might return null if the best match corresponds to one of the samples that are used to train Recognito and populate its Universal Model.
    		// These samples are not stored in the database because they are not actual users.
    		if( mDbTransactionManager.userExists(bestMatch.getKey()) ) {
    			Logger.d(TAG, "User exists!");
    			User matchedUser = mDbTransactionManager.loadUser( bestMatch.getKey() );
    			return new MatchedRecord(matchedUser, bestMatch.getLikelihoodRatio());
    		} else{
    			Logger.d(TAG, "The best match corresponds to a bot. Try the next best match");
    		}
    	}
    	
    	return null;
    }
    
    /**
     * Delete the .wav file. This method should be called in order to delete any user recordings after they have been transformed to VoicePrints.
     * 
     * @param recording
     * @return Returns true if the file was successfully deleted and false if it hasn't.
     */
    private boolean deleteRecording(File recording){
    	//TODO uncomment after testing
//    	boolean result = recording.delete();
//    	Logger.d(TAG, "Delete audio file: " + result);
//
//    	return result;
    	return false;
    }
    
}