package Generators;

import Entities.Person;
import Entities.Phone;
import com.opencsv.CSVReader;
import util.hammer;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PersonPhoneGenerator {

    public static void main(String []args) throws IOException {

        ArrayList<Object> persons= new ArrayList<>();
        ArrayList<Object> phones= new ArrayList<>();
        HashMap<String,Person> personsByID=new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader("/home/morteza/Documents/Dataset/old data/Person.csv"))) {
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

        try (CSVReader reader = new CSVReader(new FileReader("/home/morteza/Documents/Dataset/old data/address.csv"))) {
            List<String[]> r = reader.readAll();
            for (int i=1;i<r.size();i++)
            {
                ((Person)persons.get(i-1)).Country=r.get(i)[0];
                ((Person)persons.get(i-1)).PostalCode=r.get(i)[1];
                ((Person)persons.get(i-1)).Street=r.get(i)[2];
                ((Person)persons.get(i-1)).City=r.get(i)[3];
                personsByID.put(((Person)persons.get(i-1)).ID,(Person)persons.get(i-1));
            }
        }

        try (CSVReader reader = new CSVReader(new FileReader("/home/morteza/Documents/Dataset/old data/Phone.csv"))) {
            List<String[]> r = reader.readAll();
            for (int i=1;i<r.size();i++)
            {
                Phone phone=new Phone();
                String ID=r.get(i)[0];
                Person person=personsByID.get(ID);
                phone.Type=r.get(i)[1];
                phone.Number=r.get(i)[2];
                phone.FirstName=person.FirstName;
                phone.LastName=person.LastName;
                phone.DateOfBirth=person.DateOfBirth;
                phones.add(phone);
            }
        }

        hammer.writeToFile("/home/morteza/Documents/Dataset/Person.csv",persons);
        hammer.writeToFile("/home/morteza/Documents/Dataset/Phone.csv",phones);
    }
}
