public class KeysUser {
    private int n;
    private long e;
    private long d;

    public KeysUser() {
        RandomPrimeNumbers numbers = new RandomPrimeNumbers();
        int p = numbers.getPrime1();
        int q = numbers.getPrime2();
        this.n = p * q;
        int phi_n = (p - 1) * (q - 1);
        this.e = GenerateE.generateRandomE(phi_n);
        this.d = ModularInverse.modInverse(e, phi_n);
    }

    public int getN() {
        return n;
    }

    public long getE() {
        return e;
    }

    public long getD() {
        return d;
    }
}
