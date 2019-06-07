package ru.com.testunsplashclient.core.data;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import ru.com.testunsplashclient.core.data.model.Photo;

public class PhotosDb {

    RealmConfiguration photosConfig;

    RealmConfiguration favouritesConfig;

    public PhotosDb() {
        photosConfig = new RealmConfiguration.Builder()
                .name("photos.realm")
                .build();
        favouritesConfig = new RealmConfiguration.Builder()
                .name("favourites.realm")
                .build();
    }

    public Single<List<Photo>> getPhotos() {
        Realm realm = Realm.getInstance(photosConfig);
        List<Photo> photos = realm.copyFromRealm(realm.where(Photo.class).findAll());
        realm.close();
        return Single.just(photos);
    }

    public Single<List<Photo>> getFavourites() {
        Realm realm = Realm.getInstance(favouritesConfig);
        List<Photo> photos = realm.copyFromRealm(realm.where(Photo.class).findAll());
        realm.close();
        return Single.just(photos);
    }

    public Single<Photo> getPhoto(String id) {
        Realm realm = Realm.getInstance(photosConfig);
        Photo photo = null;
        Photo realmPhoto = realm.where(Photo.class).equalTo("id", id).findFirst();
        if (realmPhoto != null) {
            photo = realm.copyFromRealm(realmPhoto);
        }
        realm.close();
        return Single.just(photo);
    }

    public Single<Photo> getFavouritePhoto(String id) {
        Realm realm = Realm.getInstance(favouritesConfig);
        Photo photo = null;
        Photo realmPhoto = realm.where(Photo.class).equalTo("id", id).findFirst();
        if (realmPhoto != null) {
            photo = realm.copyFromRealm(realmPhoto);
        }
        realm.close();
        return Single.just(photo);
    }

    public Completable updatePhotos(List<Photo> photos) {
        return Completable.fromAction(() -> {
            Realm realm = Realm.getInstance(photosConfig);
            realm.executeTransaction(realm1 -> {
                realm.insertOrUpdate(photos);
            });
            realm.close();
            Realm.compactRealm(photosConfig);
        });
    }

    public Completable updateFavourites(List<Photo> photos) {
        return Completable.fromAction(() -> {
            Realm realm = Realm.getInstance(favouritesConfig);
            realm.executeTransaction(realm1 -> {
                realm.insertOrUpdate(photos);
            });
            realm.close();
            Realm.compactRealm(favouritesConfig);
        });
    }

    public Completable updatePhoto(Photo photo) {
        return Completable.fromAction(() -> {
            Realm realm = Realm.getInstance(photosConfig);
            realm.executeTransaction(realm1 -> {
                realm.insertOrUpdate(photo);
            });
            realm.close();
            Realm.compactRealm(photosConfig);
        });
    }

    public Completable insertOrDeleteFavourite(Photo photo) {
        return Completable.fromAction(() -> {

            Realm favouriteRealm = Realm.getInstance(favouritesConfig);
            favouriteRealm.executeTransaction(realm1 -> {
                Photo realmPhoto = favouriteRealm.where(Photo.class).equalTo("id", photo.getId()).findFirst();
                if(realmPhoto == null) {
                    favouriteRealm.insert(photo);
                } else {
                    realmPhoto.deleteFromRealm();
                }
            });
            favouriteRealm.close();
            Realm.compactRealm(favouritesConfig);

            Realm photoRealm = Realm.getInstance(photosConfig);
            photoRealm.executeTransaction(realm1 -> {
                Photo realmPhoto = photoRealm.where(Photo.class).equalTo("id", photo.getId()).findFirst();
                if(realmPhoto != null) {
                    photoRealm.insertOrUpdate(photo);
                }
            });
            photoRealm.close();
            Realm.compactRealm(photosConfig);
        });
    }

    public Completable clearAllPhotos() {
        return Completable.fromAction(() -> {
            Realm realm = Realm.getInstance(photosConfig);
            realm.executeTransaction(realm1 -> {
                realm.deleteAll();
            });
            realm.close();
            Realm.compactRealm(photosConfig);
        });
    }

    public Completable clearAllFavourites() {
        return Completable.fromAction(() -> {
            Realm realm = Realm.getInstance(favouritesConfig);
            realm.executeTransaction(realm1 -> {
                realm.deleteAll();
            });
            realm.close();
            Realm.compactRealm(favouritesConfig);
        });
    }

    public boolean isEmptyPhotos() {
        Realm realm = Realm.getInstance(photosConfig);
        boolean isEmpty = realm.isEmpty();
        realm.close();
        return isEmpty;
    }

    public boolean isEmptyFavourites() {
        Realm realm = Realm.getInstance(favouritesConfig);
        boolean isEmpty = realm.isEmpty();
        realm.close();
        return isEmpty;
    }

    public boolean containsFavourite(String id) {
        Realm realm = Realm.getInstance(favouritesConfig);
        Photo realmPhoto = realm.where(Photo.class).equalTo("id", id).findFirst();
        realm.close();
        return realmPhoto != null;
    }
}
