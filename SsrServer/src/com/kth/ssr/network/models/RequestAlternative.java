package com.kth.ssr.network.models;

import java.io.Serializable;

import com.bitsinharmony.recognito.VoicePrint;
import com.google.gson.annotations.SerializedName;

public class RequestAlternative implements Serializable{

	public enum RequestType {
		CREATE_USER,
		RECOGNIZE_AND_UPDATE_USER
	}
	
	@SerializedName("username")
	private String mUsername;
	
	@SerializedName("voicePrint")
	private VoicePrint mVoicePrint;

	@SerializedName("timestamp")
	private long mTimestamp;

	@SerializedName("requestType")
	private RequestType mRequestType;
	
	public RequestAlternative(VoicePrint voiceSamle, long timestamp, RequestType requestType) {
		this(null, voiceSamle, timestamp, requestType);
	}
	
	public RequestAlternative(String username, VoicePrint voiceSample, long timestamp, RequestType requestType) {
		
		if(requestType == RequestType.CREATE_USER && username == null){
			throw new IllegalArgumentException("A username must be provided for RequestType.CREATE_USER");
		}
		
		setUsername(username);
		setVoiceSample(voiceSample);
		setTimestamp(timestamp);
		setRequestType(requestType);
	}


	public String getUsername() {
		return mUsername;
	}

	public void setUsername(String username) {
		mUsername = username;
	}

	public VoicePrint getVoiceSample() {
		return mVoicePrint;
	}

	public void setVoiceSample(VoicePrint voiceSample) {
		mVoicePrint = voiceSample;
	}
	
	public long getTimestamp() {
		return mTimestamp;
	}
	
	public void setTimestamp(long timestamp) {
		mTimestamp = timestamp;
	}

	public RequestType getRequestType() {
		return mRequestType;
	}

	public void setRequestType(RequestType requestType) {
		mRequestType = requestType;
	}
}
