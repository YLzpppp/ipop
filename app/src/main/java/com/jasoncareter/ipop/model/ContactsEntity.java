package com.jasoncareter.ipop.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "contacts")
public class ContactsEntity {


    @PrimaryKey(autoGenerate = true)
    private int Id;

    @ColumnInfo(name = "identity")
    private int Identity;

    private String Username;

    @ColumnInfo
    private String IP;


    public ContactsEntity(){
        //Empty constructor
    }

    @Ignore
    public ContactsEntity(int Identity , String Username,String ip){
        this.Identity = Identity ;
        this.Username = Username;
        IP = ip;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setIdentity(int identiry) {
        Identity = identiry;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public int getId() {
        return Id;
    }

    public int getIdentity() {
        return Identity;
    }

    public String getUsername() {
        return Username;
    }

    public String getIP() {
        return IP;
    }
}
