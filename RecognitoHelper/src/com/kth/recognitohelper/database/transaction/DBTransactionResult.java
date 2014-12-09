package com.kth.recognitohelper.database.transaction;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

/**
 * Created by georgios.savvidis on 26/09/14.
 */
public class DBTransactionResult implements Serializable{
	
	public static final int RESULT_SUCCESS = 0;
	public static final int RESULT_FAILURE = -1;
	
	public static final int FAILURE_REASON_UNSPECIFIED = -1;
    public static final int FAILURE_REASON_USER_ALREADY_EXISTS = 0;
    public static final int FAILURE_REASON_USER_DOES_NOT_EXIST = 1;
    
    private int mResultCode;
    private int mFailureReason;
    
    public DBTransactionResult(int resultCode){
    	this(resultCode, FAILURE_REASON_UNSPECIFIED);
    }
    
    public DBTransactionResult(int resultCode, int failureReason){
    	setResultCode(resultCode);
    	setFailureReason(failureReason);
    }

    public int getResultCode() {
		return mResultCode;
	}
	
    public void setResultCode(int resultCode) {
		mResultCode = resultCode;
	}
    
	public int getFailureReason() {
		return mFailureReason;
	}
	
	public void setFailureReason(int failureReason) {
		mFailureReason = failureReason;
	}
}
