package com.example.musteriprogrami.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.musteriprogrami.R;
import com.example.musteriprogrami.controllers.ApiClient;
import com.example.musteriprogrami.entities.Musteri;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusteriFormActivity extends AppCompatActivity {

    private TextInputEditText etAd, etSoyad, etMail, etTC, etDogumTarihi, etKayitTarihi;
    private EditText etId;
    private RadioGroup rgCinsiyet;
    private RadioButton rbErkek, rbKadin;
    private MaterialButton btnKaydet, btnIptal;
    private TextView tvBaslik;
    private TextInputLayout layoutKayitTarihi;

    private boolean isDuzenlemeModu = false;
    private String musteriId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musteri_form);

        initViews();
        setupClickListeners();
        checkDuzenlemeModu();
    }

    private void initViews() {
        etAd = findViewById(R.id.etAd);
        etSoyad = findViewById(R.id.etSoyad);
        etMail = findViewById(R.id.etMail);
        etTC = findViewById(R.id.etTC);
        etDogumTarihi = findViewById(R.id.etDogumTarihi);
        etKayitTarihi = findViewById(R.id.etKayitTarihi);
        etId = findViewById(R.id.etId);
        rgCinsiyet = findViewById(R.id.rgCinsiyet);
        rbErkek = findViewById(R.id.rbErkek);
        rbKadin = findViewById(R.id.rbKadin);
        btnKaydet = findViewById(R.id.btnKaydet);
        btnIptal = findViewById(R.id.btnIptal);
        tvBaslik = findViewById(R.id.tvBaslik);
        layoutKayitTarihi = findViewById(R.id.layoutKayitTarihi);
    }

    private void setupClickListeners() {
        btnIptal.setOnClickListener(v -> finish());
        btnKaydet.setOnClickListener(v -> kaydet());

        // Tarih picker
        etDogumTarihi.setOnClickListener(v -> showDatePicker());
    }

    private void checkDuzenlemeModu() {
        Intent intent = getIntent();
        if (intent.hasExtra("musteri")) {
            isDuzenlemeModu = true;
            tvBaslik.setText("MÜŞTERİ DÜZENLE");
            layoutKayitTarihi.setVisibility(android.view.View.VISIBLE);

            // Müşteri bilgilerini form'a doldur
            Musteri musteri = (Musteri) intent.getSerializableExtra("musteri");
            if (musteri != null) {
                fillForm(musteri);
            }
        }
    }

    private void fillForm(Musteri musteri) {
        etId.setText(musteri.getId());
        etAd.setText(musteri.getAd());
        etSoyad.setText(musteri.getSoyad());
        etMail.setText(musteri.getEmail());
        etTC.setText(musteri.getTc());
        etDogumTarihi.setText(musteri.getDg());
        etKayitTarihi.setText(musteri.getKt());
        musteriId = musteri.getId();

        // Cinsiyet ayarla
        if (musteri.getCins()) {
            rbKadin.setChecked(true);
        } else {
            rbErkek.setChecked(true);
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(),
                            "%02d/%02d/%d", dayOfMonth, month + 1, year);
                    etDogumTarihi.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private boolean getCinsiyetValue() {
        // rbKadin seçiliyse true (Kadın), rbErkek seçiliyse false (Erkek)
        return rbKadin.isChecked();
    }

    private void kaydet() {
        if (!validateForm()) {
            return;
        }

        Musteri musteri = new Musteri(
                etAd.getText().toString().trim(),
                etSoyad.getText().toString().trim(),
                etMail.getText().toString().trim(),
                etDogumTarihi.getText().toString().trim(),
                etTC.getText().toString().trim(),
                getCinsiyetValue(), // cinsiyet değeri
                "", // kt - kayıt tarihi backend'de otomatik ayarlanacak
                ""  // gt - güncelleme tarihi backend'de otomatik ayarlanacak
        );

        if (isDuzenlemeModu) {
            musteriGuncelle(musteri);
        } else {
            yeniMusteriEkle(musteri);
        }
    }

    private boolean validateForm() {
        if (etAd.getText().toString().trim().isEmpty()) {
            etAd.setError("Ad zorunludur");
            return false;
        }
        if (etSoyad.getText().toString().trim().isEmpty()) {
            etSoyad.setError("Soyad zorunludur");
            return false;
        }
        if (etMail.getText().toString().trim().isEmpty()) {
            etMail.setError("E-mail zorunludur");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(etMail.getText().toString().trim()).matches()) {
            etMail.setError("Geçerli bir e-mail adresi girin");
            return false;
        }
        if (etTC.getText().toString().trim().isEmpty() || etTC.getText().toString().length() != 11) {
            etTC.setError("TC Kimlik 11 haneli olmalıdır");
            return false;
        }

        // Cinsiyet kontrolü - RadioGroup'ta mutlaka bir seçim olmalı
        if (rgCinsiyet.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Cinsiyet seçimi zorunludur", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void yeniMusteriEkle(Musteri musteri) {
        Call<Musteri> call = ApiClient.getApiService().musteriEkle(musteri);
        call.enqueue(new Callback<Musteri>() {
            @Override
            public void onResponse(Call<Musteri> call, Response<Musteri> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MusteriFormActivity.this,
                            "Müşteri başarıyla eklendi!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(MusteriFormActivity.this,
                            "Hata: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Musteri> call, Throwable t) {
                Toast.makeText(MusteriFormActivity.this,
                        "Network hatası: " + t.getMessage() + ". Bağlantınızı kontrol edin.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void musteriGuncelle(Musteri musteri) {
        Call<Musteri> call = ApiClient.getApiService().musteriGuncelle(musteriId, musteri);
        call.enqueue(new Callback<Musteri>() {
            @Override
            public void onResponse(Call<Musteri> call, Response<Musteri> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MusteriFormActivity.this,
                            "Müşteri başarıyla güncellendi!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(MusteriFormActivity.this,
                            "Hata: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Musteri> call, Throwable t) {
                Toast.makeText(MusteriFormActivity.this,
                        "Network hatası: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}