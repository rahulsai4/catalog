import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

public final class JsonInput {

    private JsonInput() { /* utility class */ }

    /** Container for parsed data. */
    public static final class InputData {
        public final int n;
        public final int k;
        public final List<Share> shares;

        public InputData(int n, int k, List<Share> shares) {
            this.n = n;
            this.k = k;
            this.shares = shares;
        }
    }

    public static InputData read(String path) throws IOException {
        String jsonText = Files.readString(Path.of(path), StandardCharsets.UTF_8);
        try {
            JSONObject root = new JSONObject(jsonText);

            // Extract keys.n and keys.k
            if (!root.has("keys")) {
                throw new IllegalArgumentException("Missing 'keys' object.");
            }
            JSONObject keys = root.getJSONObject("keys");

            int n = getInt(keys, "n");
            int k = getInt(keys, "k");
            if (k <= 0) {
                throw new IllegalArgumentException("'k' must be positive.");
            }

            // Collect all numeric-key entries as shares
            List<Share> shares = new ArrayList<>();
            Set<BigInteger> seenX = new HashSet<>();

            for (Iterator<String> it = root.keys(); it.hasNext(); ) {
                String key = it.next();
                if ("keys".equals(key)) continue;

                // Only process numeric JSON property names
                if (!isNumericKey(key)) {
                    // Ignore non-numeric keys silently, or throw if you want strictness
                    continue;
                }

                BigInteger x = new BigInteger(key);
                JSONObject obj = root.getJSONObject(key);

                // Base can be stored as number or string in provided inputs
                int base = parseBase(obj.get("base"));
                if (base < 2 || base > 36) {
                    throw new IllegalArgumentException("Invalid base " + base + " for x=" + x + " (allowed 2..36).");
                }

                String valueStr = obj.get("value").toString().trim();
                BigInteger y;
                try {
                    y = new BigInteger(valueStr, base);
                } catch (NumberFormatException nfe) {
                    throw new IllegalArgumentException(
                        "Invalid digits for base " + base + " at x=" + x + ": '" + valueStr + "'", nfe);
                }

                if (!seenX.add(x)) {
                    throw new IllegalArgumentException("Duplicate x detected: " + x);
                }
                shares.add(new Share(x, y));
            }

            if (shares.size() < k) {
                throw new IllegalArgumentException(
                    "Not enough points: have " + shares.size() + ", need k=" + k);
            }

            // Sort deterministically by x (ascending)
            Collections.sort(shares, Comparator.comparing(s -> s.x));

            // n is declared; we won't enforce equality with shares.size() because
            // the JSON may omit some indices. You can uncomment the check if desired.
            // if (shares.size() != n) {
            //     throw new IllegalArgumentException("Declared n=" + n + " but found " + shares.size() + " points.");
            // }

            return new InputData(n, k, shares);

        } catch (JSONException je) {
            throw new IllegalArgumentException("Invalid JSON structure: " + je.getMessage(), je);
        }
    }

    // ---- helpers ----

    private static int getInt(JSONObject obj, String field) {
        if (!obj.has(field)) {
            throw new IllegalArgumentException("Missing field: keys." + field);
        }
        Object v = obj.get(field);
        if (v instanceof Number) {
            return ((Number) v).intValue();
        }
        try {
            return Integer.parseInt(v.toString());
        } catch (NumberFormatException nfe) {
            throw new IllegalArgumentException("keys." + field + " must be an integer. Got: " + v);
        }
    }

    private static boolean isNumericKey(String s) {
        // Accept digits only; no sign, no decimals
        if (s == null || s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') return false;
        }
        return true;
    }

    private static int parseBase(Object baseObj) {
        if (baseObj instanceof Number) {
            return ((Number) baseObj).intValue();
        }
        String s = baseObj.toString().trim();
        return Integer.parseInt(s);
    }
}
