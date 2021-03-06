package com.android.project.view.wall;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.project.R;
import com.android.project.model.Image;
import com.android.project.util.ImageManager;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Lobster on 21.05.16.
 */

public class RecordRecyclerViewAdapter extends RecyclerView.Adapter<RecordRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = RecordRecyclerViewAdapter.class.getSimpleName();

    private Long mRecordId;
    private List<Image> mImages;
    private WallPresenter.ActionListener mPresenter;
    private RequestManager mGlide;

    public RecordRecyclerViewAdapter(WallPresenter.ActionListener presenter, RequestManager glide) {
        mPresenter = presenter;
        mGlide = glide;

    }

    public void setContent(Long recordId, List<Image> images) {
        mRecordId = recordId;
        mImages = images;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_progress_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setContent(mImages.get(position));
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.record_image)
        ImageView imageView;
        @BindView(R.id.progress_bar)
        ProgressBar spinner;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick({R.id.record_image, R.id.progress_bar})
        public void onImageClick() {
            mPresenter.openRecordDetail(mRecordId);
        }

        public void setContent(final Image image) {
            spinner.setVisibility(View.VISIBLE);

            mGlide.load(ImageManager.IMAGE_URL + image.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            spinner.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);
        }
    }
}
