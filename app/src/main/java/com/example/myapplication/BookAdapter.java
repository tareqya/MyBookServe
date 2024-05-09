package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.calback.BookListener;
import com.example.myapplication.data.Book;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Book> books;
    private Context context;
    private BookListener bookListener;

    public BookAdapter(Context context, ArrayList<Book> books){
        this.books = books;
        this.context = context;
    }
    public void setBookListener(BookListener bookListener){
        this.bookListener = bookListener;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Book book = getItem(position);
        BookViewHolder bookViewHolder = (BookViewHolder) holder;

        bookViewHolder.book_RB_rate.setRating((float)book.getRate());
        bookViewHolder.bookItem_TV_name.setText(book.getName());
        bookViewHolder.bookItem_TV_description.setText(book.getDescription());
        Glide.with(context).load(book.getImageUrl()).into(bookViewHolder.bookItem_IV_image);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public Book getItem(int pos){
        return this.books.get(pos);
    }

    public class BookViewHolder extends  RecyclerView.ViewHolder {
        public ImageView bookItem_IV_image;
        public TextView bookItem_TV_name;
        public RatingBar book_RB_rate;
        public TextView bookItem_TV_description;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookItem_IV_image = itemView.findViewById(R.id.bookItem_IV_image);
            bookItem_TV_name = itemView.findViewById(R.id.bookItem_TV_name);
            book_RB_rate = itemView.findViewById(R.id.book_RB_rate);
            bookItem_TV_description = itemView.findViewById(R.id.bookItem_TV_description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Book book = getItem(pos);
                    bookListener.onClick(book);
                }
            });
        }
    }
}
