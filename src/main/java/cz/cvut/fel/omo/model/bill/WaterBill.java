package cz.cvut.fel.omo.model.bill;

/**
 * This class represents the water bill wrapper for the core bill
 */
public class WaterBill extends BillDecorator {
    private double watBill;


    public WaterBill(Bill bill, double watBill) {
        super(bill);
        this.watBill = watBill;
    }

    /**
     * This method processes the payment of the water bill
     */
    @Override
    public void payBill() {
        super.payBill();
        this.watBill = 0;
    }

    /**
     * This method returns the bill amount containing
     * previous bills and water bill
     */
    @Override
    public double getBillAmount() {
        return bill.getBillAmount() + watBill;
    }

    @Override
    public void addCosts(double costs) {
        watBill += costs;
    }
}
