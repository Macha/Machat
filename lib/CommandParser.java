/*
 *  Machat
 *
 *  Copyright (C) Macha <macha.hack@gmail.com>
 *
 *  This file is part of Machat.
 *
 *  Machat is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Machat is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Machat.  If not, see <http://www.gnu.org/licenses/>.
 */
package machat.lib;

import java.io.IOException;

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
     * @throws IOException 
     */
    public static String[] parseRecievedCommand(String command) throws IOException {
        if(command == null || command.length() == 0) {
        	return null;
        } else if(command.charAt(0) == '/') {
            command = command.substring(1);
            String[] commandArray = command.split(":");
            return commandArray;
        } else {
        	System.out.println(command);
        	throw new IOException();
        }
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

        int found = original.indexOf(find);
        while (found != -1) {
            original = original.substring(0,found) + replace + original.substring(found + find.length());
            found += replace.length();
            found = original.indexOf(find, found);
         }
        return original;
    }
}
