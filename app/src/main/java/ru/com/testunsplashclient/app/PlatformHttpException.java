package ru.com.testunsplashclient.app;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.com.testunsplashclient.core.data.model.BaseRESTModel;

@AllArgsConstructor
@Getter
public class PlatformHttpException extends RuntimeException {

    private BaseRESTModel restModel;

}
