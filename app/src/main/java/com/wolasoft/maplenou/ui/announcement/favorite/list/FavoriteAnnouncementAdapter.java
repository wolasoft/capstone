package com.wolasoft.maplenou.ui.announcement.favorite.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolasoft.maplenou.R;
import com.wolasoft.maplenou.data.entities.Announcement;
import com.wolasoft.maplenou.databinding.ListItemAnnouncementBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class FavoriteAnnouncementAdapter extends
        PagedListAdapter<Announcement, FavoriteAnnouncementAdapter.ViewHolder> {

    private OnAnnouncementClickedListener listener;

    FavoriteAnnouncementAdapter(OnAnnouncementClickedListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ListItemAnnouncementBinding dataBinding =
                DataBindingUtil.inflate(inflater, R.layout.list_item_announcement, viewGroup, false);
        return new ViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Announcement announcement = getItem(position);
        viewHolder.bind(announcement);
    }

    private static DiffUtil.ItemCallback<Announcement> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Announcement>() {
                @Override
                public boolean areItemsTheSame(@NonNull Announcement old,
                                               @NonNull Announcement fresh) {
                    return old.getUuid().equals(fresh.getUuid());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Announcement old,
                                                  @NonNull Announcement fresh) {
                    return old.equals(fresh);
                }
            };

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ListItemAnnouncementBinding dataBinding;

        ViewHolder(@NonNull ListItemAnnouncementBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Announcement announcement = getItem(position);
            listener.announcementClicked(announcement);
        }

        void bind(Announcement announcement) {
            this.dataBinding.setAnnouncement(announcement);
        }
    }

    public interface OnAnnouncementClickedListener {
        void announcementClicked(Announcement announcement);
    }
}