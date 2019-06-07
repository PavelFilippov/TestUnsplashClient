package ru.com.testunsplashclient.core.data.model;

import java.io.Serializable;

import io.realm.RealmObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends RealmObject implements Serializable {

    private String name;

}
