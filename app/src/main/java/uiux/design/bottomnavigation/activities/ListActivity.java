package uiux.design.bottomnavigation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
import uiux.design.bottomnavigation.adapter.MusicAdapter;
import uiux.design.bottomnavigation.model.Song;
import uiux.design.bottomnavigation.utils.PlayerActivity;
import uiux.design.bottomnavigation.utils.PlayerService;
import uiux.design.bottomnavigation.utils.Tools;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Song> listsong = new ArrayList<>();
    private MusicAdapter musicAdapter;
    private SweetAlertDialog pDialog;
    private  String type;
    private String q;
    private TextView textViewjudul;

    public ListActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        pDialog = new SweetAlertDialog(ListActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        recyclerView=findViewById(R.id.recyclesearch);
        textViewjudul=findViewById(R.id.title);
        musicAdapter = new MusicAdapter(listsong,ListActivity.this,R.layout.item_song_circle);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ListActivity.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(musicAdapter);
        musicAdapter.setOnItemClickListener(new MusicAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Intent intent = new Intent(ListActivity.this, PlayerActivity.class);
                intent.putExtra("pos",pos);
                intent.putExtra("from","online");
                PlayerService.currentlist=listsong;

                startActivity(intent);
            }
        });

        type=getIntent().getStringExtra("from");
        q=getIntent().getStringExtra("q");
        textViewjudul.setText(q);
        getsongs(q,type);




    }
    public void getsongs(final String q, final String type){
        pDialog.show();
        listsong.clear();
        recyclerView.removeAllViews();
        String url;
        if (type.equals("genre")){
            url="https://api-v2.soundcloud.com/charts?genre=soundcloud:genres:"+q+"&high_tier_only=false&kind=top&limit=100&client_id="+ Tools.key;
        }
        else{
            url="https://api-v2.soundcloud.com/search/tracks?q="+q+"&client_id="+Tools.key+"&limit=100";

        }
        final JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

//                linearLayout.setVisibility(View.GONE);

                if (type.equals("genre")){
                    try {
                        JSONArray jsonArray1=response.getJSONArray("collection");

                        for (int i = 0;i<jsonArray1.length();i++){
                            JSONObject jsonObject1=jsonArray1.getJSONObject(i);
                            JSONObject jsonObject=jsonObject1.getJSONObject("track");
                            Song listModalClass = new Song();
                            listModalClass.setId(jsonObject.getInt("id"));
                            listModalClass.setJudul(jsonObject.getString("title"));
                            listModalClass.setLinkimage(jsonObject.getString("artwork_url"));
                            listModalClass.setDurasi(jsonObject.getString("full_duration"));
                            listModalClass.setType("online");
                            listModalClass.setPenyanyi(q);
                            listsong.add(listModalClass);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}

                else if (type.equals("search")){
                    try {
                        JSONArray jsonArray1=response.getJSONArray("collection");

                        for (int i = 0;i<jsonArray1.length();i++){
                            JSONObject jsonObject=jsonArray1.getJSONObject(i);
                            Song listModalClass = new Song();
                            listModalClass.setId(jsonObject.getInt("id"));
                            listModalClass.setJudul(jsonObject.getString("title"));
                            listModalClass.setLinkimage(jsonObject.getString("artwork_url"));
                            listModalClass.setDurasi(jsonObject.getString("full_duration"));
                            listModalClass.setType("online");


                            try {
                                JSONObject jsonArray3=jsonObject.getJSONObject("publisher_metadata");
                                listModalClass.setPenyanyi(jsonArray3.getString("artist"));

                            }
                            catch (JSONException e){
                                listModalClass.setPenyanyi("Artist");

                            }
                            listsong.add(listModalClass);


                        }





                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                pDialog.dismissWithAnimation();
                musicAdapter.notifyDataSetChanged();
//                songAdapter.notifyDataSetChanged();
                //    System.out.println("update"+listsongModalSearch);




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);


    }
}