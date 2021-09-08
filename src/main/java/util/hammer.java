package util;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class hammer {


    public static void writeToFile(String pathToFile, ArrayList<Object> objects)
    {
        Class<?> objClass = objects.get(0).getClass();

        ArrayList<String> temp=new ArrayList<>();
        for(Field field : objClass.getDeclaredFields()) {
            temp.add(field.getName());
        }

        String []header=new String[temp.size()];
        int j=0;
        for (String t : temp) {
            header[j++] = t;
        }

        File file = new File(pathToFile);
        try {
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile, ',',
                    CSVWriter.NO_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END);

            writer.writeNext(header);

            for (Object obj:objects) {
                String[] row=new String[header.length];

                for (int i=0;i<header.length;i++)
                {
                    Field f = objClass.getDeclaredField(header[i]);
                    f.setAccessible(true);
                    row[i] = (String) f.get(obj);
                }
                writer.writeNext(row);
            }
            writer.close();
        }
        catch (IOException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
