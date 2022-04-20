package com.dogukanhan.kayitlar.activity.customer.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dogukanhan.kayitlar.R;
import com.dogukanhan.kayitlar.model.Customer;

import java.util.List;

public class CustomerAdapter extends ArrayAdapter<Customer> {

    public CustomerAdapter(Context context, List<Customer> customers) {
        super(context, 0, customers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Customer customer = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.customer_item, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.textViewCustomerName);
        TextView tvIncome = (TextView) convertView.findViewById(R.id.textViewCustomerIncome);
        // Populate the data into the template view using the data object
        tvName.setText((position+1)+" -) "+customer.getName()+" "+customer.getSurname());
        tvName.setTextSize(18);
        tvIncome.setTextSize(18);
        // Return the completed view to render on screen
        if(customer.getCurrentIncome()!=null){
            tvIncome.setText("\t"+customer.getCurrentIncome().toString()+" TL");
        }
        else{
            tvIncome.setText("\t"+"0");
        }
        return convertView;
    }
}