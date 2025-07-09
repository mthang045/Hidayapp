package com.hidaymovie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.hidaymovie.model.Cast;
import com.hidaymovie.R;
import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private Context context;
    private List<Cast> castList;

    public CastAdapter(Context context, List<Cast> castList) {
        this.context = context;
        this.castList = castList;
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // File layout này chúng ta sẽ tạo ở bước tiếp theo
        View view = LayoutInflater.from(context).inflate(R.layout.cast_item, parent, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {
        Cast cast = castList.get(position);
        holder.actorName.setText(cast.getName());
        holder.characterName.setText(cast.getCharacter());

        String profileUrl = "https://image.tmdb.org/t/p/w185" + cast.getProfilePath();
        Glide.with(context)
                .load(profileUrl)
                .transform(new CircleCrop()) // Bo tròn ảnh diễn viên
                .placeholder(R.drawable.ic_profile) // Ảnh mặc định nếu không có ảnh diễn viên
                .into(holder.actorImage);
    }

    @Override
    public int getItemCount() {
        // Giới hạn chỉ hiển thị tối đa 10 diễn viên cho gọn
        return castList != null ? Math.min(castList.size(), 10) : 0;
    }

    static class CastViewHolder extends RecyclerView.ViewHolder {
        ImageView actorImage;
        TextView actorName, characterName;

        public CastViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ các view từ layout "cast_item.xml"
            actorImage = itemView.findViewById(R.id.actor_image);
            actorName = itemView.findViewById(R.id.actor_name);
            characterName = itemView.findViewById(R.id.character_name);
        }
    }
}