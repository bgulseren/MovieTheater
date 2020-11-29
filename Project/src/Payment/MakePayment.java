package Payment;

import java.util.Date;

public class MakePayment {

    private PaymentSystem paymentSystem;
    private TransactionForm transactionForm;

    public MakePayment(PaymentSystem paymentSystem) {
        setPaymentSystem(paymentSystem);
    }

    public boolean payWithCreditCard(String cc, int cvv, double amount) {
        return true;
    }

    public double payWithVoucher(int vouchNum, double amount) {
        for (Voucher voucher : paymentSystem.getVouchers())
            if (voucher.getVouchNum() == vouchNum)
                if (voucher.getExpiryDate().compareTo(new Date()) > 0) {
                    if (voucher.getAmount() >= amount)
                        return 0;
                    else
                        return amount - voucher.getAmount();
                } else
                    return -1;
        return -1;
    }

    public void setPaymentSystem(PaymentSystem paymentSystem) {
        this.paymentSystem = paymentSystem;
    }
}
