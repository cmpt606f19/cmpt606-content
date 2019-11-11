package tour;

import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

public class SummaryReportByGender {
	@MongoId
	private String gender;
	private double avgAge;
	private int count;
	
	public SummaryReportByGender() {}
	
	public SummaryReportByGender(String gender, double avgAge, int count) {
		this.gender = gender;
		this.avgAge = avgAge;
		this.count = count;
	}
	
	public String getGender() {
		return gender;
	}

	public double getAvgAge() {
		return avgAge;
	}

	public int getCount() {
		return count;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setAvgAge(double avgAge) {
		this.avgAge = avgAge;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return String.format("Average Age for %s: %.2f - Count: %d", gender, avgAge, count);
	}
}