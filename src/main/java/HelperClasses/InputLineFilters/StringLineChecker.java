package HelperClasses.InputLineFilters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringLineChecker implements SearchLineErrors {
    private static final Pattern patternWhiteSpaceChar = Pattern.compile("\\s");

    @Override
    public int chek(String line) {
        if (!(line == null)) {
            Matcher matcherWhiteSpaceChar = patternWhiteSpaceChar.matcher(line);
            if (matcherWhiteSpaceChar.find() || line.equals("")) {
                return 1;
            }
        }
        return 0;
    }
}