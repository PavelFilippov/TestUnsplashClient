package ru.com.testunsplashclient.app;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.com.testunsplashclient.core.data.model.Photo;

public interface Api {

    @GET("photos")
    Observable<List<Photo>> getPhotos(
            @Query("client_id") String clientId,
            @Query("page") int page
    );

    @GET("photos/{id}")
    Observable<Photo> getPhotoById(
            @Path("id") String id,
            @Query("client_id") String clientId
    );

}
