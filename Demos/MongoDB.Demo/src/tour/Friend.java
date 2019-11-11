package tour;

import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Friend {
	/*
	 _id is a unique identifier of every MongoDB document. 
	 If it is not set, it is auto-generated. 
	 To handle it with Jongo, one attribute has to be named _id 
	 or annotated with @MongoId. 
	 This attribute datatype could be ObjectId class 
	 or a simple String annotated @MongoObjectId.
	 */
	@MongoId
	@MongoObjectId
    private String id;
    private String name;
    private int age;
    private Address address;
    //Use @JsonIgnore annotation to ignore an attribute and not store it in the collection
    //@JsonIgnore 
    private String gender;

    public Friend() {
    }

    public Friend(String name, int age, String gender, Address address) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
		return gender;
	}

    public void setGender(String gender) {
		this.gender = gender;
	}

	public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
    	String template = "Friend {"
        		.concat("id='%s'")
        		.concat(", name='%s'")
        		.concat(", age=%d")
        		.concat(", gender='%s'")
        		.concat(", address=%s }");
        return String.format(template, id, name, age, gender, address);
    }
}