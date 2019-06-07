package ru.com.testunsplashclient.core.configuration;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static ru.com.testunsplashclient.core.configuration.ExclusionEliminationStrategy.Direction.DESERIALIZATION;
import static ru.com.testunsplashclient.core.configuration.ExclusionEliminationStrategy.Direction.SERIALIZATION;

public class GsonConfiguredFactory {

    private static final String TAG = "GsonConfiguredFactory";

    public static final String DATETIME_WITH_Z_LITERAL = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String DATETIME_WITH_TIMEZONE = "yyyy-MM-dd'T'hh:mm:ssZ";

    public static Gson getGson() {
        return new GsonBuilder()
                .addDeserializationExclusionStrategy(new ExclusionEliminationStrategy(DESERIALIZATION))
                .addSerializationExclusionStrategy(new ExclusionEliminationStrategy(SERIALIZATION))
                .registerTypeAdapter(Date.class, getDateJsonDeserializer())
                .registerTypeAdapter(Float.class, getFloatJsonDeserializer())
                .setLenient()
                .create();
    }

    @NonNull
    private static JsonDeserializer<Float> getFloatJsonDeserializer() {
        return (json, typeOfT, context) -> {
            String asString = json.getAsString();
            if (asString == null || asString.length() == 0) return null;
            try {
                return Float.parseFloat(asString);
            } catch (NumberFormatException e) {
                Log.e(TAG, "deserialize: ", e);
                return null;
            }
        };
    }

    @NonNull
    private static JsonDeserializer<Date> getDateJsonDeserializer() {
        return (json, typeOfT, context) -> {

            DateFormat datetimeWithTimezone = new SimpleDateFormat(DATETIME_WITH_TIMEZONE, Locale.getDefault());
            DateFormat datetimeWithZLiteral = new SimpleDateFormat(DATETIME_WITH_Z_LITERAL, Locale.getDefault());

            String asString = json.getAsString();

            if (asString == null) return null;

            try {
                return datetimeWithTimezone.parse(asString);
            } catch (ParseException e1) {

                try {
                    return datetimeWithZLiteral.parse(asString);
                }
                catch (ParseException e2) {
                    Log.e(TAG, "deserialize: ", e2);
                    return null;
                }

            }

        };
    }
}
