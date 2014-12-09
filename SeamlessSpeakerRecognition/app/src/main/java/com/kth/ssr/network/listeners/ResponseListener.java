package com.kth.ssr.network.listeners;

import com.kth.recognitohelper.database.models.MatchedRecord;
import com.kth.recognitohelper.database.transaction.DBTransactionResult;

/**
 * Created by georgios.savvidis on 09/11/14.
 */
public interface ResponseListener {
    void onCreateUserResponse(DBTransactionResult result);
    void onRecognizeUserResponse(MatchedRecord matchedRecord);
}
