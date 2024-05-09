package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.calback.BookCallBack;
import com.example.myapplication.calback.BookListener;
import com.example.myapplication.data.Book;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private Context context;
    private SearchView searchView;
    private Chip chip_Horror, chip_Adventure, chip_Travel, chip_Romance;
    private RecyclerView fHome_RV_books;
    private Database database;
    private ArrayList<String> categoryFilter;
    private ArrayList<Book> allBooks;
    public HomeFragment(Context context) {
        this.context = context;
        database = new Database();
        categoryFilter = new ArrayList<>();
    }

    public void searchFilter(String text){
        ArrayList<Book> books = new ArrayList<Book>();
        if(text.isEmpty()){
            books = allBooks;
        }else{
            for(Book book: allBooks){
                if(book.getName().startsWith(text)){
                    books.add(book);
                }
            }
        }

        BookAdapter bookAdapter = new BookAdapter(context, books);
        bookAdapter.setBookListener(new BookListener() {
            @Override
            public void onClick(Book book) {
                openCommentScreen(book);
            }
        });
        fHome_RV_books.setLayoutManager(new GridLayoutManager(context, 2));
        fHome_RV_books.setHasFixedSize(true);
        fHome_RV_books.setItemAnimator(new DefaultItemAnimator());
        fHome_RV_books.setAdapter(bookAdapter);
        fHome_RV_books.getAdapter().notifyDataSetChanged();
    }

    private void openCommentScreen(Book book) {
        Intent intent = new Intent(context, AddComentActivity.class);
        intent.putExtra("BOOK", book);
        startActivity(intent);
    }

    public void filterBooks(){
        ArrayList<Book> books = new ArrayList<Book>();
        if(categoryFilter.size() ==0){
            books = allBooks;
        }

        for(Book book : allBooks){
            for(String category: categoryFilter){
                if(book.getCategory().equals(category)){
                    books.add(book);
                }
            }
        }

        BookAdapter bookAdapter = new BookAdapter(context, books);
        bookAdapter.setBookListener(new BookListener() {
            @Override
            public void onClick(Book book) {
                openCommentScreen(book);
            }
        });
        fHome_RV_books.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        fHome_RV_books.setHasFixedSize(true);
        fHome_RV_books.setItemAnimator(new DefaultItemAnimator());
        fHome_RV_books.setAdapter(bookAdapter);
        fHome_RV_books.getAdapter().notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        findViews(view);
        initVars();
        return view;

    }

    private void findViews(View view) {
        searchView = view.findViewById(R.id.searchView);
        chip_Horror = view.findViewById(R.id.chip_Horror);
        chip_Adventure = view.findViewById(R.id.chip_Adventure);
        chip_Travel = view.findViewById(R.id.chip_Travel);
        chip_Romance = view.findViewById(R.id.chip_Romance);
        fHome_RV_books = view.findViewById(R.id.fHome_RV_books);

    }

    private void initVars() {
        database.setBookCallBack(new BookCallBack() {
            @Override
            public void onFetchBooksComplete(ArrayList<Book> books) {
                allBooks = books;
                BookAdapter bookAdapter = new BookAdapter(context, books);
                bookAdapter.setBookListener(new BookListener() {
                    @Override
                    public void onClick(Book book) {
                        openCommentScreen(book);
                    }
                });
                fHome_RV_books.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                fHome_RV_books.setHasFixedSize(true);
                fHome_RV_books.setItemAnimator(new DefaultItemAnimator());
                fHome_RV_books.setAdapter(bookAdapter);
            }

            @Override
            public void onBookUploadComplete(Task<Void> task) {

            }
        });

        database.fetchBooks();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchFilter(newText);
                return false;
            }
        });
        chip_Horror.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    categoryFilter.add("Horror");
                }else{
                    categoryFilter.remove("Horror");
                }

                filterBooks();
            }
        });

        chip_Adventure.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    categoryFilter.add("Adventure");
                }else{
                    categoryFilter.remove("Adventure");
                }

                filterBooks();
            }
        });

        chip_Travel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    categoryFilter.add("Travel");
                }else{
                    categoryFilter.remove("Travel");
                }

                filterBooks();
            }
        });

        chip_Romance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    categoryFilter.add("Romance");
                }else{
                    categoryFilter.remove("Romance");
                }

                filterBooks();
            }
        });
    }
}