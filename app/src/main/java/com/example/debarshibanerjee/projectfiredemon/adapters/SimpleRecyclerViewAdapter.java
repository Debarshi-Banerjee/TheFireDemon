package com.example.debarshibanerjee.projectfiredemon.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.debarshibanerjee.projectfiredemon.R;
import com.example.debarshibanerjee.projectfiredemon.pojo.Contributor;

import java.util.List;

/**
 * Created by debarshibanerjee on 11/10/17.
 */

public class SimpleRecyclerViewAdapter extends RecyclerView.Adapter<SimpleRecyclerViewAdapter.SimpleViewHolder> {


    private List<Contributor> mContributer;

    public SimpleRecyclerViewAdapter() {

    }

    public void swapData(List<Contributor> contributorList) {
        this.mContributer = contributorList;
        notifyDataSetChanged();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_simple_view_holder, parent, false);
        SimpleViewHolder viewHolder = new SimpleViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {

        Contributor contributor = mContributer.get(position);
        holder.textViewLogin.setText(contributor.getLogin());
        holder.textViewHtmlUrl.setText(contributor.getHtmlUrl());
        holder.textViewContribution.setText(String.valueOf(contributor.getContributions()));

    }

    @Override
    public int getItemCount() {
        if (mContributer == null)
            return 0;
        else
            return mContributer.size();
    }

    public class SimpleViewHolder extends RecyclerView.ViewHolder {
        public final TextView textViewLogin;
        public final TextView textViewHtmlUrl;
        public final TextView textViewContribution;


        public SimpleViewHolder(View itemView) {
            super(itemView);
            textViewContribution = itemView.findViewById(R.id.tv_contribution);
            textViewHtmlUrl = itemView.findViewById(R.id.tv_html_url);
            textViewLogin = itemView.findViewById(R.id.tv_login);
        }
    }

}