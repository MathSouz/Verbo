package com.android.verbo.bible;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.android.verbo.R;
import com.android.verbo.VerboApp;
import com.android.verbo.bible.models.Book;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChapterViewFragment extends Fragment
{
    private final int pos;
    private Book book;
    private List<String> verses = new ArrayList<>();
    private TextView textView;
    private Toolbar toolbar;
    private View loadingView;
    private ScrollView textScrollView;

    public ChapterViewFragment(int position, Book book)
    {
        this.pos = position;
        this.book = book;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_chapter_view, container, false);
        textView = view.findViewById(R.id.text_view);
        toolbar = view.findViewById(R.id.toolbar);
        loadingView = view.findViewById(R.id.loading_view);
        textScrollView = view.findViewById(R.id.text_scroll_view);

        toolbar.setTitle(book.getAbbrev().toUpperCase() + " " + (pos + 1));
        toolbar.setSubtitle(book.getName());
        textView.setCustomSelectionActionModeCallback(new StyleCallback());

        return view;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        new GetChapterAsyncTask().execute();
    }

    private class GetChapterAsyncTask extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... voids)
        {
            String url = String.format("https://bibleapi.co/api/verses/%s/%s/%s", "acf", book.getAbbrev(), pos + 1);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response)
                {
                    verses.clear();

                    try
                    {
                        JSONArray jsonArray = response.getJSONArray("verses");

                        for (int i = 0; i < jsonArray.length(); i++)
                        {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int number = object.getInt("number");
                            String text = object.getString("text");
                            verses.add(text);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    onFinishRequest();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + ((VerboApp)getActivity().getApplication()).getToken());
                    return headers;
                }
            };

            ((VerboApp)getActivity().getApplication()).getRequestQueue().add(request);

            return null;
        }
    }

    private void onFinishRequest()
    {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < verses.size(); i++)
        {
            String s = verses.get(i);
            builder.append(i + 1);
            builder.append(" ");
            builder.append(s);

            if(i < verses.size() - 1) {
                builder.append("\n");
            }
        }

        textView.setText(builder.toString());
        loadingView.setVisibility(View.GONE);
        textScrollView.setVisibility(View.VISIBLE);
    }

    private class StyleCallback implements ActionMode.Callback
    {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu)
        {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.mark, menu);
            menu.removeItem(android.R.id.selectAll);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item)
        {
            if(item.getItemId() == R.id.mark_action) {
                mode.finish();
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    }
}
