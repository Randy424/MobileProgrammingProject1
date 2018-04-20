package edu.fsu.cs.mobile.mobileprogrammingproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Randy Bruno-Piverger on 4/10/2018.
 */

public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.ViewHolder> {

    private final Context context;
    private final String[] title;
    private final String[] desc;
    private final String[] time;
    private final String[] email;

    public RecylerViewAdapter(Context context, String[] title, String[] desc, String[] time, String[] email) {
        this.context = context;
        this.title = title;
        this.desc = desc;
        this.time = time;
        this.email = email;
    }

    @Override
    public RecylerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_items, parent, false);

        return new ViewHolder(view);
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

    static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView tv1;
        final TextView tv2;
        final TextView tv3;
        final TextView tv4;

        ViewHolder(View itemView) {
            super(itemView);

            tv1 = itemView.findViewById(R.id.textView1);
            tv2 = itemView.findViewById(R.id.textView2);
            tv3 = itemView.findViewById(R.id.textView3);
            tv4 = itemView.findViewById(R.id.textViewEmail);
        }
    }
}
