package com.kth.ssr.network.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.bitsinharmony.recognito.utils.Logger;
import com.kth.recognitohelper.database.models.MatchedRecord;
import com.kth.recognitohelper.database.transaction.DBTransactionResult;
import com.kth.ssr.network.clientrequest.RequestHandler;
import com.kth.ssr.network.models.RequestAlternative;
import com.kth.ssr.network.models.RequestAlternative.RequestType;

/**
 * @author georgios.savvidis
 *
 */
public class SocketHandler extends Thread{
	private static final String TAG = SocketHandler.class.getCanonicalName();

	private Socket mSocket;
	
	public SocketHandler(Socket socket){
		Logger.d(TAG, "SocketHandler");
		mSocket = socket;
	}
	
	@Override
	public void run() {
		super.run();
		
		try{
			ObjectInputStream objectInputStream = new ObjectInputStream(mSocket.getInputStream());
			ObjectOutputStream objectOutputStream = new ObjectOutputStream( mSocket.getOutputStream() );

			//Read request
			RequestAlternative params = (RequestAlternative)objectInputStream.readObject();
			RequestHandler requestHandler = new RequestHandler();
			
			//Write response
			if( params.getRequestType() == RequestType.CREATE_USER ){
				DBTransactionResult transactionResult = requestHandler.handleRequestCreateUser(params);

				Logger.d(TAG, "Write response Create user");
				objectOutputStream.writeObject(transactionResult);
	            objectOutputStream.flush();

			} else if( params.getRequestType() == RequestType.RECOGNIZE_AND_UPDATE_USER ){
				MatchedRecord matchedRecord = requestHandler.handleRequestRecognizeAndUpdateUser(params);
			
				Logger.d(TAG, "Write response Recognize user");
				objectOutputStream.writeObject(matchedRecord);
				objectOutputStream.flush();
			}

			objectInputStream.close();
			objectOutputStream.close();
			mSocket.close();
			
		} catch (IOException e){
			e.printStackTrace();
			Logger.d(TAG, "IO error");

		} catch (ClassNotFoundException e){
			e.printStackTrace();
			Logger.d(TAG, "Failed to parse InputStream");
		}
	}
}