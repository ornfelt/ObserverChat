package communication;

/**
 *
 * @author jonas
 */
public class Encrypt extends TextDecorator {

    TextDecorator textMessage;

    public Encrypt(TextDecorator textMessage) {
        this.textMessage = textMessage;
    }

    //encodes to my super implementation: flip the message and surround with lower case "x", as well as make every other character to upper- and lower case
    public String getMessage() {
        this.message = textMessage.getMessage();
        String vowels = "aeyuoåäöi";
        String outString = "x";
        int length = message.length();

        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) {
                outString += message.toUpperCase().charAt(i);
            } else {
                outString += message.toLowerCase().charAt(i);
            }
        }
        outString += "x";

        String reversedOutString = "";
        //flip message
        for (int i = 0; i < outString.length(); i++) {
            reversedOutString += outString.charAt(outString.length()-1 - i);
        }
        return reversedOutString;
    }
}
