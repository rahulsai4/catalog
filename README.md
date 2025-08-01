# Shamir's Secret Sharing Implementation

This project implements Shamir's Secret Sharing algorithm using Lagrange interpolation to reconstruct the secret from polynomial shares.

## File Structure

### Core Files

#### `Main.java`

The entry point. Reads the JSON file path from the command line, calls the parser, runs the math, and prints the secret c.

#### `JsonInput.java`

Opens the JSON file, reads n and k, decodes each entry's base + value into a big integer y, turns the JSON key into x, and returns a list of (x, y) points.

#### `Share.java`

A tiny container for one data point: x and y (both big integers). Nothing fancy.

#### `Fraction.java`

Exact rational numbers using big integers (numerator/denominator). Provides add, mul, and always keeps the fraction reduced. Avoids floating-point errors.

#### `Interpolator.java`

Does the math. Uses Lagrange interpolation at x = 0 to compute the secret c = f(0) directly from any k points.

### Directories

#### `input/`

Folder holding your test JSON files (e.g., `sample.json`, `testcase2.json`).

#### `lib/json-20240205.jar` (optional but recommended)

A small JSON library (like org.json) so `JsonInput.java` can parse JSON easily without you writing a custom parser. If you don't want any jar, you'd need to hand-write simple JSON parsing.

## Dependencies

-   Java 8 or higher
-   org.json library (json-20240205.jar) for JSON parsing

## Installation

### 1. Install Java

Make sure you have Java 8 or higher installed:

```bash
java -version
```

### 2. Download JSON Library

Download the org.json library JAR file:

```bash
# Create lib directory
mkdir -p lib

# Download the JSON library JAR
wget https://repo1.maven.org/maven2/org/json/json/20240205/json-20240205.jar -O lib/json-20240205.jar
```

**Alternative download methods:**

-   **Using curl:**
    ```bash
    curl -o lib/json-20240205.jar https://repo1.maven.org/maven2/org/json/json/20240205/json-20240205.jar
    ```
-   **Manual download:** Visit https://repo1.maven.org/maven2/org/json/json/20240205/ and download `json-20240205.jar` to your `lib/` folder

## Compilation and Usage

### Compile with JAR in classpath:

```bash
javac -cp "lib/json-20250517.jar" *.java
```

### Run with JAR in classpath:

```bash
java -cp ".:lib/json-20250517.jar" Main input/sample.json
```

### Alternative: All-in-one commands

```bash
# Compile all Java files
javac -cp "lib/json-20250517.jar" *.java

# Run the program
java -cp ".:lib/json-20250517.jar" Main input/sample.json
```

**Note**: On Windows, use semicolon (`;`) instead of colon (`:`) as the classpath separator:

```cmd
java -cp ".;lib/json-20250517.jar" Main input/sample.json
```
# catalog
