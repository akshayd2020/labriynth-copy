import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.concurrent.*;

/**
 * Given an input and output stream, delegate to JsonToGameState
 */
public class Communication {
     public static void main(String[] args) {
          client(args);
          //System.exit(0);
     }

     private static void server(String[] args) {
          try {
               int portNum = Integer.parseInt(args[0]);
               new JsonToServer(System.in, System.out, portNum).parseAndPrint();
          } catch (UnsupportedEncodingException e) {
               System.out.println("Cannot read in input: " + e.getMessage());
          }
     }

     private static void client(String[] args) {
          try {
               int portNum = Integer.parseInt(args[0]);
               Optional<String> optionalIp = args.length == 2 ? Optional.of(args[1]) : Optional.empty();
               new JsonToClient(System.in, portNum, optionalIp).parseAndConnect();
          } catch (UnsupportedEncodingException e) {
               System.out.println("Cannot read in input: " + e.getMessage());
          }
     }
}