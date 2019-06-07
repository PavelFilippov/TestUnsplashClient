package ru.com.testunsplashclient.ui.adapters.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public abstract class BaseAdapter<T, V extends BaseViewHolder> extends RecyclerView.Adapter {

    protected Context context;
    private List<T> list = new ArrayList<>();
    @Setter
    @Getter
    private IRecyclerTouchListener<T> recyclerTouchListener;
    @Setter
    private IReadyListener readyListener;

    public BaseAdapter(Context context) {
        this.context = context;
        setHasStableIds(true);
    }

    public void remove(T data) {
        Iterator<T> iterator = list.iterator();
        int position = -1;
        while (iterator.hasNext()) {
            position++;
            T next = iterator.next();
            if (next.equals(data)) {
                iterator.remove();
                break;
            }
        }
        notifyDataSetChanged();
    }


    public void remove(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }

    protected T getItemById(int id) {
        return list.get(id);
    }

    public T obtainItem(int position) {
        return getItemById(position);
    }

    protected View inflateView(@LayoutRes int layoutId, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(layoutId, viewGroup, false);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        try {
            onBindViewHolderInternal((V)holder, position);
            if (position == 0) {
                holder.itemView.post(() -> {
                    if (readyListener != null) {
                        readyListener.onFirstElementReady(holder.itemView);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void onBindViewHolderInternal(V holder, int position);

    public void refresh() {
        notifyDataSetChanged();
    }

    public void clear() {
        this.list.clear();
        notifyDataSetChanged();
    }

    public void addData(int position, T data) {
        if (list.contains(data)) return;
        this.list.add(position, data);
        notifyDataSetChanged();
    }

    public void addData(T data) {
        if (list.contains(data)) return;
        this.list.add(data);
        notifyDataSetChanged();
    }

    public void addData(@NonNull List<T> data) {
        if (data.isEmpty()) return;
        int size = getItemCount();
        this.list.addAll(data);
        notifyDataSetChanged();
    }

    public void addDataToStart(@NonNull List<T> data) {
        if (this.list.isEmpty()) {
            this.list.addAll(data);
        } else {
            this.list.addAll(0, data);
        }
        notifyItemRangeInserted(0, data.size());
    }

    public void setData(List<T> list) {
        this.list = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    public void setupListener(@NonNull View view, @NonNull T data, int position) {
        view.setOnClickListener(v -> {
            if (recyclerTouchListener != null) recyclerTouchListener.onTouch(data, position);
        });
    }

    public void replaceItem(int index, T item) {
        list.set(index, item);
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public List<T> getData() {
        return list;
    }

    public interface IReadyListener {
        void onFirstElementReady(View view);
    }

}
