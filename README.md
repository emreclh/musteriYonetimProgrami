# Mobil Müşteri Yönetim Uygulaması

Bu Android uygulaması, Spring Boot ile geliştirilen Müşteri Yönetim API'si ile entegre olarak çalışan bir kullanıcı arayüzüdür. Müşteri bilgilerini görüntüleme, ekleme, güncelleme ve silme işlevlerini sağlar.

## 🚀 Teknolojiler

- **Android SDK**: Android uygulama geliştirme ortamı
- **Java**: Uygulama geliştirme dili 
- **Retrofit**: RESTful API'ler ile HTTP iletişimini sağlamak için
- **Gson**: JSON verilerini Java nesnelerine dönüştürmek için
- **OkHttp Logging Interceptor**: HTTP istek/yanıtlarını loglamak için
- **Material Design Components**: Modern ve kullanıcı dostu arayüz bileşenleri
- **RecyclerView**: Müşteri listesini verimli bir şekilde görüntülemek için

## ⚙️ Kurulum ve Çalıştırma

### 1. API'yi Çalıştırma

Bu mobil uygulamanın düzgün çalışabilmesi için öncelikle MüşteriDB API'si projesinin çalışır durumda olması gerekmektedir.

### 2. Projeyi Klonlama

```bash
git clone https://github.com/emreclh/musteriDB.git
```

### 3. API Temel URL'sini Yapılandırma

`com.example.musteriprogrami.controllers.ApiClient.java` dosyasını açın ve `BASE_URL` değişkenini API'nizin çalıştığı doğru adrese göre ayarlayın:

```java
private static final String BASE_URL = "http://YOUR_API_IP_ADDRESS:8080/";
// Emülatör kullanıyorsanız genellikle "http://10.0.2.2:8080/"
```

### 4. Bağımlılıkları Senkronize Etme

- Android Studio'yu açın ve projeyi içe aktarın
- `build.gradle (Module: app)` dosyasında gerekli bağımlılıkların mevcut olduğundan emin olun:

```gradle
dependencies {
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.9.3'
    implementation 'com.google.android.material:material:1.9.0'
    // Diğer bağımlılıklar...
}
```

- Gradle senkronizasyonunu çalıştırın (`File -> Sync Project with Gradle Files`)

### 5. Uygulamayı Çalıştırma

Android Studio'dan emülatör veya fiziksel bir cihaz seçerek uygulamayı çalıştırın.

## 📱 Uygulama Ekranları ve İşlevsellik

### Müşteri Listesi Ekranı (`MainActivity`)

- Mevcut tüm müşterileri gösterir
- Arama/filtreleme çubuğu ile müşteri listesini filtreleme
- "Yeni Müşteri" butonu ile yeni müşteri ekleme formu açma
- "Yenile" butonu ile listeyi manuel olarak güncelleme (otomatik güncelleme de mevcuttur)

### Müşteri Formu Ekranı (`MusteriFormActivity`)

- Yeni müşteri ekleme veya mevcut müşteri bilgilerini düzenleme
- Zorunlu alanlar için yerel validasyon kontrolleri:
  - Boş alan kontrolü
  - Email formatı doğrulama
  - TCKN uzunluğu ve formatı kontrolü
  - Doğum tarihi formatı kontrolü
- Doğum tarihi için tarih seçici 
- "Kaydet" butonu ile API'ye veri gönderme
- "İptal" butonu ile ana ekrana dönme

### Müşteri Kartı (`item_musteri.xml` ve `MusteriAdapter`)

- Her bir müşterinin listeleme ekranındaki görünümünü sağlar
- "Düzenle" ve "Sil" butonları ile ilgili müşteri üzerinde işlem yapma
- Müşteri silme öncesi onay diyalogu

## 🔄 Otomatik Liste Yenileme

- Bir müşteri başarıyla eklendiğinde veya güncellendiğinde, form ekranı kapandığında ana listedeki veriler otomatik olarak yenilenir
- Müşteri silindiğinde de liste otomatik olarak güncellenir
- Güncel bilgiler her zaman kullanıcıya gösterilir

## 🐛 Hata Yönetimi

### API İletişimi
- Ağ sorunları veya API'den dönen hatalar (başarısız HTTP kodları) `Toast` mesajlarıyla kullanıcıya bildirilir
- Bağlantı zaman aşımı ve ağ kesintileri için uygun hata mesajları gösterilir

### Form Validasyonu
- Eksik veya yanlış girişler için alan bazlı hata mesajları (`setError()`)
- Genel validasyon hataları için `Toast` mesajları
- Kullanıcı dostu hata açıklamaları

## 📋 Özellikler

- ✅ CRUD operasyonları (Create, Read, Update, Delete)
- ✅ Arama ve filtreleme
- ✅ Otomatik liste yenileme
- ✅ Form validasyonu
- ✅ Material Design arayüzü
- ✅ Hata yönetimi
- ✅ Onay diyalogları

## 🔧 Geliştirme Notları

- Uygulama ağırlıklı olarak Java ile geliştirilmiştir
- API bağlantısı için Retrofit kütüphanesi kullanılmıştır
- Material Design prensipleri benimsenmiştir
- RecyclerView ile performanslı liste görüntüleme sağlanmıştır

## 📄 API Bağımlılığı

Bu mobil uygulama, backend API'si ile çalışmak üzere tasarlanmıştır. API projesi hakkında daha fazla bilgi için [Müşteri Yönetim API'si](https://github.com/emreclh/musteriDB) repository'sini ziyaret edebilirsiniz.
