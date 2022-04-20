package com.dogukanhan.kayitlar.activity.wholesaler.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.model.Wholesaler;

import java.util.List;

public class WholesalerAdapter extends ArrayAdapter<Wholesaler> {

    public WholesalerAdapter(Context context, List<Wholesaler> wholesalers) {
        super(context, 0, wholesalers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Wholesaler wholesaler = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.wholesaler_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.textViewWholesalerName);
        TextView tvIncome = (TextView) convertView.findViewById(R.id.textViewWholesalerpayout);
        tvName.setTextSize(18);
        tvIncome.setTextSize(18);
        // Populate the data into the template view using the data object
        tvName.setText((position+1)+" -) "+wholesaler.getName()+" "+wholesaler.getSurname());
        // Return the completed view to render on screen
        tvIncome.setText("\t"+wholesaler.getCurrentPayout().toString()+" TL");
        return convertView;
    }
}