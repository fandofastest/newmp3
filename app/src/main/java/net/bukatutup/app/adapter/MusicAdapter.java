package net.bukatutup.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import net.bukatutup.app.R;
import net.bukatutup.app.model.Song;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {
     private   List <Song> listsong= new ArrayList<>();
     private Context ctx;
     private SweetAlertDialog sweetAlertDialog;
     private int item;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int pos);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }


    public MusicAdapter(List<Song> listsong, Context ctx, int item) {
        this.listsong = listsong;
        this.ctx = ctx;
        this.item=item;

    }

    @NonNull
    @Override
    public MusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(item,parent,false);

        return new ViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.ViewHolder holder, int position) {
        Song song =listsong.get(position);
        Glide.with(ctx)
                .load(song.getLinkimage())
                .centerCrop()
                .placeholder(R.drawable.ic_music)
                .into(holder.imageView);
        holder.tvtitle.setText(song.getJudul());
        holder.tvartist.setText(song.getPenyanyi());
        holder.mainly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, position);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return listsong.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvtitle,tvartist;
        LinearLayout mainly;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageartist);
            tvtitle=itemView.findViewById(R.id.title);
            tvartist=itemView.findViewById(R.id.artist);
            mainly=itemView.findViewById(R.id.mainlayout);
        }
    }
}
