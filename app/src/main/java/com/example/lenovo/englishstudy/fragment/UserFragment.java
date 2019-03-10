package com.example.lenovo.englishstudy.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lenovo.englishstudy.LoginActivity;
import com.example.lenovo.englishstudy.MainActivity;
import com.example.lenovo.englishstudy.userdefined.MyView;
import com.example.lenovo.englishstudy.R;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class UserFragment extends Fragment implements MyView.OnRootClickListener {
    private LinearLayout oneItem;
    private LinearLayout twoItem;
    private LinearLayout threeItem;
    private LinearLayout log;
    private TextView login;
    private CircleImageView photo;
    private Tencent mTencent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.userfragment, container, false);
        Log.d("123","1");
        oneItem = view.findViewById(R.id.one_item);
        twoItem = view.findViewById(R.id.two_item);
        threeItem = view.findViewById(R.id.three_item);
        login = view.findViewById(R.id.login);
        photo = view.findViewById(R.id.photo);
        log = view.findViewById(R.id.log);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getContext(), LoginActivity.class));

            }
        });
        initView();
//        Bundle bundle = getArguments();
//        if(bundle != null) {
//            login.setText(bundle.getString("user_name"));
//            Log.d("12345", bundle.getString("user_name"));
//            Uri parse = Uri.parse(bundle.getString("user_photo"));
//            photo.setImageURI(parse);
//        }
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("345","2");
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    login.setText(data.getStringExtra("user_name"));
                    Log.d("12345", data.getStringExtra("user_name"));
                    Uri parse = Uri.parse(data.getStringExtra("user_photo"));
                    photo.setImageURI(parse);
                }

                break;
            default:
        }

    }

    public void initView() {
        //添加我的消息
        oneItem.addView(new MyView(getContext())
                .initMine(R.drawable.ic_message, "我的消息", true)
                .setOnRootClickListener(this, 1));
        //添加我的收藏
        oneItem.addView(new MyView(getContext())
                .initMine(R.drawable.ic_collect, "我的收藏夹", true)
                .setOnRootClickListener(this, 2));
        //添加我的评论
        oneItem.addView(new MyView(getContext())
                .initMine(R.drawable.ic_comment, "我的评论", false)
                .setOnRootClickListener(this, 3));

        //添加帮助反馈
        twoItem.addView(new MyView(getContext())
                .initMine(R.drawable.ic_help, "帮助反馈", true)
                .setOnRootClickListener(this, 4));

        //添加推荐美文
        twoItem.addView(new MyView(getContext())
                .initMine(R.drawable.ic_recommend, "推荐美文", false)
                .setOnRootClickListener(this, 5));

        //添加设置
        threeItem.addView(new MyView(getContext())
                .initMine(R.drawable.ic_set, "设置", false)
                .setOnRootClickListener(this, 6));

    }

    @Override
    public void onRootClick(View view) {
        switch ((int) view.getTag()) {
            case 1:
                //   startActivity(new Intent(getContext(), MyDownloadActivity.class));
                break;
            case 2:
                //    startActivity(new Intent(getContext(), MyCollectionActivity.class));
                break;
        }

    }

}
