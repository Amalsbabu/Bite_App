package com.example.bite;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProgramAdapter extends RecyclerView.Adapter<ProgramAdapter.ViewHolder> {

    Context context;
    String[] hotelNameList;
    String[] hotelRating;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView rowName;
        TextView rowRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowName = itemView.findViewById(R.id.textview1);
            rowRating = itemView.findViewById(R.id.textview2);
            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context,Hotel1.class);
                    context.startActivity(intent);
                }
            });
        }
    }

    public ProgramAdapter(Context context, String[] hotelNameList,String[] hotelRating, int[] image)
    {
        this.context = context;
        this.hotelNameList = hotelNameList;
        this.hotelRating = hotelRating;

    }
    @NonNull
    @Override
    public ProgramAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_item,parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgramAdapter.ViewHolder holder, int position) {
        holder.rowName.setText(hotelNameList[position]);
        holder.rowRating.setText(hotelRating[position]);

    }

    @Override
    public int getItemCount() {

        return hotelNameList.length;
    }
}
