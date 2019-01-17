package com.ck.flickster.models;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Movie {

    String posterPath;
    String title;
    String overview;

    public Movie(@NonNull JSONObject jsonObject) throws JSONException { // whoever uses this has to deal with it
        posterPath = jsonObject.getString("poster_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
    }

    public static List<Movie> fromJsonArray(@NonNull JSONArray movieJsonArray) throws JSONException{
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++){
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    public String getPosterPath() {
        Log.d("smile", "getPosterPath: ");
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);

    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

}
