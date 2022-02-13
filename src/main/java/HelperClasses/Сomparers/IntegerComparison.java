package HelperClasses.Сomparers;

public class IntegerComparison implements MyComparison {

    @Override
    public int comparison(String line1, String line2, boolean sortAscending) {
        if (line1 == null && line2 == null) {
            return -1;
        }
        if (line1 == null) {
            return 2;
        }
        if (line2 == null) {
            return 1;
        }
        //comparison for integer
        int first = Integer.parseInt(line1);
        int second = Integer.parseInt(line2);
        if (first > second) {
            return sortAscending ? 2 : 1;
        }
        if (first < second) {
            return sortAscending ? 1 : 2;
        }
        return 0;
    }

}
