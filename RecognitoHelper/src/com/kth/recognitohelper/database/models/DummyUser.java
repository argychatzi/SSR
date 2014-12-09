package com.kth.recognitohelper.database.models;

import com.bitsinharmony.recognito.VoicePrint;
import com.j256.ormlite.table.DatabaseTable;

/**
 * This class is used to create and store in the DB dummy users that will only be used 
 * by recognito to populate its Universal Model. It's better to keep these records in 
 * a separate table than the actual users and that's the purpose of this class.
 * 
 * Created by georgios.savvidis on 26/09/14.
 */
@DatabaseTable(tableName = "dummy_users")
public class DummyUser extends User{
	
	public DummyUser() {
		super();
	}
	
	public DummyUser(String username, VoicePrint voiceprint){
		super(username, voiceprint);
	}
}