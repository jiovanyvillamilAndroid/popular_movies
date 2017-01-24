package com.example.jiovany.popularmovies;

import java.util.ArrayList;

public interface OnRequestFinish<T> {
    void onSuccess(ArrayList<T> response);

    void onFailure(int errorMessageResId);
}
