package com.android.verbo.bible;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;

import com.android.verbo.R;
import com.android.verbo.bible.models.Book;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

public class BookViewActivity extends FragmentActivity
{
    private ViewPager2 pager;
    private FragmentStateAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_view);

        Bundle bundle = getIntent().getExtras();

        if(bundle == null) {
            finish();
        } else {
            Book book = new Book(bundle.getInt("bookId"), bundle.getString("bookAbbrev"), bundle.getString("bookName"), bundle.getInt("bookChapters"));
            System.out.println(bundle.getInt("bookChapters"));
            requestChapters(book);
        }
    }

    private void requestChapters(Book book)
    {
        this.pager = findViewById(R.id.view_pager);
        this.adapter = new ScreenPagerAdapter(book);
        pager.setAdapter(adapter);
    }

    private class ScreenPagerAdapter extends FragmentStateAdapter
    {
        private final Book book;

        public ScreenPagerAdapter(Book book)
        {
            super(BookViewActivity.this);
            this.book = book;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position)
        {
            return new ChapterViewFragment(position, this.book);
        }

        @Override
        public int getItemCount()
        {
            return this.book.getChapters();
        }
    }
}