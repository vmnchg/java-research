package dynamicproxy;

/**
 * Created by vchhieng on 19/06/2015.
 */
public class SalaryImp implements Salary {
//    Employee employee;
    long amount;
    public SalaryImp() {
    }
    public SalaryImp(long amount) {
        this.amount = amount;
    }

    @Override
    public long getAmount() {
        return amount;
    }
}
