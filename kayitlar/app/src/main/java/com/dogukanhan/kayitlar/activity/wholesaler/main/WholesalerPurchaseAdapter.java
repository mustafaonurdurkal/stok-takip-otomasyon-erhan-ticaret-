package com.dogukanhan.kayitlar.activity.wholesaler.main;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dogukanhan.kayitlar.model.Purchase;

import java.util.List;

public class WholesalerPurchaseAdapter extends BaseAdapter {

    private static final LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT, 1);

    private Context context;

    private List<Purchase> purchases;

    public WholesalerPurchaseAdapter(Context context, List<Purchase> purchases){
        this.context = context;
        this.purchases = purchases;
    }

    @Override
    public int getCount() {
        return purchases.size();
    }

    @Override
    public Object getItem(int i) {

        return purchases.get(i);
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


                textViewForMe(layout,String.valueOf(i+1));
                textViewForMe(layout,purchases.get(i).getProduct().getName());
                textViewForMe(layout,purchases.get(i).getAmount().toString());
                textViewForMe(layout,purchases.get(i).getPerCost().toString()+" TL");

               if(purchases.get(i).getCost().toString().equals("0")){
                   textViewForMe(layout,"Ödendi.");
                 }
                else{
                    textViewForMe(layout,purchases.get(i).getCost().toString()+" TL");
                 }
                textViewForMe(layout,purchases.get(i).getDate().toString());

            if(i % 2 == 0){
                layout.setBackgroundColor(Color.GRAY);
            }


        return layout;
    }
}
