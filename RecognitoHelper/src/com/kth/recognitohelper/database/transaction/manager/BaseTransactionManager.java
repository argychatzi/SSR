package com.kth.recognitohelper.database.transaction.manager;

import java.util.List;

import com.kth.recognitohelper.database.models.DummyUser;
import com.kth.recognitohelper.database.models.User;
import com.kth.recognitohelper.database.transaction.DBTransactionResult;

public abstract class BaseTransactionManager {

	public abstract DBTransactionResult createUser(User user);

	public abstract void createDummyUser(DummyUser dummyUser);
	
    public abstract DBTransactionResult updateUser(User user);

    public abstract boolean userExists(String username);
    
    public abstract void deleteUser(User user);

    public abstract void deleteAllUsers();

    public abstract User loadUser(String username);

    public abstract List<User> loadAllUsers(boolean includeDummyUsers);

    public abstract boolean dummyUsersCreated();
}