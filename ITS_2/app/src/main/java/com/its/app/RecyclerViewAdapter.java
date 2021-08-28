package com.its.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> modelList;

    private OnItemClickListener mItemClickListener;


    public RecyclerViewAdapter(Context context, List<String> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(List<String> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final String model = getItem(position);
            ViewHolder genericViewHolder = (ViewHolder) holder;
            genericViewHolder.itemTxtMessage.setText(ellipsis(model));


        }
    }

    private String ellipsis(String model) {
        if (model.length() > 25) {
            return model.substring(0, 25).concat("...");
        } else {
            return model;
        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private String getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, String model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTxtMessage;


        public ViewHolder(final View itemView) {
            super(itemView);

            this.itemTxtMessage = itemView.findViewById(R.id.item_txt_message);

            itemView.setOnClickListener(view -> mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition())));

        }
    }

}

