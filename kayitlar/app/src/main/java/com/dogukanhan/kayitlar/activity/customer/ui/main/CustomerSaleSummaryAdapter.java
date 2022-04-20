package com.dogukanhan.kayitlar.activity.customer.ui.main;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dogukanhan.kayitlar.model.Income;
import java.util.List;


public class CustomerSaleSummaryAdapter extends BaseAdapter{
    private static final LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT, 1);

    private Context context;

    private List<Income> incomes;
    public CustomerSaleSummaryAdapter(Context context, List<Income> incomes){
        this.context = context;
        this.incomes = incomes;
    }

    @Override
    public int getCount() {
        return incomes.size();
    }

    @Override
    public Object getItem(int i) {

        return incomes.get(i);
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


        textViewForMe(layout,String.valueOf(incomes.get(i).getSale().getId()));
        textViewForMe(layout,incomes.get(i).getDate().toString());
        textViewForMe(layout,incomes.get(i).getAmount().toString()+" TL");


        if(i % 2 == 0){
            layout.setBackgroundColor(Color.GRAY);
        }


        return layout;
    }
}
