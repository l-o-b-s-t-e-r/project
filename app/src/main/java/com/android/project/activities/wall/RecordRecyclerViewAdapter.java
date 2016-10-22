package com.android.project.activities.wall;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.project.R;
import com.android.project.model.Image;
import com.android.project.util.ImageManager;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by Lobster on 21.05.16.
 */

public class RecordRecyclerViewAdapter extends RecyclerView.Adapter<RecordRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = RecordRecyclerViewAdapter.class.getSimpleName();
    private List<Image> mImages;
    private WallPresenter.ActionListener mActionListener;

    public RecordRecyclerViewAdapter(List<Image> images, WallPresenter.ActionListener actionListener) {
        mImages = images;
        mActionListener = actionListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.record_recycler_item, parent, false);

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
        @BindView(R.id.progressBar)
        ProgressBar spinner;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.record_image)
        public void onImageClick() {
            mActionListener.openRecordDetail(mImages.get(0).getRecordId());
        }

        public void setContent(Image image) {
            imageView.setImageResource(R.drawable.user);
            spinner.setVisibility(View.GONE);

            if (new File(image.getImage()).exists()) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(image.getImage()));
                spinner.setVisibility(View.GONE);
            } else {
                Subscriber<Bitmap> subscriber = new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                        spinner.setVisibility(View.GONE);
                    }
                };

                ImageManager.getInstance().addImageSubscriber(image.getImage(), subscriber);
            }
        }
    }
}