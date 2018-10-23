package com.weex.wdialog_lib;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xuwx on 2018/10/15.
 */

public class WDialog extends DialogFragment {

    private TagFlowLayout container;
    private TextView cancelAction;

    public static WDialog newInstance(List<Item> datas) {

        Bundle args = new Bundle();
        args.putSerializable("data", (Serializable) datas);
        WDialog fragment = new WDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_w, null);
        container = view.findViewById(R.id.action_container);
        container.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Toast.makeText(getContext(), "view -> " + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        container.setAdapter(new TagAdapter<Item>((List<Item>) getArguments().getSerializable("data")) {
            @Override
            public View getView(FlowLayout parent, int position, Item item) {
                View itemParent = LayoutInflater.from(getContext()).inflate(R.layout.item, null);
                ImageView imageView = itemParent.findViewById(R.id.img);
                imageView.setImageResource(item.getItemResource());
                TextView tv = itemParent.findViewById(R.id.tv);
                tv.setText(item.getItemName());
                return itemParent;
            }
        });
        cancelAction = view.findViewById(R.id.action_cancel);
        cancelAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        builder.setView(view);
        for (int i = 0; i < container.getChildCount(); i++) {
            container.getChildAt(i).setVisibility(View.INVISIBLE);
        }
        handler.sendEmptyMessageDelayed(0, 500);
        return builder.create();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (container.getChildAt(msg.what) == null) return;
            View item = container.getChildAt(msg.what);
            item.setVisibility(View.VISIBLE);
            item.requestLayout();
            Animator anim = ObjectAnimator.ofFloat(item, "translationY", 200f, 0f);
            anim.setInterpolator(new BounceInterpolator());
            anim.setDuration(300);
            anim.start();
            sendEmptyMessageDelayed(++msg.what, 500);
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
