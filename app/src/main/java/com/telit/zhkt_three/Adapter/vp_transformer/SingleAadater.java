package com.telit.zhkt_three.Adapter.vp_transformer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.telit.zhkt_three.JavaBean.AutonomousLearning.QuestionBank;
import com.telit.zhkt_three.R;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class SingleAadater extends RecyclerView.Adapter<SingleAadater.ItemHolder>{
    private static final String TAG = "SingleAadater";
    private Context mContext;
    private QuestionBank questionBank;
    private  Map<String, String> optionMap;

    private ArrayList<String> liftList=new ArrayList<>();
    private ArrayList<String> rightList=new ArrayList<>();

    public SingleAadater(Context mContext, QuestionBank questionBank) {

        this.mContext = mContext;
        this.questionBank = questionBank;
        String answerOptions = questionBank.getAnswerOptions();
        if (!TextUtils.isEmpty(answerOptions)) {
            Gson gson = new Gson();
            optionMap = gson.fromJson(answerOptions, new TypeToken<Map<String, String>>() {
            }.getType());
            if (optionMap != null) {
                Log.i(TAG, "SingleAadater: " + optionMap);
                Iterator<Map.Entry<String, String>> iterator = optionMap.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    liftList.add(entry.getKey());
                    rightList.add(entry.getValue());
                }
            }
        }




    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.judge_select_option_complete_layout, viewGroup, false);

        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder myHolder, int i) {
      /*  myHolder.option_do_htv.setText(liftList.get(i));
        myHolder.option_do_tv.setText(rightList.get(i));*/


        myHolder.option_do_tv.setText(liftList.get(i));
        myHolder.option_do_htv.setText(rightList.get(i));
    }

    @Override
    public int getItemCount() {
        if (optionMap==null){
            return  0;
        }

        return optionMap.size();
    }

    protected class ItemHolder extends RecyclerView.ViewHolder{

        private final TextView option_do_tv;
        private final HtmlTextView option_do_htv;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            option_do_tv = itemView.findViewById(R.id.option_do_tv);
            option_do_htv = itemView.findViewById(R.id.option_do_htv);
        }
    }
}
