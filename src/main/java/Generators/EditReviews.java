package Generators;

import Entities.WriteReview;
import util.hammer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class EditReviews {

    public static ArrayList<Object> reviews= new ArrayList<>();

    public static void main(String []args) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader("dataset/WriteReview.csv"))) {
            String line;
            //FirstName,LastName,DateOfBirth,ProductID,Star,Comment

            while ((line = br.readLine()) != null) {
                if(line.startsWith("FirstName"))
                    continue;
                String []var=line.split(",");
                if(var.length>=6)
                {
                    WriteReview review=new WriteReview();
                    review.FirstName=var[0];
                    review.LastName=var[1];
                    review.DateOfBirth=var[2];
                    review.ProductID=var[3];
                    review.Star=var[4];
                    review.Comment=var[5];
                    for (int i=6;i<var.length;i++)
                    {
                        review.Comment+=var[i];
                    }
                    review.Comment=review.Comment.replace("\"","");
                    review.Comment=review.Comment.replace("'","");

                    reviews.add(review);
                }
                else
                    System.out.println(line);
            }

            hammer.writeToFile("dataset/NewReviews.csv",reviews);
        }
    }

}
