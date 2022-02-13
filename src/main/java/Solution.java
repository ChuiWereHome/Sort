import HelperClasses.InputLineFilters.IntLineChecker;
import HelperClasses.InputLineFilters.SearchLineErrors;
import HelperClasses.InputLineFilters.StringLineChecker;
import HelperClasses.Сomparers.IntegerComparison;
import HelperClasses.Сomparers.MyComparison;
import HelperClasses.Сomparers.StringComparison;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Solution {
    private static boolean sortAscending = true; // Default sort method
    private static final ArrayList<String> inputFileName = new ArrayList<>();
    private static String resultFileName;
    private static MyComparison myComparison; //object for comparison
    private static SearchLineErrors lineChecker;  //line error search object

    public static void main(String[] args) {
        //validation of startup arguments
        argumentInit(args);

        //begin merge
        if (inputFileName.size() == 0) {
            System.out.println("Вы не ввели пути к файлам");
        }
        if (inputFileName.size() == 1) {
            Path tempFile = createTempFile();
            inputFileName.add(tempFile.toString());
        }
        while (inputFileName.size() > 1) {
            inputFileName.add(merge(inputFileName.get(0), inputFileName.get(1)));
        }

        //write to result file
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(inputFileName.get(0)));
             BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultFileName))) {
            while (bufferedReader.ready()) {
                bufferedWriter.write(bufferedReader.readLine());
                bufferedWriter.flush();
                if (bufferedReader.ready()) {
                    bufferedWriter.write("\n");
                }
            }
        } catch (IOException e) {
            System.out.println("Невозможно записать результаты в файл(Файл не доступен)");
        }
        System.out.println("Файлы отсортированы");
    }

    //merge method
    public static String merge(String file1, String file2) {
        Path tempFile = createTempFile();
        try (BufferedReader readerFile1 = new BufferedReader(new FileReader(file1));
             BufferedReader readerFile2 = new BufferedReader(new FileReader(file2));
             BufferedWriter tempFileWriter = new BufferedWriter(new FileWriter(String.valueOf(tempFile)))) {
            String line1 = readerFile1.readLine();
            String line2 = readerFile2.readLine();
            String tempLine1 = null;
            String tempLine2 = null;
            while (true) {
                //line error checking start
                if (line1 == null && line2 == null) {
                    break;
                }
                int firstResult = lineChecker.chek(line1);
                if (firstResult == 1) {
                    if (readerFile1.ready()) {
                        line1 = readerFile1.readLine();
                    } else {
                        line1 = null;
                    }
                    continue;
                }
                int secondResult = lineChecker.chek(line2);
                if (secondResult == 1) {
                    if (readerFile2.ready()) {
                        line2 = readerFile2.readLine();
                    } else {
                        line2 = null;
                    }
                    continue;
                }

                //skipping unsorted lines in a file
                if (myComparison.comparison(line1, tempLine1, sortAscending) == 1 && tempLine1 != null) {
                    line1 = readerFile1.readLine();
                    continue;
                }
                if (myComparison.comparison(line2, tempLine2, sortAscending) == 1 && tempLine2 != null) {
                    line2 = readerFile2.readLine();
                    continue;
                }

                //current element comparison
                if (myComparison.comparison(line1, line2, sortAscending) == 1) {
                    tempFileWriter.write(line1 + "\n");
                    tempLine1 = line1;
                    if (readerFile1.ready()) {
                        line1 = readerFile1.readLine();
                    } else {
                        line1 = null;
                    }
                } else {
                    tempFileWriter.write(line2 + "\n");
                    tempLine2 = line2;
                    if (readerFile2.ready()) {
                        line2 = readerFile2.readLine();
                    } else {
                        line2 = null;
                    }
                }

                //recording last lines
                if (!readerFile1.ready() && !readerFile2.ready()) {
                    if (line1 != null && !line1.equals("") && !(lineChecker.chek(line1) == 1)) {
                        tempFileWriter.write(line1 + "\n");
                    }
                    if (line2 != null && !line2.equals("") && !(lineChecker.chek(line2) == 1)) {
                        tempFileWriter.write(line2 + "\n");
                    }
                    tempFileWriter.flush();
                    break;
                }
                tempFileWriter.flush();
            }
        } catch (Exception e) {
            System.out.println("Ошибка чтения/записи в файл");
        }

        //clearing extra paths and deleting temporary files
        for (int i = 0; i < 2; i++) {
            if (inputFileName.get(0).contains("tempFile")) {
                clearTempFile(Paths.get(inputFileName.get(0)));
            }
            inputFileName.remove(0);
        }
        return tempFile.toString();
    }

    public static void argumentInit(String[] args) {
        if (args.length == 0) {
            System.out.println("Вы забыли ввести параметры,можете ввести их сейчас");
            try (BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in))) {
                String param = scanner.readLine();
                String[] tempArgs = param.split(" ");
                args = new String[tempArgs.length];
                for (int i = 0; i < tempArgs.length; i++) {
                    args[i] = tempArgs[i].trim();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (String arg : args) {
            switch (arg) {
                case "-d":
                    sortAscending = false;
                    break;
                case "-s":
                    myComparison = new StringComparison();
                    lineChecker = new StringLineChecker();
                    break;
                case "-i":
                    myComparison = new IntegerComparison();
                    lineChecker = new IntLineChecker();
                    break;
            }
        }

        //collect all file paths
        ArrayList<String> temp = new ArrayList<>();
        for (String fileName : args) {
            if (!fileName.startsWith("-") && !fileName.equals("")) {
                temp.add(fileName);
            }
        }

        //division into input and output
        resultFileName = temp.get(0);
        inputFileName.addAll(temp.subList(1, temp.size()));
    }

    private static Path createTempFile() {
        Path tempFile = null;
        try {
            tempFile = Files.createTempFile("tempFile", null);
        } catch (IOException e) {
            System.out.println("Ошибка при создании временного файла");
        }
        return tempFile;
    }

    public static void clearTempFile(Path file) {
        try {
            Files.delete(file);
        } catch (IOException e) {
            System.out.println("Невозможно удалить временные файлы");
        }
    }
}