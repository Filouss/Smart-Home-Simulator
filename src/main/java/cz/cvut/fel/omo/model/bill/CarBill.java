package cz.cvut.fel.omo.model.bill;

public class CarBill extends BillDecorator{
    private double petrolBill;

    public CarBill(Bill bill, double petrolBill) {
        super(bill);
    }

    /**
     * This method processes the payment of the petrol bill for refuelling cars
     */
    @Override
    public void payBill() {
        super.payBill();
        petrolBill = 0;
    }

    /**
     * This method returns the bill amount containing
     * previous bills and petrol bill
     */
    @Override
    public double getBillAmount() {
        return bill.getBillAmount() + petrolBill;
    }

    public void addCosts(double petrolBill) {
        this.petrolBill += petrolBill;
    }

    public double getPetrolBill() {
        return petrolBill;
    }
}
