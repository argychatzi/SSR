package com.kth.ssr.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kth.ssr.R;
import com.kth.ssr.fragments.interfaces.OnManualRecordingListener;
import com.kth.ssr.services.voicerecognition.RecognitionService;

import java.io.File;

/**
 * Created by georgios.savvidis on 27/10/14.
 */
public class CreateAccountFragment extends Fragment implements OnManualRecordingListener {

    public static final String TAG = CreateAccountFragment.class.getCanonicalName();

    private TextView mRecordingTextView;
    private EditText mUsernameEditText;
    private Button mRecordButton;
    private Button mCreateAccountButton;
    private String mFirstRecordingFileName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Log.d(TAG, "OnCreateView");
        View contentView = inflater.inflate(R.layout.fragment_create_account, null, false);

        mRecordingTextView = (TextView) contentView.findViewById(R.id.recording_textview);
        mUsernameEditText = (EditText) contentView.findViewById(R.id.username_edittext);
        mRecordButton = (Button) contentView.findViewById(R.id.record_button);
        mCreateAccountButton = (Button) contentView.findViewById(R.id.create_account_button);

        initListeners();

        return contentView;
    }

    private void initListeners(){
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final int length = s.length();

                if(length > 0){
                    mRecordButton.setEnabled(true);
                } else{
                    mRecordButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        OnClickListener onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if( v.equals(mRecordButton) ){
                    Log.d(TAG, "Record");

                    hideKeyboard();

                    RecordPromptFragment recordPromptFragment = new RecordPromptFragment(CreateAccountFragment.this);
                    recordPromptFragment.show(getChildFragmentManager(), null);

                } else if( v.equals(mCreateAccountButton) ){
                    Log.d(TAG, "Create account");

                    Toast.makeText(getActivity(), getString(R.string.toast_creating_profile), Toast.LENGTH_LONG).show();

                    Intent newUserIntent = RecognitionService.createIntentForNewUserCreation(getActivity(), mUsernameEditText.getText().toString(), new File(mFirstRecordingFileName));
                    getActivity().startService(newUserIntent);
                }
            }
        };

        mUsernameEditText.addTextChangedListener(textWatcher);
        mRecordButton.setOnClickListener(onClickListener);
        mCreateAccountButton.setOnClickListener(onClickListener);
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onSuccess(String filename) {
        mFirstRecordingFileName = filename;

        mRecordingTextView.setText(R.string.recording_success);
        mCreateAccountButton.setEnabled(true);
    }

    @Override
    public void onCanceled() {
        //TODO delete recording from disk

        mRecordingTextView.setText(R.string.recording_failure);
        mCreateAccountButton.setEnabled(false);
    }
}
