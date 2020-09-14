package by.deathsmell.clientmanager.utils;

import org.fusesource.jansi.Ansi;

import java.util.regex.Pattern;

import static org.fusesource.jansi.Ansi.Color.*;
import static org.fusesource.jansi.Ansi.ansi;

public class StringColorizeUtil {

    // %[argument_index$][flags][width][.precision][t]conversion
    private static final String formatSpecifier
            = "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";

    private static final Pattern fsPattern = Pattern.compile(formatSpecifier);

    /**
     * Finds format specifiers in the format string.
     */
//    private List<Formatter> parse(String s) {
//        ArrayList<FixedString> al = new ArrayList<>();
//        Matcher m = fsPattern.matcher(s);
//        for (int i = 0, len = s.length(); i < len; ) {
//            if (m.find(i)) {
//                // Anything between the start of the string and the beginning
//                // of the format specifier is either fixed text or contains
//                // an invalid format string.
//                if (m.start() != i) {
//                    // Make sure we didn't miss any invalid format specifiers
//                    checkText(s, i, m.start());
//                    // Assume previous characters were fixed text
//                    al.add(new FixedString(s, i, m.start()));
//                }
//
//                al.add(new FormatSpecifier(s, m));
//                i = m.end();
//            } else {
//                // No more valid format specifiers.  Check for possible invalid
//                // format specifiers.
//                checkText(s, i, len);
//                // The rest of the string is fixed text
//                al.add(new FixedString(s, i, s.length()));
//                break;
//            }
//        }
//        return al;
//    }
    public static String setColor(String text, Ansi.Color textColor) {
        return setColor(text, textColor, DEFAULT);
    }

    public static String setColor(String text, Ansi.Color textColor, Ansi.Color backgroundColor) {
        Ansi result = ansi()
                .eraseScreen()
                .fg(textColor)
                .bg(backgroundColor)
                .a(text)
                .reset();
        return result.toString();
    }
}
