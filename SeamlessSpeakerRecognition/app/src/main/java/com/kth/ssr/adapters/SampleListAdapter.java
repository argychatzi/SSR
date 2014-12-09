package com.kth.ssr.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.kth.ssr.R;
import com.kth.ssr.voicesample.models.VoiceSample;

/**
 * Created by argychatzi on 3/29/14.
 */
public class SampleListAdapter extends SSRBaseAdapter {

    public SampleListAdapter(List<VoiceSample> data) {
        super(data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        VoiceSample sample = (VoiceSample) mData.get(position);

        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.voice_sample_list_item, null);
        TextView sampleTitle = (TextView) view.findViewById(R.id.sample_list_item_title);
        sampleTitle.setText(sample.getFileName());

        return view;
    }
}
