package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;


/**
 * Created by Randy Bruno-Piverger on 4/10/2018.
 */

public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.ViewHolder> {

    Context context;
    String[] title;
    String[] desc;
    String[] time;
    String[] email;
    View view;
    ViewHolder viewHolder;

    public RecylerViewAdapter(Context context, String[] title, String[] desc, String[] time, String[] email) {
        this.context = context;
        this.title = title;
        this.desc = desc;
        this.time = time;
        this.email = email;
    }

    @Override
    public RecylerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.recycle_items, parent, false);


        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecylerViewAdapter.ViewHolder holder, int position) {

        holder.tv1.setText(title[position]);
        holder.tv2.setText(desc[position]);
        holder.tv3.setText(time[position]);
        holder.tv4.setText(email[position]);

    }

    @Override
    public int getItemCount() {
        return time.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv1;
        TextView tv2;
        TextView tv3;
        TextView tv4;

        public ViewHolder(View itemView) {
            super(itemView);

            tv1 = (TextView) itemView.findViewById(R.id.textView1);
            tv2 = (TextView) itemView.findViewById(R.id.textView2);
            tv3 = (TextView) itemView.findViewById(R.id.textView3);
            tv4 = (TextView) itemView.findViewById(R.id.textViewEmail);
        }
    }
}
