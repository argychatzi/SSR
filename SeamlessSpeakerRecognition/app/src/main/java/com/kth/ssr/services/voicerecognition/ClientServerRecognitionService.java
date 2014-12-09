package com.kth.ssr.services.voicerecognition;

import android.util.Log;

import com.bitsinharmony.recognito.Recognito;
import com.bitsinharmony.recognito.VoicePrint;
import com.kth.recognitohelper.database.models.MatchedRecord;
import com.kth.recognitohelper.database.transaction.DBTransactionResult;
import com.kth.ssr.network.ServerConnection;
import com.kth.ssr.network.listeners.ResponseListener;
import com.kth.ssr.network.models.RequestAlternative;
import com.kth.ssr.network.models.RequestAlternative.RequestType;
import com.kth.ssr.utils.recorder.RecorderConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Created by argychatzi on 10/5/14.
 */
public class ClientServerRecognitionService extends AbstractVoiceRecognitionService implements ResponseListener{

    private static final String TAG = ClientServerRecognitionService.class.getCanonicalName();

    private ServerConnection mServerConnection;

    private Recognito mRecognito;

    public ClientServerRecognitionService() {
        this(ClientServerRecognitionService.class.getCanonicalName());
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ClientServerRecognitionService(String name) {
        super(name);
    }



    @Override
    public void onCreate() {
        super.onCreate();

        mServerConnection = new ServerConnection(this);
        mRecognito = new Recognito(RecorderConfiguration.SAMPLE_RATE_IN_HZ);
    }

    @Override
    protected void createNewUser(String username, File recording) {
        Log.d(TAG, "CreateNewUser");
        try{
            VoicePrint voicePrint = mRecognito.createVoicePrint(username, recording);
            RequestAlternative request = new RequestAlternative(username, voicePrint, System.currentTimeMillis(), RequestType.CREATE_USER);

            mServerConnection.sendRequest(request);

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private long mTimeBefore;

    @Override
    protected void recognizeVoice(File recording) {
        Log.d(TAG, "RecognizeVoice");

        mTimeBefore = System.currentTimeMillis();
        try{
            VoicePrint voicePrint = mRecognito.createVoicePrint("unknown", recording);
            RequestAlternative request = new RequestAlternative(voicePrint, System.currentTimeMillis(), RequestType.RECOGNIZE_AND_UPDATE_USER);

            mServerConnection.sendRequest(request);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateUserResponse(DBTransactionResult result) {
        sendBroadcastCreateUserResult(result);
    }

    @Override
    public void onRecognizeUserResponse(MatchedRecord matchedRecord) {
        Log.d(TAG, "OnResponse. Delay: " + (System.currentTimeMillis() - mTimeBefore));
        sendBroadcastRecognizeUserResult(matchedRecord);
    }
}