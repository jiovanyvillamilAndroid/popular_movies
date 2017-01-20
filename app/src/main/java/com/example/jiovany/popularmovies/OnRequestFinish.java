package com.example.jiovany.popularmovies;

import java.util.ArrayList;

/**
 * Created by jiovany on 1/19/17.
 */

public interface OnRequestFinish<T>{
    void onSuccess(ArrayList<T> response);

    void onFailure(int errorMessageResId);
}
