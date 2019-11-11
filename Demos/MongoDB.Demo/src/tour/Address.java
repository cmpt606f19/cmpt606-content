package tour;

public final class Address {

    private String street;
    private String city;

    public Address() {
    }

    /**
     * Construct a new instance
     *
     * @param street the street
     * @param city the city
     */
    public Address(final String street, final String city) {
        this.street = street;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(final String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(final String city) {
        this.city = city;
    }

    @Override
    public String toString() {
    	String template = "Address {street='%s'"
    	         		  + ", city='%s'}";
        return String.format(template, street, city);
    }
}