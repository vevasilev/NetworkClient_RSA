import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomPrimeNumbers {
    private int prime1;
    private int prime2;

    public RandomPrimeNumbers() {
        int upperLimit = 1000;
        List<Integer> primes = generatePrimes(upperLimit);
        Random random = new Random();
        int index1 = random.nextInt(primes.size());
        int index2;
        do {
            index2 = random.nextInt(primes.size());
        } while (index2 == index1);
        this.prime1 = primes.get(index1);
        this.prime2 = primes.get(index2);
    }

    private static List<Integer> generatePrimes(int limit) {
        boolean[] isComposite = new boolean[limit + 1];
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= limit; i++) {
            if (!isComposite[i]) {
                primes.add(i);
                for (int j = i * i; j <= limit; j += i) {
                    isComposite[j] = true;
                }
            }
        }
        return primes;
    }

    public int getPrime1() {
        return prime1;
    }

    public int getPrime2() {
        return prime2;
    }
}