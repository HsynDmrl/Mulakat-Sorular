public class Main {
    static final int LIMIT = 600000;
    static boolean[] isPrime;

    public static void main(String[] args) {
        sieveOfEratosthenes(LIMIT);

        int lowerPrime = findLargestPrimeBelow(500000);
        int upperPrime = findSmallestPrimeAbove(500000);
        int difference = upperPrime - lowerPrime;

        System.out.println("500.000'den küçük en büyük asal sayı: " + lowerPrime);
        System.out.println("500.000'den büyük en küçük asal sayı: " + upperPrime);
        System.out.println("Aralarındaki fark: " + difference);
    }

    public static void sieveOfEratosthenes(int n) {
        isPrime = new boolean[n + 1];
        for (int i = 2; i <= n; i++) {
            isPrime[i] = true;
        }
        for (int p = 2; p * p <= n; p++) {
            if (isPrime[p]) {
                for (int i = p * p; i <= n; i += p) {
                    isPrime[i] = false;
                }
            }
        }
    }

    public static int findLargestPrimeBelow(int limit) {
        for (int i = limit - 1; i > 1; i--) {
            if (isPrime[i]) return i;
        }
        return -1;
    }

    public static int findSmallestPrimeAbove(int limit) {
        int num = limit + 1;
        while (num < LIMIT && !isPrime[num]) {
            num++;
        }
        return num;
    }
}