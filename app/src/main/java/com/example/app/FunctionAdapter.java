package com.example.app;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by 635901193 on 2017/7/20.
 */

public class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.ViewHolder> {
    private Context context;
    private List<Function> functionList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView name;
        ImageView image;

        public ViewHolder(View view){
            super(view);
            mView = view;
            name = (TextView) view.findViewById(R.id.func_name);
            image = (ImageView) view.findViewById(R.id.func_image);
        }
    }

    public FunctionAdapter(List<Function> functionList){
        this.functionList = functionList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.function_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Function function = functionList.get(position);

        holder.name.setText(function.getName());
        Glide.with(context).load(function.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return functionList.size();
    }
}
