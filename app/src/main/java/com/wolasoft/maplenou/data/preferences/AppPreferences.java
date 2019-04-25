package com.wolasoft.maplenou.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.wolasoft.waul.encryptions.Encryption;

import java.lang.reflect.Type;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppPreferences {
    private static final String PREFS_NAME = "com.wolasoft.maplenou.preferences";

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    @Inject
    public AppPreferences(Context context, Gson gson) {
        this.gson = gson;
        this.preferences = context.getApplicationContext().getSharedPreferences(
                PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Save an object to app preference
     * @param key Value key
     * @param data Object to save
     * @param <T> Generic object type
     */
    public <T> void saveObject(String key, T data) {
        String jsonData = this.gson.toJson(data);
        editor = preferences.edit();
        editor.putString(Encryption.toBase64(key), Encryption.toBase64(jsonData));
        editor.apply();
    }

    /**
     * Save an object to app preference
     * @param key The value key
     * @param data The object to save
     * @param type The specific genericized type of the object
     * @param <T> Generic object type
     */
    public <T> void saveObject(String key, T data, Type type) {
        String jsonData = this.gson.toJson(data, type);
        editor = preferences.edit();
        editor.putString(Encryption.toBase64(key), Encryption.toBase64(jsonData));
        editor.apply();
    }

    /**
     * Save an object to app preference
     * @param key The value key
     * @param type The specific genericized type of the object
     * @param defaultValue The default value to return if no value found
     * @param <T> Generic object type
     * @return
     */
    public <T> T getObject(String key, Type type, T defaultValue) {
        String encryptedData = preferences.getString(Encryption.toBase64(key), null);
        String decodedData = Encryption.fromBase64(encryptedData);

        return this.gson.fromJson(decodedData, type);
    }
}
