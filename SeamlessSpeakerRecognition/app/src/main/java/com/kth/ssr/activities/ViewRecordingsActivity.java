package com.kth.ssr.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kth.ssr.R;
import com.kth.ssr.adapters.SampleListAdapter;
import com.kth.ssr.voicesample.models.VoiceSample;
import com.kth.ssr.voicesample.VoiceSampleFinder;

import java.util.List;

/**
 * Shows the files stored under the SSR directory in the external storage§
 * Created by argychatzi on 3/23/14.
 */
public class ViewRecordingsActivity extends Activity {
    public static final String PATH_OF_OPERATION = "PATH_OF_OPERATION";

    public static Intent getDefaultLaunchIntent(Activity activityContext) {
        return getLaunchIntent(activityContext);
    }

    public static Intent getLaunchIntent(Activity activityContext) {
        Intent launchIntent = new Intent(activityContext, ViewRecordingsActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString(PATH_OF_OPERATION, Environment.getExternalStorageDirectory().getAbsolutePath().toString());
        launchIntent.putExtras(bundle);

        return  launchIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_voices);

        String pathOfOperation = getIntent().getExtras().getString(PATH_OF_OPERATION);
        ListView listView = (ListView) findViewById(R.id.select_voice_sample_list);

        final List<VoiceSample> samples = VoiceSampleFinder.getSamples(pathOfOperation);

        SampleListAdapter adapter = new SampleListAdapter(samples);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VoiceSample sampleSelected = samples.get(position);
//                Intent voiceTokenizerIntent = VoiceTokenizerActivity.getLaunchIntent(SelectVoiceActivity.this, sampleSelected);
//                startActivity(voiceTokenizerIntent);
//                Intent playSampleIntent = PlayVoiceSampleActivity.getLaunchIntent(SelectVoiceActivity.this, sampleSelected);
//                startActivity(playSampleIntent);
            }
        });
    }
}
