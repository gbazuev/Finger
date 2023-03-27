package HWm4t2n6;

public class RequestValidator {

    public String validateDate(String date) {
        return checkDateLength(validateDateSeparators(date));
    }


    private String validateDateSeparators(String date)    {
        StringBuilder builder = new StringBuilder(date);
        char[] separators = {'.', '-', '\\', '_', '|'};

        for (int i = 0; i < builder.length(); i++) {
            for (char separator : separators)   {
                if (builder.charAt(i) == separator)    {
                    int start = builder.indexOf(String.valueOf(separator));
                    builder.replace(start, start + 1, "/");
                }
            }
        }
        return builder.toString();
    }

    private boolean checkDateFormat(String date)   {
        String year = date.substring(date.lastIndexOf('/') + 1);
        return year.length() == 4;
    }

    private String checkDateLength(String date) {
        StringBuilder builder = new StringBuilder(date);
        if (builder.substring(0, builder.indexOf("/")).length() != 2) {
            builder.insert(0, '0');
        }
        if (builder.substring(builder.indexOf("/") + 1, builder.lastIndexOf("/")).length() != 2) {
            builder.insert(builder.indexOf("/") + 1, '0');
        }
        return builder.toString();
    }

    public void printCurrencyException(String currency, boolean status) {
        if (status) System.err.println("\"" + currency + "\" - некорректное название валюты!");
        else System.err.println("\"" + currency + "\" - incorrect currency name!");
    }

    public void printLanguageException(String language, boolean status)     {
        if (status) System.err.println("\"" + language + "\" - некорректное название языка!");
        else System.err.println("\"" + language + "\"");
    }

    public void printDateException(String date, boolean status) {
        if (status) System.err.println("\"" + date + "\" - некорректный ввод даты");
       else System.err.println("\"" + date + "\"incorrect date typing!");
    }

    public void printRequestException(String request, boolean status)   {
        if (status) System.err.println("\"" + request + "\" - некорректный ввод запроса!");
        else System.err.println("\"" + request + "\" - incorrect request!");
    }
}
