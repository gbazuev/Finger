package HWm4t2n6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Parser {
    static Scanner scanner = new Scanner(System.in);
    static Downloader loader = new Downloader();
    static RequestValidator validator = new RequestValidator();

    static CurrencyStorage storage;

    static {
        try {
            storage = new CurrencyStorage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static ResponseParser parser = new ResponseParser();
    static Url url = new Url();
    static boolean status = true;

    public static void main(String[] args) throws Exception {
        printDescription();

        while (true) {
            String[] message = scanner.nextLine().split(":\\s");

            switch (message[0]) {
                case "--get --today" -> printToday(message);
                case "--get --date" -> printDesiredDate(message);
                case "--get --interval" -> printInterval(message);
                case "--exit" -> {
                    System.out.println("Parser has been stopped! Goodbye:)");
                    return;
                }
                case "--language" -> changeLanguage(message);
                case "--help" -> printDescription();
                default -> validator.printRequestException(Arrays.toString(message), status);
            }
        }
    }

    static void printDescription() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("ParserDescription.txt"));
        for (int i = 1; i < 18; i++) {
            System.out.println(reader.readLine());
        }
        reader.close();
    }

    static void printToday(String[] message) throws Exception {
        parser.setResponse(loader.downloadWebPage(url.buildTodayUrl()));

        if (message.length == 2) {
            String[] arguments = message[1].split(",\\s");
            for (String arg : arguments) {
                String id = storage.getCurrencyId(arg.toUpperCase(), status);

                if (id == null) {
                    validator.printCurrencyException(arg, status);
                    continue;
                }

                System.out.println(parser.getNominal(id) + " " + arg + " = " + parser.getValue(id));
            }
        } else {
            for (int i = 0; i < storage.getNamespaceSize(status); i++) {
                String name = storage.getName(i, status);
                String id = storage.getCurrencyId(name.toUpperCase(), status);
                String value = parser.getValue(id);
                if (value.equals("NOT STATED")) continue;
                System.out.println(parser.getNominal(id) + " " + name + " = " + value);
            }
        }

    }

    static void printDesiredDate(String[] message) throws Exception {
        String[] arguments = message[1].split(",\\s");

        try {
            parser.setResponse(loader.downloadWebPage(url.buildUrlWithDate(validator.validateDate(arguments[0]))));
            if (parser.response.contains("Error in parameters")) {
                validator.printDateException(arguments[0], status);
                return;
            }
        } catch (Exception e) {
            validator.printDateException(arguments[0], status);
            return;
        }

        if (arguments.length == 1) {
            for (int i = 0; i < storage.getNamespaceSize(status); i++) {
                String name = storage.getName(i, status);
                String id = storage.getCurrencyId(name.toUpperCase(), status);
                String value = parser.getValue(id);

                if (value.equals("NOT STATED")) continue;

                System.out.println(parser.getNominal(id) + " " + name + " = " + parser.getValue(id));
            }
        } else if (arguments.length >= 2) {
            for (int i = 1; i < arguments.length; i++) {
                String id = storage.getCurrencyId(arguments[i].toUpperCase(), status);

                if (id == null) {
                    validator.printCurrencyException(arguments[i], status);
                    continue;
                }

                System.out.println(parser.getNominal(id) + " " + arguments[i] + " = " + parser.getValue(id));
            }
        }
    }

    static void printInterval(String[] message) throws Exception {
        String[] arguments = message[1].split(",\\s");
        String id = storage.getCurrencyId(arguments[2].toUpperCase(), status);

        if (id == null) {
            validator.printCurrencyException(arguments[2], status);
            return;
        }

        try {
            parser.setResponse(loader.downloadWebPage(url.buildUrlWithRange(validator.validateDate(arguments[0]), validator.validateDate(arguments[1]), id)));
            if (parser.response.contains("Error in parameters")) {
                validator.printDateException(Arrays.toString(new String[]{arguments[0], arguments[1]}), status);
                return;
            }
        } catch (Exception e) {
            validator.printDateException(Arrays.toString(new String[]{arguments[0], arguments[1]}), status);
            return;
        }

        System.out.println(parser.getNominal(id) + " " + arguments[2]);
        while (true) {
            try {
                System.out.println(parser.getRangeDate(id) + " = " + parser.getValue(id));
                parser.clearResponseRecord();
            } catch (Exception e)   { return; }
        }
    }

    static void changeLanguage(String[] message) throws Exception {
        if (message[1].contains("ru") || message[1].contains("Ru")) status = true;
        else if (message[1].contains("en") || message[1].contains("En")) status = false;
        else validator.printLanguageException(message[1], status);
    }
}
