package com.example.diary.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.diary.Model.DiaryDetail;
import com.example.diary.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyAdapterRecycler extends RecyclerView.Adapter<MyAdapterRecycler.ViewHolder> {

    private List<DiaryDetail> mdetails;
    private Context mcontext;

    public MyAdapterRecycler(Context context,List<DiaryDetail> details){
        mdetails = details;
        mcontext = context;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView lb_day_passed;
        public TextView lb_time;
        public TextView lb_title;
        public TextView lb_content;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lb_day_passed = (TextView) itemView.findViewById(R.id.post_date_label);
            lb_time = (TextView) itemView.findViewById(R.id.post_time_label);
            lb_title = (TextView) itemView.findViewById(R.id.post_title);
            lb_content = (TextView) itemView.findViewById(R.id.post_content);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_recycler,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DiaryDetail diaryDetail = mdetails.get(position);
        holder.lb_day_passed.setText(DiaryDetail.countDate(diaryDetail.getDateOfCreation_()));
        holder.lb_time.setText(getTimeToShow(diaryDetail.getDateOfCreation_()));
        holder.lb_title.setText(diaryDetail.getTitle_());
        holder.lb_content.setText(diaryDetail.getContent_());
    }

    @Override
    public int getItemCount() {
        return mdetails.size();
    }

    private String getTimeToShow(Date date) {
        String time = "";
        Calendar itemCal = Calendar.getInstance();
        itemCal.setTime(date);
        time += itemCal.get(Calendar.HOUR) + ":" + itemCal.get(Calendar.MINUTE);
        if (itemCal.get(Calendar.AM_PM) == Calendar.AM) {
            time += "\nA.M";
        } else {
            time += "\nP.M";
        }
        return time;
    }

}
