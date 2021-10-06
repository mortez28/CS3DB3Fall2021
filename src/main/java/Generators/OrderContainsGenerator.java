package Generators;

import Entities.*;
import com.opencsv.CSVReader;
import util.hammer;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.util.*;

public class OrderContainsGenerator {


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

        addOrderContains();


        hammer.writeToFile(path+"/OrderContains.csv",orderContains);
    }

    public static void addOrderContains()
    {
        Random r=new Random();
        Set<String> selected=new HashSet<>();
        for (Object obj:orders)
        {
            selected.clear();
            Order order=(Order) obj;
            int numberOfProducts=r.nextInt(6)+1;
            for (int i=0;i<numberOfProducts;i++) {
                OrderContains details=new OrderContains();
                details.OrderID=order.OrderID;
                while (true) {
                    Product p=(Product) products.get(r.nextInt(products.size()-1));
                    if (!selected.contains(p.ProductID)) {
                        details.ProductID=p.ProductID;
                        details.Quantity=String.valueOf(r.nextInt(4)+1);
                        orderContains.add(details);
                        selected.add(p.ProductID);
                        break;
                    }
                }
            }
        }
    }

}
