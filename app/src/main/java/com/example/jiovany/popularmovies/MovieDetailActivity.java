package com.example.jiovany.popularmovies;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.ViewPropertyAnimation;
import com.example.jiovany.popularmovies.utils.Constants;

public class MovieDetailActivity extends AppCompatActivity {
    public static String MOVIE_KEY = "movie_to_show_detail";
    private Movie currentMovie;
    private FrameLayout headerContainer;
    private ImageView backDrop;
    private ImageView poster;
    private TextView title;
    private TextView adult;
    private TextView language;
    private TextView releaseDate;
    private TextView overview;
    private TextView originalTitle;
    private TextView popularity;
    private TextView voteCount;
    private TextView voteAverage;
    private ImageButton playTrailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        AppBarLayout appBarLayout = (AppBarLayout)findViewById(R.id.appbar);
        poster = (ImageView)findViewById(R.id.iv_movie_poster);
        final int initialPosterHeight = poster.getLayoutParams().height;
        final int initialPosterWidth = poster.getLayoutParams().width;
        final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.ll_container);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percent = (100 - ((verticalOffset)+528)*100/528);
                float newHeight = initialPosterHeight - (initialPosterHeight*(percent/100));
                float newWidth = initialPosterWidth - (initialPosterWidth*(percent/100));
                Log.e("diffPercent",percent+"");
                Log.e("initialPoster",initialPosterHeight+"");
                Log.e("height",newHeight+"");
                poster.getLayoutParams().height = Math.round(newHeight);
                poster.getLayoutParams().width = Math.round(newWidth);
                poster.requestLayout();
                linearLayout.setPadding(0,poster.getLayoutParams().height / 2,0,0);
            }
        });
       /* getMovieFromIntent();
        initUI();
        bindData();*/
    }
/*
    private void bindData(){
        Glide.with(this).
                load(currentMovie.getBackdropCompleteUrl(true))
                .centerCrop()
                .into(backDrop);
        Glide.with(this).
                load(currentMovie.getPosterCompleteUrl(false))
                .fitCenter()
                .into(poster);
        title.setText(currentMovie.getTitle());
        if(currentMovie.isAdult()){
            adult.setVisibility(View.VISIBLE);
            adult.setText("+18");
        }else{
            adult.setVisibility(View.GONE);
        }
        language.setText("Language: "+currentMovie.getOriginalLanguage());
        releaseDate.setText("Release Date: "+currentMovie.getReleaseDate());
        overview.setText(currentMovie.getOverview());
        originalTitle.setText("Original Title: ".concat(currentMovie.getOriginalTitle()));
        popularity.setText("Popularity: "+String.valueOf(currentMovie.getPopularity()));
        voteCount.setText(("Vote Count: "+String.valueOf(currentMovie.getVoteCount())));
        voteAverage.setText(String.valueOf(currentMovie.getVoteAverage()).concat(Constants.BLACK_STAR_UNICODE));
        showTrailerButton();
        setActionBarSubtitle(currentMovie.getTitle());
    }

    private void setActionBarSubtitle(String subtitle){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setSubtitle(subtitle);
        }
    }

    private void initUI() {
        headerContainer = (FrameLayout) findViewById(R.id.fl_header_container);
        backDrop = (ImageView) findViewById(R.id.iv_backdrop);
        poster = (ImageView) findViewById(R.id.iv_movie_poster);
        title = (TextView) findViewById(R.id.tv_title);
        adult = (TextView) findViewById(R.id.tv_adult);
        language = (TextView) findViewById(R.id.tv_language);
        releaseDate = (TextView) findViewById(R.id.tv_release_date);
        overview = (TextView) findViewById(R.id.tv_overview);
        originalTitle = (TextView) findViewById(R.id.tv_original_title);
        popularity = (TextView) findViewById(R.id.tv_popularity);
        voteCount = (TextView) findViewById(R.id.tv_vote_count);
        voteAverage = (TextView) findViewById(R.id.tv_vote_average);
        playTrailer = (ImageButton) findViewById(R.id.ib_play_trailer);
    }

    private void showTrailerButton(){
        if (currentMovie.isVideo()) {
            playTrailer.setVisibility(View.VISIBLE);
        }else{
            playTrailer.setVisibility(View.GONE);
        }
    }

    private void getMovieFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentMovie = (Movie) extras.getSerializable(MOVIE_KEY);
        } else {
            finish();
        }
    }*/
}
