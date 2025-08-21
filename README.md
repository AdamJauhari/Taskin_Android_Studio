## Taskin

Aplikasi Android untuk membantu pelajar/mahasiswa mengelola aktivitas belajar sehari‑hari: jadwal kelas, materi (catatan visual dan berkas), grup belajar, serta pengingat/tugas. Mendukung tema terang/gelap, multi‑bahasa, dan ekspor/impor data ke berkas JSON.

### Ringkas
- **Paket**: `uca.aidama.taskin`
- **Versi**: 2.0 (versionCode 2)
- **minSdk/targetSdk/compileSdk**: 31 / 34 / 34

## Fitur Utama
- **Beranda**: gambaran singkat dan pintasan ke fitur inti.
- **Jadwal Kelas**: kelola jadwal mingguan, tambah/edit/hapus entri; tampilan per hari; kartu pengaturan nama program/kelas dan semester.
- **Materi**: pengelompokan per mata pelajaran dari jadwal; dukungan catatan visual (kamera/galeri) dan berkas dokumen; pratinjau dan manajemen item.
- **Grup Belajar**: buat grup berdasarkan mata pelajaran, tambah anggota, peran opsional, dan pengelolaan anggota.
- **Pengingat & Tugas**: buat/edit/hapus tugas dengan tenggat tanggal/waktu opsional; status selesai/terlambat; notifikasi mendekati tenggat.
- **Tema & Bahasa**: toggle Mode Terang/Gelap; pilihan bahasa English / Indonesian / Chinese (Simplified).
- **Ekspor/Impor Data (JSON)**: simpan seluruh data ke berkas `.json` dan muat kembali. Berguna untuk backup/migrasi.

## Teknologi
- **Bahasa**: Java dengan **ViewBinding**
- **Jetpack**: Navigation, Lifecycle (ViewModel/LiveData), Room (database lokal)
- **UI**: Material Components
- **Lainnya**: Glide (gambar), Gson (serialisasi JSON)

## Persyaratan Pengembangan
- Android Studio terbaru (Giraffe/Koala atau yang lebih baru)
- JDK 11
- Android SDK Platform 34

## Menjalankan Proyek
### 1) Dengan Android Studio
1. File → Open… dan pilih folder proyek ini (`Taskin`).
2. Tunggu sinkronisasi Gradle selesai.
3. Pilih konfigurasi `app` lalu Run.

### 2) Dengan Gradle (CLI)
Di root proyek jalankan:

```bash
# Windows
gradlew.bat assembleDebug

# macOS/Linux
./gradlew assembleDebug
```

APK debug akan tersedia di `app/build/outputs/apk/debug/`.

## Struktur Proyek (ringkas)
```
Taskin/
├─ app/
│  ├─ src/main/
│  │  ├─ AndroidManifest.xml
│  │  ├─ java/uca/aidama/taskin/ (kode sumber utama)
│  │  └─ res/ (resources XML, ikon, layout, dsb.)
│  └─ build.gradle.kts
├─ build.gradle.kts
├─ settings.gradle.kts
└─ gradle/ (wrapper, versi libs)
```

## Izin yang Digunakan
- `POST_NOTIFICATIONS` (Android 13+): menampilkan notifikasi pengingat.
- `CAMERA`: mengambil foto untuk catatan visual.
- Akses media/penyimpanan sesuai versi Android: `READ_MEDIA_*` (Android 13+), `READ_EXTERNAL_STORAGE` (≤ 32), `WRITE_EXTERNAL_STORAGE` (≤ 28).

## Lokalisasi
- Bahasa yang didukung: **English (`en`)**, **Indonesian (`in`)**, **Chinese Simplified (`zh`)**.
- Penggantian bahasa dapat diakses dari menu pengaturan di toolbar.

## Ekspor/Impor Data
- Buka menu di toolbar → pilih **Ekspor Data** untuk menyimpan ke berkas `taskin_data_export.json`.
- Pilih **Impor Data** untuk memuat berkas JSON. Perhatian: proses ini akan menimpa data yang ada.

## Keterangan
Program ini dibuat untuk menyelesaikan tugas Mata Kuliah Pengantar Interaksi Manusia dan Komputer

## Anggota Kelompok
- Adam Arias Jauhari
- MHD Aldy Syahputra
- Vicky Maulana Romadan
- Aidil Ilham Wertefe

# Link Download Aplikasi
  https://drive.google.com/file/d/1Pw66T3yefu8Xs1oJjI8n5ToqeHo8pjQs/view?usp=sharing

