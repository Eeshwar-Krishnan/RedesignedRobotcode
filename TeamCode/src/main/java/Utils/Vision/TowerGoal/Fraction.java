package Utils.Vision.TowerGoal;

class Fraction {
        private int numerator, denominator;

        Fraction(double a, double b) {
            numerator = (int) (a / gcd(a, b));
            denominator = (int) (b / gcd(a, b));
        }
        private double gcd(double a, double b) {
            return b == 0 ? a : gcd(b, a % b);
        }

        public int getNumerator() {
            return numerator;
        }

        public int getDenominator() {
            return denominator;
        }
    }