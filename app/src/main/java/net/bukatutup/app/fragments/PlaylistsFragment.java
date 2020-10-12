package net.bukatutup.app.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import net.bukatutup.app.BuildConfig;
import com.musicapp.tubidyapp.R;
import net.bukatutup.app.activities.MainActivity;
import net.bukatutup.app.adapter.MusicAdapter;
import net.bukatutup.app.model.Song;
import net.bukatutup.app.utils.Ads;
import net.bukatutup.app.utils.PlayerActivity;
import net.bukatutup.app.utils.PlayerService;

import static net.bukatutup.app.utils.PlayerService.listplaylist;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlaylistsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaylistsFragment extends Fragment {
    private RecyclerView recyclerView;
    private MusicAdapter musicAdapter;
    private Context context;
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public PlaylistsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlaylistsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlaylistsFragment newInstance(String param1, String param2) {
        PlaylistsFragment fragment = new PlaylistsFragment();
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
        context=getContext();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlists, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottom_sheet =view.findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);
        recyclerView=view.findViewById(R.id.recycleplaylists);
        musicAdapter = new MusicAdapter(listplaylist,getContext(),R.layout.item_song_circle);
        RecyclerView.LayoutManager mLayoutManagersonggenre = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManagersonggenre);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(musicAdapter);


        musicAdapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Intent intent = new Intent(getContext(), PlayerActivity.class);
                intent.putExtra("pos",pos);
                intent.putExtra("from","online");
                PlayerService.currentlist=listplaylist;
                Song song =listplaylist.get(pos);
                showitemdialog(intent,song);

            }
        });



        if (context instanceof MainActivity) {
            listplaylist=((MainActivity)context).getplaylists();

            musicAdapter.notifyDataSetChanged();


        }


    }
    private void showitemdialog(Intent intent, Song song) {

        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View view = getLayoutInflater().inflate(R.layout.buttomnav, null);

        mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // set background transparent
        ((View) view.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

        final TextView btnShare = view.findViewById(R.id.btnShare);
        final TextView btnDl    = view.findViewById(R.id.btnDl);
        final TextView btnPlay = view.findViewById(R.id.btnPlay);
        final TextView btnAddpl = view.findViewById(R.id.btnAddpl);
        btnAddpl.setText("Remove from Playlists");
        final TextView btnCancel = view.findViewById(R.id.btnCancel);

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch(Exception e) {
                    //e.toString();
                }
                mBottomSheetDialog.dismiss();

            }
        });

        btnDl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Ads ads = new Ads(context,true);
                ads.setCustomObjectListener(new Ads.MyCustomObjectListener() {
                    @Override
                    public void onAdsfinish() {

                        startActivity(intent);

                    }

                    @Override
                    public void onRewardOk() {

                    }
                });
                mBottomSheetDialog.dismiss();
            }
        });
        btnAddpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof MainActivity) {
                    if ( ((MainActivity)context).removeplaylists(song)){
                        musicAdapter.notifyDataSetChanged();
                    }

                }

                mBottomSheetDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });

    }




}