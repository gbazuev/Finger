package HWm4t2n6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class CurrencyStorage {
    HashMap<String, String> storageRussian = new HashMap<>();
    HashMap<String, String> storageEnglish = new HashMap<>();
    ArrayList<String> rusNames = new ArrayList<>();
    ArrayList<String> engNames = new ArrayList<>();

    public CurrencyStorage() throws IOException {
        StringBuilder file = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader("Test.json"));
        for (int i = 1; i < 546; i++) {
            file.append(reader.readLine());
        }

        reader.close();

        for (int i = 1; i < 69; i++) {
            try {
                String current = file.substring(file.indexOf(String.valueOf(i)), file.indexOf("]"));

                String rusName = current.substring(current.indexOf("name_ru") + 11, current.indexOf(",") - 1);
                String engName = current.substring(current.indexOf("name_eng") + 12, current.indexOf("id") - 3);
                String id = current.substring(current.indexOf("id") + 6, current.lastIndexOf("\""));

                rusNames.add(rusName);
                engNames.add(engName);

                storageRussian.put(rusName.toUpperCase(), id);
                storageEnglish.put(engName.toUpperCase(), id);

                file.delete(file.indexOf(String.valueOf(i)) - 1, file.indexOf("]") + 2);
            } catch (Exception e) {
                System.err.println("Exception by storages loader. Cannot load storages");
                return;
            }
        }
    }

    public String getCurrencyId(String name, boolean status) {
        return status ? storageRussian.get(name) : storageEnglish.get(name);
    }

    public String getName(int index, boolean status) {
        return status ? rusNames.get(index) : engNames.get(index);
    }

    public int getNamespaceSize(boolean status) {
        return status ? rusNames.size() : engNames.size();
    }
}