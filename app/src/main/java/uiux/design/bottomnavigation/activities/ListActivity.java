package uiux.design.bottomnavigation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import uiux.design.bottomnavigation.BuildConfig;
import uiux.design.bottomnavigation.R;
import uiux.design.bottomnavigation.adapter.MusicAdapter;
import uiux.design.bottomnavigation.model.Song;
import uiux.design.bottomnavigation.utils.Ads;
import uiux.design.bottomnavigation.utils.Constants;
import uiux.design.bottomnavigation.utils.PlayerActivity;
import uiux.design.bottomnavigation.utils.PlayerService;
import uiux.design.bottomnavigation.utils.RealmHelper;
import uiux.design.bottomnavigation.utils.Tools;

import static android.widget.Toast.LENGTH_LONG;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Song> listsong = new ArrayList<>();
    private MusicAdapter musicAdapter;
    private SweetAlertDialog pDialog;
    private  String type;
    private String q;
    private TextView textViewjudul;
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;
    Realm realm;
    RealmHelper realmHelper;

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
        bottom_sheet =findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);
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
                Song song= listsong.get(pos);
                showitemdialog(intent,song);
            }
        });

        type=getIntent().getStringExtra("from");
        q=getIntent().getStringExtra("q");
        textViewjudul.setText(q);
        getsongs(q,type);

    initrealm();


    }
    public void  initrealm(){
        Realm.init(ListActivity.this);
        RealmConfiguration configuration = new RealmConfiguration.Builder().build();
        realm = Realm.getInstance(configuration);

    }

    public void getsongs( String q, final String type){
        pDialog.show();
        listsong.clear();
        recyclerView.removeAllViews();
        String url;
        if (type.equals("genre")){
            q=q.replaceAll("\\s+","");
            q=q.replaceAll("[-+^&]*", "");

            url="https://api-v2.soundcloud.com/charts?genre=soundcloud:genres:"+q+"&high_tier_only=false&kind=top&limit=100&client_id="+ Constants.getKey();
        }
        else{
            url="https://api-v2.soundcloud.com/search/tracks?q="+q+"&client_id="+Constants.getKey()+"&limit=100";

        }
        String finalQ = q;
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
                            listModalClass.setPenyanyi(finalQ);
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

    private void showitemdialog(Intent intent,Song song) {

        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View view = getLayoutInflater().inflate(R.layout.buttomnav, null);

        mBottomSheetDialog = new BottomSheetDialog(ListActivity.this);
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
                Ads ads = new Ads();
                ads.dualadsinter(ListActivity.this, Constants.getInter(),Constants.getInterfan());
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
              addtoplaylits(song);
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
    public  void addtoplaylits(Song musicSongOnline){
        realmHelper = new RealmHelper(realm,getApplication());
        realmHelper.saveplaylists(musicSongOnline);
        Toast.makeText(getApplicationContext(),"Added to Playlists",LENGTH_LONG).show();


    }
}