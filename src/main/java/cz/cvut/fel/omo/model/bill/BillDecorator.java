package cz.cvut.fel.omo.model.bill;

/**
 * This class represents the decorator for wrapping the rent bill with additional payments
 */
public abstract class BillDecorator implements Bill{
    protected Bill bill;

    public BillDecorator(Bill bill) {
        this.bill = bill;
    }

    @Override
    public void payBill() {
        bill.payBill();
    }

    @Override
    public double getBillAmount() {
        return bill.getBillAmount();
    }
}
