package cz.cvut.fel.omo.model.bill;

/**
 * This class represents the electricity bill wrapper for the core bill
 */
public class ElectricityBill extends BillDecorator{
    private double elBill;

    public ElectricityBill(Bill bill, double elBill) {
        super(bill);
        this.elBill = elBill;
    }

    /**
     * This method processes the payment of the electricity bill
     */
    @Override
    public void payBill() {
        super.payBill();
        this.elBill = 0;
    }

    /**
     * This method returns the bill amount containing
     * previous bills and electricity bill
     */
    @Override
    public double getBillAmount() {
        return bill.getBillAmount() + elBill;
    }

    @Override
    public void addCosts(double costs) {
        elBill += costs;
    }
}
