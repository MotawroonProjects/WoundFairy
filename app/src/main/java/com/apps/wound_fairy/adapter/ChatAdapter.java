package com.apps.wound_fairy.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.apps.wound_fairy.R;
import com.apps.wound_fairy.databinding.ChatImageLeftRowBinding;
import com.apps.wound_fairy.databinding.ChatImageRightRowBinding;
import com.apps.wound_fairy.databinding.ChatMessageLeftRowBinding;
import com.apps.wound_fairy.databinding.ChatMessageRightRowBinding;
import com.apps.wound_fairy.model.MessageModel;
import com.apps.wound_fairy.tags.Tags;
import com.apps.wound_fairy.uis.activity_chat.ChatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int msg_left = 1;
    private final int msg_right = 2;
    private final int img_left = 3;
    private final int img_right = 4;
    private final int load = 9;
    private LayoutInflater inflater;
    private List<MessageModel> list;
    private Context context;
    private String current_user_id;
    private ChatActivity activity;
    private RecyclerView recyclerView;
    private boolean isScrolledUp = false;


    public ChatAdapter(Context context, String current_user_id, RecyclerView recView) {
        this.context = context;
        this.current_user_id = current_user_id;
        inflater = LayoutInflater.from(context);
        activity = (ChatActivity) context;
        this.recyclerView = recView;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (list!=null&&list.size()>0){
                    int currentItemPos =((LinearLayoutManager) recView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                    Log.e("pos",currentItemPos+"__"+dy);
                    if (dy>0){
                        Log.e("3","3");

                        if (currentItemPos<=list.size()-5) {
                            isScrolledUp = false;

                            Log.e("4","4");

                        }else if (currentItemPos==list.size()-1){
                            Log.e("5","5");

                            isScrolledUp = false;
                            activity.hideLastMessageView();

                        }
                    }else {

                        if (list.size()>5&&currentItemPos<list.size()-5){
                            Log.e("6","6");

                            isScrolledUp = true;
                        }else {
                            Log.e("7","7");

                            isScrolledUp = false;
                        }


                    }
                }

            }
        });
        recyclerView.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if (bottom<oldBottom){
                recyclerView.scrollBy(0,oldBottom-bottom);
            }
        });


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == msg_left) {
            ChatMessageLeftRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_message_left_row, parent, false);
            return new HolderMsgLeft(binding);
        } else if (viewType == msg_right) {
            ChatMessageRightRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_message_right_row, parent, false);
            return new HolderMsgRight(binding);
        } else if (viewType == img_left) {
            ChatImageLeftRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_image_left_row, parent, false);
            return new HolderImageLeft(binding);
        } else {
            ChatImageRightRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.chat_image_right_row, parent, false);
            return new HolderImageRight(binding);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModel model = list.get(position);


        ///////////////////


        if (holder instanceof HolderMsgLeft) {
            HolderMsgLeft holderMsgLeft = (HolderMsgLeft) holder;
            holderMsgLeft.binding.setModel(model);



        } else if (holder instanceof HolderMsgRight) {
            HolderMsgRight holderMsgRight = (HolderMsgRight) holder;
            holderMsgRight.binding.setModel(model);


        } else if (holder instanceof HolderImageLeft) {
            HolderImageLeft holderImageLeft = (HolderImageLeft) holder;
            holderImageLeft.binding.setModel(model);
            Glide.with(context)
                    .load(Uri.parse( model.getImage()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holderImageLeft.binding.imageChat);




        } else if (holder instanceof HolderImageRight) {
            HolderImageRight holderImageRight = (HolderImageRight) holder;
            holderImageRight.binding.setModel(model);
            Glide.with(context)
                    .load(Uri.parse( model.getImage()))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holderImageRight.binding.image);

        }


    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public class HolderMsgLeft extends RecyclerView.ViewHolder {
        private ChatMessageLeftRowBinding binding;

        public HolderMsgLeft(@NonNull ChatMessageLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class HolderMsgRight extends RecyclerView.ViewHolder {
        private ChatMessageRightRowBinding binding;

        public HolderMsgRight(@NonNull ChatMessageRightRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class HolderImageLeft extends RecyclerView.ViewHolder {
        private ChatImageLeftRowBinding binding;

        public HolderImageLeft(@NonNull ChatImageLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class HolderImageRight extends RecyclerView.ViewHolder {
        private ChatImageRightRowBinding binding;

        public HolderImageRight(@NonNull ChatImageRightRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    @Override
    public int getItemViewType(int position) {

        MessageModel messageModel = list.get(position);
        if (messageModel.getType().equals("text")) {

            if (messageModel.getFrom_user_id().equals(current_user_id)) {

                return msg_right;
            } else {
                return msg_left;
            }
        } else {

            if (messageModel.getFrom_user_id().equals(current_user_id)) {

                return img_right;
            } else {
                return img_left;
            }
        }


    }

    public void updateList(List<MessageModel> list) {
        if (list != null) {
            this.list = list;

        }

        if (this.list != null) {
            recyclerView.scrollToPosition(this.list.size()-1);
            notifyDataSetChanged();

        }



    }

    public void addMessage(MessageModel messageModel) {
        if (list != null) {
            list.add(messageModel);
            notifyItemChanged(list.size());

            recyclerView.post(() -> {
                if (this.list != null) {


                    if (!isScrolledUp){

                        recyclerView.scrollToPosition(this.list.size()-1);
                        notifyDataSetChanged();

                    }else {
                        if (!messageModel.getFrom_user_id().equals(current_user_id)){
                            activity.displayLastMessage(messageModel);

                        }else {
                            recyclerView.scrollToPosition(this.list.size()-1);
                            notifyDataSetChanged();

                        }
                    }

                }
            });

        }
    }

}
