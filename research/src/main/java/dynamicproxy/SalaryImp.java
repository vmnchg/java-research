package dynamicproxy;

import dynamicproxy.pro.SecurityHandler;

/**
 * Created by vchhieng on 19/06/2015.
 */
public class SalaryImp implements Salary, Salary2, Salary3 {
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

    @Override
    public long getAmount2() {
        return amount;
    }

    @Override
    public long getAmount3() {
        return amount;
    }
    public static Salary makeSalary(int amount) {
        final SalaryImp salaryImp = new SalaryImp(amount);
        final Salary salary = (Salary)
                java.lang.reflect.Proxy.newProxyInstance(
                        SalaryImp.class.getClassLoader(),
                        new Class[]{Salary.class, Salary2.class},
                        new SecurityHandler(salaryImp));
        return salary;
    }
}
