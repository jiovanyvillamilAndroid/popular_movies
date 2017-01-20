package com.example.jiovany.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Toast;

import com.example.jiovany.popularmovies.utils.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, OnRequestFinish<Movie>, OnItemClick {
    private MoviesAdapter moviesAdapter;
    private RecyclerView moviesRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OkHttpClient client;
    private Gson gson;
    private String type = Constants.MOST_POPULAR;
    private NetworkHelper networkHelper;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initRecyclerView();
        initClass();
    }

    private void initClass() {
        client = new OkHttpClient();
        gson = new Gson();
        swipeRefreshLayout.setRefreshing(true);
        networkHelper = new NetworkHelper(this);
        onRefresh();
    }

    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        moviesAdapter = new MoviesAdapter(this);
        moviesRecyclerView.setAdapter(moviesAdapter);
        moviesRecyclerView.hasFixedSize();
        moviesRecyclerView.setLayoutManager(gridLayoutManager);
    }

    private void initUI() {
        moviesRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_movies);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(this);
        actionBar = getSupportActionBar();
        setSubTitleActionBar(getString(R.string.title_most_popular));
    }

    private void setSubTitleActionBar(String subtitle) {
        if (actionBar != null) {
            actionBar.setSubtitle(subtitle);
        }
    }

    private void toggleMenuUserMoviePreference(MenuItem item) {
        if (type.equals(Constants.HIGHEST_RATED)) {
            type = Constants.MOST_POPULAR;
            item.setTitle(getString(R.string.title_highest_rated));
            setSubTitleActionBar(getString(R.string.title_most_popular));
        } else {
            type = Constants.HIGHEST_RATED;
            item.setTitle(getString(R.string.title_most_popular));
            setSubTitleActionBar(getString(R.string.title_highest_rated));
        }
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        String typeName = type.equals(Constants.HIGHEST_RATED) ? Constants.HIGHEST_RATED : Constants.MOST_POPULAR;
        String urlToGetData = Constants.BASE_URL_MOVIES_DATA.concat(typeName).concat(Constants.API_KEY);
        networkHelper.getMoviesData(urlToGetData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.toggle_menu_item) {
            toggleMenuUserMoviePreference(item);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSuccess(final ArrayList<Movie> response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                if (response != null) {
                    moviesAdapter.setMoviesData(response);
                    moviesRecyclerView.smoothScrollToPosition(0);
                } else {
                    onFailure(R.string.response_empty_message);
                }
            }
        });
    }

    @Override
    public void onFailure(final int errorMessageResId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), getString(errorMessageResId), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onItemClick(Movie movie) {
        Intent intentGoDetail = new Intent(MainActivity.this, MovieDetailActivity.class);
        intentGoDetail.putExtra(MovieDetailActivity.MOVIE_KEY, movie);
        startActivity(intentGoDetail);
    }
}
