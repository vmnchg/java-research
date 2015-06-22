package dynamicproxy;

import dynamicproxy.pro.SecurityHandler;

/**
 * Created by vchhieng on 22/06/2015.
 */
public class SalaryBuilder {
    public static Salary makeSalary(int amount) {
        final SalaryImp salaryImp = new SalaryImp(amount);
        final Salary salary = (Salary)
                java.lang.reflect.Proxy.newProxyInstance(
                        SalaryImp.class.getClassLoader(),
                        new Class[]{Salary.class},
                        new SecurityHandler(salaryImp));
        return salary;
    }

    public static Salary2 makeSalary2(int amount) {
        final SalaryImp salaryImp = new SalaryImp(amount);
        final Salary2 salary2 = (Salary2)
                java.lang.reflect.Proxy.newProxyInstance(
                        SalaryImp.class.getClassLoader(),
                        new Class[]{Salary.class, Salary2.class},
                        new SecurityHandler(salaryImp));
        return salary2;
    }
}
