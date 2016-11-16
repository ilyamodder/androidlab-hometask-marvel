package ru.ilyamodder.marvel.adapter;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.ilyamodder.marvel.R;
import ru.ilyamodder.marvel.sqlite.ComicsTable;

/**
 * Created by ilya on 16.11.16.
 */

public class ComicsAdapter extends RecyclerView.Adapter<ComicsAdapter.ViewHolder> {
    private Cursor mCursor;

    public ComicsAdapter(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cartoon_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        DateFormat format = SimpleDateFormat.getDateInstance();
        holder.mDate.setText(format.format(mCursor.getLong(mCursor.getColumnIndex(ComicsTable.Columns.DATE_MODIFIED)) * 1000));
        holder.mTitle.setText(mCursor.getString(mCursor.getColumnIndex(ComicsTable.Columns.TITLE)));
        holder.mFormat.setText(mCursor.getString(mCursor.getColumnIndex(ComicsTable.Columns.FORMAT)));
        holder.mDescription.setText(mCursor.getString(mCursor.getColumnIndex(ComicsTable.Columns.DESCRIPTION)));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(mCursor.getString(mCursor.getColumnIndex(ComicsTable.Columns.URL))));
                view.getContext().startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.title)
        TextView mTitle;
        @BindView(R.id.description)
        TextView mDescription;
        @BindView(R.id.date)
        TextView mDate;
        @BindView(R.id.format)
        TextView mFormat;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
