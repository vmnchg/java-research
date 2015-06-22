package dynamicproxy.logic;

import java.security.Permission;

/**
 * Created by vchhieng on 19/06/2015.
 */
public class SecurityChecker {
    Permission manyBucksPermission;

    public long check(long amount) throws SecurityException {
        if (amount > 1000000) {
//            new SecurityManager().checkPermission(manyBucksPermission);
            throw new SecurityException("Amount is very large. You need to get security clearance");
        }
        return amount;
    }
}