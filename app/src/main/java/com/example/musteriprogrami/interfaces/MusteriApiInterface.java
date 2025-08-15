package com.example.musteriprogrami.interfaces;

import com.example.musteriprogrami.entities.Musteri;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;

public interface MusteriApiInterface {

    @GET("api/musteriler")
    Call<List<Musteri>> tumMusteriler();

    @GET("api/musteriler/{id}")
    Call<Musteri> musteriById(@Path("id") String id);

    @POST("api/musteriler")
    Call<Musteri> musteriEkle(@Body Musteri musteri);

    @PUT("api/musteriler/{id}")
    Call<Musteri> musteriGuncelle(@Path("id") String id, @Body Musteri musteri);

    @DELETE("api/musteriler/{id}")
    Call<Void> musteriSil(@Path("id") String id);
}
