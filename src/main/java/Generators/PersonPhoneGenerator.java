package Generators;

import Entities.*;
import Entities.StoreEmployeeWorks;
import com.opencsv.CSVReader;
import util.hammer;

import java.io.*;
import java.util.*;

public class PersonPhoneGenerator {


    public static ArrayList<Object> persons= new ArrayList<>();
    public static ArrayList<Object> phones= new ArrayList<>();
    public static ArrayList<Object> storeOwners= new ArrayList<>();
    public static ArrayList<Object> storeEmployees= new ArrayList<>();
    public static ArrayList<Object> stores= new ArrayList<>();
    public static ArrayList<Object> storeEmployeeWorks= new ArrayList<>();

    public static void main(String []args) throws IOException {

        //String path="/home/morteza/Documents/Dataset/old data/Person.csv";
        String path="c:/morteza/3DB3 Dataset";


        HashMap<String,Person> personsByID=new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(path+"/old data/Person.csv"))) {
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
                //
            }
        }

        try (CSVReader reader = new CSVReader(new FileReader(path+"/old data/address.csv"))) {
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

        try (CSVReader reader = new CSVReader(new FileReader(path+"/old data/Phone.csv"))) {
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

        addStores();
        addStoreOwner(10);
        addStoreEmployee(70);
        addStoreRelations();



        hammer.writeToFile(path+"/Person.csv",persons);
        hammer.writeToFile(path+"/Phone.csv",phones);
        hammer.writeToFile(path+"/Store.csv",stores);
        hammer.writeToFile(path+"/StoreOwner.csv",storeOwners);
        hammer.writeToFile(path+"/StoreEmployee.csv",storeEmployees);
        hammer.writeToFile(path+"/StoreEmployeeWorks.csv",storeEmployeeWorks);
    }

    public static void addStoreRelations()
    {
        for (int i=0;i<stores.size();i++)
        {
            ((Store)stores.get(i)).OwnerFirstName=((StoreOwner)storeOwners.get(i)).FirstName;
            ((Store)stores.get(i)).OwnerLastName=((StoreOwner)storeOwners.get(i)).LastName;
            ((Store)stores.get(i)).OwnerDoB=((StoreOwner)storeOwners.get(i)).DateOfBirth;
        }

        for (Object employee: storeEmployees)
        {
            Random r=new Random();
            int storesWorksAt=r.nextInt(2)+1;
            Set<Integer> selected=new HashSet<>();
            for (int i=0;i<storesWorksAt;i++)
            {
                while (true) {
                    int index = r.nextInt(stores.size());
                    if (!selected.contains(index)) {
                        selected.add(index);
                        StoreEmployeeWorks stEmpWorks=new StoreEmployeeWorks();
                        stEmpWorks.EmployeeFirstName=((StoreEmployee)employee).FirstName;
                        stEmpWorks.EmployeeLastName=((StoreEmployee)employee).LastName;
                        stEmpWorks.EmployeeDoB=((StoreEmployee)employee).DateOfBirth;

                        stEmpWorks.StoreID=((Store)stores.get(index)).StoreID;

                        storeEmployeeWorks.add(stEmpWorks);
                        break;
                    }
                }
            }
        }
    }

    public static void addStoreOwner(int size)
    {
        Random r=new Random();
        Set<Integer> selected=new HashSet<>();
        for (int i=0;i<size;i++)
        {
            while (true)
            {
                int index=r.nextInt(persons.size());
                if(!selected.contains(index))
                {
                    selected.add(index);
                    StoreOwner owner=new StoreOwner();
                    owner.FirstName=((Person)persons.get(index)).FirstName;
                    owner.LastName=((Person)persons.get(index)).LastName;
                    owner.DateOfBirth=((Person)persons.get(index)).DateOfBirth;
                    owner.BusinessExpenses=String.valueOf(r.nextInt(1000000)+1500);
                    storeOwners.add(owner);
                    break;
                }
            }
        }
    }

    public static void addStoreEmployee(int size)
    {
        Random r=new Random();
        Set<Integer> selected=new HashSet<>();
        for (int i=0;i<size;i++)
        {
            while (true)
            {
                int index=r.nextInt(persons.size());
                if(!selected.contains(index))
                {
                    selected.add(index);
                    StoreEmployee employee=new StoreEmployee();
                    employee.FirstName=((Person)persons.get(index)).FirstName;
                    employee.LastName=((Person)persons.get(index)).LastName;
                    employee.DateOfBirth=((Person)persons.get(index)).DateOfBirth;
                    employee.Salary=String.valueOf(r.nextInt(20000)+2230);
                    employee.YearsOfService=String.valueOf(r.nextInt(15));
                    storeEmployees.add(employee);
                    break;
                }
            }
        }
    }

    public static void addStores()
    {
        Store st1=new Store();
        st1.StoreID="100";
        st1.Description="STK e-Shop";
        st1.Page="www.stuff.ca/STK_e-Shop";
        st1.StartDate="20180201";
        stores.add(st1);

        Store st2=new Store();
        st2.StoreID="101";
        st2.Description="MyHobbyStore Retail";
        st2.Page="www.stuff.ca/MyHobbyStore_Retail";
        st2.StartDate="20190201";
        stores.add(st2);

        Store st3=new Store();
        st3.StoreID="102";
        st3.Description="Toy Aren";
        st3.Page="www.stuff.ca/Toy_Aren";
        st3.StartDate="20130105";
        stores.add(st3);

        Store st4=new Store();
        st4.StoreID="103";
        st4.Description="kirara-ya";
        st4.Page="www.stuff.ca/kirara-ya";
        st4.StartDate="20210105";
        stores.add(st4);

        Store st5=new Store();
        st5.StoreID="104";
        st5.Description="Beeee!";
        st5.Page="www.stuff.ca/Beeee";
        st5.StartDate="20180501";
        stores.add(st5);

        Store st6=new Store();
        st6.StoreID="105";
        st6.Description="Santa Trading JP";
        st6.Page="www.stuff.ca/Santa_Trading_JP";
        st6.StartDate="20100521";
        stores.add(st6);

        Store st7=new Store();
        st7.StoreID="106";
        st7.Description="Au Pullman";
        st7.Page="www.stuff.ca/Au_Pullman";
        st7.StartDate="20190719";
        stores.add(st7);

        Store st8=new Store();
        st8.StoreID="107";
        st8.Description="Gaugemaster";
        st8.Page="www.stuff.ca/Gaugemaster";
        st8.StartDate="20160809";
        stores.add(st8);

        Store st9=new Store();
        st9.StoreID="108";
        st9.Description="Smaller World Future";
        st9.Page="www.stuff.ca/Smaller_World_Future";
        st9.StartDate="20170502";
        stores.add(st9);

        Store st10=new Store();
        st10.StoreID="109";
        st10.Description="rogerjohn28";
        st10.Page="www.stuff.ca/rogerjohn28";
        st10.StartDate="20200825";
        stores.add(st10);
    }
}
