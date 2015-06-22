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

    @Test(expected = SecurityException.class)
    public void shouldThrowException() {
        salary = SalaryImp.makeSalary(10000000);
        salary.getAmount();
    }

/*
    @Test(expected = SecurityException.class)
    public void shouldThrowException2() {
        salary = SalaryImp.makeSalary(10000000);
        salary.getAmount2();
    }
*/

    @Test
    public void shouldNotThrowException() {
        salary = SalaryImp.makeSalary(100);
        salary.getAmount();
    }
}
