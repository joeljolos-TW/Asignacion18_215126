package org.example;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Main {
    static void main() {
        MongoClient cliente = MongoClients.create("mongodb://localhost:27017/");
        MongoDatabase db = cliente.getDatabase("restaurante");
        MongoCollection<Document> col= db.getCollection("cafes");

        Document cafe1= new Document();

        cafe1.append("name","cafe de la plaza");
        cafe1.append("stars",4.3);
        cafe1.append("categories", Arrays.asList(new String[]{"Cafe","Postres","Desayuno"}));

        col.insertOne(cafe1);

        List<Document> cafes = new ArrayList<>();

        cafes.add(
                new Document("name","Espresso express")
                        .append("stars",4.8)
                        .append("categories", Arrays.asList(new String[]{"Cafe","Rapido","Takeaway"}))
        );

        cafes.add(
                new Document("name","The Tea House")
                        .append("stars",3.9)
                        .append("categories", Arrays.asList(new String[]{"Te","Infusiones","Postres"}))
        );

        cafes.add(
                new Document("name","Morning Brew")
                        .append("stars",4.0)
                        .append("categories", Arrays.asList(new String[]{"Cafe","Desayuno","Bakery"}))
        );

        col.insertMany(cafes);

        for(Document d : col.find(Filters.gte("stars",4.5))){
            System.out.println(d.toJson());
        }

        for(Document d : col.find(Filters.regex("name","Cafe"))){
            System.out.println(d.toJson());
        }

        for(Document d : col.find(Filters.in("categories","Postres"))){
            System.out.println(d.toJson());
        }

        for(Document d : col.find(Filters.and(Filters.gte("stars",3),Filters.lte("stars",4.3)))){
            System.out.println(d.toJson());
        }

        for(Document d : col.find(Filters.regex("name","^T"))){
            System.out.println(d.toJson());
        }

        col.updateOne(Filters.eq("name","Morning Brew"), Updates.set("stars",4.5));

        col.updateMany(Filters.in("categories","Bakery","Desayuno"),Updates.inc("stars",0.2));

        col.updateOne(
                Filters.eq("name","cafe de la plaza"),
                Updates.combine(
                        Updates.set("phone","555-111-2222"),
                        Updates.set("open",true))
        );

        col.deleteOne(Filters.eq("name","Espresso Express"));

        col.deleteMany(Filters.lt("stars",4));

        col.deleteMany(Filters.in("categories","Takeaway","Infusiones"));
    }
}
