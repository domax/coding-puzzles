import static java.util.Comparator.comparing;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * Create a calculator that computes a net salary from given gross one.
 *
 * <p>The result depends on pre-configured list of gross brackets where each bracket defines a tax
 * rate, like this:
 *
 * <ol>
 *   <li>0k - 10k -> 0%
 *   <li>10k - 20k -> 20%
 *   <li>20k - 40k -> 40%
 *   <li>40k - Inf -> 50%
 * </ol>
 *
 * Keep in mind that these rates are applied not to whole gross sum, but only to part that is fits
 * to according bracket. Here are some examples of given gross to net calculations:
 *
 * <ul>
 *   <li>10k -> 0% -> 10k
 *   <li>15k -> 10k @ 0%, 5k @ 20% -> 10k + 4k -> 14k
 *   <li>25k -> 10k @ 0%, 10k @ 20%, 5k @ 40% -> 10k + 8k + 3k -> 21k
 * </ul>
 */
public class GrossToNet {

  public interface NetSalaryCalculator {
    double calculate(double grossSalary);
  }

  public record RateBracket(double low, double high, double rate) {}

  private static class NetSalaryCalculatorImpl implements NetSalaryCalculator {

    private final List<RateBracket> rateBrackets;

    private NetSalaryCalculatorImpl(Collection<RateBracket> rateBrackets) {
      this.rateBrackets = rateBrackets.stream().sorted(comparing(RateBracket::low)).toList();
    }

    @Override
    public double calculate(double grossSalary) {
      var netSalary = 0d;
      var grossLeft = grossSalary;
      for (final var bracket : rateBrackets) {
        final var kf = 1d - bracket.rate;
        if (grossSalary > bracket.high) {
          final var grossDiff = bracket.high - bracket.low;
          grossLeft -= grossDiff;
          netSalary += grossDiff * kf;
        } else {
          netSalary += grossLeft * kf;
          break;
        }
      }
      return netSalary;
    }
  }

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    final NetSalaryCalculator calculator =
        new NetSalaryCalculatorImpl(
            List.of(
                new RateBracket(0d, 10_000d, 0d),
                new RateBracket(10_000d, 20_000d, 0.2d),
                new RateBracket(20_000d, 40_000d, 0.4d),
                new RateBracket(40_000d, Double.MAX_VALUE, 0.5d)));
    Stream.of(10_000d, 15_000d, 25_000d, 50_000d)
        .forEach(v -> System.out.printf("gross: %.2f; net: %.2f%n", v, calculator.calculate(v)));
  }
}
