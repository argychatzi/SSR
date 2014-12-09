package com.kth.ssr.utils.phonecall;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;


/**
 *
 * Created by georgios.savvidis on 05/09/14.
 */
public class PhoneCallManager extends PhoneStateListener{

    private PhoneCallListener mPhoneCallListener;
    private TelephonyManager mTelephonyManager;

    private int mPreviousState = TelephonyManager.CALL_STATE_IDLE;

    public PhoneCallManager(Context context, PhoneCallListener listener){
        mPhoneCallListener = listener;
        
        mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public void startListeningForIncomingCalls(){
        mTelephonyManager.listen(this, LISTEN_CALL_STATE);
    }


    public void stopListeningForIncomingCalls(){
        mTelephonyManager.listen(this, LISTEN_NONE);
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        if(mPhoneCallListener != null){

            if( state == TelephonyManager.CALL_STATE_OFFHOOK ){
                mPhoneCallListener.onPhoneCallStarted();

            } else if( (state == TelephonyManager.CALL_STATE_IDLE) && (mPreviousState == TelephonyManager.CALL_STATE_OFFHOOK) ){
                mPhoneCallListener.onPhoneCallEnded();
            }
        }

        mPreviousState = state;
    }
}