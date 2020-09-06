package uiux.design.bottomnavigation.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Song extends RealmObject {
    @PrimaryKey
    private int id;
    private String judul;
    private  String penyanyi;
    private  String album;
    private String durasi;
    private  String type;
    private  String linkimage;
    private  String inplaylists;
    private  String recent;

    public Song() {
    }

    public String getInplaylists() {
        return inplaylists;
    }

    public void setInplaylists(String inplaylists) {
        this.inplaylists = inplaylists;
    }

    public String getRecent() {
        return recent;
    }

    public void setRecent(String recent) {
        this.recent = recent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPenyanyi() {
        return penyanyi;
    }

    public void setPenyanyi(String penyanyi) {
        this.penyanyi = penyanyi;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getDurasi() {
        return durasi;
    }

    public void setDurasi(String durasi) {
        this.durasi = durasi;
    }


    public String getLinkimage() {
        return linkimage;
    }

    public void setLinkimage(String linkimage) {
        this.linkimage = linkimage;
    }
}
