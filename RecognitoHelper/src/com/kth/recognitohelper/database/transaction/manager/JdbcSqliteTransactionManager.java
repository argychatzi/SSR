package com.kth.recognitohelper.database.transaction.manager;

import java.util.List;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.kth.recognitohelper.database.helpers.JdbcHelper;
import com.kth.recognitohelper.database.models.DummyUser;
import com.kth.recognitohelper.database.models.User;
import com.kth.recognitohelper.database.transaction.DBTransactionResult;

public class JdbcSqliteTransactionManager extends BaseTransactionManager{

	@Override
    public DBTransactionResult createUser(User user) {
        JdbcHelper jdbcDatabaseHelper = new JdbcHelper();
        RuntimeExceptionDao<User, String> dao = jdbcDatabaseHelper.getUserDao();
        
        boolean userAlreadyExists = dao.idExists(user.getUsername());
        DBTransactionResult transactionResult;
        
        if( !userAlreadyExists ){
            dao.create(user);
            transactionResult = new DBTransactionResult(DBTransactionResult.RESULT_SUCCESS);
        } else{
            transactionResult = new DBTransactionResult(DBTransactionResult.RESULT_FAILURE, DBTransactionResult.FAILURE_REASON_USER_ALREADY_EXISTS);
        }

        jdbcDatabaseHelper.close();
        
        return transactionResult;
    }
	
	@Override
	public void createDummyUser(DummyUser dummyUser) {
		JdbcHelper jdbcDatabaseHelper = new JdbcHelper();
		RuntimeExceptionDao<DummyUser, String> dao = jdbcDatabaseHelper.getDummyUserDao();
		
		dao.create(dummyUser);
		
		jdbcDatabaseHelper.close();
	}

	@Override
    public DBTransactionResult updateUser(User user) {
        JdbcHelper jdbcDatabaseHelper = new JdbcHelper();
        RuntimeExceptionDao<User, String> dao = jdbcDatabaseHelper.getUserDao();

        boolean userExists = dao.idExists(user.getUsername());
        DBTransactionResult transactionResult;

        if( userExists ){
            dao.update(user);
            transactionResult = new DBTransactionResult(DBTransactionResult.RESULT_SUCCESS);
        } else{
        	transactionResult = new DBTransactionResult(DBTransactionResult.RESULT_FAILURE, DBTransactionResult.FAILURE_REASON_USER_DOES_NOT_EXIST);
        }

        jdbcDatabaseHelper.close();
        
        return transactionResult;
    }

	@Override
	public boolean userExists(String username) {
		
		JdbcHelper jdbcDatabaseHelper = new JdbcHelper();
		RuntimeExceptionDao<User, String> dao = jdbcDatabaseHelper.getUserDao();
		
		boolean userExists = dao.idExists(username);
		jdbcDatabaseHelper.close();

		return userExists;
	}
	
	@Override
    public void deleteUser(User user) {
        JdbcHelper jdbcDatabaseHelper = new JdbcHelper();
        RuntimeExceptionDao<User, String> dao = jdbcDatabaseHelper.getUserDao();

        dao.delete(user);

        jdbcDatabaseHelper.close();
    }
	
	@Override
	public void deleteAllUsers() {
		JdbcHelper jdbcDatabaseHelper = new JdbcHelper();
		jdbcDatabaseHelper.clearTable(User.class);
		
		jdbcDatabaseHelper.close();
	}

	@Override
    public User loadUser(String username) {
        JdbcHelper jdbcDatabaseHelper = new JdbcHelper();
        RuntimeExceptionDao<User, String> dao = jdbcDatabaseHelper.getUserDao();

        User user = dao.queryForId(username);

        jdbcDatabaseHelper.close();

        return user;
    }

	@Override
    public List<User> loadAllUsers(boolean includeDummyUsers) {
        JdbcHelper jdbcDatabaseHelper = new JdbcHelper();
        RuntimeExceptionDao<User, String> dao = jdbcDatabaseHelper.getUserDao();

        List<User> users = dao.queryForAll();
        
        if( includeDummyUsers ){
        	RuntimeExceptionDao<DummyUser, String> dummyUserDao = jdbcDatabaseHelper.getDummyUserDao();
        	if( dummyUserDao.isTableExists() ){
        		users.addAll( dummyUserDao.queryForAll() );
        	}
        }

        jdbcDatabaseHelper.close();

        return users;
    }
	
	@Override
	public boolean dummyUsersCreated() {
        JdbcHelper jdbcDatabaseHelper = new JdbcHelper();
        RuntimeExceptionDao<DummyUser, String> dummyUserDao = jdbcDatabaseHelper.getDummyUserDao();

        long countOf = 0;
        
        if( dummyUserDao.isTableExists() ){
        	countOf = dummyUserDao.countOf();
        }
		
		jdbcDatabaseHelper.close();
		
		return (countOf > 0);
	}
}