import java.util.Random;

public class GenerateE {
    public static long generateRandomE(long n) {
        Random rand = new Random();
        long e;
        do {
            e = rand.nextLong() % (n - 2) + 2;
            if (e < 0) {
                e += n;
            }
        } while (e <= 2 || gcd(e, n) != 1);
        return e;
    }

    public static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
}