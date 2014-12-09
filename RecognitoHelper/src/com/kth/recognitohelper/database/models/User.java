package com.kth.recognitohelper.database.models;

import java.io.Serializable;

import com.bitsinharmony.recognito.VoicePrint;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by georgios.savvidis on 26/09/14.
 */
@DatabaseTable(tableName = "users")
public class User implements Serializable{

    @DatabaseField(id = true, columnName="username")
	private String mUsername;

    @DatabaseField(columnName="voiceprint", dataType = DataType.SERIALIZABLE)
    private VoicePrint mVoicePrint;
    
    public User(){}
    
	public User(String username, VoicePrint voiceprint){
		setUsername(username);
		setVoicePrint(voiceprint);
	}

	public String getUsername() {
		return mUsername;
	}

	public void setUsername(String username) {
		mUsername = username;
	}

	public VoicePrint getVoicePrint() {
		return mVoicePrint;
	}

	public void setVoicePrint(VoicePrint voicePrint) {
		mVoicePrint = voicePrint;
	}
}