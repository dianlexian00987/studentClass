package com.telit.zhkt_three.Adapter.vp_transformer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.telit.zhkt_three.Adapter.QuestionAdapter.RVQuestionTvAnswerAdapter;
import com.telit.zhkt_three.Constant.Constant;
import com.telit.zhkt_three.CustomView.QuestionView.KnowledgeQuestionView;
import com.telit.zhkt_three.JavaBean.AutonomousLearning.QuestionBank;
import com.telit.zhkt_three.R;

import org.json.JSONObject;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

/**
 * author: qzx
 * Date: 2019/5/20 10:04
 * <p>
 * 如果适配器中是MATCH_PARENT那么就会单独一个Item占据一个屏幕
 */
public class RVQuestionAdapterOne extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private List<QuestionBank> datas;


    //底部是否可见
    private boolean isFootVisible = false;

    public boolean isFootVisible() {
        return isFootVisible;
    }



    //是否没有可加载的数据
    private boolean isAllEnd = false;

    public boolean isAllEnd() {
        return isAllEnd;
    }

    public void setAllEnd(boolean allEnd) {
        isAllEnd = allEnd;
    }

    public RVQuestionAdapterOne(Context context, List<QuestionBank> list) {
        mContext = context;
        datas = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == Constant.Single_Choose) {
            return new SingleChooseHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.single_choose_question_bank_image_layout, viewGroup, false));
        } else if (i == Constant.Fill_Blank) {
            //填空题
            return new FillBlankHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.fill_blank_question_bank_image_layout, viewGroup, false));
        } else if (i == Constant.Subject_Item) {

            return new SubjectItemHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.subject_item_question_bank_inage_layout, viewGroup, false));
        }  else if (i == Constant.Judge_Item) {
            return new JudgeItemHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.judgeselect__question_bank_two_image_layout, viewGroup, false));
        } else if (i == Constant.Multi_Choose) {
            return new MultiChooseHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.multi_choose__question_bank_image_layout, viewGroup, false));
        }

        return null;
      //  return new RVQuestionViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_item_bank_item_layout, viewGroup, false));
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof SingleChooseHolder) {
            SingleChooseHolder singleChooseHolder = (SingleChooseHolder) holder;
            //静止滑动
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            singleChooseHolder.rv_single_change.setOverScrollMode(View.OVER_SCROLL_NEVER);
            singleChooseHolder.rv_single_change.setLayoutManager(linearLayoutManager);
            //设置数据
            if (!TextUtils.isEmpty(datas.get(i).getAnswerOptions())){
                SingleAadater singleAadater=new SingleAadater(mContext,datas.get(i));
                singleChooseHolder.rv_single_change.setAdapter(singleAadater);
            }


        }

    }



    public class SingleChooseHolder extends RecyclerView.ViewHolder {

        private final TextView item_bank_head_title;
        private final HtmlTextView item_bank_head_content;
        private final RecyclerView rv_single_change;
        private final TextView item_bank_tv_answer_show;

        public SingleChooseHolder(@NonNull View itemView) {
            super(itemView);
            //头题型
            item_bank_head_title = itemView.findViewById(R.id.Item_Bank_head_title);
            //头内容
            item_bank_head_content = itemView.findViewById(R.id.Item_Bank_head_content);
            //题的内容
            rv_single_change = itemView.findViewById(R.id.rv_single_change);

          //查看解析
            item_bank_tv_answer_show = itemView.findViewById(R.id.Item_Bank_tv_answer_show);





        }
    }

    public class MultiChooseHolder extends RecyclerView.ViewHolder {
        private final LinearLayout ll_check_multi;

        // private final LinearLayout ll_check_f;

        public MultiChooseHolder(@NonNull View itemView) {
            super(itemView);
            ll_check_multi = itemView.findViewById(R.id.rv_check_multi);
        }
    }

    public class SubjectItemHolder extends RecyclerView.ViewHolder {

        private final LinearLayout ll_subjective_info;

        public SubjectItemHolder(@NonNull View itemView) {
            super(itemView);
            ll_subjective_info = itemView.findViewById(R.id.ll_subjective_info);
        }
    }

    public class FillBlankHolder extends RecyclerView.ViewHolder {

        // private final LinearLayout ll_fill_one;


        public FillBlankHolder(@NonNull View itemView) {
            super(itemView);

        }
    }


    public class JudgeItemHolder extends RecyclerView.ViewHolder {

        private final LinearLayout ll_judge_option;

        public JudgeItemHolder(@NonNull View itemView) {
            super(itemView);
            ll_judge_option = itemView.findViewById(R.id.ll_judge_option);
        }
    }


    public class RVQuestionViewHolder extends RecyclerView.ViewHolder {


        private KnowledgeQuestionView questionView;

        public RVQuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionView = itemView.findViewById(R.id.item_bank_questionView);
        }
    }


    @Override
    public int getItemCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (Constant.Single_Choose == datas.get(position).getQuestionChannelType()) {
            return Constant.Single_Choose;
        } else if (Constant.Multi_Choose == datas.get(position).getQuestionChannelType()) {
            return Constant.Multi_Choose;
        } else if (Constant.Fill_Blank == datas.get(position).getQuestionChannelType()) {
            return Constant.Fill_Blank;
        } else if (Constant.Subject_Item == datas.get(position).getQuestionChannelType()) {
            return Constant.Subject_Item;
        } else if (Constant.Linked_Line == datas.get(position).getQuestionChannelType()) {
            return Constant.Linked_Line;
        } else if (Constant.Judge_Item == datas.get(position).getQuestionChannelType()) {
            return Constant.Judge_Item;
        }
        return Constant.Single_Choose;
    }

}
