package com.kth.ssr.services.voicerecognition;

import android.os.Environment;
import android.util.Log;

import com.kth.recognitohelper.RecognitoHelper;
import com.kth.recognitohelper.database.models.MatchedRecord;
import com.kth.recognitohelper.database.transaction.DBTransactionResult;
import com.kth.ssr.utils.recorder.RecorderConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by argychatzi on 10/5/14.
 */
public class StandaloneRecognitionService extends AbstractVoiceRecognitionService {

    private static final String TAG = StandaloneRecognitionService.class.getCanonicalName();

    //TODO find a better way to do this
    private static final String PATH_TO_DUMMY_SAMPLES = Environment.getExternalStorageDirectory().getPath() + "/SSR/DummySamples";

    private RecognitoHelper mRecognitoHelper;

    public StandaloneRecognitionService() {
        this(StandaloneRecognitionService.class.getCanonicalName());
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public StandaloneRecognitionService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
        super.onCreate();

        mRecognitoHelper = new RecognitoHelper(this, RecorderConfiguration.SAMPLE_RATE_IN_HZ, PATH_TO_DUMMY_SAMPLES);

        try{
            String[] assets = getAssets().list("dummySamples");

            for(int i=0; i<assets.length; i++){
                Log.d(TAG, "Asset: " + assets[i]);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void createNewUser(String username, File recording) {
        Log.d(TAG, "Create new user. " + recording.getName());
        DBTransactionResult transactionResult = mRecognitoHelper.createUser(username, recording);

        sendBroadcastCreateUserResult(transactionResult);
    }

    @Override
    protected void recognizeVoice(File recording) {
        Log.d(TAG, "Recognize voice");

        long timeBefore = System.currentTimeMillis();
        MatchedRecord matchedRecord = mRecognitoHelper.recognizeAndUpdateUser(recording);
        Log.d(TAG, "Finished Recognition. Delay: " + (System.currentTimeMillis() - timeBefore));

        sendBroadcastRecognizeUserResult(matchedRecord);
    }
}