import static java.util.Optional.ofNullable;
import static java.util.function.Predicate.not;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pig Latin is a language game or argot in which English words are altered, usually by adding a
 * fabricated suffix or by moving the onset or initial consonant or consonant cluster of a word to
 * the end of the word and adding a vocalic syllable to create such a suffix.
 *
 * <p>Write a simplified solution that implement the following rules:
 *
 * <p>For words that begin with consonant sounds, all letters before the initial vowel are placed at
 * the end of the word sequence. Then, "ay" is added, as in the following examples:
 *
 * <ul>
 *   <li>"pig" = "ig-pay"
 *   <li>"latin" = "atin-lay"
 *   <li>"banana" = "anana-bay"
 *   <li>"will" = "ill-way"
 * </ul>
 *
 * <p>When words begin with consonant clusters, the whole sound is added to the end when speaking or
 * writing.
 *
 * <ul>
 *   <li>"smile" = "ile-smay"
 *   <li>"string" = "ing-stray"
 *   <li>"stupid" = "upid-stay"
 * </ul>
 *
 * <p>For words that begin with vowel sounds, just only a syllable 'way' is appended to the end:
 *
 * <ul>
 *   <li>"eat" = "eat-way"
 *   <li>"omelet" = "omelet-way"
 *   <li>"are" = "are-way"
 * </ul>
 *
 * <p>Add a "-" separator between modified word and suffix for convenience. Letter cases should be
 * preserved, and all the punctuation should be untouched. So, e.g. the phrase:<br>
 * <code>I've been wondering - what is the answer?!?!? How will we know it's correct?</code> <br>
 * should be transformed to:<br>
 * <code>
 * I've-way een-bay ondering-way - at-whay is-way e-thay answer-way?!?!? ow-Hay ill-way e-way ow-knay it's-way orrect-cay?
 * </code>
 */
public class PigLatin {

  private static final Pattern WORDS = Pattern.compile("\\w+([a-zA-Z0-9_']+\\w+)?");
  private static final Pattern CONSONANTS =
      Pattern.compile("^([bcdfghjklmnpqrstvwxzBCDFGHJKLMNPQRSTVWXZ']+)(.*)$");
  private static final Pattern VOWELS = Pattern.compile("^[aeiouyAEIOUY']+.*$");

  public static String translate(String phrase) {
    if (ofNullable(phrase).filter(not(String::isEmpty)).isEmpty()) return phrase;

    var result = phrase;
    final Matcher m = WORDS.matcher(phrase);
    while (m.find()) {
      final String word = m.group(0);
      result = result.replaceAll("\\b" + word + "\\b", toPigLatin(word));
    }
    return result;
  }

  private static String toPigLatin(String word) {
    return Optional.of(CONSONANTS.matcher(word))
        .filter(Matcher::matches)
        .map(m -> m.group(2) + "-" + m.group(1) + "ay")
        .or(
            () ->
                Optional.of(VOWELS.matcher(word))
                    .filter(Matcher::matches)
                    .map(m -> m.group(0) + "-way"))
        .orElse(word);
  }

  @SuppressWarnings("java:S106")
  public static void main(String[] args) {
    final String phrase =
        Utils.getArg(args, 0)
            .orElse("I've been wondering - what is the answer?!?!? How will we know it's correct?");
    System.out.println("phrase: " + phrase);
    System.out.println("result: " + translate(phrase));
  }
}
