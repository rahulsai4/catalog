import java.math.BigInteger;

public final class Fraction {
    public static final Fraction ZERO = new Fraction(BigInteger.ZERO, BigInteger.ONE);

    private final BigInteger num; // numerator
    private final BigInteger den; // denominator (always > 0)

    public Fraction(BigInteger num, BigInteger den) {
        if (den.equals(BigInteger.ZERO)) {
            throw new ArithmeticException("Denominator cannot be zero");
        }
        // normalize sign to denominator and reduce by gcd
        if (den.signum() < 0) {
            num = num.negate();
            den = den.negate();
        }
        BigInteger g = num.gcd(den);
        if (!g.equals(BigInteger.ONE)) {
            num = num.divide(g);
            den = den.divide(g);
        }
        this.num = num;
        this.den = den;
    }

    public static Fraction of(BigInteger n) {
        return new Fraction(n, BigInteger.ONE);
    }

    public Fraction add(Fraction other) {
        BigInteger n = this.num.multiply(other.den).add(other.num.multiply(this.den));
        BigInteger d = this.den.multiply(other.den);
        return new Fraction(n, d);
    }

    public Fraction mul(Fraction other) {
        BigInteger n = this.num.multiply(other.num);
        BigInteger d = this.den.multiply(other.den);
        return new Fraction(n, d);
    }

    public Fraction negate() {
        return new Fraction(this.num.negate(), this.den);
    }

    public boolean isInteger() {
        return den.equals(BigInteger.ONE);
    }

    public BigInteger toBigIntegerExact() {
        if (!isInteger()) throw new ArithmeticException("Fraction not an integer: " + this);
        return num; // den == 1
    }

    @Override public String toString() { return num + (isInteger() ? "" : "/" + den); }
}
