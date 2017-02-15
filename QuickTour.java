package com.examples;

import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.exists;
import static com.mongodb.client.model.Filters.gt;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Updates.inc;
import static com.mongodb.client.model.Updates.set;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.BulkWriteOptions;
import com.mongodb.client.model.DeleteOneModel;
import com.mongodb.client.model.InsertOneModel;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.UpdateOneModel;
import com.mongodb.client.model.WriteModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

/**
 * The QuickTour code example see: https://mongodb.github.io/mongo-java-driver/3.0/getting-started
 */
public class QuickTour {
    /**
     * Run this main method to see the output of this quick example.
     *
     * @param args takes an optional single argument for the connection string
     */
    public static void main(final String[] args) {
        MongoClient mongoClient;

        if (args.length == 0) {
            // connect to the local database server
            mongoClient = new MongoClient();
            
            /*
             * MongoClient mongoClient = new MongoClient(); //connects to default host and port i.e 127.0.0.1:27017
    		// or
    		 * MongoClient mongoClient = new MongoClient( "localhost" ); //connects to default port i.e 27017
    		// or
    		 * MongoClient mongoClient = new MongoClient( "localhost" , 27017 ); // should use this always
             */
            
        } else {
            mongoClient = new MongoClient(new MongoClientURI(args[0]));
        }
        
        System.out.println(mongoClient.listDatabaseNames());
        MongoCursor<String> dbCursor = (mongoClient.listDatabaseNames()).iterator();
        while (dbCursor.hasNext()) {
            System.out.println(dbCursor.next());
        }
        // get handle to "test" database
        MongoDatabase database = mongoClient.getDatabase("test");
        
        System.out.println(database.listCollectionNames());
        MongoCursor<String> colCursor = (database.listCollectionNames()).iterator();
        while (colCursor.hasNext()) {
            System.out.println(colCursor.next());
        }
        // get a handle to the "users" collection
        MongoCollection<Document> collection = database.getCollection("users");
        
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }
        
        // drop all the data in it
        collection.drop();

        // make a document and insert it
        Document doc = new Document("empno", 132584)
                       .append("empname", "Vinay Sagiri")
                       .append("dept", "E&U")
                       .append("sal", 480000);
//                       .append("info", new Document("x", 203).append("y", 102));

        collection.insertOne(doc);

        // get it (since it's the only one in there since we dropped the rest earlier on)
        Document myDoc = collection.find().first();
        System.out.println(myDoc.toJson());

        // now, lets add lots of little documents to the collection so we can explore queries and cursors
        List<Document> documents = new ArrayList<Document>();
        for (int i = 0; i < 100; i++) {
            documents.add(new Document("i", i));
        }
        collection.insertMany(documents);
        System.out.println("total # of documents after inserting 100 small ones (should be 101) " + collection.count());

        // find first
        myDoc = collection.find().first();
        System.out.println(myDoc.toJson());

        // lets get all the documents in the collection and print them out
        MongoCursor<Document> Colcursor = collection.find().iterator();
        try {
            while (Colcursor.hasNext()) {
                System.out.println(Colcursor.next().toJson());
            }
        } finally {
        	Colcursor.close();
        }

/*        System.out.println(" ********* ");
        for (Document cur : collection.find()) {
            System.out.println(cur.toJson());
        }
*/
        // now use a query to get 1 document out
        myDoc = collection.find(eq("i", 71)).first();
        System.out.println(" Queried record is "+myDoc.toJson());

        // now use a range query to get a larger subset
        cursor = collection.find(gt("i", 50)).iterator();
        System.out.println(" Records with i> 50 ");
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }

        // range query with multiple constraints
        cursor = collection.find(and(gt("i", 25), lte("i", 50))).iterator();
        System.out.println(" records with > 25 and <= 50");
        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } finally {
            cursor.close();
        }

        // Query Filters
        myDoc = collection.find(eq("i", 71)).first();
        System.out.println(myDoc.toJson());

        // now use a range query to get a larger subset
        Block<Document> printBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(" Document is : "+document.toJson());
            }
        };
        collection.find(and(gt("i", 90), lte("i",95))).forEach(printBlock);

        // filter where; 50 < i <= 100
//        collection.find(and(gt("i", 50), lte("i", 100))).forEach(printBlock);

        // Sorting
        myDoc = collection.find(exists("i")).sort(descending("i")).first();
        System.out.println(" after sorting decending ");
        System.out.println(myDoc.toJson());
        myDoc = collection.find(exists("i")).sort(ascending("i")).first();
        System.out.println(" after sorting ascending ");
        System.out.println(myDoc.toJson());

        // Projection
        myDoc = collection.find().projection(excludeId()).first();
        System.out.println(myDoc.toJson());
/*
        // Aggregation
        collection.aggregate(asList(
                match(gt("i", 0)),
                project(Document.parse("{ITimes10: {$multiply: ['$i', 10]}}")))
        ).forEach(printBlock);

        myDoc = collection.aggregate(singletonList(group(null, sum("total", "$i")))).first();
        System.out.println(myDoc.toJson());

        // Update One
        collection.updateOne(eq("i", 10), set("i", 110));

        // Update Many
        UpdateResult updateResult = collection.updateMany(lt("i", 100), inc("i", 100));
        System.out.println(updateResult.getModifiedCount());

        // Delete One
        collection.deleteOne(eq("i", 110));

        // Delete Many
        DeleteResult deleteResult = collection.deleteMany(gte("i", 100));
        System.out.println(deleteResult.getDeletedCount());

        collection.drop();

        // ordered bulk writes
        List<WriteModel<Document>> writes = new ArrayList<WriteModel<Document>>();
        writes.add(new InsertOneModel<Document>(new Document("_id", 4)));
        writes.add(new InsertOneModel<Document>(new Document("_id", 5)));
        writes.add(new InsertOneModel<Document>(new Document("_id", 6)));
        writes.add(new UpdateOneModel<Document>(new Document("_id", 1), new Document("$set", new Document("x", 2))));
        writes.add(new DeleteOneModel<Document>(new Document("_id", 2)));
        writes.add(new ReplaceOneModel<Document>(new Document("_id", 3), new Document("_id", 3).append("x", 4)));

        collection.bulkWrite(writes);

        collection.drop();

        collection.bulkWrite(writes, new BulkWriteOptions().ordered(false));
        //collection.find().forEach(printBlock);

        // Clean up
        database.drop();
*/
        // release resources
        mongoClient.close();
    }
}