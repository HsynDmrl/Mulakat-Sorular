# Mülakat Projeleri

Bu repo, farklı teknik görevlerin ve mülakat sorularının çözümlerini içeren bir koleksiyondur. Her görev belirli bir yazılım geliştirme pratiğini test etmek için tasarlanmıştır ve farklı dillerde, veritabanı yönetimi, algoritmalar ve sistem tasarımı ile ilgilidir.

## Proje Yapısı

```
Mülakat-Projeleri/
├── Gorev1/  # Sosyal medya platformu mimarisi
│   ├── Gorev 1.pdf
│   ├── sunucu_mimarisi_diagramı.png
│   ├── ölçeklenebilirlik_dokumantasyonu.md
├── Gorev2/  # Fibonacci ve asal sayılar hesaplama
│   ├── Main.java
│   ├── Makefile
│   ├── Dockerfile
│   ├── aciklama.txt
├── Gorev3/  # Basit bir web uygulaması geliştirme
│   ├── frontend/
│   ├── backend/
│   ├── db/
│   ├── docker-compose.yml
│   ├── Makefile
├── Gorev4/  # Veri normalizasyonu algoritması
│   ├── Main.java
│   ├── Makefile
│   ├── Dockerfile
├── Gorev5/  # SQL sorgusu ile çalışan hesaplama
│   ├── gorev.sql
│   ├── aciklama.txt
├── SORU1.pdf  # Uygulama güvenliği soruları
├── SORU2.pdf  # Java performans optimizasyonu
├── SORU3.pdf  # Tasarım şablonları
├── SORU4.pdf  # RESTful ve SOAP web servisleri
├── SORU5.pdf  # SQL sorgu analizi
```

## Kurulum

### Gereksinimler
- Java 8+
- PostgreSQL
- Docker
- Maven
- Makefile

### Çalıştırma

Her görev farklı bir teknolojiyi kapsadığı için aşağıdaki talimatları takip edebilirsiniz:

**Java Tabanlı Görevler**
```sh
cd Gorev2
javac Main.java
java Main
```

**Web Uygulaması (Frontend + Backend)**
```sh
cd Gorev3
docker-compose up -d
```

**SQL Görevini Çalıştırma**
```sh
cd Gorev5
psql -U postgres -d testdb -f gorev.sql
```

## İçerik Açıklamaları

### Görev 1: Sosyal Medya Platformu Mimari Tasarımı
- Kullanıcıların içerik paylaşabileceği, etkileşimde bulunabileceği bir sistem tasarımı.
- Ölçeklenebilirlik, önbellekleme, güvenlik stratejileri gibi konulara değinildi.

### Görev 2: Fibonacci ve Asal Sayılar
- 500.000’den küçük en büyük asal sayı ve 500.000’den büyük en küçük asal sayıyı bulan bir Java uygulaması.
- Performans optimizasyonları içeren bir algoritma.

### Görev 3: Basit Web Uygulaması
- Bootstrap ile kullanıcı dostu bir arayüz.
- jQuery ve AJAX ile RESTful API entegrasyonu.
- PostgreSQL veritabanına veri ekleme ve listeleme özellikleri.
- Docker-compose ile ölçeklenebilir dağıtım.

### Görev 4: Veri Normalizasyonu
- 1-10.000 arası rastgele 500 sayı üreten bir algoritma.
- Minimum ve maksimum değerleri güncelleyerek normalizasyon uygulayan bir sistem.

### Görev 5: SQL Çalışan Sayısı Hesaplama
- Departman başına çalışan sayısını hesaplayan SQL sorgusu.
- Sonucun kaç satır döndüreceğinin analizi.

