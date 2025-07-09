package com.hidaymovie.network;


import com.hidaymovie.model.Movie;

import java.util.List;

public class MovieResponse {

    private List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }
}
