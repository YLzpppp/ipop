package com.jasoncareter.ipop.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class MeInfoEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String meIdentity;
    private int mePort;

    public MeInfoEntity(){

    }
    @Ignore
    public MeInfoEntity(String identity ,int Port){
        meIdentity = identity;
        mePort = Port;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setMePort(int mePort) {
        this.mePort = mePort;
    }

    public void setMeIdentity(String meIdentity) {
        this.meIdentity = meIdentity;
    }

    public int getMePort() {
        return mePort;
    }

    public int getId() {
        return id;
    }

    public String getMeIdentity() {
        return meIdentity;
    }
}
