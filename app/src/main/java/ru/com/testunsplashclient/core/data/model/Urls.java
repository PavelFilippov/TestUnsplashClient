package ru.com.testunsplashclient.core.data.model;

import java.io.Serializable;

import io.realm.RealmObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Urls extends RealmObject implements Serializable {

    private String raw;
    private String full;
    private String regular;
    private String small;
    private String thumb;

}
