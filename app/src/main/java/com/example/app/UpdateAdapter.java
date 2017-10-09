package com.example.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 635901193 on 2017/7/20.
 */

public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.ViewHolder> {
    private Context context;
    private List<UpdateInformation> list;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView version,information;

        public ViewHolder(View view){
            super(view);
            mView = view;
            version = (TextView) view.findViewById(R.id.update_version);
            information = (TextView) view.findViewById(R.id.update_information);
        }
    }

    public UpdateAdapter(List<UpdateInformation> list){
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(context == null){
            context = parent.getContext();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.update_information,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UpdateInformation ui = list.get(position);
        holder.version.setText(ui.getVersion());
        holder.information.setText(ui.getInformation());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}


