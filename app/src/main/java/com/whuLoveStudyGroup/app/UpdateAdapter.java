package com.whuLoveStudyGroup.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 635901193 on 2017/7/20.
 */

public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.ViewHolder> {
    private Context context;
    private List<UpdateInformation> list;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        TextView version,information;
        ImageView current;

        public ViewHolder(View view){
            super(view);
            mView = view;
            version = (TextView) view.findViewById(R.id.update_version);
            information = (TextView) view.findViewById(R.id.update_information);
            current = (ImageView) view.findViewById(R.id.update_current);
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
        if (ui.getVersion().equals("Version 0.5.33.180103"))
            holder.current.setVisibility(View.VISIBLE);
        else
            holder.current.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
