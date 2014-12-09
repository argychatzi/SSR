package com.kth.recognitohelper.database.models;

import java.io.Serializable;


/**
 * Created by georgios.savvidis on 26/09/14.
 */
public class MatchedRecord implements Serializable{

	private User mUser;

    private int mLikelihood;
    
	public MatchedRecord(User user, int likelihood){
		setUser(user);
		setLikelihood(likelihood);
	}

	public User getUser() {
		return mUser;
	}

	public void setUser(User user) {
		mUser = user;
	}

	public int getLikelihood() {
		return mLikelihood;
	}

	public void setLikelihood(int likelihood) {
		mLikelihood = likelihood;
	}
}