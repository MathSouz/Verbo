package com.android.verbo.bible.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.verbo.R;
import com.android.verbo.bible.models.Book;
import com.google.android.material.transition.Hold;

public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.Holder>
{
    private final Context context;
    private Book[] books;
    private OnItemListClickListener listClickListener;

    public BookListAdapter(Context context, Book[] book, OnItemListClickListener clickListener)
    {
        this.context = context;
        this.books = book;
        this.listClickListener = clickListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.book_list_item, parent, false);
        return new Holder(view, listClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position)
    {
        final Book book = books[position];
        holder.abbrev.setText(book.getAbbrev());
        holder.name.setText(book.getName());
    }

    @Override
    public int getItemCount()
    {
        return books.length;
    }

    public static class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView abbrev, name;
        private OnItemListClickListener clickListener;

        public Holder(@NonNull View itemView, OnItemListClickListener clickListener)
        {
            super(itemView);
            this.abbrev = itemView.findViewById(R.id.abbrev_view);
            this.name = itemView.findViewById(R.id.name_view);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            clickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListClickListener
    {
        void onItemClick(int position);
    }
}
