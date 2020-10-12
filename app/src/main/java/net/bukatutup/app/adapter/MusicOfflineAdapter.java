package net.bukatutup.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import com.musicapp.tubidyapp.R;
import net.bukatutup.app.model.SongOffline;
import net.bukatutup.app.utils.Tools;

public class MusicOfflineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

private List<SongOffline> items = new ArrayList<>();

private Context ctx;

private MusicOfflineAdapter.OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(final MusicOfflineAdapter.OnItemClickListener mItemClickListener) {
            this.mOnItemClickListener = mItemClickListener;
            }


    public MusicOfflineAdapter(Context context, List<SongOffline> items) {
            this.items = items;
            ctx = context;
            }

public class OriginalViewHolder extends RecyclerView.ViewHolder {
    public ImageView image;
    public TextView title;
    public TextView artist;

    public View lyt_parent;

    public OriginalViewHolder(View v) {
        super(v);
        image = v.findViewById(R.id.imageartist);
        title = v.findViewById(R.id.title);
        artist=v.findViewById(R.id.artist);
        lyt_parent=v.findViewById(R.id.mainlayout);

    }
}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song_circle, parent, false);
        vh = new MusicOfflineAdapter.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof MusicOfflineAdapter.OriginalViewHolder) {
            MusicOfflineAdapter.OriginalViewHolder view = (MusicOfflineAdapter.OriginalViewHolder) holder;

            final SongOffline musicSongOffline = items.get(position);
            view.title.setText(musicSongOffline.getFilename());
            view.artist.setText("Local");
            Tools.displayImageOriginallocal(ctx, view.image, R.drawable.artist1);
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, position);
                    }
                }
            });


        }
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

public interface OnItemClickListener {
    void onItemClick(View view, int pos);
}



}