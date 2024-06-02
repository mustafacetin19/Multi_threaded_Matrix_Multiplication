// Gerekli Java kütüphanelerinin import edilmesi
import java.io.*; // Giriş/çıkış işlemleri için gerekli sınıfları içeren kütüphane
import java.util.Random; // Rastgele sayı üretmek için gerekli sınıfı içeren kütüphane

// Ana sınıf: MatrixMultiplication
class MatrixMultiplication {
    // Matris boyutları için sabitler
    static final int SATIR1 = 4; // A matrisinin satır sayısı
    static final int ORTAK = 1;   // A matrisinin sütunu ile B matrisinin satır sayısı (Matris çarpım kuralı)
    static final int SUTUN2 = 7;  // B matrisinin sütun sayısı

    // Matrislerin tanımlanması
    static int[][] A = new int[SATIR1][ORTAK];
    static int[][] B = new int[ORTAK][SUTUN2];
    static int[][] C = new int[SATIR1][SUTUN2]; // Sonuç matrisi
    static int satirSayisi = 0;                 // Thread sayısını belirlemek için kullanılan sayaç

    // Ana metod: main
    public static void main(String[] args) {
        randomDosyayaYaz();                    // Rastgele A ve B matrislerini oluşturup dosyaya yazma işlemini gerçekleştirir
        dosyadanAktar();                       // Dosyadan oluşturulan A ve B matrislerini okuma işlemini gerçekleştirir

        // Threadi başlatma
        Thread[] threadler = new Thread[SATIR1];
        for (int i = 0; i < SATIR1; i++) {
            threadler[i] = new Thread(new Carpim()); // Her bir satır için bir Thread oluşturur
            threadler[i].start();                    // Oluşturulan Threadi başlatır
        }

        // Threadlerin tamamlanmasını bekler
        for (int i = 0; i < SATIR1; i++) {
            try {
                threadler[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Sonuç matrisini ekrana yazdırma
        for (int i = 0; i < SATIR1; i++) {
            for (int j = 0; j < SUTUN2; j++) {
                if (C[i][j] < 10)
                    System.out.print(C[i][j] + "     ");
                else if (C[i][j] < 100)
                    System.out.print(C[i][j] + "    ");
                else if (C[i][j] < 1000)
                    System.out.print(C[i][j] + "   ");
                else if (C[i][j] < 10000)
                    System.out.print(C[i][j] + "  ");
                else
                    System.out.print(C[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Thread Sınıfı: Carpim
    static class Carpim implements Runnable {
        @Override
        public void run() {
            long baslangic = System.currentTimeMillis(); // Thread'in çalışma süresinin başlangıç zamanını alır
            int threadNo;
            synchronized (MatrixMultiplication.class) {
                threadNo = satirSayisi++; // Thread'in hangi satırda çalıştığını belirler
            }
            // Çarpma işlemini gerçekleştirir ve sonucu C matrisine kaydeder
            for (int i = 0; i < SUTUN2; i++) {
                for (int j = 0; j < ORTAK; j++) {
                    C[threadNo][i] += (A[threadNo][j] * B[j][i]);
                }
            }
            long son = System.currentTimeMillis();                                                                            // Thread'in çalışma süresinin sonunu alır
            double calismaZamani = (son - baslangic) / 1000.0;                                                                // Thread'in çalışma süresini hesaplar
            System.out.printf("Çarpımın sonuç matrisinin %d. satırı %f saniyede hesaplandı!\n", threadNo + 1, calismaZamani); // Thread'in çalışma süresini ekrana yazdırır
        }
    }

    // Rastgele Dosyaya Yazma Metodu: randomDosyayaYaz
    static void randomDosyayaYaz() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("matrisler.txt"))) {
            Random rand = new Random();
            // A matrisini dosyaya yazma
            for (int i = 0; i < SATIR1; i++) {
                for (int j = 0; j < ORTAK; j++) {
                    int sayi = rand.nextInt(100);
                    writer.write(String.format("%03d", sayi));                                     // Rastgele oluşturulan A matrisini dosyaya yazar
                }
            }
            // B matrisini dosyaya yazma
            for (int i = 0; i < ORTAK; i++) {
                for (int j = 0; j < SUTUN2; j++) {
                    int sayi = rand.nextInt(100);
                    writer.write(String.format("%03d", sayi));                                     // Rastgele oluşturulan B matrisini dosyaya yazar
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        // Dosyadan Okuma Metodu: dosyadanAktar
    static void dosyadanAktar() {
        try (BufferedReader reader = new BufferedReader(new FileReader("matrisler.txt"))) {
                                                                                                         // A matrisini dosyadan okuma
            for (int i = 0; i < SATIR1; i++) {
                for (int j = 0; j < ORTAK; j++) {
                    char[] okuma = new char[3];
                    reader.read(okuma);
                    A[i][j] = Integer.parseInt(new String(okuma));                                       // Dosyadan oluşturulan A matrisini okur
                }
            }
                                                                                                         // B matrisini dosyadan okuma
            for (int i = 0; i < ORTAK; i++) {
                for (int j = 0; j < SUTUN2; j++) {
                    char[] okuma = new char[3];
                    reader.read(okuma);
                    B[i][j] = Integer.parseInt(new String(okuma));                                       // Dosyadan oluşturulan B matrisini okur
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
