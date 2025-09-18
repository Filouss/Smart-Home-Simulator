package cz.cvut.fel.omo.model.bill;

/**
 * This class represents the core bill for paying which is rent
 */
public class HouseBill implements Bill{
    private double rent;

    public HouseBill(double rent) {
        this.rent = rent;
    }

    /**
     * This method processes the payment of the rent bill
     */
    @Override
    public void payBill() {
        this.rent = 0;
    }

    /**
     * This method returns the bll amount containing rent
     */
    @Override
    public double getBillAmount() {
        return rent;
    }

    @Override
    public void addCosts(double costs) {
        this.rent += costs;
    }
}
