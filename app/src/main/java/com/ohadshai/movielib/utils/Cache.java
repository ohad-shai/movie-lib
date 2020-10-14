package com.ohadshai.movielib.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Represents a cache utility (caches objects in the application session).
 * Created by Ohad on 3/23/2017.
 */
public class Cache {

    //region Private Members

    /**
     * Holds the list of objects in cache.
     */
    private static ArrayList<CacheObj> _cacheObjs = new ArrayList<>();

    //endregion

    //region Public Static API

    /**
     * Puts an object to cache (overrides the current object if the key is already in cache).
     *
     * @param object The object to cache.
     * @param key    The key to the object in the cache to put.
     * @param <T>    The type of the object.
     */
    public static <T> void put(@Nullable T object, @NonNull String key) {
        // Checks if the key already in cache:
        for (CacheObj obj : _cacheObjs) {
            if (obj._key.equals(key)) {
                obj._object = object;
                return;
            }
        }

        // The key not exists, so adds a new cache object:
        _cacheObjs.add(new CacheObj<T>(object, key));
    }

    /**
     * Indicates whether an object key is in cache or not.
     *
     * @param key The key to the object in the cache to check.
     * @return Returns true if the object key is in cache, otherwise false.
     */
    public static boolean isInCache(@NonNull String key) {
        for (CacheObj obj : _cacheObjs) {
            if (obj._key.equals(key))
                return true;
        }
        return false;
    }

    /**
     * Gets an object in the cache, by the key.
     *
     * @param key The key to the object in the cache to get.
     * @param <T> The type of the object.
     * @return Returns the object in the cache if found, otherwise null.
     */
    public static <T> T get(@NonNull String key) {
        for (CacheObj obj : _cacheObjs) {
            if (obj._key.equals(key))
                return (T) obj._object;
        }

        return null;
    }

    /**
     * Gets an object in the cache (by the key) if it exists, otherwise puts it in the cache an returns it back.
     *
     * @param key       The key to the object in the cache to get / put.
     * @param putObject The object to put when the key not found in the cache.
     * @param <T>       The type of the object.
     * @return Returns the object in the cache if found, otherwise returns back the object to put in the cache.
     */
    public static <T> T getOtherwisePut(@NonNull String key, T putObject) {
        for (CacheObj obj : _cacheObjs) {
            if (obj._key.equals(key))
                return (T) obj._object;
        }

        Cache.put(putObject, key);
        return putObject;
    }

    /**
     * Removes an object in the cache, by the key.
     *
     * @param key The key to the object in the cache to remove.
     */
    public static void remove(@NonNull String key) {
        for (CacheObj obj : _cacheObjs) {
            if (obj._key.equals(key)) {
                _cacheObjs.remove(obj);
                return;
            }
        }
    }

    //endregion

    //region Inner Classes

    /**
     * Represents a cache object.
     */
    private static class CacheObj<T> {

        //region Private Members

        /**
         * Holds the object to cache.
         */
        private T _object;

        /**
         * Holds the key to the object in the cache.
         */
        private String _key;

        //endregion

        //region C'tor

        /**
         * Initializes a new instance of a cache object.
         *
         * @param object The object to cache.
         * @param key    The key to the object in the cache.
         */
        public CacheObj(@Nullable T object, @NonNull String key) {
            this._object = object;
            this._key = key;
        }

        //endregion

    }

    /**
     * Represents all the cache object keys in the cache utility.
     */
    public static class Keys {

        /**
         * Holds a constant for the cache object key: "Genres list from the database".
         */
        public static final String GENRES_LIST_FROM_DB = "genres_list_from_db_key";

    }

    //endregion

}
