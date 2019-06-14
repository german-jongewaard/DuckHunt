package com.jongewaard.dev.duckhunt.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jongewaard.dev.duckhunt.Models.User;
import com.jongewaard.dev.duckhunt.R;

import java.util.List;


public class MyUserRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;

    public MyUserRecyclerViewAdapter(List<User> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mItem = mValues.get(position);
        int pos = position+1;
        holder.textViewPosition.setText(pos + "º");
        holder.textViewDucks.setText(String.valueOf(mValues.get(position).getDuck()));
        holder.textViewNick.setText(mValues.get(position).getNick());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textViewPosition;
        public final TextView textViewDucks;
        public final TextView textViewNick;
        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            textViewPosition = (TextView) view.findViewById(R.id.textViewPosition);
            textViewDucks = (TextView) view.findViewById(R.id.textViewDucks);
            textViewNick = (TextView) view.findViewById(R.id.textViewNick);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + textViewNick.getText() + "'";
        }
    }
}
