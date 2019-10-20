package entity;

public class OrderSummary {
    private String customerName;
    private long totalQty;

    public OrderSummary() {
    }
    
    public OrderSummary(String customerName, long totalQty) {
        this.customerName = customerName;
        this.totalQty = totalQty;
    }

    public String geCustomertName() {
        return customerName;
    }

    public void setCustomerName(String name) {
        this.customerName = name;
    }

    public long getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(long totalQty) {
        this.totalQty = totalQty;
    }
}