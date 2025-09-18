package cz.cvut.fel.omo.model.bill;

/**
 * This class represents the repair bill wrapper for the core bill, which occurs
 * in case an appliance is damaged
 */
public class ApplianceBill extends BillDecorator {
    private double costs;

    public ApplianceBill(Bill bill, double costs) {
        super(bill);
    }

    /**
     * This method processes the payment of the repair bill
     */
    @Override
    public void payBill() {
        super.payBill();
        costs = 0;
    }

    /**
     * This method returns the bill amount containing
     * previous bills and repair bill
     */
    @Override
    public double getBillAmount() {
        return bill.getBillAmount() + costs;
    }

    /**
     * This method adds costs of appliance bill related actions
     */
    @Override
    public void addCosts(double costs) {
        this.costs += costs;
    }
}
