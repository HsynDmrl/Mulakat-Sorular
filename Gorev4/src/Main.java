import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int SIZE = 500;
        int MIN_VALUE = 1;
        int MAX_VALUE = 10000;
        int[] dataset = new int[SIZE];
        int currentMin = Integer.MAX_VALUE;
        int currentMax = Integer.MIN_VALUE;
        Random random = new Random();

        // 500 farklı rastgele sayı üretir ve diziye ekler
        for (int i = 0; i < SIZE; i++) {
            int newNumber = random.nextInt(MAX_VALUE - MIN_VALUE + 1) + MIN_VALUE;

            // Benzersiz sayı üretmek için tekrar edenleri kontrol eder
            for (int j = 0; j < i; j++) {
                if (dataset[j] == newNumber) {
                    newNumber = random.nextInt(MAX_VALUE - MIN_VALUE + 1) + MIN_VALUE;
                    j = -1; // Baştan başlatmak için sıfırlıyorum
                }
            }

            dataset[i] = newNumber;

            // Min-max güncelleme
            if (newNumber < currentMin) currentMin = newNumber;
            if (newNumber > currentMax) currentMax = newNumber;

            // Normalizasyon ve ekrana yazdırma
            System.out.print("Veri Seti (" + (i + 1) + " elemanlı): {");
            for (int j = 0; j <= i; j++) {
                System.out.print(dataset[j] + (j < i ? ", " : ""));
            }
            System.out.println("}");

            System.out.print("Normalizasyon: [");
            for (int j = 0; j <= i; j++) {
                double normalized = (currentMax != currentMin) 
                    ? (double) (dataset[j] - currentMin) / (currentMax - currentMin)
                    : 0.0;
                System.out.printf("%.6f", normalized);
                if (j < i) System.out.print(", ");
            }
            System.out.println("]");
            System.out.println("-------------------------------------------------");
        }
    }
}
