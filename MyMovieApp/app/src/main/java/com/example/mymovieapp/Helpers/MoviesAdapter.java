package com.example.mymovieapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovieapp.R;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder>
{
    private ArrayList arrayList;
    Context context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MoviesAdapter(Context context, ArrayList list)
    {
        this.context = context;
        this.arrayList = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public TextView tv_hobby;
        public MyViewHolder(View view) {
            super(view);
            tv_hobby =view.findViewById(R.id.tv_hobby);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MoviesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_hobby_view, parent, false);
        // set the view's size, margins, paddings and layout parameters {...}
        //pass the view to View Holder
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        // set the data in items
        Object output = arrayList.get(position);
        String soutput= output.toString();
        holder.tv_hobby.setText(soutput);
        // implement setOnClickListener event on item view. {...}

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount()
    {
        return arrayList.size();
    }
}
