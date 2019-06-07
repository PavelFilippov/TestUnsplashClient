package ru.com.testunsplashclient.core.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Photo extends RealmObject implements Serializable {

    @PrimaryKey
    private String id;
    private String description;
    @SerializedName("alt_description")
    private String altDescription;
    private int likes;
    private User user;
    private Urls urls;

    private boolean isFavourite;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Photo that = (Photo) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

}
