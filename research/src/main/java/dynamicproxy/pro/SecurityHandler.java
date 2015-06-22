package dynamicproxy.pro;

import dynamicproxy.expert.SecurityChecker;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.InvocationHandler;

/**
 * Created by vchhieng on 19/06/2015.
 */
public class SecurityHandler implements InvocationHandler {
    static SecurityChecker sc = new SecurityChecker();
    final Object realObject;

    /**
     * contructor accepts the real subject
     */
    public SecurityHandler(Object real) {
        realObject = real;
    }

    /**
     * a generic, reflection-based secure invocation
     */
    public Object invoke(Object target, java.lang.reflect.Method method, Object[] arguments) throws Throwable {
        try {
            // call framework and then reflect the app-logic
            final AnnotatedType annotatedReturnType = method.getAnnotatedReturnType();

            if (method.getName().equals("getAmount")) {
                final Object returnObject = method.invoke(realObject, arguments);
                sc.check((Long) returnObject);
                return returnObject;
            } else if (method.getName().equals("getAmount2")) {
                final Object returnObject = method.invoke(realObject, arguments);
                sc.check((Long) returnObject);
                return returnObject;
            } else {
                return method.invoke(realObject, arguments);
            }
        } catch (java.lang.reflect.InvocationTargetException e) {
            // reconvert nested application exceptions
            throw e.getTargetException();
        }
    }
}
