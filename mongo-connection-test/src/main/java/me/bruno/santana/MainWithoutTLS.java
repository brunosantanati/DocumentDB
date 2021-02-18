package me.bruno.santana;

    import com.mongodb.MongoClient;
    import com.mongodb.MongoClientURI;
    import com.mongodb.client.MongoCursor;
    import com.mongodb.client.MongoDatabase;
    import com.mongodb.client.MongoCollection;
    import org.bson.Document;

// Example based on https://docs.aws.amazon.com/documentdb/latest/developerguide/connect_programmatically.html#connect_programmatically-tls_disabled
public final class MainWithoutTLS {
    private MainWithoutTLS() {
    }
    public static void main(String[] args) {

        String template = "mongodb://%s:%s@%s/sample-database?replicaSet=rs0&readpreference=%s";
        String username = "<sample-user>";
        String password = "<password>";
        String clusterEndpoint = "sample-cluster.node.us-east-1.docdb.amazonaws.com:27017";
        String readPreference = "secondaryPreferred";
        String connectionString = String.format(template, username, password, clusterEndpoint, readPreference);

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

