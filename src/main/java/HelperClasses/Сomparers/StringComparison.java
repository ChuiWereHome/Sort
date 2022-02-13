package HelperClasses.Сomparers;

public class StringComparison implements MyComparison {
    @Override
    public int comparison(String line1, String line2, boolean sortAscending) {
        if (line1 == null) {
            return 2;
        }
        if (line2 == null) {
            return 1;
        }
        if (line1.equals(line2)) {
            return 0;
        }
        char[] lineFirst = line1.toLowerCase().toCharArray();
        char[] lineSecond = line2.toLowerCase().toCharArray();
        int countChar = 0;
        for (int j = 0; j < lineFirst.length; j++) {
            if (lineFirst[countChar] == lineSecond[countChar]) {
                if (countChar == lineSecond.length - 1) {
                    return sortAscending ? 2 : 1;
                }
                if (countChar == lineFirst.length - 1) {
                    return sortAscending ? 1 : 2;
                }
                countChar++;
                continue;
            }
            if (lineFirst[countChar] > lineSecond[countChar]) {
                return sortAscending ? 2 : 1;
            } else {
                return sortAscending ? 1 : 2;
            }
        }
        return 0;
    }
}
