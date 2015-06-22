package dynamicproxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by vchhieng on 22/06/2015.
 * 005
 */
@RunWith(JUnit4.class)
public class SalaryTest {

    @Test(expected = SecurityException.class)
    public void shouldThrowException() {
        Salary salary = SalaryBuilder.makeSalary(10000000);
        salary.getAmount();
    }

    @Test
    public void shouldNotThrowException() {
        Salary salary = SalaryBuilder.makeSalary(100);
        salary.getAmount();
    }

    @Test(expected = SecurityException.class)
    public void shouldThrowException2() {
        Salary2 salary = SalaryBuilder.makeSalary2(2000000);
        salary.getAmount2();
    }

}
