package com.kth.ssr.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.kth.ssr.R;
import com.kth.ssr.fragments.interfaces.OnManualRecordingListener;
import com.kth.ssr.utils.recorder.AudioSoundRecorder;

/**
 * Created by georgios.savvidis on 27/10/14.
 */
public class RecordPromptFragment extends DialogFragment {

    private OnManualRecordingListener mOnManualRecordingListener;
    private Button mRecordButton;
    private AudioSoundRecorder mAudioSoundRecorder;
    private boolean mIsRecording = false;

    public RecordPromptFragment(){}

    public RecordPromptFragment(OnManualRecordingListener onManualRecordingListener){
        mOnManualRecordingListener = onManualRecordingListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =  new  AlertDialog.Builder(getActivity(), getTheme())
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_record_prompt, null, false);

        builder.setView(rootView);

        mRecordButton = (Button) rootView.findViewById(R.id.record_button);
        mAudioSoundRecorder = new AudioSoundRecorder();

        initListeners();

        return builder.create();
    }

    private void initListeners(){
        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mIsRecording){
                    mRecordButton.setText(R.string.done);
                    mAudioSoundRecorder.start();

                    mIsRecording = true;

                } else{
                    mRecordButton.setText(R.string.dialog_start_recording);
                    mAudioSoundRecorder.stop();

                    if(mOnManualRecordingListener != null){
                        mOnManualRecordingListener.onSuccess(mAudioSoundRecorder.getLastStoredFile());
                    }

                    mIsRecording = false;

                    dismiss();
                }
            }
        };

        mRecordButton.setOnClickListener(onClickListener);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if(mAudioSoundRecorder.isRecording()){
            mAudioSoundRecorder.stop();
            mOnManualRecordingListener.onCanceled();
        }
    }
}
