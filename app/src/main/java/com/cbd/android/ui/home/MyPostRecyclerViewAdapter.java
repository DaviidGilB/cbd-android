package com.cbd.android.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cbd.android.R;
import com.cbd.android.common.Constants;
import com.cbd.android.common.Utils;
import com.cbd.android.models.Post;

import java.util.List;

public class MyPostRecyclerViewAdapter extends RecyclerView.Adapter<MyPostRecyclerViewAdapter.ViewHolder> {

    private List<Post> mValues;
    private Context content;

    public MyPostRecyclerViewAdapter(Context content, List<Post> items) {
        this.mValues = items;
        this.content = content;
    }

    public void setData(List<Post> postList) {
        this.mValues = postList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_post, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (mValues != null) {
            holder.mItem = mValues.get(position);

            holder.title.setText(holder.mItem.getTitle());
            holder.description.setText(holder.mItem.getDescription());
            holder.price.setText(holder.mItem.getPrice() + " €");
            holder.userInfo.setText(holder.mItem.getUser().getName() + " (" + holder.mItem.getUser().getEmail() + ")");
            if (!holder.mItem.getUser().getAvatar().isEmpty()) {
                Glide.with(content)
                        .load(Constants.BASE_URL + holder.mItem.getUser().getAvatar())
                        .into(holder.avatar);
            } else {
                Glide.with(content)
                        .load(R.drawable.ic_usuario)
                        .into(holder.avatar);
            }
            if (!holder.mItem.getPhoto().isEmpty()) {
                Glide.with(content)
                        .load(Constants.BASE_URL + holder.mItem.getPhoto())
                        .into(holder.photo);
            } else {
                holder.photo.setVisibility(View.GONE);
            }
        }

        /*
        .dontAnimate()
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .skipMemoryCache(true)
         */

    }

    @Override
    public int getItemCount() {
        if (mValues != null) {
            return mValues.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView avatar;
        public final ImageView photo;
        public final TextView title;
        public final TextView description;
        public final TextView price;
        public final TextView userInfo;
        public Post mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            avatar = (ImageView) view.findViewById(R.id.user_imagen_post);
            photo = (ImageView) view.findViewById(R.id.imagen_post) ;
            title = (TextView) view.findViewById(R.id.post_title);
            description = (TextView) view.findViewById(R.id.post_description);
            price = (TextView) view.findViewById(R.id.post_price);
            userInfo = (TextView) view.findViewById(R.id.post_user_info);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + title.getText() + "'";
        }
    }
}
