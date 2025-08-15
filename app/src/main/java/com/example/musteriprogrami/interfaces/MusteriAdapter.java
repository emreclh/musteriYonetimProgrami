package com.example.musteriprogrami.interfaces;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musteriprogrami.R;
import com.example.musteriprogrami.activities.MusteriFormActivity;
import com.example.musteriprogrami.controllers.ApiClient;
import com.example.musteriprogrami.entities.Musteri;
import com.google.android.material.button.MaterialButton;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusteriAdapter extends RecyclerView.Adapter<MusteriAdapter.MusteriViewHolder> {

    private List<Musteri> musteriler;
    private Context context;
    private OnMusteriDeletedListener deleteListener;

    public interface OnMusteriDeletedListener {
        void onMusteriDeleted();
    }

    public MusteriAdapter(List<Musteri> musteriler, Context context) {
        this.musteriler = musteriler;
        this.context = context;
    }

    public void setOnMusteriDeletedListener(OnMusteriDeletedListener listener) {
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public MusteriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(com.example.musteriprogrami.R.layout.item_musteri, parent, false);
        return new MusteriViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusteriViewHolder holder, int position) {
        Musteri musteri = musteriler.get(position);
        holder.bind(musteri);
    }

    @Override
    public int getItemCount() {
        return musteriler.size();
    }

    class MusteriViewHolder extends RecyclerView.ViewHolder {
        TextView tvAdSoyad, tvEmail, tvTC, tvDogumTarihi, tvKayitTarihi, tvGuncellemeTarihi;
        MaterialButton btnDuzenle, btnSil;

        MusteriViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAdSoyad = itemView.findViewById(R.id.tvAdSoyad);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvTC = itemView.findViewById(R.id.tvTC);
            tvDogumTarihi = itemView.findViewById(R.id.tvDogumTarihi);
            tvKayitTarihi = itemView.findViewById(R.id.tvKayitTarihi);
            tvGuncellemeTarihi = itemView.findViewById(R.id.tvGuncellemeTarihi);
            btnDuzenle = itemView.findViewById(R.id.btnDuzenle);
            btnSil = itemView.findViewById(R.id.btnSil);
        }

        void bind(Musteri musteri) {
            tvAdSoyad.setText(musteri.getAd() + " " + musteri.getSoyad());
            tvEmail.setText(musteri.getEmail());
            tvTC.setText("TC: " + musteri.getTc());
            tvDogumTarihi.setText("Doğum: " + musteri.getDg());
            tvKayitTarihi.setText("Kayıt: " + musteri.getKt());
            tvGuncellemeTarihi.setText("Güncelleme: " + musteri.getGt());

            btnDuzenle.setOnClickListener(v -> {
                Intent intent = new Intent(context, MusteriFormActivity.class);
                intent.putExtra("musteri", musteri);
                context.startActivity(intent);
            });

            btnSil.setOnClickListener(v -> showDeleteConfirmDialog(musteri, getAdapterPosition()));
        }

    }

        private void showDeleteConfirmDialog(Musteri musteri, int position) {
            new AlertDialog.Builder(context)
                    .setTitle("Müşteri Sil")
                    .setMessage(musteri.getAd() + " " + musteri.getSoyad() +
                            " adlı müşteriyi silmek istediğinizden emin misiniz?")
                    .setPositiveButton("Sil", (dialog, which) -> deleteMusteri(musteri.getId(), position))
                    .setNegativeButton("İptal", null)
                    .show();
        }

        private void deleteMusteri(String musteriId, int position) { // musteriId ve position al
            Call<Void> call = ApiClient.getApiService().musteriSil(musteriId);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Müşteri silindi!", Toast.LENGTH_SHORT).show();
                        // Pozisyonu kullanarak doğrudan kaldır
                        musteriler.remove(position);
                        notifyItemRemoved(position);
                        // Eğer hala bir dış dinleyiciye ihtiyaç varsa çağır
                        if (deleteListener != null) {
                            deleteListener.onMusteriDeleted();
                        }
                    } else {
                        Toast.makeText(context, "Silme hatası: " + response.code(),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Network hatası: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
