import java.math.BigInteger;
import java.util.List;

/**
 * Computes c = f(0) using Lagrange interpolation on any k shares.
 *
 * c = sum_i y_i * prod_{j!=i} (-x_j)/(x_i - x_j)
 */
public final class Interpolator {
    private Interpolator() {}

    public static BigInteger secretAtZero(List<Share> shares, int k) {
        if (shares.size() < k) {
            throw new IllegalArgumentException("Need at least k shares");
        }
        Fraction acc = Fraction.ZERO;

        for (int i = 0; i < k; i++) {
            BigInteger xi = shares.get(i).x;
            BigInteger yi = shares.get(i).y;

            Fraction term = Fraction.of(yi); // start with y_i
            for (int j = 0; j < k; j++) {
                if (i == j) continue;
                BigInteger xj = shares.get(j).x;

                BigInteger num = xj.negate();        // -x_j
                BigInteger den = xi.subtract(xj);    // (x_i - x_j)
                if (den.signum() == 0) {
                    throw new IllegalArgumentException("Duplicate x detected during interpolation");
                }
                term = term.mul(new Fraction(num, den));
            }
            acc = acc.add(term);
        }

        return acc.toBigIntegerExact(); // must reduce to integer if inputs are consistent
    }
}
