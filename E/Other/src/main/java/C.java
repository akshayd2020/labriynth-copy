import java.io.UnsupportedEncodingException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.IOException;

import com.google.gson.JsonStreamParser;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javafx.util.Pair;

class C {
     InputStream inputStream;
     PrintStream printStream;
     Map<Pair<String, String>, Character> jsonToUnicode;

     public C(List<Pair<String, String>> jsons, List<Character> characters,
         InputStream inStream, OutputStream outStream) throws IOException {
          this.inputStream = inStream;
          this.printStream = new PrintStream(outStream, true, "UTF-8");
          this.jsonToUnicode = getJsonToUnicode(jsons, characters);
     }

     /*
     1. Read in input
     2. Parse input -> List of unicode characters
     3. Print output
     4. Close streans
      */
     public void start() throws IOException {
          List<String> unicodeResultList = parseInput();
          printOutput(unicodeResultList);
          close();
     }

     // Closes the connection
     public void close() throws IOException {
          this.printStream.close();
          this.inputStream.close();
     }
     /**
      * Read in input, Parse input, convert input to unicode strings
      * @return     list of unicode strings from input stream
      */
     private List<String> parseInput() throws UnsupportedEncodingException, IOException {
          JsonStreamParser jsonReader =
              new JsonStreamParser(new InputStreamReader(this.inputStream, "UTF-8"));
          List<String> unicodeResultList = new ArrayList<>();
          try {
               while (jsonReader.hasNext()) {
                    JsonObject json = jsonReader.next().getAsJsonObject();
                    convertToUnicode(unicodeResultList, json);
               }
          } catch (Exception e) {
               return new ArrayList<>();
          }
          return unicodeResultList;
     }

     /**
      * Converts token from scanner to unicode string
      * @param unicodeResultList   list to be printed
      * @param inputJson           next JSON object token
      */
     private void convertToUnicode(List<String> unicodeResultList, JsonObject inputJson) {
          char convertedUnicode = getUnicodeChar(inputJson);
          String unicodeAsString = "\"" + convertedUnicode + "\"";
          unicodeResultList.add(unicodeAsString);
     }

     /**
      * Convert JSON into unicode character
      * @param jsonObject     to be converted to unicode character
      * @return               unicode character
      */
     private char getUnicodeChar(JsonObject jsonObject) {
          String horizontal = jsonObject.get("horizontal").getAsString();
          String vertical = jsonObject.get("vertical").getAsString();
          return this.jsonToUnicode.get(new Pair(horizontal, vertical));
     }

     /**
      * Returns map of json pair representation to unicode characters
      * @param pairs      A list of pairs that represents JSON objects where the first string represents
      *                   the horizontal and the second string represents the vertical
      * @param characters List of unicode characters
      * @return           Map of json representation to unicode characters
      */
     static Map<Pair<String, String>, Character> getJsonToUnicode(List<Pair<String,
         String>> pairs,
         List<Character> characters) {
          Map<Pair<String, String>, Character> jsonToUnicode = new HashMap<>();
          for (int i = 0; i < pairs.size(); i += 1) {
               jsonToUnicode.put(pairs.get(i), characters.get(i));
          }
          return jsonToUnicode;
     }

     private void printOutput(List<String> unicodeResultList) throws IOException {
          StringBuilder responseStrBuilder = new StringBuilder();
          responseStrBuilder.append(Arrays.toString(unicodeResultList.toArray()));
          printStream.println(responseStrBuilder.toString());
     }
}