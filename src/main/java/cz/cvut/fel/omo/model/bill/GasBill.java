package cz.cvut.fel.omo.model.bill;

/**
 * This class represents the gas bill wrapper for the core bill
 */
public class GasBill extends BillDecorator{
    private double gasBill;

    public GasBill(Bill bill, double gasBill) {
        super(bill);
        this.gasBill = gasBill;
    }

    /**
     * This method processes the payment of the gas bill
     */
    @Override
    public void payBill() {
        super.payBill();
        this.gasBill = 0;
    }

    /**
     * This method returns the bill amount containing
     * previous bills and gas bill
     */
    @Override
    public double getBillAmount() {
        return bill.getBillAmount() + gasBill;
    }

    @Override
    public void addCosts(double costs) {
        gasBill += costs;
    }
}
