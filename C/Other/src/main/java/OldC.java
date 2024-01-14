import java.io.Reader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import org.json.JSONObject;
import java.util.Scanner;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.util.Pair;

class OldC {

  /*
  1. Read in input
  2. Parse input -> List of unicode characters
  3. Print output
  4. Close scanners and readers
   */
  public static void main(String[] args) {
    try {
      Reader inputStreamReader = new InputStreamReader(System.in, "UTF-8");
      PrintStream printStream = new PrintStream(System.out, true, "UTF-8");
      StringBuilder responseStrBuilder = new StringBuilder();
      Scanner scanner = new Scanner(inputStreamReader);
      List<String> unicodeResultList = returnUnicodeResultList(scanner);
      responseStrBuilder.append(Arrays.toString(unicodeResultList.toArray()));
      printStream.println(responseStrBuilder.toString());
    } catch (Exception e) {
      System.out.println("There was an error");
      System.out.println(e.toString());
    }
  }

  private static List<String> returnUnicodeResultList(Scanner scanner) {
    List<String> unicodeResultList = new ArrayList<>();
    scanner.useDelimiter("}");

    while (scanner.hasNext()) {
      String inputStr = scanner.next();

      if (inputStr.trim().length() != 0) {
        inputStr += "}";
        JSONObject jsonResult = new JSONObject(inputStr);
        char convertedUnicode = getConvertedUnicode(jsonResult);
        String unicodeAsString = "\"" + convertedUnicode + "\"";
        unicodeResultList.add(unicodeAsString);
      }
    }

    return unicodeResultList;
  }

  private static char getConvertedUnicode(JSONObject jsonObject) {
    HashMap<Pair<String, String>, Character> jsonToUnicode = new HashMap<>();
    jsonToUnicode.put(new Pair("LEFT", "UP"), '\u2518');
    jsonToUnicode.put(new Pair("LEFT", "DOWN"), '\u2510');
    jsonToUnicode.put(new Pair("RIGHT", "UP"), '\u2514');
    jsonToUnicode.put(new Pair("RIGHT", "DOWN"), '\u250C');

    String horizontal = jsonObject.getString("horizontal");
    String vertical = jsonObject.getString("vertical");
    return jsonToUnicode.get(new Pair(horizontal, vertical));
  }
}