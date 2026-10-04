# Kalkulator Desktop Modern Pro (Java Swing & AWT)

Aplikasi kalkulator desktop modern generasi baru berbasis **Java (Standard JDK)** dengan antarmuka grafis (**Neo-Fluent / Cyber-Minimal**) yang memukau, bersih, dan sangat responsif.

---

## 💎 Pembaruan Visual & Tampilan Antarmuka Baru

1. **Desain Glassmorphic & Elevated Cards**:
   - Layar hasil kalkulasi kini berupa **kartu melayang (*elevated glass card*)** dengan sudut melengkung halus (radius 18px), pencahayaan tepi atas (*top edge highlight*), dan garis batas kontras lembut.
   - Status badge terintegrasi di dalam layar:
     - Badge Mode: `[ STANDAR ]` / `[ ILMIAH ]`
     - Badge Sudut interaktif: `[ DEG ]` / `[ RAD ]` (dapat diklik langsung untuk mengganti satuan)
     - Indikator Memori: Lampu titik hijau menyala `● M` saat ada nilai tersimpan di memori.
     - Tombol Salin Cepat (*Quick Copy*) dengan notifikasi feedback instan `"✓ Tersalin!"`.

2. **Bentuk Tombol Squircle & Glass Highlight**:
   - Sudut tombol membulat modern (*squircle* 14px-16px) dengan jarak rongga napas yang lega (jarak 7px).
   - Efek garis kilau kaca di sisi atas tombol (*tactile glass highlight*) memberikan kedalaman 3D tanpa terasa kuno.

3. **Tombol Sama Dengan Radiant Gradient (`=`)**:
   - Tombol hasil (`=`) didesain mencolok sebagai elemen utama dengan **gradien warna bercahaya** dari *Electric Royal Blue* (`#3B82F6`) ke *Emerald Cyan* (`#10B981`), lengkap dengan efek hover menyala.

4. **Header Modern dengan Badge "PRO"**:
   - Logo kalkulator modern multi-warna di pojok kiri atas jendela dengan label **"Kalkulator PRO"**.
   - Tombol toggle mode, tema, dan riwayat didesain dalam bentuk kapsul (*pill buttons*) yang elegan.

5. **100% Bebas Kotak Kosong (Anti-Tofu)**:
   - Seluruh ikon (Hamburger Menu, Penggaris Geometri, Matahari, Bulan Sabit, Jam Analog, Tempat Sampah, Panah Backspace, Clipboard) digambar langsung dengan rendering vektor anti-aliased Java2D tanpa bergantung pada font sistem.

---

## ⌨️ Pintasan Keyboard (Shortcuts)

| Tombol Keyboard | Aksi |
|-----------------|------|
| `0` - `9` | Memasukkan angka |
| `+`, `-`, `*`, `/` | Operasi tambah, kurang, kali, bagi |
| `.` atau `,` | Titik desimal |
| `Enter` atau `=` | Menghitung hasil |
| `Backspace` | Menghapus digit terakhir |
| `Escape` | Reset kalkulator (`C`) |
| `Delete` | Bersihkan input saat ini (`CE`) |
| `(` dan `)` | Tanda kurung |
| `%` | Persentase |
| `^` | Pangkat |
| `Ctrl + H` | Buka / tutup panel Riwayat |
| `Ctrl + S` | Ganti mode Standar / Ilmiah |
| `Ctrl + T` | Beralih Tema Gelap / Terang |
| `Ctrl + C` | Salin hasil kalkulasi ke clipboard |

---

## 💻 Cara Menjalankan

### Cara 1: Menggunakan File Batch (Paling Cepat)
Cukup **klik dua kali** file:
```
run_calculator.bat
```

### Cara 2: Klik Langsung File JAR
Cukup **klik dua kali** file:
```
Calculator.jar
```

### Cara 3: Melalui Terminal
```bash
java -jar Calculator.jar
```
