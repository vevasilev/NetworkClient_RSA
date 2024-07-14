public class ModularInverse {
    public static long modInverse(long e, long n) {
        long[] result = extendedEuclidean(e, n);
        if (result[0] != 1) {
            throw new ArithmeticException("The modular inverse does not exist.");
        }
        long d = result[1];
        if (d < 0) {
            d += n;
        }
        return d;
    }

    public static long[] extendedEuclidean(long e, long n) {
        if (e == 0) {
            return new long[]{n, 0, 1};
        }
        long[] values = extendedEuclidean(n % e, e);
        long gcd = values[0];
        long x = values[2] - (n / e) * values[1];
        long y = values[1];
        return new long[]{gcd, x, y};
    }
}
