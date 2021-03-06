package com.example.lenovo.englishstudy.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.englishstudy.activity.ChooseHistoryActivity;
import com.example.lenovo.englishstudy.R;
import com.example.lenovo.englishstudy.Util.GetRequest_Interface;
import com.example.lenovo.englishstudy.animation.ExplosionField;
import com.example.lenovo.englishstudy.animation.MoveImageView;
import com.example.lenovo.englishstudy.animation.PointFTypeEvaluator;
import com.example.lenovo.englishstudy.bean.WordList;
import com.example.lenovo.englishstudy.bean.WordTranslate;
import com.example.lenovo.englishstudy.db.Sentence;
import com.example.lenovo.englishstudy.userdefined.FlowLayout;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChooseFragment extends Fragment {

    private ImageView hezi;
    private RelativeLayout contain;
    private Toolbar toolbar;
    private ImageView end;
    private ImageView ianswer;
    private TextView answer1;
    private TextView answer2, answer3;
    private String as = "";
    private Boolean flag1 = true;
    private Boolean flag2 = false;
    private Boolean flag3 = false;
    private List<String> mNames = new ArrayList<>();
    //  private String mNames[] = {};
    private FlowLayout mFlowLayout;
    private View view;
    private ImageButton ichange_word;
    private TextView tchange_word;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.choosefragment, container, false);
        end = view.findViewById(R.id.end);
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag3) {
                    flag1 = false;
                    end.setVisibility(View.GONE);
                    answer2.setText(as);
                    requestWordTranslate(as);
                    beginAnimation2();
                }
            }
        });
        end.setVisibility(View.GONE);
        answer1 = view.findViewById(R.id.answer1);
        answer1.setVisibility(View.GONE);
        answer2 = view.findViewById(R.id.answer2);
        answer2.setVisibility(View.GONE);
        answer3 = view.findViewById(R.id.answer3);
        answer3.setVisibility(View.GONE);
        toolbar = view.findViewById(R.id.title);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        ianswer = view.findViewById(R.id.ianswer);
        ianswer.setVisibility(View.GONE);
        contain = view.findViewById(R.id.search);
        hezi = view.findViewById(R.id.hezi);
        ichange_word = view.findViewById(R.id.image_change_word);
        tchange_word = view.findViewById(R.id.text_change_word);
        //    initChildViews(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("999999", "1");
        requestWordList();
        ichange_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFlowLayout.removeAllViews();
                mNames.clear();
                requestWordList();
            }
        });
        tchange_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFlowLayout.removeAllViews();
                mNames.clear();
                requestWordList();
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history:
                Intent intent = new Intent(getActivity(), ChooseHistoryActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    public void beginAnimation1(View view) {
        int[] childCoordinate = new int[2];
        int[] parentCoordinate = new int[2];
        int[] shopCoordinate = new int[2];
        //1.分别获取被点击View、父布局、购物车在屏幕上的坐标xy。
        view.getLocationInWindow(childCoordinate);
        contain.getLocationInWindow(parentCoordinate);
        hezi.getLocationInWindow(shopCoordinate);


        //2.自定义ImageView 继承ImageView
        MoveImageView img = new MoveImageView(getContext());
        img.setImageResource(R.mipmap.ic_heart);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(120, 120);//两个400分别为添加图片的大小
        img.setLayoutParams(params);

        //3.设置img在父布局中的坐标位置
        img.setX(childCoordinate[0] - parentCoordinate[0]);
        img.setY(childCoordinate[1] - parentCoordinate[1]);
        //4.父布局添加该Img
        contain.addView(img);

        //5.利用 二次贝塞尔曲线 需首先计算出 MoveImageView的2个数据点和一个控制点
        PointF startP = new PointF();
        PointF endP = new PointF();
        PointF controlP = new PointF();
        //开始的数据点坐标就是 addV的坐标
        startP.x = childCoordinate[0] - parentCoordinate[0];
        startP.y = childCoordinate[1] - parentCoordinate[1];
        //结束的数据点坐标就是 shopImg的坐标
        endP.x = shopCoordinate[0] - parentCoordinate[0] + 150;
        endP.y = shopCoordinate[1] - parentCoordinate[1] + 80;
        //控制点坐标 x等于 购物车x；y等于 addV的y
        controlP.x = endP.x;
        controlP.y = startP.y;

        //启动属性动画
        ObjectAnimator animator = ObjectAnimator.ofObject(img, "mPointF",
                new PointFTypeEvaluator(controlP), startP, endP);
        animator.setDuration(1000);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //动画结束后 父布局移除 img
                Object target = ((ObjectAnimator) animation).getTarget();
                contain.removeView((View) target);
                //shopImg 开始一个放大动画
                Animation scaleAnim = AnimationUtils.loadAnimation(getContext(), R.anim.anim);
                hezi.startAnimation(scaleAnim);
                scaleAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        flag3 = true;
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void beginAnimation2() {
        Animation rotateAnimation = new RotateAnimation(0, 5, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new CycleInterpolator(5));
        rotateAnimation.setDuration(1000);
        hezi.startAnimation(rotateAnimation);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setSelfAndChildDisappearOnClick(hezi);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    /**
     * 为自己以及子View添加破碎动画，动画结束后，把View消失掉
     *
     * @param view 可能是ViewGroup的view
     */
    private void setSelfAndChildDisappearOnClick(final View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                setSelfAndChildDisappearOnClick(viewGroup.getChildAt(i));
            }
        } else {

            new ExplosionField(ChooseFragment.this.getContext()).explode(view,
                    new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            view.setVisibility(View.GONE);
                            answer1.setVisibility(View.VISIBLE);
                            ianswer.setVisibility(View.VISIBLE);
                            answer2.setVisibility(View.VISIBLE);
                            answer3.setVisibility(View.VISIBLE);
                            flag2 = true;
                            flag1 = true;
                            as = "";

                        }
                    });
        }

    }

    private void initChildViews(View view) {
        // TODO Auto-generated method stub
        mFlowLayout = (FlowLayout) view.findViewById(R.id.flowlayout);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = 10;
        lp.rightMargin = 10;
        lp.topMargin = 10;
        lp.bottomMargin = 10;
        for (int i = 0; i < mNames.size(); i++) {
            final TextView textView = new TextView(getContext());
            textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview));
            textView.setText(mNames.get(i));
            textView.setTextColor(Color.BLACK);
            mFlowLayout.addView(textView, lp);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    textView.setBackgroundDrawable(getResources().getDrawable(R.drawable.textview_click));
                    flag3 = false;
                    as += textView.getText() + " ";
                    if (flag1) {
                        beginAnimation1(view);
                        end.setVisibility(View.VISIBLE);
                    }
                    if (flag2) {
                        hezi.setVisibility(View.VISIBLE);
                        answer1.setVisibility(View.GONE);
                        answer2.setVisibility(View.GONE);
                        answer3.setVisibility(View.GONE);
                        ianswer.setVisibility(View.GONE);
                    }
                }
            });
        }

    }

    public void requestWordTranslate(final String word) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.fanyi.baidu.com/api/trans/vip/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);
        Call<WordTranslate> call = request.getWordTranslateCall(word, md5("20180809000192979" + word + "1435660288" + "ty5IoV55tyJoTtK5WBid"));
        call.enqueue(new Callback<WordTranslate>() {
            @Override
            public void onResponse(Call<WordTranslate> call, Response<WordTranslate> response) {
                final WordTranslate wordTranslate = response.body();
                if (wordTranslate != null) {
                    Sentence sentence = new Sentence();
                    sentence.setSentence(word);
                    sentence.setSentence_translate(wordTranslate.getTrans_result().get(0).getDst());
                    sentence.save();
                    answer3.setText(wordTranslate.getTrans_result().get(0).getDst());
                    mFlowLayout.removeAllViews();
                    initChildViews(getView());
                } else {
                    Toast.makeText(getActivity(), "获取翻译失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<WordTranslate> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getActivity(), "获取翻译失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestWordList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.zhangshuo.fun/words/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetRequest_Interface request = retrofit.create(GetRequest_Interface.class);
        Call<WordList> call = request.getWordListCall();
        call.enqueue(new Callback<WordList>() {
            @Override
            public void onResponse(Call<WordList> call, Response<WordList> response) {
                final WordList wordList = response.body();
                if (wordList != null) {
                    if (wordList.getStatus() == 0) {
                        for (WordList.DataBean word : wordList.getData()) {
                            mNames.add(word.getWord());
                        }
                        initChildViews(view);
                    }
                }
            }

            @Override
            public void onFailure(Call<WordList> call, Throwable t) {
                t.printStackTrace();
                Log.d("0000000",t.toString());
                Toast.makeText(getContext(), "获取单词失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


}




