package ru.com.testunsplashclient.core.utils;

import android.util.Log;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RxUtils {

    private static ExecutorService executorService;

    public static <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                subscriber -> {
                    try {
                        subscriber.onNext(func.call());
                    } catch (Exception ex) {
                        Log.e("", "Error reading from the database", ex);
                        subscriber.onError(ex);
                    }
                    subscriber.onComplete();
                });
    }

    private static ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newFixedThreadPool(10);
        }
        return executorService;
    }

    public static <T> ObservableTransformer<T, T> applySchedulers() {

        return observable -> observable
                .subscribeOn(Schedulers.from(getExecutorService()))
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> SingleTransformer<T, T> applySchedulerSingle() {
        return observable -> observable
                .subscribeOn(Schedulers.from(Executors.newSingleThreadExecutor()))
                .observeOn(AndroidSchedulers.mainThread());
    }

}
