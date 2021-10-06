package Generators;

import Entities.Order;
import Entities.OrderContains;
import Entities.Person;
import Entities.Product;
import com.opencsv.CSVReader;
import util.hammer;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CheckEntities {

    public static ArrayList<Object> persons= new ArrayList<>();
    public static ArrayList<Object> phones= new ArrayList<>();
    public static ArrayList<Object> storeOwners= new ArrayList<>();
    public static ArrayList<Object> storeEmployees= new ArrayList<>();
    public static ArrayList<Object> stores= new ArrayList<>();
    public static ArrayList<Object> storeEmployeeWorks= new ArrayList<>();
    public static ArrayList<Object> customers= new ArrayList<>();
    public static ArrayList<Object> orders= new ArrayList<>();
    public static ArrayList<Object> shipments= new ArrayList<>();
    public static ArrayList<Object> hasShipments= new ArrayList<>();
    public static ArrayList<Object> products= new ArrayList<>();
    public static ArrayList<Object> orderContains= new ArrayList<>();

    public static String path="dataset";

    public static void main(String []args) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(path+"/Product.csv"))) {
            //ProductID,Name,ModelNumber,Description,Brand,Price,StoreID
            List<String[]> r = reader.readAll();
            for (int i=1;i<r.size();i++)
            {
                Product p=new Product();
                p.ProductID=r.get(i)[0];
                p.Name=r.get(i)[1];
                p.ModelNumber=r.get(i)[2];
                p.Description=r.get(i)[3];
                p.Brand=r.get(i)[4];
                p.Price=r.get(i)[5];
                p.StoreID=r.get(i)[6];
                products.add(p);
            }
        }

        try (CSVReader reader = new CSVReader(new FileReader(path+"/Order.csv"))) {
            //OrderID,Date,Time,year,dayOfTheYear,FirstName,LastName,DateOfBirth
            List<String[]> r = reader.readAll();
            for (int i=1;i<r.size();i++)
            {
                Order c=new Order();
                c.OrderID=r.get(i)[0];
                c.Date=r.get(i)[1];
                c.Time=r.get(i)[2];
                c.year=r.get(i)[3];
                c.dayOfTheYear=r.get(i)[3];
                c.FirstName=r.get(i)[3];
                c.LastName=r.get(i)[3];
                c.DateOfBirth=r.get(i)[3];
                orders.add(c);
            }
        }

        try (CSVReader reader = new CSVReader(new FileReader(path+"/OrderContains.csv"))) {
            //OrderID,ProductID,Quantity
            List<String[]> r = reader.readAll();
            for (int i=1;i<r.size();i++)
            {
                OrderContains c=new OrderContains();
                c.OrderID=r.get(i)[0];
                c.ProductID=r.get(i)[1];
                c.Quantity=r.get(i)[2];
                orderContains.add(c);
            }
        }

        try (CSVReader reader = new CSVReader(new FileReader(path+"/Person.csv"))) {
            //ID,FirstName,LastName,DateOfBirth,Sex,Country,City,Street,PostalCode
            List<String[]> r = reader.readAll();
            for (int i=1;i<r.size();i++)
            {
                Person p=new Person();
                p.ID=r.get(i)[0];
                p.FirstName=r.get(i)[1];
                p.LastName=r.get(i)[2];
                p.DateOfBirth=r.get(i)[3];
                p.Sex=r.get(i)[4];
                p.Country=r.get(i)[5];
                p.City=r.get(i)[6];
                p.Street=r.get(i)[7];
                p.PostalCode=r.get(i)[8];
                persons.add(p);
            }
        }

        //checkProductsInOrders();

        checkPersons();

        //hammer.writeToFile(path+"/OrderContains.csv",orderContains);
    }

    public static void checkPersons()
    {
        HashSet<String> allPersons=new HashSet<>();
        for (Object obj:persons) {
            Person p=(Person) obj;
            String val=p.FirstName+"#"+p.LastName+"#"+p.DateOfBirth;
            if(!allPersons.contains(val))
                allPersons.add(val);
            else
                System.out.println(val);
        }
    }

    public static void checkProductsInOrders()
    {
        HashSet<String> allProducts=new HashSet<>();
        for (Object obj: products) {
            Product p = (Product) obj;
            allProducts.add(p.ProductID);
        }

        HashSet<String> allProductsInOrders=new HashSet<>();
        for (Object obj: orderContains) {
            OrderContains c = (OrderContains) obj;
            allProductsInOrders.add(c.ProductID);
        }

        allProducts.removeAll(allProductsInOrders);

        allProducts.forEach(System.out::println);
    }

}
