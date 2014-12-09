package com.kth.ssr.network;

import android.util.Log;

import com.kth.recognitohelper.database.models.MatchedRecord;
import com.kth.recognitohelper.database.transaction.DBTransactionResult;
import com.kth.ssr.BuildConfig;
import com.kth.ssr.network.listeners.ResponseListener;
import com.kth.ssr.network.models.RequestAlternative.RequestType;
import com.kth.ssr.network.models.RequestAlternative;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class ServerConnection {

    private static final String TAG = ServerConnection.class.getCanonicalName();

	private static final String DEFAULT_HOST = BuildConfig.SERVER_IP;
	private static final int DEFAULT_PORT = BuildConfig.SERVER_PORT;

    private ResponseListener mResponseListener;

    public ServerConnection(ResponseListener responseListener){
        mResponseListener = responseListener;
    }


	public void sendRequest(RequestAlternative data){
        Log.d(TAG, "Send Request");
        Socket clientSocket = null;

        try {
            clientSocket = new Socket(DEFAULT_HOST, DEFAULT_PORT);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

            //Write request
            objectOutputStream.writeObject(data);
            objectOutputStream.flush();

            //Read response
            Object response = null;
            try{
                response = objectInputStream.readObject();
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            }

            objectOutputStream.close();
            objectInputStream.close();
            clientSocket.close();

            Log.d(TAG, "Checking response...");
            if(mResponseListener != null){
                if( data.getRequestType() == RequestType.CREATE_USER ){
                    mResponseListener.onCreateUserResponse((DBTransactionResult) response);
                } else if( data.getRequestType() == RequestType.RECOGNIZE_AND_UPDATE_USER ){
                    mResponseListener.onRecognizeUserResponse((MatchedRecord) response);
                }
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
