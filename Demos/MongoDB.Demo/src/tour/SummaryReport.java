package tour;

public class SummaryReport {
	private double avgAge;
	private int count;
	
	public SummaryReport() {}
		
	public SummaryReport(double avgAge, int count) {
		this.avgAge = avgAge;
		this.count = count;
	}
	
	public double getAvgAge() {
		return avgAge;
	}

	public int getCount() {
		return count;
	}

	public void setAvgAge(double avgAge) {
		this.avgAge = avgAge;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return String.format("Average Age: %.2f - Count: %d", avgAge, count);
	}
}