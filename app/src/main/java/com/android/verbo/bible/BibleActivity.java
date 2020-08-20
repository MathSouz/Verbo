package com.android.verbo.bible;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.verbo.R;
import com.android.verbo.VerboApp;
import com.android.verbo.bible.adapter.BookListAdapter;
import com.android.verbo.bible.models.Book;
import com.android.verbo.login.LoginActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BibleActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, BookListAdapter.OnItemListClickListener
{
    private boolean searchBoxHidden = true;
    private RecyclerView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Book> bookList = new ArrayList<>();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible);
        listView = findViewById(R.id.list_view);
        swipeRefreshLayout = findViewById(R.id.refresh_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.swipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();
    }

    @Override
    public void onRefresh()
    {
        swipeRefreshLayout.setRefreshing(true);
        new GetBooksAsyncTask().execute();
    }

    @Override
    public void onItemClick(int position)
    {
        Book book = bookList.get(position);
        Intent intent = new Intent(this, BookViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("bookId", book.getId());
        bundle.putString("bookAbbrev", book.getAbbrev());
        bundle.putString("bookName", book.getName());
        bundle.putInt("bookChapters", book.getChapters());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class GetBooksAsyncTask extends AsyncTask<String, Void, Void>
    {
        @Override
        protected Void doInBackground(String... tokens)
        {
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, "https://bibleapi.co/api/books", null, new Response.Listener<JSONArray>()
            {
                @Override
                public void onResponse(JSONArray response)
                {
                    for (int i = 0; i < response.length(); i++)
                    {
                        try
                        {
                            JSONObject book = response.getJSONObject(i);
                            bookList.add(
                                    new Book(i, book.getJSONObject("abbrev").getString("pt"), book.getString("name"), book.getInt("chapters"))
                            );
                        }

                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    Book[] book = new Book[bookList.size()];
                    bookList.toArray(book);
                    listView.setLayoutManager(new LinearLayoutManager(BibleActivity.this));
                    listView.setAdapter(new BookListAdapter(BibleActivity.this, book, BibleActivity.this));
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error)
                {
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(BibleActivity.this, LoginActivity.class));
                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + ((VerboApp)getApplication()).getToken());
                    return headers;
                }
            };

            ((VerboApp)getApplication()).getRequestQueue().add(request);
            return null;
        }
    }
}