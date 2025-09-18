package cz.cvut.fel.omo.model.bill;

/**
 * Design pattern: Decorator
 */
public interface Bill {
    /**
     * This method processes the payment of the bill
     */
    void payBill();
    /**
     * This method returns the bill amount
     */
    double getBillAmount();
    /**
     * This method increases the bill amount
     */
    void addCosts(double costs);
}
