package me.bruno.santana;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

// Example based on https://docs.aws.amazon.com/documentdb/latest/developerguide/connect_programmatically.html#connect_programmatically-tls_enabled

/*
This example only worked using rds-ca-2019-root.pem instead of rds-combined-ca-bundle.pem. Download here:
https://s3.amazonaws.com/rds-downloads/rds-ca-2019-root.pem
After downloading you should run this:
keytool -importcert -trustcacerts -file rds-ca-2019-root.pem -alias rds19 -keystore rds-ca-certs -storepass keyStorePassword
 */
public final class MainWithTLS {
    private MainWithTLS() {
    }
    public static void main(String[] args) {

        String template = "mongodb://%s:%s@%s/sample-database?ssl=true&replicaSet=rs0&readpreference=%s";
        String username = "<sample-user>";
        String password = "<password>";
        String clusterEndpoint = "sample-cluster.node.us-east-1.docdb.amazonaws.com:27017";
        String readPreference = "secondaryPreferred";
        String connectionString = String.format(template, username, password, clusterEndpoint, readPreference);

        String truststore = "C:\\cert\\rds-ca-certs";
        String truststorePassword = "<truststorePassword>";

        System.setProperty("javax.net.ssl.trustStore", truststore);
        System.setProperty("javax.net.ssl.trustStorePassword", truststorePassword);

        MongoClientURI clientURI = new MongoClientURI(connectionString);
        MongoClient mongoClient = new MongoClient(clientURI);

        MongoDatabase testDB = mongoClient.getDatabase("sample-database");
        MongoCollection<Document> numbersCollection = testDB.getCollection("user");

        Document doc = new Document("name", "Bruno").append("email", "bruno@bruno.com");
        numbersCollection.insertOne(doc);

        MongoCursor<Document> cursor = numbersCollection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }

    }
}

