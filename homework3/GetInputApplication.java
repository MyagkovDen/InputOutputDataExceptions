package homework3;

import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

public class GetInputApplication {
    public static void main(String[] args) {
        getInputDataFromUser();
//        String s = "08.08.2023";
//        LocalDate date = null;
//        String[] strDate = s.split("\\.");
//        int[] intDate = new int[3];
//        for (int i = 0; i < 3; i++) {
//            intDate[i] = Integer.parseInt(strDate[i]);
//        }
//        date = LocalDate.of(intDate[2], intDate[1], intDate[0]);
//        System.out.println(date);
    }


    public static void getInputDataFromUser() {
        Scanner iScanner = new Scanner(System.in);
        System.out.println("Введите через пробел следующие данные: \n" +
                "- фамилия, имя, отчество, \n" +
                "- дата рождения (в формате dd.mm.yyyy), \n" +
                "- номер телефона (только цифры),\n" +
                "- пол (в формате 'f'/'m') : ");
        String[] inputData = iScanner.nextLine().split(" ");
        if (!checkDataOnNumber(inputData)) return;
        try {
            parseInputData(inputData);
        }catch (Exception e){

        }

    }

    public static boolean checkDataOnNumber(String[] str) {
        if (str.length == 6) {
            return true;
        } else {
            try {
                throw new DataWrongNumberException(str.length);
            } catch (DataWrongNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    public static void parseInputData(String[] input) {
        String[] data = new String[input.length];
        for (int i = 0; i < input.length; i++) {
            data[i] = input[i].trim();
        }
        StringBuilder name = new StringBuilder();
        LocalDate birthdate = null;
        String phoneNumber = "-1";
        char sex = '0';
        for (String s : data) {
            if (s.contains(".")) {
                birthdate = parseDate(s);
            } else if (s.length() == 1) {
                sex = parseSex(s);
            } else if (Character.isDigit(s.charAt(0))) {
                phoneNumber = s;
            } else {
                name.append(s).append(" ");
            }
        }
        System.out.println(name + " " + birthdate + " " + phoneNumber + " " + sex);
    }

//        public static int parsePhoneNumber (String s){
//            int phoneNumber = -1;
//            try {
//                phoneNumber = Integer.parseInt(s);
//            } catch (NumberFormatException e) {
//                try {
//                    System.out.println(e.getMessage());
//                    throw new DataPhoneFormatException(s);
//                } catch (DataPhoneFormatException ex) {
//                    System.out.println(ex.getMessage());
//                }
//            }
//            return phoneNumber;
//        }


    public static LocalDate parseDate(String s) {
        LocalDate date = null;
        try {
            String[] strDate = s.split("\\.");
            int[] intDate = new int[3];
            for (int i = 0; i < 3; i++) {
                intDate[i] = Integer.parseInt(strDate[i]);
            }
            date = LocalDate.of(intDate[2], intDate[1], intDate[0]);
        } catch (Exception e) {
            try {
                throw new DataDateFormatException(s, e.getMessage());
            } catch (DataDateFormatException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return date;
    }

    public static Character parseSex(String s) {
        char c = s.charAt(0);
        if (c == 'f') {
            return 'f';
        } else if (c == 'm') {
            return 'm';
        } else {
            try {
                throw new DataWrongSexException(c);
            } catch (DataWrongSexException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
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

class DataWrongNumberException extends RuntimeException {
    public DataWrongNumberException(int n) {
        super("Вы ввели неверное количество данных: " +
                "требуется ввести 6 полей, введено - " + n);
    }
}

class DataDateFormatException extends RuntimeException {
    public DataDateFormatException(String s, String st) {
        super("Некорректно введена дата рождения! " + st + " Требуемый формат: dd.mm.yyyy, " +
                "введенный формат: " + s);
    }
}
