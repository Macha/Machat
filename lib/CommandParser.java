package machat.lib;

/**
 * This class contains utility methods for parsing strings
 * @author Macha <macha.hack@gmail.com>
 */
public class CommandParser {
    /**
     * This breaks up a recieved command into the command name, and it's parameters.
     * @param command The command to parse.
     * @return An array of strings with the command in position 0, and the
     * parameters in the command as the other elements of the array.
     */
    public static String[] parseRecievedCommand(String command) {
        if(command.charAt(0) == '/') {
            command = command.substring(1);
            String[] commandArray = command.split(":");
            return commandArray;
        }
        return null;
    }
    /**
     * Internal helper method to replace variable lengths strings with other
     * variable length strings. The Java API method only works on chars.
     *
     * @param original The string to perform the replace operation on.
     * @param find The string to be replaced.
     * @param replace The string that should be inserted instead of the one
     * specified by find.
     * @return The modified String.
     */
    private static String replace(String original, String find, String replace) {
        if (original == null)  return original;
        if (find == null)  return original;
        if (replace == null)  replace = "";

        int found = original.indexOf( find );
        while (found != -1) {
            original = original.substring(0,found) + replace + original.substring(found+find.length());
            found += replace.length();
            found = original.indexOf( find, found );
         }
        return original;
    }
}
