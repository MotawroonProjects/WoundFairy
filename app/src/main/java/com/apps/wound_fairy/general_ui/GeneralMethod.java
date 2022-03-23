package com.apps.wound_fairy.general_ui;

import android.net.Uri;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;


import com.apps.wound_fairy.R;

import com.apps.wound_fairy.model.ReservationModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }

    @BindingAdapter("reservation_status")
    public static void reservationStatus(TextView btnStatus, ReservationModel reservationModel) {
        if (reservationModel != null) {
            String status = reservationModel.getStatus();
            if (status.equals("new")) {
                btnStatus.setText(R.string.under_review);
            } else if (status.equals("accepted")) {
                btnStatus.setText(R.string.approved);
            } else if (status.equals("Refused")) {
                btnStatus.setText(R.string.canceled);
            } else if (status.equals("ended")) {
                btnStatus.setText(R.string.done);
            }
        }
    }

    @BindingAdapter("image")
    public static void image(View view, String imageUrl) {

        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                if (view instanceof CircleImageView) {
                    CircleImageView imageView = (CircleImageView) view;
                    if (imageUrl != null) {
                        RequestOptions options = new RequestOptions().override(view.getWidth(), view.getHeight());
                        Glide.with(view.getContext()).asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .load(imageUrl)
                                .apply(options)
                                .into(imageView);
                    }
                } else if (view instanceof RoundedImageView) {
                    RoundedImageView imageView = (RoundedImageView) view;

                    if (imageUrl != null) {

                        RequestOptions options = new RequestOptions().override(view.getWidth(), view.getHeight());
                        Glide.with(view.getContext()).asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .load(imageUrl)
                                .apply(options)
                                .into(imageView);

                    }
                } else if (view instanceof ImageView) {
                    ImageView imageView = (ImageView) view;

                    if (imageUrl != null) {

                        RequestOptions options = new RequestOptions().override(view.getWidth(), view.getHeight());
                        Glide.with(view.getContext()).asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .load(imageUrl)
                                .apply(options)
                                .into(imageView);
                    }
                }

            }
        });


    }

    @BindingAdapter("user_image")
    public static void user_image(View view, String imageUrl) {


        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (imageUrl != null) {

                Glide.with(view.getContext()).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.circle_avatar)
                        .load(imageUrl)
                        .centerCrop()
                        .into(imageView);

            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (imageUrl != null) {

                Glide.with(view.getContext()).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.circle_avatar)
                        .load(imageUrl)
                        .centerCrop()
                        .into(imageView);

            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (imageUrl != null) {

                Glide.with(view.getContext()).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.circle_avatar)
                        .load(imageUrl)
                        .centerCrop()
                        .into(imageView);
            }
        }

    }

    @BindingAdapter("qr_image")
    public static void qr_image(View view, String imageUrl) {

        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (imageUrl != null) {
                RequestOptions options = new RequestOptions();
                Glide.with(view.getContext()).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .load(imageUrl)
                        .apply(options)
                        .into(imageView);
            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (imageUrl != null) {

                RequestOptions options = new RequestOptions();
                Glide.with(view.getContext()).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .load(imageUrl)
                        .apply(options)
                        .into(imageView);

            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (imageUrl != null) {

                RequestOptions options = new RequestOptions();
                Glide.with(view.getContext()).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .load(imageUrl)
                        .apply(options)
                        .into(imageView);
            }
        }


    }

    @BindingAdapter("departmentImage")
    public static void department_image(View view, String imageUrl) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.get().load(Uri.parse(imageUrl)).into(imageView);

            } else {
            }

        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.get().load(Uri.parse(imageUrl)).into(imageView);

            } else {
            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.get().load(Uri.parse(imageUrl)).into(imageView);

            } else {
            }
        }

    }


    @BindingAdapter("createAt")
    public static void dateCreateAt(TextView textView, String s) {
        if (s != null) {
            try {
                String[] dates = s.split("T");
                textView.setText(dates[0]);
            } catch (Exception e) {

            }

        }

    }


}










