package com.dogukanhan.kayitlar.activity.customer.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dogukanhan.kayitlar.model.Sale;

import java.util.List;

public class CustomerSalesAdapter extends BaseAdapter {

    private static final LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT, 1);

    private Context context;

    private List<Sale> sales;

    public CustomerSalesAdapter(Context context, List<Sale> sales){
        this.context = context;
        this.sales = sales;
    }

    @Override
    public int getCount() {
        return sales.size();
    }

    @Override
    public Object getItem(int i) {

        return sales.get(i);
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


                textViewForMe(layout,String.valueOf(sales.get(i).getId()));
                textViewForMe(layout,sales.get(i).getProduct().getName());
                textViewForMe(layout,sales.get(i).getAmount().toString());
                textViewForMe(layout,sales.get(i).getPerCost().toString()+" TL");

                if(sales.get(i).getCost().toString().equals("0")){
                    textViewForMe(layout,"Ödendi.");
                }
                else{
                    textViewForMe(layout,sales.get(i).getCost().toString()+" TL");
                }

                textViewForMe(layout,sales.get(i).getDate().toString());

            if(i % 2 == 0){
                layout.setBackgroundColor(Color.GRAY);
            }


        return layout;
    }
}
