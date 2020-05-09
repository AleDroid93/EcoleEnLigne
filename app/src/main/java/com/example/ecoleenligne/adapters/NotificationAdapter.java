package com.example.ecoleenligne.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecoleenligne.R;
import com.example.ecoleenligne.models.Notification;
import com.example.ecoleenligne.views.HomeActivity;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{
    private ArrayList<Notification> mNotifications;
    private Context context;


    public NotificationAdapter(ArrayList<Notification> mNotifications) {
        this.mNotifications = mNotifications;
    }

    public NotificationAdapter(Context ctx, ArrayList<Notification> notifications) {
        this.mNotifications = notifications;
        this.context = ctx;
    }


    public ArrayList<Notification> getmNotifications() {
        return mNotifications;
    }

    public void setmNotifications(ArrayList<Notification> mNotifications) {
        this.mNotifications = mNotifications;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item_view, parent, false);
        NotificationViewHolder vh = new NotificationAdapter.NotificationViewHolder(view, context);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = mNotifications.get(position);
        String dateTime = notification.getDatetime();
        String title = notification.getTopic();
        boolean toRead = notification.getRead();
        if(toRead)
            holder.toReadImg.setVisibility(View.VISIBLE);
        else
            holder.toReadImg.setVisibility(View.GONE);
        holder.notificationTitle.setText(title);
        holder.notificationDatetime.setText(dateTime);
        holder.notification = notification;
        if(notification.getScope().equalsIgnoreCase("course"))
            holder.notificationLogo.setImageResource(R.drawable.ic_study_notification_logo);
    }

    @Override
    public int getItemCount() {
        return mNotifications.size();
    }


    /**
     * ViewHolder Inner Class
     */
    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout notificationLayout;
        public TextView notificationDatetime;
        public TextView notificationTitle;
        public ImageView toReadImg;
        public ImageView notificationLogo;
        public Context ctx;
        public Notification notification;


        public NotificationViewHolder(@NonNull View itemView,Context ctx) {
            super(itemView);
            this.notification = new Notification();
            notificationLayout = itemView.findViewById(R.id.notification_layout);
            notificationDatetime = itemView.findViewById(R.id.notification_datetime);
            notificationTitle = itemView.findViewById(R.id.notification_title);
            notificationLogo = itemView.findViewById(R.id.notification_logo);
            toReadImg = itemView.findViewById(R.id.dot_to_read);
            notificationLayout.setOnClickListener(getNotificationClickListener(ctx));
            /*
            if (toRead)
                toReadImg.setVisibility(View.VISIBLE);
            else
                toReadImg.setVisibility(View.GONE);
            notificationLogo.setImageResource(logo);
            */
        }



        private View.OnClickListener getNotificationClickListener(Context ctx){
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("NotificationAdapter", "notification clicked");
                    ImageView toRead = v.findViewById(R.id.dot_to_read);
                    if(toRead.getVisibility() == View.VISIBLE){
                        // TODO notify viewModel to mark this notification as read
                        HomeActivity activity = (HomeActivity) ctx;
                        activity.markNotificationAsRead(notification);
                        toReadImg.setVisibility(View.GONE);
                    }
                }
            };
        }
    }
}
