package HelperClasses.InputLineFilters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntLineChecker implements SearchLineErrors {
    private static final Pattern patternWhiteSpaceChar = Pattern.compile("\\s");
    private static final Pattern patternOnlyDigit = Pattern.compile("[0-9]]");

    @Override
    public int chek(String line) {
        if (!(line == null)) {
            Matcher matcherWhiteSpaceChar = patternWhiteSpaceChar.matcher(line);
            Matcher matcherOnlyDigit = patternOnlyDigit.matcher(line);
            if (matcherWhiteSpaceChar.find() || line.equals("") || matcherOnlyDigit.find()) {
                return 1;
            } else {
                try {
                    Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    return 1;
                }
            }
        }
        return 0;
    }
}

