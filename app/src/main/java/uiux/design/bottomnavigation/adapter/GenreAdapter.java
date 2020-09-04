package uiux.design.bottomnavigation.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import uiux.design.bottomnavigation.R;
import uiux.design.bottomnavigation.model.Genre;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {

   private List<Genre> listgenre = new ArrayList<>();
   private Context ctx;
    @NonNull
    @Override
    public GenreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre,parent,false);

        return new GenreAdapter.ViewHolder(itemview);
    }

    public GenreAdapter(List<Genre> listgenre, Context ctx) {
        this.listgenre = listgenre;
        this.ctx = ctx;
    }

    @Override
    public void onBindViewHolder(@NonNull GenreAdapter.ViewHolder holder, int position) {
        Genre genre = listgenre.get(position);
        holder.genre.setText(genre.getGenre());
        holder.imageView.setImageResource(R.drawable.genre);
        holder.genreshort.setText(genre.getGenre());

    }

    @Override
    public int getItemCount() {
        return listgenre.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView genre,genreshort;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_genre);
            genre=itemView.findViewById(R.id.text_genre);
            genreshort=itemView.findViewById(R.id.shortgenre);
        }
    }
}
