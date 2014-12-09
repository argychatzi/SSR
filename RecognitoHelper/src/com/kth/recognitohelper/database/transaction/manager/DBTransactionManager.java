package com.kth.recognitohelper.database.transaction.manager;

import java.util.List;

import android.content.Context;

import com.kth.recognitohelper.database.models.DummyUser;
import com.kth.recognitohelper.database.models.User;
import com.kth.recognitohelper.database.transaction.DBTransactionResult;

/**
 * This class provides a level of abstraction when dealing with database transactions. Based on the selected
 * {@link com.kth.recognitohelper.database.transaction.manager.DBTransactionManager.DatabaseType} it will use either the {@link AndroidSqliteTransactionManager}
 * or the {@link JdbcSqliteTransactionManager} to perform transactions with the database. 
 * 
 * Implementations of this class need only set the correct DatabaseType and use its methods without having to deal directly with either of the mentioned helper classes.
 * 
 * Note that if the selected DatabaseType is {@link com.kth.recognitohelper.database.transaction.manager.DBTransactionManager.DatabaseType#TYPE_ANDROID_SQLITE},
 * then the contructor of this class should be called with a non-null context.
 * 
 * Created by georgios.savvidis on 26/09/14.
 */
public class DBTransactionManager extends BaseTransactionManager{

    public enum DatabaseType {
        TYPE_ANDROID_SQLITE,
        TYPE_JDBC_SQLITE
    };

    private BaseTransactionManager mTransactionManager;

    public DBTransactionManager(DatabaseType type){
    	this(type, null);
    }

    public DBTransactionManager(DatabaseType type, Context context){

    	if( (type == DatabaseType.TYPE_ANDROID_SQLITE) && (context == null) ){
        	throw new IllegalArgumentException("Context cannot be null for DatabaseType.TYPE_SQLITE.");
        }
    	
    	if( type == DatabaseType.TYPE_ANDROID_SQLITE ){
    		mTransactionManager = new AndroidSqliteTransactionManager(context);
    	} else{
    		mTransactionManager = new JdbcSqliteTransactionManager();
    	}
    }

    @Override
    public DBTransactionResult createUser(User user){
    	return mTransactionManager.createUser(user);
    }

    @Override
    public void createDummyUser(DummyUser dummyUser) {
    	mTransactionManager.createDummyUser(dummyUser);
    }
    
    @Override
    public DBTransactionResult updateUser(User user){
    	return mTransactionManager.updateUser(user);
    }
    
    @Override
    public boolean userExists(String username) {
    	return mTransactionManager.userExists(username);
    }

    @Override
    public void deleteUser(User user){
    	mTransactionManager.deleteUser(user);
    }
    
    @Override
    public void deleteAllUsers() {
    	mTransactionManager.deleteAllUsers();
    }

    @Override
    public User loadUser(String username){
    	return mTransactionManager.loadUser(username);
    }

    @Override
    public List<User> loadAllUsers(boolean includeDummyUsers){
    	return mTransactionManager.loadAllUsers(includeDummyUsers);
    }
    
    @Override
    public boolean dummyUsersCreated() {
    	return mTransactionManager.dummyUsersCreated();
    }
}