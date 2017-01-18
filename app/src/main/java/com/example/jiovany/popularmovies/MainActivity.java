package com.example.jiovany.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.jiovany.popularmovies.utils.Constants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private MoviesAdapter moviesAdapter;
    private RecyclerView moviesRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OkHttpClient client;
    private Gson gson;
    private String type = Constants.MOST_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initRecyclerView();
        client = new OkHttpClient();
        gson = new Gson();
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        moviesAdapter = new MoviesAdapter();
        moviesRecyclerView.setAdapter(moviesAdapter);
        moviesRecyclerView.hasFixedSize();
        moviesRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void initUI() {
        moviesRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_movies);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        getSupportActionBar().setSubtitle(getString(R.string.title_most_popular));
    }

    @Override
    public void onRefresh() {
        String typeName = type.equals(Constants.HIGHEST_RATED) ? Constants.HIGHEST_RATED : Constants.MOST_POPULAR;
        String urlToGetData = Constants.BASE_URL_MOVIES_DATA.concat(typeName).concat(Constants.API_KEY);
        new GetMoviesDataTask().execute(urlToGetData);
    }

    public class GetMoviesDataTask extends AsyncTask<String, Void, Movie[]> {

        @Override
        protected Movie[] doInBackground(String... params) {
            Movie[] movieData = null;
            Request request = new Request.Builder()
                    .url(params[0])
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    try {
                        String responseString = response.body().string();
                        JSONObject responseJSON = new JSONObject(responseString);
                        JSONArray resultsArray = responseJSON.getJSONArray("results");
                        movieData = new Movie[resultsArray.length()];
                        for (int i = 0; i < resultsArray.length(); i++) {
                            Movie movie = gson.fromJson(resultsArray.getJSONObject(i).toString(), Movie.class);
                            movieData[i] = movie;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return movieData;
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            swipeRefreshLayout.setRefreshing(false);
            if (movies != null) {
                moviesAdapter.setMoviesData(movies);
                moviesRecyclerView.smoothScrollToPosition(0);
            } else {
                Toast.makeText(getApplicationContext(), "Error trayendo data", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toggle_menu_item) {
            if (type.equals(Constants.HIGHEST_RATED)) {
                type = Constants.MOST_POPULAR;
                item.setTitle(getString(R.string.title_highest_rated));
                getSupportActionBar().setSubtitle(getString(R.string.title_most_popular));
            } else {
                type = Constants.HIGHEST_RATED;
                item.setTitle(getString(R.string.title_most_popular));
                getSupportActionBar().setSubtitle(getString(R.string.title_highest_rated));
            }
            swipeRefreshLayout.setRefreshing(true);
            onRefresh();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
