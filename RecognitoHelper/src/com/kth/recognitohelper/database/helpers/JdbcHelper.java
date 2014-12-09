package com.kth.recognitohelper.database.helpers;

import java.sql.SQLException;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.kth.recognitohelper.database.models.DummyUser;
import com.kth.recognitohelper.database.models.User;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides the DAOs used by the
 * other classes.
 *
 * Created by georgios.savvidis on 26/09/14.
*/
public class JdbcHelper implements BaseHelper{

    // Name of the database
    private static final String DATABASE_URL = "jdbc:sqlite:ssr.db";

    // Version of the database used, if changes are made to the classes
    // that are saved this needs to be incremented by at least 1.
    //private static final int DATABASE_VERSION = 1;

    private JdbcConnectionSource mConnectionSource;

    private RuntimeExceptionDao<User, String> mUserRuntimeDao = null;
    private RuntimeExceptionDao<DummyUser, String> mDummyUserRuntimeDao = null;

    public JdbcHelper() {
        try{
            mConnectionSource = new JdbcConnectionSource(DATABASE_URL);
            TableUtils.createTableIfNotExists(mConnectionSource, User.class);
            TableUtils.createTableIfNotExists(mConnectionSource, DummyUser.class);

        } catch ( SQLException e ){
            throw new RuntimeException(e);
        }
    }

//    private void upgrade(){
//        //TODO find a way to check if the DB version has changed, and if yes drop and re-create the tables.
//    }

//    /**
//     * This is called when the application is upgraded and it has a higher DATABASE_VERSION number. This allows us to adjust the old data to
//     * match the new database structure.
//     */
//    @Override
//    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
//        try {
//        	TableUtils.dropTable(connectionSource, User.class, true);
//
//            // after we drop the old databases, we create the new ones
//            onCreate(db, connectionSource);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//
//        onCreate(db, connectionSource);
//    }

    @Override
    public boolean clearTable(Class<?> cls) {
        try {
            TableUtils.clearTable(mConnectionSource, cls);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our User class. It will create it or just give the cached value.
     */
    @Override
    public RuntimeExceptionDao<User, String> getUserDao() {
        if( mUserRuntimeDao == null ){
            try{
                mUserRuntimeDao = RuntimeExceptionDao.createDao(mConnectionSource, User.class);
            } catch (SQLException e){
                throw new RuntimeException(e);
            }
        }

        return mUserRuntimeDao;
    }
    
    /**
     * Returns the Database Access Object (DAO) for our User class. It will create it or just give the cached value.
     */
    @Override
    public RuntimeExceptionDao<DummyUser, String> getDummyUserDao() {
    	if( mDummyUserRuntimeDao == null ){
    		try{
    			mDummyUserRuntimeDao = RuntimeExceptionDao.createDao(mConnectionSource, DummyUser.class);
    		} catch (SQLException e){
    			throw new RuntimeException(e);
    		}
    	}
    	
    	return mDummyUserRuntimeDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        try{
            mConnectionSource.close();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        mUserRuntimeDao = null;
        mDummyUserRuntimeDao = null;
    }
}
