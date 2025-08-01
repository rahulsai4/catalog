import java.math.BigInteger;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                System.err.println("Usage: java -cp \"lib/json-20240205.jar:.\" Main <input.json>");
                return;
            }

            JsonInput.InputData data = JsonInput.read(args[0]);
            // Use the first k shares (they are already sorted in JsonInput)
            List<Share> shares = data.shares.subList(0, data.k);

            BigInteger secret = Interpolator.secretAtZero(shares, data.k);
            System.out.println(secret.toString());

        } catch (Exception e) {
            // Keep errors readable
            System.err.println("Error: " + e.getMessage());
        }
    }
}
