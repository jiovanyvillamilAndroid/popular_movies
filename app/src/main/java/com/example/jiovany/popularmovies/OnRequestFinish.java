package com.example.jiovany.popularmovies;

import java.util.ArrayList;

/**
 * Created by jiovany on 1/19/17.
 */

public interface OnRequestFinish {
    void onSuccess(ArrayList<Movie> response);

    void onFailure(int errorMessageResId);
}
