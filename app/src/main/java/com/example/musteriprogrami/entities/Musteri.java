package com.example.musteriprogrami.entities;

import java.io.Serializable;
public class Musteri implements Serializable{
    private String id;
    private String ad;
    private String soyad;
    private String dg;
    private String tc;
    private String email;
    private String kt;
    private String gt;

    public Musteri() {}

    public Musteri(String ad, String soyad, String email, String dg, String tc, String kt, String gt) {
        this.ad = ad;
        this.soyad = soyad;
        this.email = email;
        this.dg= dg;
        this.tc = tc;
        this.kt = kt;
        this.gt = gt;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAd() {
        return ad;
    }
    public void setAd(String ad) {
        this.ad = ad;
    }
    public String getSoyad() {
        return soyad;
    }
    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }
    public String getEmail() {
        return email;
    }
    public void setMail(String mail) {
        this.email = mail;
    }
    public String getTc() {
        return tc;
    }
    public void setTc(String tc) {
        this.tc = tc;
    }
    public String getDg() {
        return dg;
    }
    public void setDg(String dg) {
        this.dg = dg;
    }
    public String getKt() {
        return kt;
    }
    public void setKt(String kt) {
        this.kt = kt;
    }
    public String getGt() {
        return gt;
    }
    public void setGt(String gt) {
        this.gt = gt;
    }

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return "Musteri{" +
                "id='" + id + '\'' +
                ", ad='" + ad + '\'' +
                ", soyad='" + soyad + '\'' +
                ", email='" + email + '\'' +
                ", tc='" + tc + '\'' +
                ", dg='" + dg + '\'' +
                ", kt='" + kt + '\'' +
                ", gt='" + gt + '\'' +
                '}';
    }
}
