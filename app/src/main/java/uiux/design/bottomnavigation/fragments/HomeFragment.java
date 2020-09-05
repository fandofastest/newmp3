package uiux.design.bottomnavigation.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import uiux.design.bottomnavigation.R;
import uiux.design.bottomnavigation.activities.ListActivity;
import uiux.design.bottomnavigation.utils.PlayerActivity;
import uiux.design.bottomnavigation.adapter.GenreAdapter;
import uiux.design.bottomnavigation.adapter.MusicAdapter;
import uiux.design.bottomnavigation.model.Genre;
import uiux.design.bottomnavigation.model.Song;
import uiux.design.bottomnavigation.utils.PlayerService;
import uiux.design.bottomnavigation.utils.Tools;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private SweetAlertDialog pDialog;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context ctx;
    private RecyclerView recyclerView,recyclerViewgenre,recyclerViewtopgenre;
    private MusicAdapter musicAdapter,musicAdaptertopgenre;
    private GenreAdapter genreAdapter;
    private List<Song> listsong = new ArrayList<>();
    private List<Genre> listgenre= new ArrayList<>();
    private  List<Song> listtopgenre= new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        ctx=getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pDialog = new SweetAlertDialog(ctx, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        recyclerView=view.findViewById(R.id.recycle);
        musicAdapter = new MusicAdapter(listsong,getContext(),R.layout.item_song_grid);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ctx,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(musicAdapter);
        musicAdapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Intent intent = new Intent(getContext(), PlayerActivity.class);
                intent.putExtra("pos",pos);
                intent.putExtra("from","online");
                PlayerService.currentlist=listsong;

                startActivity(intent);
            }
        });


        recyclerViewtopgenre=view.findViewById(R.id.recyclesonggenre);
        musicAdaptertopgenre = new MusicAdapter(listtopgenre,getContext(),R.layout.item_song_circle);
        RecyclerView.LayoutManager mLayoutManagersonggenre = new LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false);
        recyclerViewtopgenre.setLayoutManager(mLayoutManagersonggenre);
        recyclerViewtopgenre.setItemAnimator(new DefaultItemAnimator());
        recyclerViewtopgenre.setAdapter(musicAdaptertopgenre);

        musicAdaptertopgenre.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Intent intent = new Intent(getContext(), PlayerActivity.class);
                intent.putExtra("pos",pos);
                intent.putExtra("from","online");
                PlayerService.currentlist=listsong;

                startActivity(intent);
            }
        });



        recyclerViewgenre=view.findViewById(R.id.recyclegenre);
        genreAdapter=new GenreAdapter(listgenre,getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ctx,LinearLayoutManager.HORIZONTAL,false);
        recyclerViewgenre.setLayoutManager(linearLayoutManager);
        recyclerViewgenre.setItemAnimator(new DefaultItemAnimator());
        recyclerViewgenre.setAdapter(genreAdapter);
        genreAdapter.setOnItemClickListener(new GenreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Genre obj) {
                Intent intent = new Intent(getContext(), ListActivity.class);
                intent.putExtra("q",obj.getGenre());
                intent.putExtra("from","genre");
                startActivity(intent);
            }
        });


        // display image


        gettopsong();
        getallgenre();
        gettopgenre("pop");



    }

    private void gettopgenre (String genre){
        String url="https://api-v2.soundcloud.com/charts?genre=soundcloud:genres:"+genre+"&high_tier_only=false&kind=trending&limit=100&&client_id="+Tools.key;

        pDialog.show();
        recyclerViewtopgenre.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    recyclerViewtopgenre.removeAllViews();
                    listtopgenre.clear();

                    JSONArray jsonArray1=response.getJSONArray("collection");

                    for (int i = 0;i<jsonArray1.length();i++){
                        JSONObject jsonObject1=jsonArray1.getJSONObject(i);
                        JSONObject jsonObject=jsonObject1.getJSONObject("track");
                        Song musicSongOnline = new Song();
                        musicSongOnline.setId(jsonObject.getInt("id"));
                        musicSongOnline.setJudul(jsonObject.getString("title"));
                        musicSongOnline.setLinkimage(jsonObject.getString("artwork_url"));
                        musicSongOnline.setDurasi(jsonObject.getString("full_duration"));
                        musicSongOnline.setType("online");


                        try {
                            JSONObject jsonArray3=jsonObject.getJSONObject("publisher_metadata");
                            musicSongOnline.setPenyanyi(jsonArray3.getString("artist"));

                        }
                        catch (JSONException e){
                            musicSongOnline.setPenyanyi("Artist");

                        }
                        listtopgenre.add(musicSongOnline);

                    }


                } catch (JSONException e) {

                    System.out.println("fando err " );
                    e.printStackTrace();
                }

                musicAdaptertopgenre.notifyDataSetChanged();
                pDialog.hide();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                System.out.println(error);
                System.out.println("fando  rr" );

            }
        });


        Volley.newRequestQueue(ctx).add(jsonObjectRequest);


    }

    private void gettopsong (){
        String url="https://api-v2.soundcloud.com/charts?charts-top:all-music&&high_tier_only=false&kind=top&limit=100&client_id="+Tools.key;

        pDialog.show();
        recyclerView.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {

                    recyclerView.removeAllViews();
                    listsong.clear();

                    JSONArray jsonArray1=response.getJSONArray("collection");

                    for (int i = 0;i<jsonArray1.length();i++){
                        JSONObject jsonObject1=jsonArray1.getJSONObject(i);
                        JSONObject jsonObject=jsonObject1.getJSONObject("track");
                        Song musicSongOnline = new Song();
                        musicSongOnline.setId(jsonObject.getInt("id"));
                        musicSongOnline.setJudul(jsonObject.getString("title"));
                        musicSongOnline.setLinkimage(jsonObject.getString("artwork_url"));
                        musicSongOnline.setDurasi(jsonObject.getString("full_duration"));
                        musicSongOnline.setType("online");


                        try {
                            JSONObject jsonArray3=jsonObject.getJSONObject("publisher_metadata");
                            musicSongOnline.setPenyanyi(jsonArray3.getString("artist"));

                        }
                        catch (JSONException e){
                            musicSongOnline.setPenyanyi("Artist");

                        }
                        listsong.add(musicSongOnline);

                    }


                } catch (JSONException e) {

                    System.out.println("fando err " );
                    e.printStackTrace();
                }

                musicAdapter.notifyDataSetChanged();
                pDialog.hide();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pDialog.hide();
                System.out.println(error);
                System.out.println("fando  rr" );

            }
        });


        Volley.newRequestQueue(ctx).add(jsonObjectRequest);


    }

    private void  getallgenre (){
        listgenre.clear();
        String [] genrelist ={"Alternative Rock","Ambient","Audiobooks","Business","Classical","Comedy","Country","Dance & EDM","Dancehall","Deep House","Disco","Drum & Bass","Dubstep","Electronic","Entertainment","Folk & Singer-Songwriter","Hip Hop & Rap","House","Indie","Jazz & Blues","Latin","Learning","Metal","News & Politics","Piano","Pop","R&B & Soul","Reggae","Reggaeton","Religion & Spirituality","Rock","Science","Soundtrack","Sports","Storytelling","Techno","Technology","Trance","Trap","Trending Audio","Trending Music","Trip Hop","World"};

        for (int i = 0; i < genrelist.length; i++) {
            listgenre.add(new Genre(genrelist[i]));

        }




        genreAdapter.notifyDataSetChanged();


    }
}