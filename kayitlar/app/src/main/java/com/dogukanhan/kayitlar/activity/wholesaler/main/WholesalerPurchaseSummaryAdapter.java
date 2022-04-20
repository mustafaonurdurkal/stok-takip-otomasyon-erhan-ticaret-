package com.dogukanhan.kayitlar.activity.wholesaler.main;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dogukanhan.kayitlar.model.Income;
import com.dogukanhan.kayitlar.model.Payout;

import java.util.List;


public class WholesalerPurchaseSummaryAdapter extends BaseAdapter{
    private static final LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT, 1);

    private Context context;

    private List<Payout> payouts;
    public WholesalerPurchaseSummaryAdapter(Context context, List<Payout> payouts){
        this.context = context;
        this.payouts = payouts;
    }


    @Override
    public int getCount() {
        return payouts.size();
    }

    @Override
    public Object getItem(int i) {

        return payouts.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }

    private TextView textViewForMe(View view, String text){

        LinearLayout layout = (LinearLayout) view;

        TextView textView = new TextView(context);
        textView.setText(text);
        textView.setLayoutParams(param);

        layout.addView(textView);

        return textView;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View layout;


        layout = new LinearLayout(context);


        textViewForMe(layout,String.valueOf(payouts.get(i).getPurchase().getId()));
        textViewForMe(layout,payouts.get(i).getDate().toString());
        textViewForMe(layout,payouts.get(i).getAmount().toString()+" TL");


        if(i % 2 == 0){
            layout.setBackgroundColor(Color.GRAY);
        }


        return layout;
    }

}
