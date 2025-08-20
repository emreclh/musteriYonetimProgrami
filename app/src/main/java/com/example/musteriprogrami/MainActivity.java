package com.example.musteriprogrami;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.musteriprogrami.activities.MusteriFormActivity;
import com.example.musteriprogrami.interfaces.MusteriAdapter;
import com.example.musteriprogrami.controllers.ApiClient;
import com.example.musteriprogrami.entities.Musteri;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MusteriAdapter.OnMusteriDeletedListener {

    private RecyclerView recyclerViewMusteriler;
    private MusteriAdapter musteriAdapter;
    private List<Musteri> musteriler = new ArrayList<>();
    private List<Musteri> filteredMusteriler = new ArrayList<>();

    private MaterialButton btnYeniMusteri, btnYenile;
    private TextInputEditText etArama;
    private ProgressBar progressBar;

    private ActivityResultLauncher<Intent> formActivityLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupActivityLauncher();

        initViews();
        setupRecyclerView();
        setupClickListeners();
        setupSearch();

        loadMusteriler();
    }

    private void initViews() {
        recyclerViewMusteriler = findViewById(R.id.recyclerViewMusteriler);
        btnYeniMusteri = findViewById(R.id.btnYeniMusteri);
        btnYenile = findViewById(R.id.btnYenile);
        etArama = findViewById(R.id.etArama);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setupRecyclerView() {
        musteriAdapter = new MusteriAdapter(filteredMusteriler, this, formActivityLauncher);
        musteriAdapter.setOnMusteriDeletedListener(this);
        recyclerViewMusteriler.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMusteriler.setAdapter(musteriAdapter);
    }

    private void setupClickListeners() {
        btnYeniMusteri.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MusteriFormActivity.class);
            formActivityLauncher.launch(intent);
        });

        btnYenile.setOnClickListener(v -> loadMusteriler());
    }

    private void setupSearch() {
        etArama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMusteriler(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupActivityLauncher() {
        formActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadMusteriler();
                    }
                }
        );
    }

    // Müşteri kaydetme, güncelleme veya silme aktivitelerinden sonra ya da yenile butonuna tıklandığında listeyi yenile.
    private void loadMusteriler() {
        showLoading(true);
        Call<List<Musteri>> call = ApiClient.getApiService().tumMusteriler();
        call.enqueue(new Callback<List<Musteri>>() {
            @Override
            public void onResponse(Call<List<Musteri>> call, Response<List<Musteri>> response) {
                showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    musteriler.clear();
                    musteriler.addAll(response.body());
                    filterMusteriler(etArama.getText().toString());
                    Log.d("MainActivity", "Müşteri sayısı: " + musteriler.size());
                } else {
                    Toast.makeText(MainActivity.this,
                            "Müşteriler yüklenemedi: " + response.code(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Musteri>> call, Throwable t) {
                showLoading(false);
                Toast.makeText(MainActivity.this,
                        "Network hatası: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.e("MainActivity", "API Error: " + t.getMessage());
            }
        });
    }

    private void filterMusteriler(String query) {
        filteredMusteriler.clear();
        if (query.isEmpty()) {
            filteredMusteriler.addAll(musteriler);
        } else {
            String lowerQuery = query.toLowerCase();
            for (Musteri musteri : musteriler) {
                if (musteri.getAd().toLowerCase().contains(lowerQuery) ||
                        musteri.getSoyad().toLowerCase().contains(lowerQuery) ||
                        musteri.getEmail().toLowerCase().contains(lowerQuery) ||
                        musteri.getTc().contains(query)) {
                    filteredMusteriler.add(musteri);
                }
            }
        }
        musteriAdapter.notifyDataSetChanged();
    }

    private void showLoading(boolean show) {
        if (show) {
            progressBar.setVisibility(android.view.View.VISIBLE);
        } else {
            progressBar.setVisibility(android.view.View.GONE);
        }
    }

    @Override
    public void onMusteriDeleted() {
        loadMusteriler(); // Silme işleminden sonra listeyi yenile
    }
}
