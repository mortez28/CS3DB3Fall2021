package Generators;

import Entities.*;
import com.opencsv.CSVReader;
import util.hammer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.util.*;

public class ExtraGenerator {


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

    public static String path="dataset";

    public static void main(String []args) throws IOException {
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

        try (CSVReader reader = new CSVReader(new FileReader(path+"/Customer.csv"))) {
            //FirstName,LastName,DateOfBirth,Membership
            List<String[]> r = reader.readAll();
            for (int i=1;i<r.size();i++)
            {
                Customer c=new Customer();
                c.FirstName=r.get(i)[0];
                c.LastName=r.get(i)[1];
                c.DateOfBirth=r.get(i)[2];
                c.Membership=r.get(i)[3];
                customers.add(c);
            }
        }

        addOrders();

        addShipment();

        hammer.writeToFile(path+"/Order.csv",orders);
        hammer.writeToFile(path+"/Shipment.csv",shipments);
        hammer.writeToFile(path+"/HasShipment.csv",hasShipments);
    }

    public static void addOrders()
    {
        Random r=new Random();
        Set<String> selected=new HashSet<>();
        for (Object obj:customers)
        {
            Customer customer = (Customer) obj;
            int numberOfOrders=r.nextInt(2)+1;
            for (int i=0;i<numberOfOrders;i++) {

                while (true)
                {
                    String orderID= String.valueOf(r.nextInt(10000000));
                    int len = orderID.length();
                    for (int j = len; j <= 8; j++)
                        orderID = "0" + orderID;

                    if (!selected.contains(orderID)) {
                        selected.add(orderID);

                        Order order = new Order();
                        order.FirstName = customer.FirstName;
                        order.LastName = customer.LastName;
                        order.DateOfBirth = customer.DateOfBirth;
                        order.OrderID=orderID;

                        GregorianCalendar gc = new GregorianCalendar();
                        int year = randBetween(2010, 2021);
                        gc.set(gc.YEAR, year);
                        int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
                        gc.set(gc.DAY_OF_YEAR, dayOfYear);
                        order.Date = (gc.get(gc.MONTH) + 1) + "/" + gc.get(gc.DAY_OF_MONTH) + "/" + gc.get(gc.YEAR);

                        order.year=String.valueOf(year);
                        order.dayOfTheYear=String.valueOf(dayOfYear);

                        int millisInDay = 24*60*60*1000;
                        Time time = new Time((long)r.nextInt(millisInDay));
                        order.Time=time.toString();

                        orders.add(order);
                        break;
                    }
                }
            }
        }
    }

    public static void addShipment() {
        ArrayList<String> shippingCarriers = new ArrayList<>();
        shippingCarriers.add("Canada Post");
        shippingCarriers.add("DHL Express");
        shippingCarriers.add("Purolator");
        shippingCarriers.add("Canpar Express");
        shippingCarriers.add("eShipper");
        shippingCarriers.add("Freightera");

        Set<String> selected = new HashSet<>();

        Random r = new Random();

        for (int i = 0; i < orders.size(); i++) {
            double mergeOrders = r.nextDouble();
            if (mergeOrders < 0.15 && i+1<orders.size()) {
                //Merge some orders.

                Shipment shipment = new Shipment();
                shipment.Carrier = shippingCarriers.get(r.nextInt(shippingCarriers.size()));

                GregorianCalendar gc = new GregorianCalendar();
                gc.set(gc.YEAR, Integer.valueOf(((Order) orders.get(i)).year));
                int dayOfYear = Integer.valueOf(((Order) orders.get(i)).dayOfTheYear);
                if (dayOfYear < 350)
                    dayOfYear += r.nextInt(4) + 1;
                gc.set(gc.DAY_OF_YEAR, dayOfYear);
                shipment.DeliveryDate = (gc.get(gc.MONTH) + 1) + "/" + gc.get(gc.DAY_OF_MONTH) + "/" + gc.get(gc.YEAR);

                ((Order) orders.get(i+1)).Date=((Order) orders.get(i)).Date;


                while (true) {
                    String trackingNumber = String.valueOf(r.nextInt(1000000000));
                    int len = trackingNumber.length();
                    for (int j = len; j <= 10; j++)
                        trackingNumber = "0" + trackingNumber;
                    if (!selected.contains(trackingNumber)) {
                        selected.add(trackingNumber);
                        shipment.TrackingNumber = trackingNumber;
                        break;
                    }
                }
                shipments.add(shipment);
                HasShipment relation = new HasShipment();
                relation.OrderID = ((Order) orders.get(i)).OrderID;
                relation.TrackingNumber = shipment.TrackingNumber;
                hasShipments.add(relation);

                HasShipment relation2 = new HasShipment();
                relation2.OrderID = ((Order) orders.get(i+1)).OrderID;
                relation2.TrackingNumber = shipment.TrackingNumber;
                hasShipments.add(relation2);

                i++;

            }
            else
                {
                Shipment shipment = new Shipment();
                shipment.Carrier = shippingCarriers.get(r.nextInt(shippingCarriers.size()));

                GregorianCalendar gc = new GregorianCalendar();
                gc.set(gc.YEAR, Integer.valueOf(((Order) orders.get(i)).year));
                int dayOfYear = Integer.valueOf(((Order) orders.get(i)).dayOfTheYear);
                if (dayOfYear < 350)
                    dayOfYear += r.nextInt(4) + 1;
                gc.set(gc.DAY_OF_YEAR, dayOfYear);
                shipment.DeliveryDate = (gc.get(gc.MONTH) + 1) + "/" + gc.get(gc.DAY_OF_MONTH) + "/" + gc.get(gc.YEAR);

                while (true) {
                    String trackingNumber = String.valueOf(r.nextInt(1000000000));
                    int len = trackingNumber.length();
                    for (int j = len; j <= 10; j++)
                        trackingNumber = "0" + trackingNumber;
                    if (!selected.contains(trackingNumber)) {
                        selected.add(trackingNumber);
                        shipment.TrackingNumber = trackingNumber;
                        break;
                    }
                }
                shipments.add(shipment);
                HasShipment relation = new HasShipment();
                relation.OrderID = ((Order) orders.get(i)).OrderID;
                relation.TrackingNumber = shipment.TrackingNumber;
                hasShipments.add(relation);
            }
        }

    }



    public static int randBetween(int start, int end) {
        return start + (int)Math.round(Math.random() * (end - start));
    }

}
