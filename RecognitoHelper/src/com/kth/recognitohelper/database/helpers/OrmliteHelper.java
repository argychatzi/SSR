package com.kth.recognitohelper.database.helpers;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.kth.recognitohelper.database.models.DummyUser;
import com.kth.recognitohelper.database.models.User;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides the DAOs used by the
 * other classes.
 *
 * Created by georgios.savvidis on 26/09/14.
*/
public class OrmliteHelper extends OrmLiteSqliteOpenHelper implements BaseHelper{
    @SuppressWarnings("unused")
    private static final String TAG = "DatabaseHelper";

    // Name of the database
    private static final String DATABASE_NAME = "ssr-android-user.db";

    // Version of the database used, if changes are made to the classes
    // that are saved this needs to be incremented by at least 1.
    private static final int DATABASE_VERSION = 1;

    private RuntimeExceptionDao<User, String> mUserRuntimeDao = null;
    private RuntimeExceptionDao<DummyUser, String> mDummyUserRuntimeDao = null;

    public OrmliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is first created. All the tables used in this project are created here.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
        	TableUtils.createTableIfNotExists(connectionSource, User.class);
        	TableUtils.createTableIfNotExists(connectionSource, DummyUser.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when the application is upgraded and it has a higher DATABASE_VERSION number. This allows us to adjust the old data to
     * match the new database structure.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
        	TableUtils.dropTable(connectionSource, User.class, true);
        	TableUtils.dropTable(connectionSource, DummyUser.class, true);

            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        onCreate(db, connectionSource);
    }

    @Override
    public boolean clearTable(Class<?> cls) {
        try {
            TableUtils.clearTable(connectionSource, cls);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our User class. It will create it or just give
     * the cached value. RuntimeExceptionDao only throw RuntimeExceptions.
     */
    @Override
    public RuntimeExceptionDao<User, String> getUserDao() {
        if (mUserRuntimeDao == null) {
            mUserRuntimeDao = getRuntimeExceptionDao(User.class);
        }
        return mUserRuntimeDao;
    }
    
    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our User class. It will create it or just give
     * the cached value. RuntimeExceptionDao only throw RuntimeExceptions.
     */
    @Override
    public RuntimeExceptionDao<DummyUser, String> getDummyUserDao() {
    	if (mDummyUserRuntimeDao == null) {
    		mDummyUserRuntimeDao = getRuntimeExceptionDao(DummyUser.class);
    	}
    	return mDummyUserRuntimeDao;
    }
    
    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        mUserRuntimeDao = null;
        mDummyUserRuntimeDao = null;
    }
}
