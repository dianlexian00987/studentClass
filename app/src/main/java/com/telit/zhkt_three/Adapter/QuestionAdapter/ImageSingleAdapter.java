package com.telit.zhkt_three.Adapter.QuestionAdapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.telit.zhkt_three.JavaBean.HomeWork.QuestionInfo;
import com.telit.zhkt_three.R;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

public class ImageSingleAdapter  extends RecyclerView.Adapter<ImageSingleAdapter.MyHolder> {


    private Context mContext;
    private List<QuestionInfo.SelectBean> selectBeans;

    public ImageSingleAdapter(Context mContext, List<QuestionInfo.SelectBean> selectBeans) {

        this.mContext = mContext;
        this.selectBeans = selectBeans;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.judge_select_option_complete_layout, viewGroup, false);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
       myHolder.option_do_tv.setText(selectBeans.get(i).getOptions());
    }

    @Override
    public int getItemCount() {
        return selectBeans!=null? selectBeans.size():0;
    }

    protected class MyHolder extends RecyclerView.ViewHolder{
        private TextView option_do_tv;
        private HtmlTextView option_do_htv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            option_do_tv = itemView.findViewById(R.id.option_do_tv);
            option_do_htv = itemView.findViewById(R.id.option_do_htv);
            option_do_htv.setVisibility(View.GONE);
            Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "PingFang-SimpleBold.ttf");
            option_do_tv.setTypeface(typeface);
            option_do_htv.setTypeface(typeface);
        }
    }
}
