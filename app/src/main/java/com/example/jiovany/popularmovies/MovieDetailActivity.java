package com.example.jiovany.popularmovies;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.jiovany.popularmovies.utils.Constants;

public class MovieDetailActivity extends AppCompatActivity {
    public static String MOVIE_KEY = "movie_to_show_detail";
    private Movie currentMovie;
    private CardView cardViewPoster;
    private AppBarLayout appBarLayout;
    private ImageView poster;
    private ImageView backDrop;
    private TextView overView;
    private TextView releaseDate;
    private TextView voteAverage;
    private TextView voteCount;
    private TextView popularity;
    private LinearLayout contentLinearLayout;
    private TextView toolbarSubtitle;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        getMovieFromIntent();
        initUI();
        initToolbar();
        bindData();
        scalePosterOnCollapse();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(currentMovie.getTitle());
        }
        toolbarSubtitle.setText(String.valueOf(currentMovie.getVoteAverage()).concat(Constants.BLACK_STAR_UNICODE));
    }

    private void changeActionBarColor(int color) {
        collapsingToolbarLayout.setBackgroundColor(color);
        collapsingToolbarLayout.setContentScrimColor(color);
        collapsingToolbarLayout.setStatusBarScrimColor(color);
    }

    private void changeStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
        }
    }

    private void scalePosterOnCollapse() {
        final int initialPosterHeight = cardViewPoster.getLayoutParams().height;
        final int initialPosterWidth = cardViewPoster.getLayoutParams().width;
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percent = (100 - ((verticalOffset) + Constants.COLLAPSED_TOOLBAR_SIZE) * 100 / Constants.COLLAPSED_TOOLBAR_SIZE);
                float newHeight = initialPosterHeight - (initialPosterHeight * (percent / 100));
                float newWidth = initialPosterWidth - (initialPosterWidth * (percent / 100));
                cardViewPoster.getLayoutParams().height = Math.round(newHeight);
                cardViewPoster.getLayoutParams().width = Math.round(newWidth);
                cardViewPoster.requestLayout();
                cardViewPoster.setAlpha((100 - percent) / 100);
                FrameLayout.LayoutParams buttonLayoutParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                buttonLayoutParams.setMargins(0, (cardViewPoster.getLayoutParams().height / 2), 0, 0);
                contentLinearLayout.setLayoutParams(buttonLayoutParams);
            }
        });
    }

    private void bindData() {
        loadBackDrop();
        Glide.with(this)
                .load(currentMovie.getPosterCompleteUrl(false))
                .centerCrop()
                .into(poster);
        overView.setText(currentMovie.getOverview());
        releaseDate.setText(currentMovie.getReleaseDate());
        voteAverage.setText(String.valueOf(currentMovie.getVoteAverage()).concat(Constants.BLACK_STAR_UNICODE));
        voteCount.setText(String.valueOf(currentMovie.getVoteCount()));
        popularity.setText(String.valueOf(currentMovie.getPopularity()));
    }

    private void loadBackDrop() {
        Glide.with(this)
                .load(currentMovie.getBackdropCompleteUrl(true)).asBitmap()
                .centerCrop()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                int vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.colorPrimary));
                                int darkVibrantColor = palette.getDarkVibrantColor(getResources().getColor(R.color.colorPrimaryDark));
                                changeActionBarColor(darkVibrantColor);
                                changeStatusBarColor(vibrantColor);
                            }
                        });
                        return false;
                    }
                })
                .into(backDrop);
    }

    private void initUI() {
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        toolbarSubtitle = (TextView) findViewById(R.id.toolbar_subtitle);
        contentLinearLayout = (LinearLayout) findViewById(R.id.ll_container);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        poster = (ImageView) findViewById(R.id.iv_movie_poster);
        cardViewPoster = (CardView) findViewById(R.id.cv_movie_poster);
        backDrop = (ImageView) findViewById(R.id.iv_backdrop);
        overView = (TextView) findViewById(R.id.tv_overview);
        releaseDate = (TextView) findViewById(R.id.tv_release_date);
        voteAverage = (TextView) findViewById(R.id.tv_vote_average);
        voteCount = (TextView) findViewById(R.id.tv_vote_count);
        popularity = (TextView) findViewById(R.id.tv_popularity);
    }

    private void getMovieFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            currentMovie = (Movie) extras.getSerializable(MOVIE_KEY);
        } else {
            Toast.makeText(this, getString(R.string.response_empty_message), Toast.LENGTH_LONG).show();
        }
    }
}
