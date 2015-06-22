package dynamicproxy.naive;

import dynamicproxy.SalaryImp;
import dynamicproxy.logic.SecurityChecker;

/**
 * Created by vchhieng on 19/06/2015.
 */
public class SalaryImpSecurityProxy extends SalaryImp {
    static SecurityChecker sc;
    SalaryImp realObject;

    public long getAmount() {
        return sc.check(realObject.getAmount());
    }
}
