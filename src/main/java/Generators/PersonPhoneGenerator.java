package Generators;

import Entities.Person;
import com.opencsv.CSVReader;
import util.hammer;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class PersonPhoneGenerator {

    public static void main(String []args) throws IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException {

        ArrayList<Object> persons= new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("/home/morteza/Documents/Dataset/Person.csv"))) {
            List<String[]> r = reader.readAll();
            for (int i=1;i<r.size();i++)
            {
                Person p=new Person();
                p.ID=r.get(i)[0];
                p.FirstName=r.get(i)[1];
                p.LastName=r.get(i)[2];
                p.Sex=r.get(i)[3];
                p.DateOfBirth=r.get(i)[5];
                persons.add(p);
            }
        }

        try (CSVReader reader = new CSVReader(new FileReader("/home/morteza/Documents/Dataset/address.csv"))) {
            List<String[]> r = reader.readAll();
            for (int i=1;i<r.size();i++)
            {
                ((Person)persons.get(i-1)).Country=r.get(i)[0];
                ((Person)persons.get(i-1)).PostalCode=r.get(i)[1];
                ((Person)persons.get(i-1)).Street=r.get(i)[2];
                ((Person)persons.get(i-1)).City=r.get(i)[3];
            }
        }

        hammer.writeToFile("/home/morteza/Documents/Dataset/new.csv",persons);
    }
}
