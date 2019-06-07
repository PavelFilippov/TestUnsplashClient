package ru.com.testunsplashclient.core.data.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.com.testunsplashclient.core.data.model.Photo;

@Getter
@Setter
@AllArgsConstructor
public class UpdateFavourites {

    private Photo photo;

}
