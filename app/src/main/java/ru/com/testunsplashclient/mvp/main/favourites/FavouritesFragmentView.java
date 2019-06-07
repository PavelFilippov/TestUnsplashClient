package ru.com.testunsplashclient.mvp.main.favourites;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

import java.util.List;

import ru.com.testunsplashclient.core.data.model.Photo;
import ru.com.testunsplashclient.mvp.BaseView;

public interface FavouritesFragmentView extends MvpView, BaseView {

    @StateStrategyType(SkipStrategy.class)
    void showFavourites(List<Photo> photos);

    void showDataBaseError(Throwable throwable);

}
