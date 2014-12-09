package com.kth.ssr.network.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.bitsinharmony.recognito.utils.Logger;

public class SocketMonitor {
	private static final String TAG = SocketMonitor.class.getCanonicalName();

    private static final int DEFAULT_PORT = 4444;

	private ServerSocket mServerSocket;
	private boolean mStopListening;
	
	public SocketMonitor() {
		try{
			mServerSocket = new ServerSocket(DEFAULT_PORT);
		} catch (IOException e){
			e.printStackTrace();
			Logger.d(TAG, "Could not initiate ServerSocket");
		}
	}
	
	public void startListening(){
		Logger.d(TAG, "Start listening...");
		if( mServerSocket == null){
			Logger.d(TAG, "The ServerSocket has not been initiated");
			return;
		}
		
		mStopListening = false;
		
		while( !mStopListening ){
			//This block will break only when the stopListening() is called.
			
			try{
				Socket socket = mServerSocket.accept();
                //The code bellow is blocked until a connection is received by mServerSocket.accept().
				//Handle the socket in a separate thread in order to be able to handle multiple sockets simultaneously.
				SocketHandler socketHandler = new SocketHandler(socket);
				socketHandler.start();

			} catch(IOException e){
				e.printStackTrace();
			}
		}
		
	}
	
	public void stopListening(){		
		if( mServerSocket == null){
			Logger.d(TAG, "The ServerSocket has not been initiated");
			return;
		}
		
		try{
			mStopListening = true;
			mServerSocket.close();
		}catch(IOException e){
			e.printStackTrace();
			Logger.d(TAG, "Could not close ServerSocket");
		}
	}
}
