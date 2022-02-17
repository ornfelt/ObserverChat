package communication;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 *
 * @author jonas
 */
public class IO {

    public void printError(String text) {

        /*String filePath = "";
        PrintWriter textToWrite = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)));
        textToWrite.print(text + "\n");
        textToWrite.close(); */
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String formattedDate = myDateObj.format(myFormatObj);

        System.out.println("Datum: " + formattedDate + " felmeddelande: " + text);
    }

    public void printMessage(String name, String msg) {

        /*String filePath = "";
        PrintWriter textToWrite = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)));
        textToWrite.print(text + "\n");
        textToWrite.close(); */
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        String formattedDate = myDateObj.format(myFormatObj);

        System.out.println("Datum: " + formattedDate + " Meddelande av: " + name + ": " + msg);
    }

}
