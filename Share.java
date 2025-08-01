import java.math.BigInteger;

public final class Share {
    public final BigInteger x;
    public final BigInteger y;

    public Share(BigInteger x, BigInteger y) {
        if (x == null || y == null) {
            throw new IllegalArgumentException("x and y must not be null");
        }
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Share{x=" + x + ", y=" + y + "}";
    }
}