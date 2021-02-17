package me.bruno.santana;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClientSettings;
import org.bson.Document;

//Example from https://docs.mongodb.com/drivers/java/
public class MongoConnector {

    public static void main(String[] args) {
        ConnectionString connString = new ConnectionString(
                //"mongodb+srv://<username>:<password>@<cluster-address>/test?w=majority"
                "mongodb://localhost:27017/springbootmongodb"
        );
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .retryWrites(true)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("springbootmongodb");
        MongoCollection<Document> collection = database.getCollection("user");
        collection.find().forEach(d -> System.out.println(d.toJson()));
    }

}
