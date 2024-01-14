import java.util.*;
import java.io.*;
import java.nio.charset.*;

class B {
  public static void main(String args[]) {
    try {
      InputStreamReader in = new InputStreamReader(System.in, "UTF-8");
      PrintStream out = new PrintStream(System.out, true, "UTF-8");
      Scanner sc = new Scanner(in);
      StringBuilder sb = new StringBuilder("\"");
      parseInput(sb, sc);
      sb.append("\"\n");
      out.print(sb.toString());
      System.exit(0);
    } catch (Exception e) {
      System.out.print("Stream encoding is invalid\n");
    }
  }

  private static void parseInput(StringBuilder sb, Scanner sc) {
    sc.useDelimiter("\"\n\"");
    int lineIndex = 0;

    while (sc.hasNext()) {
      String stringInput = sc.next();

      stringInput = returnValidFirstToken(stringInput, lineIndex);
      stringInput = returnValidLastToken(stringInput, lineIndex, sc);

      if (stringInput.endsWith("\n") || !isAcceptable(stringInput)) {
        System.out.print("unacceptable input\n");
        System.exit(-1);
      }
      char unicodeChar = stringInput.charAt(0);
      sb.append(unicodeChar);
      lineIndex += 1;
    }
  }

  private static String returnValidFirstToken(String stringInput, int lineIndex) {
    if (stringInput.length() == 2 && lineIndex == 0 && stringInput.startsWith("\"")) {
      stringInput = stringInput.substring(1);
    }
    return stringInput;
  }

  private static String returnValidLastToken(String stringInput, int lineIndex, Scanner sc) {
    if (lineIndex != 0 && stringInput.length() == 2 && stringInput.endsWith("\"") && !sc.hasNext("\"\n\"")) {
      stringInput = stringInput.substring(0, stringInput.length() - 1);
    }
    return stringInput;
  }

  private static boolean isAcceptable(String input) {
    if (input.length() != 1) {
      return false;
    }

    char unicodeChar = input.charAt(0);
    return isAcceptableUnicode(unicodeChar);
  }

  private static boolean isAcceptableUnicode(char unicodeChar) {
    List<Character> acceptable = new ArrayList<>(Arrays.asList('\u2510', '\u2518', '\u2514',
        '\u250C'));
    Boolean isAcceptable = false;

    for (int i = 0; i < acceptable.size(); i += 1) {
      isAcceptable = isAcceptable || unicodeChar == acceptable.get(i);
    }
    return isAcceptable;
  }

}