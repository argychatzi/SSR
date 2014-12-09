package com.kth.ssr.services.voicerecognition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.kth.ssr.BuildConfig;
import com.kth.ssr.flavors.ProductFlavor;

import java.io.File;

/**
 * Created by georgios.savvidis on 07/11/14.
 */
public class RecognitionService {

    private static final String TAG = RecognitionService.class.getCanonicalName();

    private static final ProductFlavor FLAVOR = BuildConfig.FLAVOR_TYPE;

    public static Intent createIntentForNewUserCreation(Context context, String userId, File recordingFile) {
        Intent result;

        if( FLAVOR == ProductFlavor.CLIENT_SERVER ){
            result = new Intent(context, ClientServerRecognitionService.class);
        } else{
            result = new Intent(context, StandaloneRecognitionService.class);
        }

        Bundle bundle = new Bundle();
        bundle.putString( RecognitionServiceConstants.BUNDLE_KEY_USER_ID, userId );
        bundle.putSerializable( RecognitionServiceConstants.BUNDLE_KEY_RECORDING_FILE, recordingFile );
        bundle.putSerializable( RecognitionServiceConstants.BUNDLE_KEY_OPERATION, AbstractVoiceRecognitionService.Operation.CREATE_NEW_USER );
        result.putExtras(bundle);

        return result;
    }

    public static Intent createIntentForVoiceRecognition(Context context, File recordingFile) {
        Intent result;

        if( FLAVOR == ProductFlavor.CLIENT_SERVER ){
            result = new Intent(context, ClientServerRecognitionService.class);
        } else{
            result = new Intent(context, StandaloneRecognitionService.class);
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable( RecognitionServiceConstants.BUNDLE_KEY_RECORDING_FILE, recordingFile );
        bundle.putSerializable( RecognitionServiceConstants.BUNDLE_KEY_OPERATION, AbstractVoiceRecognitionService.Operation.RECOGNIZE_USER );
        result.putExtras(bundle);

        return result;
    }
}
