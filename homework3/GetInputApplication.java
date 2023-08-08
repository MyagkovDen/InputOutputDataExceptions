package homework3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class GetInputApplication {
    public static void main(String[] args) {
        getInputDataFromUser();
    }

    public static void getInputDataFromUser() {
        Scanner iScanner = new Scanner(System.in);
        System.out.println("Введите через пробел следующие данные: \n" +
                "- фамилия, имя, отчество, \n" +
                "- дата рождения (в формате dd.mm.yyyy), \n" +
                "- номер телефона (только цифры),\n" +
                "- пол (в формате 'f'/'m') : ");
        String[] inputData = iScanner.nextLine().split(" ");
        try {
            checkDataOnNumber(inputData);
        } catch (DataWrongNumberException e) {
            System.out.println(e.getMessage());
        }
        String[] output = null;
        try {
            output = parseInputData(inputData);
        } catch (DataWrongNumberException | DataNameFormatException | DataDateFormatException |
                 DataPhoneFormatException | DataWrongSexException e1) {
            System.out.println(e1.getMessage());
            return;
        }
        String filename = null;
        try {
            filename = saveDataToFile(output);
        } catch (IOException e) {
            System.out.println("Не удалось записать данные в файл! " + e.getMessage());
        }
        System.out.println("Данные успешно сохранены в файл " + filename);
    }

    public static String saveDataToFile(String[] output) throws IOException {
        File record = new File(output[0]);
        try (FileWriter fw = new FileWriter(record, true)) {
            for (int i = 0; i < output.length; i++) {
                fw.append("<").append(output[i]).append(">");
            }
            fw.append("\n");
        }
        return record.getName();
    }

    public static boolean checkDataOnNumber(String[] str) throws DataWrongNumberException {
        if (str.length == 6) {
            return true;
        } else {
            throw new DataWrongNumberException(str.length);
        }
    }

    public static String[] parseInputData(String[] input)
            throws DataNameFormatException, DataPhoneFormatException,
            DataDateFormatException, DataWrongSexException {
        String[] data = new String[input.length];
        for (int i = 0; i < input.length; i++) {
            data[i] = input[i].trim();
        }
        String name = "";
        String birthDate = "-1";
        String phoneNumber = "-1";
        String sex = "-1";
        for (String s : data) {
            if (s.contains(".")) {
                if (parseDate(s)) {
                    birthDate = s;
                }
            } else if (s.length() == 1) {
                if (parseSex(s)) {
                    sex = s;
                }
            } else if (Character.isDigit(s.charAt(0))) {
                if (parsePhone(s)) {
                    phoneNumber = s;
                }
            } else {
                if (parseName(s)) {
                    name += s + " ";
                }
            }
        }
        String[] nameArray = name.split(" ");
        String[] outputData = new String[6];
        outputData[0] = nameArray[0];
        outputData[1] = nameArray[1];
        outputData[2] = nameArray[2];
        outputData[3] = birthDate;
        outputData[4] = phoneNumber;
        outputData[5] = sex;

        return outputData;
    }

    public static boolean parseName(String s) throws DataPhoneFormatException {
        for (Character c : s.toCharArray()) {
            if (!Character.isLetter(c)) {
                throw new DataNameFormatException(s);
            }
        }
        return true;
    }

    public static boolean parsePhone(String s) throws DataPhoneFormatException {
        for (char c : s.toCharArray()) {
            if (!Character.isDigit(c)) {
                throw new DataPhoneFormatException(s);
            }
        }
        return true;
    }

    public static boolean parseDate(String s) throws DataDateFormatException {
        LocalDate date = null;
        try {
            String[] strDate = s.split("\\.");
            int[] intDate = new int[3];
            for (int i = 0; i < 3; i++) {
                intDate[i] = Integer.parseInt(strDate[i]);
            }
            date = LocalDate.of(intDate[2], intDate[1], intDate[0]);
        } catch (Exception e) {
            throw new DataDateFormatException(s, e.getMessage());
        }
        return true;
    }

    public static boolean parseSex(String s) throws DataWrongSexException {
        char c = s.charAt(0);
        if ((c == 'f') || (c == 'm')) {
            return true;
        } else {
            throw new DataWrongSexException(c);
        }
    }
}

class DataNameFormatException extends RuntimeException {
    public DataNameFormatException(String s) {
        System.out.println("Некорректно введенo имя! Допускается ввод только " +
                "букв. Введанные данные: " + s);
    }
}

class DataPhoneFormatException extends NumberFormatException {
    public DataPhoneFormatException(String s) {
        System.out.println("Некорректно введен номер телефона! Допускается ввод только " +
                "целочисленных значений. Введанные данные: " + s);
    }
}

class DataWrongSexException extends RuntimeException {
    public DataWrongSexException(char c) {
        System.out.println("Некорректно введен пол! Требуемый формат: f / m, " +
                "введенный формат: " + c);
    }
}

class DataDateFormatException extends RuntimeException {
    public DataDateFormatException(String s, String st) {
        super("Некорректно введена дата рождения! " + st + " Требуемый формат: dd.mm.yyyy, " +
                "введенный формат: " + s);
    }
}

class DataWrongNumberException extends RuntimeException {
    public DataWrongNumberException(int n) {
        super("Вы ввели неверное количество данных: " +
                "требуется ввести 6 полей, введено - " + n);
    }
}
