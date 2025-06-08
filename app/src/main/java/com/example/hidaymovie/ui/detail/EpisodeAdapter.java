package com.example.hidaymovie.ui.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hidaymovie.R;
import com.example.hidaymovie.model.Episode;

import java.util.List;

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.EpisodeViewHolder> {

    private final List<Episode> episodeList;
    private OnEpisodeClickListener listener;

    // Interface để bắt sự kiện click tập phim
    public interface OnEpisodeClickListener {
        void onEpisodeClick(Episode episode);
    }

    public EpisodeAdapter(List<Episode> episodeList) {
        this.episodeList = episodeList;
    }

    public void setOnEpisodeClickListener(OnEpisodeClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_episode, parent, false);
        return new EpisodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {
        Episode episode = episodeList.get(position);
        holder.tvEpisodeTitle.setText(episode.getTitle());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEpisodeClick(episode);
            }
        });
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    public static class EpisodeViewHolder extends RecyclerView.ViewHolder {
        TextView tvEpisodeTitle;

        public EpisodeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEpisodeTitle = itemView.findViewById(R.id.tvEpisodeTitle);
        }
    }
}
