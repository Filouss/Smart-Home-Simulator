package cz.cvut.fel.omo.model.bill;

/**
 * This class represents the food bill wrapper for the core bill
 */
public class FoodBill extends BillDecorator{
    private double foodBill;

    public FoodBill(Bill bill, double foodBill) {
        super(bill);
        this.foodBill = foodBill;
    }

    /**
     * This method processes the payment of the food bill
     */
    @Override
    public void payBill() {
        super.payBill();
        foodBill = 0;
    }

    /**
     * This method returns the bill amount containing
     * previous bills and food bill
     */
    @Override
    public double getBillAmount() {
        return bill.getBillAmount() + foodBill;
    }

    @Override
    public void addCosts(double costs) {
        foodBill += costs;
    }
}
