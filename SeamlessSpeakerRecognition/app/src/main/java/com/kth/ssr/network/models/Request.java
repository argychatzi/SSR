package com.kth.ssr.network.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Request implements Serializable{

	public enum RequestType {
		CREATE_USER,
		RECOGNIZE_AND_UPDATE_USER
	}
	
	@SerializedName("username")
	private String mUsername;
	
	@SerializedName("voiceSample")
	private byte[] mVoiceSample;

	@SerializedName("timestamp")
	private long mTimestamp;

	@SerializedName("requestType")
	private RequestType mRequestType;
	
	public Request(byte[] voiceSample, long timestamp, RequestType requestType) {
		this(null, voiceSample, timestamp, requestType);
	}
	
	public Request(String username, byte[] voiceSample, long timestamp, RequestType requestType) {
		
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

	public byte[] getVoiceSample() {
		return mVoiceSample;
	}

	public void setVoiceSample(byte[] voiceSample) {
		mVoiceSample = voiceSample;
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
