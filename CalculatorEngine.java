import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Mathematical evaluation engine for the desktop calculator.
 * Implements a recursive descent parser for safe and precise expression evaluation.
 */
public class CalculatorEngine {
    private String angleMode = "DEG"; // "DEG" or "RAD"
    private double memory = 0.0;

    public CalculatorEngine() {}

    public String getAngleMode() {
        return angleMode;
    }

    public void setAngleMode(String mode) {
        if ("DEG".equalsIgnoreCase(mode) || "RAD".equalsIgnoreCase(mode)) {
            this.angleMode = mode.toUpperCase();
        }
    }

    public void toggleAngleMode() {
        this.angleMode = "DEG".equals(this.angleMode) ? "RAD" : "DEG";
    }

    public double getMemory() {
        return memory;
    }

    public void setMemory(double val) {
        this.memory = val;
    }

    public void memoryClear() {
        this.memory = 0.0;
    }

    public void memoryAdd(double val) {
        this.memory += val;
    }

    public void memorySubtract(double val) {
        this.memory -= val;
    }

    public double evaluate(String expression) throws Exception {
        if (expression == null || expression.trim().isEmpty()) {
            return 0.0;
        }

        String normalized = preprocess(expression);
        Parser parser = new Parser(normalized, this.angleMode);
        return parser.parse();
    }

    private String preprocess(String expr) {
        return expr.replace("×", "*")
                   .replace("x", "*")
                   .replace("÷", "/")
                   .replace(":", "/")
                   .replace("π", "pi")
                   .replace("√", "sqrt")
                   .replace(",", ".");
    }

    public static String formatResult(double val) {
        if (Double.isInfinite(val)) {
            return val > 0 ? "Tak Terhingga" : "-Tak Terhingga";
        }
        if (Double.isNaN(val)) {
            return "Bukan Angka";
        }

        // Handle floating precision noise around zero
        if (Math.abs(val) < 1e-12) {
            val = 0.0;
        }

        // Check if value is very close to an integer
        double roundedInt = Math.round(val);
        if (Math.abs(val - roundedInt) < 1e-11) {
            if (Math.abs(roundedInt) >= 1e15) {
                DecimalFormat df = new DecimalFormat("0.######E0", DecimalFormatSymbols.getInstance(Locale.US));
                return df.format(roundedInt);
            }
            return String.valueOf((long) roundedInt);
        }

        // Large or very small numbers formatted as scientific
        if (Math.abs(val) >= 1e15 || (Math.abs(val) > 0 && Math.abs(val) < 1e-9)) {
            DecimalFormat df = new DecimalFormat("0.######E0", DecimalFormatSymbols.getInstance(Locale.US));
            return df.format(val);
        }

        // Standard decimal format without trailing zeros
        DecimalFormat df = new DecimalFormat("0.##########", DecimalFormatSymbols.getInstance(Locale.US));
        return df.format(val);
    }

    /**
     * Recursive Descent Parser implementation
     */
    private static class Parser {
        private final String input;
        private final String angleMode;
        private int pos = -1;
        private int ch;

        public Parser(String input, String angleMode) {
            this.input = input;
            this.angleMode = angleMode;
        }

        private void nextChar() {
            ch = (++pos < input.length()) ? input.charAt(pos) : -1;
        }

        private boolean eat(int charToEat) {
            while (ch == ' ') nextChar();
            if (ch == charToEat) {
                nextChar();
                return true;
            }
            return false;
        }

        public double parse() throws Exception {
            nextChar();
            double x = parseExpression();
            while (ch == ' ') nextChar();
            if (pos < input.length()) {
                throw new IllegalArgumentException("Karakter tidak terduga: " + (char) ch);
            }
            return x;
        }

        // Grammar: expression = term { ('+' | '-') term }
        private double parseExpression() throws Exception {
            double x = parseTerm();
            while (true) {
                if (eat('+')) {
                    x += parseTerm();
                } else if (eat('-')) {
                    x -= parseTerm();
                } else {
                    return x;
                }
            }
        }

        // Grammar: term = factor { ('*' | '/' | '%') factor }
        private double parseTerm() throws Exception {
            double x = parseFactor();
            while (true) {
                if (eat('*')) {
                    x *= parseFactor();
                } else if (eat('/')) {
                    double divisor = parseFactor();
                    if (divisor == 0.0) {
                        throw new ArithmeticException("Tidak dapat membagi dengan nol");
                    }
                    x /= divisor;
                } else if (eat('%')) {
                    double divisor = parseFactor();
                    if (divisor == 0.0) {
                        throw new ArithmeticException("Modulus nol tidak valid");
                    }
                    x %= divisor;
                } else {
                    return x;
                }
            }
        }

        // Grammar: factor = ('+' | '-') factor | power
        private double parseFactor() throws Exception {
            if (eat('+')) return +parseFactor();
            if (eat('-')) return -parseFactor();

            double x = parsePrimary();

            // Right-associative exponentiation: base ^ exp
            if (eat('^')) {
                x = Math.pow(x, parseFactor());
            }

            // Postfix factorial: x!
            if (eat('!')) {
                x = factorial(x);
            }

            return x;
        }

        // Grammar: primary = number | constant | function '(' expr ')' | '(' expr ')'
        private double parsePrimary() throws Exception {
            while (ch == ' ') nextChar();

            int startPos = this.pos;
            if (eat('(')) {
                double x = parseExpression();
                if (!eat(')')) {
                    throw new IllegalArgumentException("Tutup kurung ')' tidak ditemukan");
                }
                return x;
            }

            // Numbers (including decimals)
            if ((ch >= '0' && ch <= '9') || ch == '.') {
                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                String numStr = input.substring(startPos, this.pos);
                try {
                    return Double.parseDouble(numStr);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Format angka tidak valid: " + numStr);
                }
            }

            // Identifiers: functions & constants
            if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
                while ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) {
                    nextChar();
                }
                String identifier = input.substring(startPos, this.pos).toLowerCase();

                // Constants
                if ("pi".equals(identifier)) return Math.PI;
                if ("e".equals(identifier)) return Math.E;

                // Function calls: require '(' expr ')'
                if (!eat('(')) {
                    throw new IllegalArgumentException("Tanda '(' diharapkan setelah fungsi " + identifier);
                }
                double arg = parseExpression();
                if (!eat(')')) {
                    throw new IllegalArgumentException("Tutup kurung ')' tidak ditemukan untuk " + identifier);
                }

                return applyFunction(identifier, arg);
            }

            throw new IllegalArgumentException("Karakter tidak terduga pada posisi " + pos);
        }

        private double applyFunction(String func, double arg) throws Exception {
            switch (func) {
                case "sqrt":
                    if (arg < 0) throw new ArithmeticException("Akar bilangan negatif tidak valid");
                    return Math.sqrt(arg);
                case "cbrt":
                    return Math.cbrt(arg);
                case "sin": {
                    double rad = "DEG".equals(angleMode) ? Math.toRadians(arg) : arg;
                    double val = Math.sin(rad);
                    return Math.abs(val) < 1e-15 ? 0.0 : val;
                }
                case "cos": {
                    double rad = "DEG".equals(angleMode) ? Math.toRadians(arg) : arg;
                    double val = Math.cos(rad);
                    return Math.abs(val) < 1e-15 ? 0.0 : val;
                }
                case "tan": {
                    double rad = "DEG".equals(angleMode) ? Math.toRadians(arg) : arg;
                    double cosVal = Math.cos(rad);
                    if (Math.abs(cosVal) < 1e-15) {
                        throw new ArithmeticException("Tan tidak terdefinisi (tak hingga)");
                    }
                    double val = Math.tan(rad);
                    return Math.abs(val) < 1e-15 ? 0.0 : val;
                }
                case "asin": {
                    if (arg < -1.0 || arg > 1.0) throw new ArithmeticException("Nilai asin harus antara -1 dan 1");
                    double res = Math.asin(arg);
                    return "DEG".equals(angleMode) ? Math.toDegrees(res) : res;
                }
                case "acos": {
                    if (arg < -1.0 || arg > 1.0) throw new ArithmeticException("Nilai acos harus antara -1 dan 1");
                    double res = Math.acos(arg);
                    return "DEG".equals(angleMode) ? Math.toDegrees(res) : res;
                }
                case "atan": {
                    double res = Math.atan(arg);
                    return "DEG".equals(angleMode) ? Math.toDegrees(res) : res;
                }
                case "log":
                    if (arg <= 0) throw new ArithmeticException("Logaritma nilai non-positif tidak valid");
                    return Math.log10(arg);
                case "ln":
                    if (arg <= 0) throw new ArithmeticException("Ln nilai non-positif tidak valid");
                    return Math.log(arg);
                case "exp":
                    return Math.exp(arg);
                case "abs":
                    return Math.abs(arg);
                case "fact":
                    return factorial(arg);
                default:
                    throw new IllegalArgumentException("Fungsi tidak dikenal: " + func);
            }
        }

        private double factorial(double n) throws Exception {
            if (n < 0 || Math.floor(n) != n) {
                throw new ArithmeticException("Faktorial hanya untuk bilangan bulat tak-negatif");
            }
            if (n > 170) {
                throw new ArithmeticException("Nilai terlalu besar untuk faktorial");
            }
            double result = 1.0;
            for (int i = 2; i <= (int) n; i++) {
                result *= i;
            }
            return result;
        }
    }
}
