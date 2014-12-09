package com.kth.ssr.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.kth.ssr.R;
import com.kth.ssr.fragments.interfaces.ChangeMainFragmentCallback;
import com.kth.ssr.fragments.interfaces.OnManualRecordingListener;
import com.kth.ssr.services.samplecollection.SampleCollectingService;
import com.kth.ssr.services.voicerecognition.RecognitionService;
import com.kth.ssr.utils.alarm.AlarmScheduler;

import java.io.File;

/**
 * Created by georgios.savvidis on 27/10/14.
 */
public class MainFragment extends Fragment implements OnManualRecordingListener{

    private static final String TAG = MainFragment.class.getCanonicalName();

    private Button mCreateAccountButton;
    private Button mInstantRecognitionButton;
    private Button mSamplesCollectingButton;
    private Button mTestButton;
    private ChangeMainFragmentCallback mChangeMainFragmentCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG, "OnCreateView");
        View contentView = inflater.inflate(R.layout.fragment_main, null, false);

        mCreateAccountButton = (Button) contentView.findViewById(R.id.create_account_button);
        mInstantRecognitionButton = (Button) contentView.findViewById(R.id.instant_recognition_button);
        mSamplesCollectingButton = (Button) contentView.findViewById(R.id.samples_collecting_button);
        mTestButton = (Button) contentView.findViewById(R.id.test);

        initListeners();
        toggleServiceButton();

        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        if(activity instanceof ChangeMainFragmentCallback){
            mChangeMainFragmentCallback = (ChangeMainFragmentCallback) activity;
        }
    }

    private void initListeners(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( v.equals(mCreateAccountButton) ){

                    if(mChangeMainFragmentCallback != null){
                        mChangeMainFragmentCallback.changeMainFragment( new CreateAccountFragment() );
                    }

                } else if( v.equals(mInstantRecognitionButton) ){
                    RecordPromptFragment recordPromptFragment = new RecordPromptFragment(MainFragment.this);
                    recordPromptFragment.show(getChildFragmentManager(), null);

                } else if( v.equals(mSamplesCollectingButton) ){

                    if(SampleCollectingService.isRunning(getActivity().getApplicationContext())){
                        //Stop bg service
                        Intent serviceIntent = new Intent(getActivity(), SampleCollectingService.class);
                        getActivity().stopService(serviceIntent);

                        toggleServiceButton();

                    } else{
                        //Show confirmation dialog
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                        dialogBuilder.setTitle(R.string.dialog_bg_service_title)
                                .setMessage( String.format(getString(R.string.dialog_bg_service_message), AlarmScheduler.getStartAlarmFormatted(getActivity()), AlarmScheduler.getStopAlarmFormatted(getActivity())) )
                                .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        //Start service
                                        Intent serviceIntent = new Intent(getActivity(), SampleCollectingService.class);
                                        getActivity().startService(serviceIntent);

                                        //Start alarms here. If they have already been started, this will just override them.
                                        AlarmScheduler.setStartAlarm(getActivity());
                                        AlarmScheduler.setStopAlarm(getActivity());

                                        dialog.dismiss();

                                        toggleServiceButton();
                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        dialogBuilder.create().show();
                    }
                } else if( v.equals(mTestButton)){
                    String file = Environment.getExternalStorageDirectory().getPath() + "/SSR/DummySamples/AdoraSvitak_2010_trimmed.wav";

                    Intent recognitionIntent = RecognitionService.createIntentForVoiceRecognition(getActivity(), new File(file));
                    getActivity().startService(recognitionIntent);

//                    Log.d(TAG, "Memory: " + Runtime.getRuntime().maxMemory());
//
////                    Intent recognitionIntent = RecognitionService.createIntentForNewUserCreation(getActivity(), "Giorgos", new File(file));
//                    Intent recognitionIntent = RecognitionService.createIntentForVoiceRecognition(getActivity(), new File(file));
//
//                    getActivity().startService(recognitionIntent);
                }
            }
        };

        mCreateAccountButton.setOnClickListener(onClickListener);
        mInstantRecognitionButton.setOnClickListener(onClickListener);
        mSamplesCollectingButton.setOnClickListener(onClickListener);
        mTestButton.setOnClickListener(onClickListener);
    }

    private void toggleServiceButton(){
        if(SampleCollectingService.isRunning(getActivity().getApplicationContext())){
            mSamplesCollectingButton.setText(R.string.stop_collecting_samples);
        } else {
            mSamplesCollectingButton.setText(R.string.start_collecting_samples);
        }
    }

    @Override
    public void onSuccess(String filename) {
        Toast.makeText(getActivity(), R.string.toast_starting_recognition, Toast.LENGTH_LONG).show();

        Intent recognitionIntent = RecognitionService.createIntentForVoiceRecognition(getActivity(), new File(filename));
        getActivity().startService(recognitionIntent);
    }

    @Override
    public void onCanceled() {
        //TODO delete recording
    }
}
