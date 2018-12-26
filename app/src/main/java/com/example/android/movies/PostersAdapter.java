package com.example.android.movies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class PostersAdapter extends RecyclerView.Adapter<PostersAdapter.PostersViewHolder> {
    public interface ListItemClickListener {
        void onItemClick(int itemIndex);
    }

    private final ListItemClickListener mOnClickListener;
    static String[] posters;
    private Context mcontext;


    public PostersAdapter(ListItemClickListener listener, String[] posters, Context context) {
        mOnClickListener = listener;
        this.posters = posters;
        mcontext = context;
    }

    public class PostersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView posterImage;

        public PostersViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImage = (ImageView) itemView.findViewById(R.id.iv_poster_image);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int clickedItem = getAdapterPosition();
            mOnClickListener.onItemClick(clickedItem);
        }
    }

    @NonNull
    @Override
    public PostersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.posters_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        PostersViewHolder viewHolder = new PostersViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostersViewHolder postersViewHolder, int position) {
        String imageUrl = "https://image.tmdb.org/t/p/w600_and_h900_bestv2";
            Picasso.with(mcontext)
                    .load(imageUrl+ posters[position])
                    .into(postersViewHolder.posterImage);
    }

    @Override
    public int getItemCount() {
        return 20;
    }
}
