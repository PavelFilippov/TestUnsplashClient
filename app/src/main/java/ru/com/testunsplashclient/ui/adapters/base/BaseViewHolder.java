package ru.com.testunsplashclient.ui.adapters.base;

import android.view.View;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

import lombok.Getter;

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    @Getter
    public View itemView;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
    }


    protected <T extends View> T getView(@IdRes int viewId) {
        return (T) itemView.findViewById(viewId);
    }

}
