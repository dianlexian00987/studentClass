package com.telit.zhkt_three.Adapter.QuestionAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;


import com.telit.zhkt_three.Constant.Constant;
import com.telit.zhkt_three.CustomView.QuestionView.JudgeSelectToDoView;
import com.telit.zhkt_three.CustomView.QuestionView.NewKnowledgeQuestionView;
import com.telit.zhkt_three.CustomView.QuestionView.SubjectiveToDoView;
import com.telit.zhkt_three.CustomView.QuestionView.TotalQuestionView;
import com.telit.zhkt_three.CustomView.QuestionView.matching.JudgeSelectToDoView_two;
import com.telit.zhkt_three.CustomView.QuestionView.matching.MatchingLayout;
import com.telit.zhkt_three.JavaBean.HomeWork.QuestionInfo;
import com.telit.zhkt_three.JavaBean.HomeWorkAnswerSave.AnswerItem;
import com.telit.zhkt_three.JavaBean.HomeWorkAnswerSave.LocalTextAnswersBean;
import com.telit.zhkt_three.MyApplication;
import com.telit.zhkt_three.R;
import com.telit.zhkt_three.Utils.ApkListInfoUtils;
import com.telit.zhkt_three.Utils.QZXTools;
import com.telit.zhkt_three.Utils.UserUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * author: qzx
 * Date: 2019/5/23 16:44
 * <p>
 * 注意：这个和拍照出题公用Adapter，所以这里判断是否是错题集入口分别进入拍照出题/题库出题
 */
public class RVQuestionTvAnswerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;

    //0未提交  1 已提交  2 已批阅
    private String taskStatus;

    //是否是错题集展示界面
    private boolean isMistakesShown;
    private int types;
    private String comType;
    private boolean isScroll;

    /**
     * 是否是图片出题模式即随堂练习模式
     */
    private boolean isImageTask;

    private List<QuestionInfo> questionInfoList;

    private String homeworkId;

    private String showAnswerDate;
    private final ExecutorService executorService;

    /**
     * 如果homeworkid为空字符串表示没有homeworkid，在QuestionInfo中存在homeworkid
     */
    public void setQuestionInfoList(List<QuestionInfo> questionInfoList, String homeworkId, String showAnswerDate) {
        //这个是提真正的数据
        this.questionInfoList = questionInfoList;
        this.homeworkId = homeworkId;
        this.showAnswerDate = showAnswerDate;
    }

    private String xd;
    private String chid;
    private String difficulty;
    private String type;

    /**
     * 塞入错题巩固必传信息:
     * 学段、学科、难易度以及题型
     */
    public void fetchNeedParam(String xd, String subjective, String difficulty, String questionType) {
        this.xd = xd;
        chid = subjective;
        this.difficulty = difficulty;
        type = questionType;
        QZXTools.logE("xd=" + xd + ";chid=" + chid + ";difficulty=" + difficulty + ";type=" + type, null);
    }

    /**
     * @param status          作业的状态
     * @param isImageQuestion 是否图片出题模式
     * @param mistakesShown   是否是错题集展示界面
     * @param types
     * @param comType
     * @param isScroll
     */
    public RVQuestionTvAnswerAdapter(Context context, String status, boolean isImageQuestion,
                                     boolean mistakesShown, int types, String comType, boolean isScroll) {
        mContext = context;
        taskStatus = status;
        isImageTask = isImageQuestion;
        isMistakesShown = mistakesShown;
        this.types = types;
        this.comType = comType;
        this.isScroll = isScroll;

        //创建线程池
        executorService = ApkListInfoUtils.getInstance().onStart();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == Constant.Single_Choose) {
            return new SingleChooseHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.single_choose_image_layout, viewGroup, false));
        } else if (i == Constant.Fill_Blank) {
            return new FillBlankHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.fill_blank_image_layout, viewGroup, false));
        } else if (i == Constant.Subject_Item) {

            return new SubjectItemHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.subject_item_inage_layout, viewGroup, false));
        } else if (i == Constant.Linked_Line) {
            return new LinkedLineHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.linked_line_image_layout, viewGroup, false));
        } else if (i == Constant.Judge_Item) {
            return new JudgeItemHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.judgeselect_two_image_layout, viewGroup, false));
        } else if (i == Constant.Multi_Choose) {
            return new MultiChooseHolder(LayoutInflater.from(mContext)
                    .inflate(R.layout.multi_choose_image_layout, viewGroup, false));
        }
        return null;
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        //单选和多选的按钮
        if (viewHolder instanceof SingleChooseHolder) {
            List<QuestionInfo.SelectBean> selectBeans = questionInfoList.get(i).getList();

            //holder里面也加一个recylecview，设置这个recycleview不能嵌套滚动用来替代这个for循环
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            mLinearLayoutManager.setInitialPrefetchItemCount(4);

            mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            ((SingleChooseHolder) viewHolder).rv_check_single.setLayoutManager(mLinearLayoutManager);
            ImageSingleAdapter imageSingleAdapter = new ImageSingleAdapter(mContext, selectBeans);
            ((SingleChooseHolder) viewHolder).rv_check_single.setAdapter(imageSingleAdapter);





            // JudgeSelectToDoView
        } else if (viewHolder instanceof MultiChooseHolder) {
            //多选
            List<QuestionInfo.SelectBean> selectBeans = questionInfoList.get(i).getList();

            //holder里面也加一个recylecview，设置这个recycleview不能嵌套滚动用来替代这个for循环
            LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
            mLinearLayoutManager.setInitialPrefetchItemCount(4);
            mLinearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            ((MultiChooseHolder) viewHolder).rv_check_multi.setNestedScrollingEnabled(false);
            ((MultiChooseHolder) viewHolder).rv_check_multi.setLayoutManager(mLinearLayoutManager);
            ImageSingleAdapter imageSingleAdapter = new ImageSingleAdapter(mContext, selectBeans);
            ((MultiChooseHolder) viewHolder).rv_check_multi.setAdapter(imageSingleAdapter);




        } else if (viewHolder instanceof FillBlankHolder) {
            //填空题
            List<QuestionInfo.SelectBean> selectBeans = questionInfoList.get(i).getList();
        } else if (viewHolder instanceof SubjectItemHolder) {
            //主观题
            List<QuestionInfo.SelectBean> selectBeans = questionInfoList.get(i).getList();
            for (int j = 0; j <= selectBeans.size(); j++) {
                SubjectiveToDoView subjectiveToDoView = new SubjectiveToDoView(mContext);
                //添加了主观题的题目
                ((SubjectItemHolder) viewHolder).ll_subjective_info.addView(subjectiveToDoView);
            }
        } else if (viewHolder instanceof JudgeItemHolder) {
            //判断题
            List<QuestionInfo.SelectBean> selectBeans = questionInfoList.get(i).getList();

            for (int j = 0; j <= selectBeans.size(); j++) {
                JudgeSelectToDoView_two judgeSelectToDoView_two = new JudgeSelectToDoView_two(mContext);
                ((JudgeItemHolder) viewHolder).ll_judge_option.addView(judgeSelectToDoView_two);
            }
        } else if (viewHolder instanceof LinkedLineHolder) {
            //连线题
            List<QuestionInfo.SelectBean> selectBeans = questionInfoList.get(i).getList();
            for (int j = 0; j <= selectBeans.size(); j++) {
                if (!isScroll) {
                    //不滑动的时候
                    MatchingLayout matchingLayout = new MatchingLayout(mContext);
                    //设置连线题的宽高
                    matchingLayout.resetItemWidthAndHeight(mContext.getResources().getDimensionPixelSize(R.dimen.y200),
                            mContext.getResources().getDimensionPixelSize(R.dimen.y55));
                    //第一步表示状态
                    matchingLayout.setStatus(Integer.parseInt(taskStatus));
                    //第二步填充数据展示
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            matchingLayout.fillData(questionInfoList.get(i).getLeftList(), questionInfoList.get(i).getRightList());
                            //第三步填充自己答题痕迹以及正确答案数据
                            matchingLayout.setLineResult(questionInfoList.get(i).getOwnList(), questionInfoList.get(i).getAnswer());

                        }
                    });

                }

            }

         /*   MatchingLayout matchingLayout=new MatchingLayout(mContext);
            //设置连线题的宽高
            matchingLayout.resetItemWidthAndHeight(mContext.getResources().getDimensionPixelSize(R.dimen.y200),
                    mContext.getResources().getDimensionPixelSize(R.dimen.y55));
            //第一步表示状态
            matchingLayout.setStatus(Integer.parseInt(taskStatus));
            //第二步填充数据展示

            matchingLayout.fillData(questionInfoList.get(i).getLeftList(), questionInfoList.get(i).getRightList());
            //第三步填充自己答题痕迹以及正确答案数据
            matchingLayout.setLineResult(questionInfoList.get(i).getOwnList(), questionInfoList.get(i).getAnswer());
            ((LinkedLineHolder)viewHolder).ll_match_porint_info.addView(matchingLayout);*/
        }


    }

    /**
     * 每一个位置的item都作为单独一项来设置
     * viewType 设置为position
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        switch (questionInfoList.get(position).getQuestionType()) {
            case Constant.Single_Choose:
                //单选题
                return Constant.Single_Choose;
            case Constant.Multi_Choose:

                return Constant.Multi_Choose;
            case Constant.Fill_Blank:

                return Constant.Fill_Blank;
            case Constant.Subject_Item:

                return Constant.Subject_Item;
            case Constant.Linked_Line:

                return Constant.Linked_Line;
            case Constant.Judge_Item:

                return Constant.Judge_Item;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return questionInfoList != null ? questionInfoList.size() : 0;
    }

    public class RVQuestionTvAnswerViewHolder extends RecyclerView.ViewHolder {

        private TotalQuestionView totalQuestionView;
        private NewKnowledgeQuestionView newKnowledgeQuestionView;
        //        private PhotoView attach_photo;
//        private ScrollView scrollView;
        private LinearLayout linearLayout;

        public RVQuestionTvAnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            totalQuestionView = itemView.findViewById(R.id.item_total_question);
            newKnowledgeQuestionView = itemView.findViewById(R.id.item_total_banks);

//            scrollView = itemView.findViewById(R.id.item_scroll);
            linearLayout = itemView.findViewById(R.id.item_scroll_linear);
        }
    }

    public class SingleChooseHolder extends RecyclerView.ViewHolder {

        private final RecyclerView rv_check_single;


        public SingleChooseHolder(@NonNull View itemView) {
            super(itemView);

            rv_check_single = itemView.findViewById(R.id.rv_check_single);


        }
    }

    public class MultiChooseHolder extends RecyclerView.ViewHolder {
        private final RecyclerView rv_check_multi;

        // private final LinearLayout ll_check_f;

        public MultiChooseHolder(@NonNull View itemView) {
            super(itemView);
            rv_check_multi = itemView.findViewById(R.id.rv_check_multi);
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

    public class LinkedLineHolder extends RecyclerView.ViewHolder {

        private final RecyclerView rv_match_porint_info;

        public LinkedLineHolder(@NonNull View itemView) {
            super(itemView);
            rv_match_porint_info = itemView.findViewById(R.id.rv_match_porint_info);
        }
    }

    public class JudgeItemHolder extends RecyclerView.ViewHolder {

        private final LinearLayout ll_judge_option;

        public JudgeItemHolder(@NonNull View itemView) {
            super(itemView);
            ll_judge_option = itemView.findViewById(R.id.ll_judge_option);
        }
    }

    /**
     * 图片出题需要新增图片,改用一个LinearLayout模式
     */
    private void addAttachImgs(LinearLayout linearLayout, String src) {
        //先移除以前的
        linearLayout.removeAllViews();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ImageView photoView = new ImageView(mContext);
        Glide.with(mContext).load(src).into(photoView);
        linearLayout.addView(photoView, layoutParams);
    }

    private boolean needShowAnswer = false;

    /**
     * 提问专用：查看答案
     */
    public void needShowAnswer() {
        needShowAnswer = true;
    }


}
