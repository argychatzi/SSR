package com.kth.recognitohelper.database.transaction.manager;

import java.util.List;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.kth.recognitohelper.database.helpers.OrmliteHelper;
import com.kth.recognitohelper.database.models.DummyUser;
import com.kth.recognitohelper.database.models.User;
import com.kth.recognitohelper.database.transaction.DBTransactionResult;

public class AndroidSqliteTransactionManager extends BaseTransactionManager{

	private Context mContext;
	
	public AndroidSqliteTransactionManager(Context context) {
		mContext = context;
	}

	@Override
     public DBTransactionResult createUser(User user) {
        OrmliteHelper androidDatabaseHelper = OpenHelperManager.getHelper(mContext, OrmliteHelper.class);
        RuntimeExceptionDao<User, String> dao = androidDatabaseHelper.getUserDao();

        boolean userAlreadyExists = dao.idExists(user.getUsername());
        DBTransactionResult transactionResult;
        
        if( !userAlreadyExists ){
            dao.create(user);
            transactionResult = new DBTransactionResult(DBTransactionResult.RESULT_SUCCESS);
        } else{
        	transactionResult = new DBTransactionResult(DBTransactionResult.RESULT_FAILURE, DBTransactionResult.FAILURE_REASON_USER_ALREADY_EXISTS);
        }

        OpenHelperManager.releaseHelper();
        androidDatabaseHelper = null;
        
        return transactionResult;
    }
	
	@Override
	public void createDummyUser(DummyUser dummyUser) {
		OrmliteHelper androidDatabaseHelper = OpenHelperManager.getHelper(mContext, OrmliteHelper.class);
		RuntimeExceptionDao<DummyUser, String> dao = androidDatabaseHelper.getDummyUserDao();
		
		dao.create(dummyUser);
		
		OpenHelperManager.releaseHelper();
		androidDatabaseHelper = null;
	}

	@Override
    public DBTransactionResult updateUser(User user) {
        OrmliteHelper androidDatabaseHelper = OpenHelperManager.getHelper(mContext, OrmliteHelper.class);
        RuntimeExceptionDao<User, String> dao = androidDatabaseHelper.getUserDao();

        boolean userExists = dao.idExists(user.getUsername());
        DBTransactionResult transactionResult;

        if( userExists ){
            dao.update(user);
            transactionResult = new DBTransactionResult(DBTransactionResult.RESULT_SUCCESS);
        } else{
        	transactionResult = new DBTransactionResult(DBTransactionResult.RESULT_FAILURE, DBTransactionResult.FAILURE_REASON_USER_DOES_NOT_EXIST);
        }

        OpenHelperManager.releaseHelper();
        androidDatabaseHelper = null;
        
        return transactionResult;
    }
	
	@Override
	public boolean userExists(String username) {
		OrmliteHelper androidDatabaseHelper = OpenHelperManager.getHelper(mContext, OrmliteHelper.class);
		RuntimeExceptionDao<User, String> dao = androidDatabaseHelper.getUserDao();
		
		boolean userExists = dao.idExists(username);

		OpenHelperManager.releaseHelper();
		androidDatabaseHelper = null;
		
		return userExists;
	}

	@Override
    public void deleteUser(User user) {
        OrmliteHelper androidDatabaseHelper = OpenHelperManager.getHelper(mContext, OrmliteHelper.class);
        RuntimeExceptionDao<User, String> dao = androidDatabaseHelper.getUserDao();

        dao.delete(user);

        OpenHelperManager.releaseHelper();
        androidDatabaseHelper = null;
    }
	
	@Override
	public void deleteAllUsers() {
		OrmliteHelper androidDatabaseHelper = OpenHelperManager.getHelper(mContext, OrmliteHelper.class);
		androidDatabaseHelper.clearTable(User.class);
		
		OpenHelperManager.releaseHelper();
		androidDatabaseHelper = null;
	}

	@Override
    public User loadUser(String username) {
        OrmliteHelper androidDatabaseHelper = OpenHelperManager.getHelper(mContext, OrmliteHelper.class);
        RuntimeExceptionDao<User, String> dao = androidDatabaseHelper.getUserDao();

        User user = dao.queryForId(username);

        OpenHelperManager.releaseHelper();
        androidDatabaseHelper = null;

        return user;
    }

	@Override
    public List<User> loadAllUsers(boolean includeDummyUsers) {
        OrmliteHelper androidDatabaseHelper = OpenHelperManager.getHelper(mContext, OrmliteHelper.class);
        RuntimeExceptionDao<User, String> dao = androidDatabaseHelper.getUserDao();

        List<User> users = dao.queryForAll();
        
        if( includeDummyUsers ){
        	RuntimeExceptionDao<DummyUser, String> dummyUserDao = androidDatabaseHelper.getDummyUserDao();
        	if( dummyUserDao.isTableExists() ){
        		users.addAll( dummyUserDao.queryForAll() );
        	}
        }

        OpenHelperManager.releaseHelper();
        androidDatabaseHelper = null;

        return users;
    }
	
	@Override
	public boolean dummyUsersCreated() {
		OrmliteHelper androidDatabaseHelper = OpenHelperManager.getHelper(mContext, OrmliteHelper.class);
		RuntimeExceptionDao<DummyUser, String> dummyUserDao = androidDatabaseHelper.getDummyUserDao();

		long countOf = 0;
        
        if( dummyUserDao.isTableExists() ){
        	countOf = dummyUserDao.countOf();
        }

		OpenHelperManager.releaseHelper();
		androidDatabaseHelper = null;

		return (countOf > 0);
	}
}