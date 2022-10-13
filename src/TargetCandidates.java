import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * For given list of integer values named "candidates", write a solution to generate all possible
 * unique combinations of elements that have a target sum.
 *
 * <p>E.g. for given candidates: [10, 1, 2, 7, 1, 6, 2, 5] and target sum 8, the result should be
 * like:
 *
 * <pre><code>
 * [[1, 2, 5], [2, 1, 5], [2, 6], [1, 1, 6], [1, 7], [7, 1], [6, 2]]
 * </code></pre>
 */
public class TargetCandidates {

  private final List<Integer> candidates;
  private final int target;

  TargetCandidates(List<Integer> candidates, int target) {
    this.candidates = candidates;
    this.target = target;
  }

  public Set<List<Integer>> get() {
    final Set<List<Integer>> result = ConcurrentHashMap.newKeySet();
    generateIndices(
            List.of(),
            idx ->
                Optional.of(idx.stream().map(candidates::get).toList())
                    .filter(v -> v.stream().mapToInt(Integer::intValue).sum() == target)
                    .ifPresent(result::add))
        .forEach(v -> {});
    return result;
  }

  private Stream<List<Integer>> generateIndices(
      List<Integer> idx, Consumer<List<Integer>> idxConsumer) {
    return Optional.of(idx.isEmpty() ? 0 : idx.get(idx.size() - 1) + 1)
        .map(
            start ->
                IntStream.range(start, candidates.size())
                    .mapToObj(i -> Stream.concat(idx.stream(), Stream.of(i)).toList())
                    .parallel()
                    .peek(idxConsumer)
                    .flatMap(v -> generateIndices(v, idxConsumer)))
        .orElseGet(Stream::empty);
  }

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    final IntFunction<Optional<String>> arg =
        i -> Optional.of(args).filter(a -> a.length > i).map(a -> a[i]);
    final var candidates =
        arg.apply(0)
            .map(a -> Arrays.stream(a.split("\\s*,\\s*")).map(Integer::parseInt).toList())
            .orElseGet(() -> List.of(10, 1, 2, 7, 1, 6, 2, 5));
    final int target = arg.apply(1).map(Integer::parseInt).orElse(8);
    System.out.println("candidates: " + candidates);
    System.out.println("target: " + target);
    System.out.println("result: " + new TargetCandidates(candidates, target).get());
  }
}
