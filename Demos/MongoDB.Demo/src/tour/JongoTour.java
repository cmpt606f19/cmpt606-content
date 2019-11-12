package tour;

import java.util.List;

import org.bson.types.ObjectId;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

//More info and examples @ https://jongo.org/
public class JongoTour {

	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient();
	    Jongo jongo = new Jongo(mongoClient.getDB("mydb"));
	    
	    MongoCollection friendsCol = jongo.getCollection("friends");
		
	    //Drop all documents
		friendsCol.drop();
		
        // make a document and insert it
        Friend ali = new Friend("Ali Faleh", 20, "Male", new Address("University St", "Doha"));
        System.out.printf("Friend object before insert: %s%n", ali);
        
        friendsCol.insert(ali);
        
        // Friend will now have an ObjectId
        System.out.printf("Friend object after insert: %s%n", ali);
        
        // Now, lets add some more friends so we can explore queries and cursors
        Friend[] myFriends = {
                new Friend("Abbess Ibn Firnas", 45, "Male", new Address("5 Arab League Street", "Al-Khor")),
                new Friend("Khadija Khaldi", 28, "Female", new Address("55 Pearl Rd", "Doha")),
                new Friend("Samira Dahih", 18, "Female", new Address("444 Corniche St", "Dukhan"))
        };
        
        //Insert multiple documents in one go...
        friendsCol.insert(myFriends);
        System.out.println("Total # of friends " + friendsCol.count());

        System.out.println("\nAll friends in the collection");
        MongoCursor<Friend> friends = friendsCol.find()
        										.sort("{name: 1}")
        										.as(Friend.class);
		for(var friend : friends) {
			System.out.println(friend);
		}

		System.out.println("\nFriends in the collection with the name contains Khaldi (/i for case-insensitive) & age >= 20");
		String query = "{name: { $regex : # }, age: {$gte: #}}";
		friends = friendsCol.find(query, "khaldi", 20).as(Friend.class); 
		for(var friend : friends) {
			System.out.println(friend);
		}
		
		System.out.println("\nFirst friend in the collection with the name contains Khaldi (/i for case-insensitive) & age >= 20");
		Friend one = friendsCol.findOne(query, "khaldi", 20).projection("{_id: 0, name: 1, age: 1}").as(Friend.class);
		System.out.println(one);
		
        System.out.println("\nFriends living in Doha");
        // now use a query to get 1 document out { address.city:  "Doha" }
        friends = friendsCol.find("{address.city: #}", "Doha").as(Friend.class);
		for(var friend : friends) {
			System.out.println(friend);
		}
		
		System.out.println("\nDistinct friend names");
		List<String> names = friendsCol.distinct("name").as(String.class);
		System.out.println(names);
		
		System.out.println("\nFriends older than 30");
		var results = friendsCol.aggregate("{$match: {age: {$gte: # }} }", 30)
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
		
		
        System.out.println("\nUpdate Ali's name and age");
        friendsCol.update("{name: #}", "Ali Faleh").with("{$set: {age: #, name: #}}", 25, "Dr. Ali Faleh");
		one = friendsCol.findOne("{name: #}", "Dr. Ali Faleh").as(Friend.class);
		System.out.println(one);
		
        System.out.println("\nUpdate Ali's address");
        WriteResult writeResult = friendsCol.update("{name: #}", "Dr. Ali Faleh").with("{$set: {address: #}}", new Address("55 Porto Arabia, The Pearl,", "Doha"));
        System.out.println(writeResult);
        one = friendsCol.findOne("{name: #}", "Dr. Ali Faleh").as(Friend.class);
		System.out.println(one);
		
		
        System.out.println("\nUpset all my female friends by increasing their age by 5 😃");
        writeResult = friendsCol.update("{gender: #}", "Female").multi().with("{$inc: {age: #}}", 5);
        System.out.println(writeResult);
		friends = friendsCol.find("{gender: #}", "Female").as(Friend.class);
		for(var friend : friends) {
			System.out.println(friend);
		}
		
		System.out.println("\nUpdate a document and it does not exist create it"); 
		writeResult = friendsCol.update("{name: #}", "Shaun the Sheep").upsert().with("{$inc: {age: 1}}");
        System.out.println(writeResult);
        one = friendsCol.findOne("{name: #}", "Shaun the Sheep").as(Friend.class);
		System.out.println(one);
		
		System.out.println("\nReplace a document"); 
		writeResult = friendsCol.update("{name: #}", "Shaun the Sheep").with(new Friend("Shaun the Sheep", 3, "Sheep", new Address("Fun St", "Utopia")));
        System.out.println(writeResult);
        one = friendsCol.findOne("{name: #}", "Shaun the Sheep").as(Friend.class);
		System.out.println(one);
		
		System.out.println("\nRemove a document"); 
		writeResult = friendsCol.remove("{name: #}", one.getName());
		System.out.println(writeResult);
		writeResult = friendsCol.remove(new ObjectId(one.getId()));
		System.out.println(writeResult);
		
        // release resources
        mongoClient.close();
	}

}