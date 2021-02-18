package me.bruno.santana;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import com.mongodb.MongoClientSettings;
import org.bson.Document;

import java.util.function.Consumer;

//Example based on https://docs.mongodb.com/drivers/java/
//I had to include the truststore code(check MainWithTLS class comments for instructions related to it)
public class MongoConnector {

    public static void main(String[] args) {

        String truststore = "C:\\cert\\rds-ca-certs";
        String truststorePassword = "<truststorePassword>";

        System.setProperty("javax.net.ssl.trustStore", truststore);
        System.setProperty("javax.net.ssl.trustStorePassword", truststorePassword);

        ConnectionString connString = new ConnectionString(
                "mongodb://sample-user:password@sample-cluster.node.us-east-1.docdb.amazonaws.com:27017/sample-database?ssl=true&replicaSet=rs0&readpreference=secondaryPreferred"
        );
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connString)
                .retryWrites(true)
                .build();
        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("sample-database");
        MongoCollection<Document> collection = database.getCollection("user");

        //collection.find().forEach(d -> System.out.println(d.toJson()));
        FindIterable<Document> documents = collection.find();
        Consumer<Document> consumer = d -> System.out.println(d.toJson());
        documents.forEach(consumer);
    }

}
