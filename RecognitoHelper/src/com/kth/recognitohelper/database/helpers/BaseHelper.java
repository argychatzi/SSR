package com.kth.recognitohelper.database.helpers;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.kth.recognitohelper.database.models.DummyUser;
import com.kth.recognitohelper.database.models.User;

/**
 * Created by georgios.savvidis on 26/09/14.
*/
public interface BaseHelper {

    public boolean clearTable(Class<?> cls);

    public RuntimeExceptionDao<User, String> getUserDao();
    
    public RuntimeExceptionDao<DummyUser, String> getDummyUserDao();

    public void close();
}
