package dynamicproxy;

import dynamicproxy.pro.SecurityHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by vchhieng on 22/06/2015.
 */
@RunWith(JUnit4.class)
public class SalaryTest {
    private Salary salary;

    @Before
    public void before() {

    }

    @Test(expected = SecurityException.class)
    public void shouldThrowException() {
        SalaryImp salaryImp = new SalaryImp(10000000);
        salary = (Salary)
                java.lang.reflect.Proxy.newProxyInstance(
                        Salary.class.getClassLoader(),
                        new Class[]{Salary.class},
                        new SecurityHandler(salaryImp));
        salary.getAmount();
    }

    @Test
    public void shouldNotThrowException() {

        SalaryImp salaryImp = new SalaryImp(100);
        salary = (Salary)
                java.lang.reflect.Proxy.newProxyInstance(
                        Salary.class.getClassLoader(),
                        new Class[]{Salary.class},
                        new SecurityHandler(salaryImp));
        salary.getAmount();
    }
}
