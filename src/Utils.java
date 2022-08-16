import java.util.Optional;

public final class Utils {

  private Utils() {
    throw new IllegalCallerException("Cannot instantiate utility class");
  }

  public static Optional<String> getArg(String[] args, int argIndex) {
    return Optional.of(args).filter(a -> a.length > argIndex).map(a -> a[argIndex]);
  }
}
