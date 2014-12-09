package com.kth.ssr.broadcast;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kth.recognitohelper.database.models.MatchedRecord;
import com.kth.recognitohelper.database.transaction.DBTransactionResult;
import com.kth.ssr.R;

/**
 * Created by georgios.savvidis on 07/11/14.
 */
public class RecognitoBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = RecognitoBroadcastReceiver.class.getCanonicalName();


    public static final String BROADCAST_ACTION_CREATE_USER_RESULT = "ActionCreateUser";
    public static final String BROADCAST_ACTION_RECOGNIZE_USER_RESULT = "ActionRecognizeUser";
    public static final String EXTRA_RESULT = "Result";

    private Activity mActivity;

    public RecognitoBroadcastReceiver(Activity activity){
        mActivity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "OnReceive");

        String action = intent.getAction();

        if(action.equals( BROADCAST_ACTION_CREATE_USER_RESULT )){
            DBTransactionResult transactionResult = (DBTransactionResult)intent.getSerializableExtra(EXTRA_RESULT);
            showUserCreationResultToast( transactionResult );

        } else if(action.equals( BROADCAST_ACTION_RECOGNIZE_USER_RESULT )){

            MatchedRecord matchedRecord = (MatchedRecord)intent.getSerializableExtra(EXTRA_RESULT);
            showRecognitionResultDialog( matchedRecord );
        }
    }

    private void showUserCreationResultToast(DBTransactionResult transactionResult){

        if(mActivity == null){
            return;
        }

        if( transactionResult.getResultCode() == DBTransactionResult.RESULT_SUCCESS ){
            Toast.makeText(mActivity, mActivity.getString( R.string.toast_profile_creation_success), Toast.LENGTH_LONG ).show();

        } else if( transactionResult.getFailureReason() == DBTransactionResult.FAILURE_REASON_USER_ALREADY_EXISTS) {
            Toast.makeText(mActivity, mActivity.getString( R.string.toast_profile_creation_failed_user_exists), Toast.LENGTH_LONG ).show();

        } else  {
            Toast.makeText(mActivity, mActivity.getString( R.string.toast_general_failure), Toast.LENGTH_LONG ).show();

        }
    }

    private void showRecognitionResultDialog(MatchedRecord matchedRecord){

        if(mActivity == null){
            return;
        }

        LayoutInflater inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_result, null);
        TextView matchedUserTextView = (TextView)dialogView.findViewById(R.id.matched_user);
        TextView likelihoodTextView = (TextView)dialogView.findViewById(R.id.likelihood);

        String matchedUserText = null;
        String likelihoodText = null;

        if(matchedRecord != null){
            matchedUserText = String.format(mActivity.getString(R.string.dialog_recognition_matched_user), matchedRecord.getUser().getUsername());
            likelihoodText = String.format(mActivity.getString(R.string.dialog_recognition_likelihood), matchedRecord.getLikelihood());

        } else{
            matchedUserText = mActivity.getString(R.string.dialog_recognition_result_no_record);
        }

        matchedUserTextView.setText( matchedUserText );
        likelihoodTextView.setText( likelihoodText );

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mActivity);
        dialogBuilder.setTitle(R.string.dialog_recognition_result_title)
                .setView(dialogView)
                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        dialogBuilder.create().show();
    }

    public static IntentFilter createIntentFilter(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION_CREATE_USER_RESULT);
        intentFilter.addAction(BROADCAST_ACTION_RECOGNIZE_USER_RESULT);

        return intentFilter;
    }
}
