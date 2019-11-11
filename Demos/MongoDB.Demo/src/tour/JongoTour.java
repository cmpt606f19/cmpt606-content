package tour;

import static com.mongodb.client.model.Filters.eq;
import static java.util.Arrays.asList;

import java.util.List;

import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;
import org.jongo.query.Query;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class JongoTour {

	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient();
	    Jongo jongo = new Jongo(mongoClient.getDB("mydb"));
	    
	    MongoCollection friendsCol = jongo.getCollection("friends");
		
		Query query = jongo.createQuery("{test:1}");
		
        // make a document and insert it
        Friend ali = new Friend("Ali Faleh", 20, "Female", new Address("University St", "Doha"));
        System.out.printf("Friend object before insert: %s%n", ali);
        
        friendsCol.insert(ali);
        
        // Friend will now have an ObjectId
        System.out.printf("Friend object after insert: %s%n", ali);
        
        // Now, lets add some more friends so we can explore queries and cursors
        List<Friend> myFriends = asList(
                new Friend("Abbess Ibn Firnas", 45, "Male", new Address("5 Arab League Street", "Al-Khor")),
                new Friend("Khadija Khaldi", 28, "Female", new Address("55 Pearl Rd", "Doha")),
                new Friend("Samir Dahih", 61, "Male", new Address("444 Corniche St", "Dukhan"))
        );
        
        for(var friend : myFriends) {
        	friendsCol.insert(friend);
        }
        System.out.println("Total # of friends " + friendsCol.count());

        System.out.println("\nAll friends in the collection");
        MongoCursor<Friend> friends = friendsCol.find().as(Friend.class);
		for(var friend : friends) {
			System.out.println(friend);
		}

		System.out.println("\nFriends in the collection with the name contains Khaldi (/i for case-insensitive) & age >= 20");
		String queryString = "{name: { $regex : # }, age: {$gte: #}}";
		friends = friendsCol.find(queryString, "khaldi", 20).as(Friend.class); 
		for(var friend : friends) {
			System.out.println(friend);
		}
		
		System.out.println("\nFirst friend in the collection with the name contains Khaldi (/i for case-insensitive) & age >= 20");
		Friend one = friendsCol.findOne(queryString, "khaldi", 20).projection("{_id: 0, name: 1, age: 1}").as(Friend.class);
		System.out.println(one);
		
        System.out.println("\nFriends living in Doha");
        // now use a query to get 1 document out { address.city:  "Doha" }
        friends = friendsCol.find("{address.city: 'Doha'}").as(Friend.class);
		for(var friend : friends) {
			System.out.println(friend);
		}
		
		System.out.println("\nDistinct friend names");
		List<String> names = friendsCol.distinct("name").as(String.class);
		System.out.println(names);
		
		System.out.println("\nFriends older than 40");
		var results = friendsCol.aggregate("{$match: {age: {$gte: 40 }} }")
					        	.and("{$limit:2}")
					        	.as(Friend.class);
		
		for(var result : results) {
			System.out.println(result);
		}
		
		System.out.println("\nAverage age of friends");
		var summaryResults = friendsCol
							  .aggregate("{ $group : {_id : null,  avgAge : {$avg : '$age'}, count:{$sum:1} }}")
							  .and("{$project: {avgAge:1, count:1 } }")
							  .as(SummaryReport.class);
		
		for(var result : summaryResults) {
			System.out.println(result);
		}
		
		System.out.println("\nAverage age of friends by gender");
		var summaryResultsByGender = friendsCol
							.aggregate("{ $group : {_id : '$gender',  avgAge : {$avg : '$age'}, count:{$sum:1} }}")
							.and("{ $sort: {avgAge: 1} }")
							.as(SummaryReportByGender.class);
		
		for(var result : summaryResultsByGender) {
			System.out.println(result);
		}
		
		friendsCol.drop();
	}

}