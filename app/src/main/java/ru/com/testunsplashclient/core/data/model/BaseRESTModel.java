package ru.com.testunsplashclient.core.data.model;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseRESTModel implements Serializable {

    public List<String> errors;

}
