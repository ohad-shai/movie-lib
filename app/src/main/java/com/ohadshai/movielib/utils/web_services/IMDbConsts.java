package com.ohadshai.movielib.utils.web_services;

/**
 * Holds all the constants for the "IMDb" web site.
 * Created by Ohad on 10/15/2016.
 */
public interface IMDbConsts {

    /**
     * Holds a constant for the url of the "IMDb" movie page.
     */
    public static final String IMDB_MOVIE_PAGE = "http://www.imdb.com/title/";

    /**
     * Holds a constant for the url of the "IMDb" person page.
     */
    public static final String IMDB_PERSON_PAGE = "http://www.imdb.com/name/";

    /**
     * Holds a constant for the url of the "IMDb" movie page (mobile). (start of the url).
     */
    public static final String IMDB_MOVIE_PAGE_MOBILE = "https://m.imdb.com/title/";

    /**
     * Holds a constant for the key of the "IMDb" video id to look for in a movie page.
     */
    public static final String IMDB_MOVIE_PAGE_VIDEO_ID_KEY = "/video/imdb/";

    /**
     * Holds a constant for the url directory of the "IMDb" video player (End of the URL).
     */
    public static final String IMDB_VIDEO_PLAYER_START = "https://www.imdb.com/video/imdb/";

    /**
     * Holds a constant for the url directory of the "IMDb" video player (Start of the URL).
     */
    public static final String IMDB_VIDEO_PLAYER_END = "/imdb/inline";

}
