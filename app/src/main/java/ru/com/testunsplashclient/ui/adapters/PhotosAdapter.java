package ru.com.testunsplashclient.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import ru.com.testunsplashclient.R;
import ru.com.testunsplashclient.core.data.model.Photo;
import ru.com.testunsplashclient.ui.adapters.base.BaseAdapter;
import ru.com.testunsplashclient.ui.adapters.base.BaseViewHolder;
import ru.com.testunsplashclient.ui.common.IOnStarClick;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;

public class PhotosAdapter extends BaseAdapter<Photo, PhotosAdapter.Holder> {

    private IOnStarClick<Photo> starClick;

    public PhotosAdapter(Context context, IOnStarClick starClick) {
        super(context);
        this.starClick = starClick;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_photo, parent, false);

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) itemView.getLayoutParams();
        layoutParams.height = context.getResources().getConfiguration().orientation == ORIENTATION_PORTRAIT ?
                (parent.getHeight() / 2) - layoutParams.topMargin - layoutParams.bottomMargin :
                parent.getHeight() - layoutParams.topMargin - layoutParams.bottomMargin;
        itemView.setLayoutParams(layoutParams);

        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolderInternal(Holder holder, int position) {

        Photo photo = getItemById(position);

        holder.txtAuthorName.setText(photo.getUser().getName() != null ? photo.getUser().getName() : context.getString(R.string.no_name));
        holder.txtDescription.setText(photo.getDescription() != null ? photo.getDescription() :
                photo.getAltDescription() != null ? photo.getAltDescription() : context.getString(R.string.no_description));

        setImage(photo, holder);

        setStar(holder, photo.isFavourite());

        setupListener(holder.itemView, photo, position);

        holder.imgFavourite.setOnClickListener(v -> {
            photo.setFavourite(!photo.isFavourite());
            starClick.onStarClick(photo, position);
            setStar(holder, photo.isFavourite());
        });
    }

    class Holder extends BaseViewHolder {

        private TextView txtAuthorName;
        private AppCompatImageView imgPhoto;
        private TextView txtDescription;
        private AppCompatImageView imgFavourite;

        public Holder(View itemView) {
            super(itemView);
            txtAuthorName = getView(R.id.txtAuthorName);
            imgPhoto = getView(R.id.imgPhoto);
            txtDescription = getView(R.id.txtDescription);
            imgFavourite = getView(R.id.imgFavourite);
        }

    }

    private void setImage(Photo photo, Holder holder) {
        String largeFile = "";
        String smallFile = "";

        if (photo.getUrls().getRegular() != null) {
            largeFile = photo.getUrls().getRegular();
        } else if (photo.getUrls().getSmall() != null) {
            largeFile = photo.getUrls().getSmall();
        } else if (photo.getUrls().getThumb() != null) {
            largeFile = photo.getUrls().getThumb();
        }

        smallFile = photo.getUrls().getThumb();

        Glide.with(context)
                .asBitmap()
                .load(largeFile)
                .thumbnail(Glide.with(context)
                        .asBitmap()
                        .load(smallFile)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .transform(new CenterCrop(), new RoundedCorners(context.getResources().getDimensionPixelSize(R.dimen.corner_radius)))
                        .placeholder(R.drawable.rounded_corners_empty_image)
                        .error(R.drawable.rounded_corners_empty_image))
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .transform(new CenterCrop(), new RoundedCorners(context.getResources().getDimensionPixelSize(R.dimen.corner_radius)))
                .placeholder(R.drawable.rounded_corners_empty_image)
                .error(R.drawable.rounded_corners_empty_image)
                .into(holder.imgPhoto);
    }

    private void setStar(Holder holder, boolean isInFavourites) {
        holder.imgFavourite.setImageDrawable(isInFavourites ?
                        context.getResources().getDrawable(R.drawable.favourites_in) :
                        context.getResources().getDrawable(R.drawable.favourites_out));
    }

}
