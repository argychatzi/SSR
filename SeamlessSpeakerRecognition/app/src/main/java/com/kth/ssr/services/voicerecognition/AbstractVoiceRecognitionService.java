package com.kth.ssr.services.voicerecognition;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.kth.recognitohelper.database.models.MatchedRecord;
import com.kth.recognitohelper.database.transaction.DBTransactionResult;
import com.kth.ssr.broadcast.RecognitoBroadcastReceiver;

import java.io.File;

/**
 * Created by argychatzi on 10/5/14.
 */
public abstract class AbstractVoiceRecognitionService extends IntentService {

    private static final String TAG = AbstractVoiceRecognitionService.class.getCanonicalName();

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public AbstractVoiceRecognitionService(String name) {
        super(name);
    }

    protected enum Operation {
        CREATE_NEW_USER, RECOGNIZE_USER
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "OnCreate");
        super.onCreate();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");

        Bundle bundle = intent.getExtras();

        Operation operation = (Operation) bundle.getSerializable( RecognitionServiceConstants.BUNDLE_KEY_OPERATION );
        File recordingFile = (File) bundle.getSerializable( RecognitionServiceConstants.BUNDLE_KEY_RECORDING_FILE );

        switch (operation){
            case CREATE_NEW_USER:
                //TODO put in a separate thread?
                String username = bundle.getString( RecognitionServiceConstants.BUNDLE_KEY_USER_ID );
                createNewUser(username, recordingFile);
                break;

            case RECOGNIZE_USER:
                //TODO put in a separate thread?
                recognizeVoice(recordingFile);
                break;

        }
    }

    protected void sendBroadcastCreateUserResult(DBTransactionResult transactionResult){
        Intent intent = new Intent();
        intent.setAction(RecognitoBroadcastReceiver.BROADCAST_ACTION_CREATE_USER_RESULT);
        intent.putExtra(RecognitoBroadcastReceiver.EXTRA_RESULT, transactionResult);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.sendBroadcast(intent);
    }


    protected void sendBroadcastRecognizeUserResult(MatchedRecord matchedRecord){
        Intent intent = new Intent();
        intent.setAction(RecognitoBroadcastReceiver.BROADCAST_ACTION_RECOGNIZE_USER_RESULT);
        intent.putExtra(RecognitoBroadcastReceiver.EXTRA_RESULT, matchedRecord);

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
        broadcastManager.sendBroadcast(intent);
    }

    protected abstract void createNewUser(String username, File recording);

    protected abstract void recognizeVoice(File recording);
}