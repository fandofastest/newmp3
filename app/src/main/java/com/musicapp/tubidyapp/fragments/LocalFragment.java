package com.musicapp.tubidyapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musicapp.tubidyapp.R;
import com.musicapp.tubidyapp.activities.MainActivity;
import com.musicapp.tubidyapp.adapter.MusicOfflineAdapter;
import com.musicapp.tubidyapp.model.SongOffline;
import com.musicapp.tubidyapp.utils.PlayerActivity;
import com.musicapp.tubidyapp.utils.PlayerService;

import static com.musicapp.tubidyapp.utils.PlayerService.listlocal;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LocalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocalFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private MusicOfflineAdapter musicAdapter;
    private Context context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LocalFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LocalFragment newInstance(String param1, String param2) {
        LocalFragment fragment = new LocalFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        context=getContext();
        return inflater.inflate(R.layout.fragment_local, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView=view.findViewById(R.id.recycle);
        musicAdapter = new MusicOfflineAdapter(getContext(),listlocal);
        RecyclerView.LayoutManager mLayoutManagersonggenre = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManagersonggenre);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(musicAdapter);


        musicAdapter.setOnItemClickListener(new MusicOfflineAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Intent intent = new Intent(getContext(), PlayerActivity.class);
                intent.putExtra("pos",pos);
                intent.putExtra("from","online");
                PlayerService.currentlistoffline=listlocal;

                ((MainActivity)context).playmusicoffline(pos,listlocal);


            }
        });


        getMusic();


    }

    public void getMusic(){

        listlocal.clear();
        recyclerView.removeAllViews();


        Uri allSongsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor =  getContext().getContentResolver().query(allSongsUri, null, null, null, selection);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    SongOffline modalClass = new SongOffline();
                    modalClass.setFilename(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                    modalClass.setFilepath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                    modalClass.setSize(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
                    modalClass.setDuration(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                    modalClass.setType("offline");
                    listlocal.add(modalClass);




                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        Log.e("listloca", String.valueOf(listlocal.size()));
        musicAdapter.notifyDataSetChanged();
    }

}