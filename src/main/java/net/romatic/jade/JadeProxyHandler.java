package net.romatic.jade;

import net.romatic.com.exception.JadeException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author zhrlnt@gmail.com
 */
public class JadeProxyHandler implements InvocationHandler {

    private Builder builder;

    public JadeProxyHandler(Builder builder) {
        this.builder = builder;
    }

    @Override
    public Object invoke(Object proxy, Method abstractMethod, Object[] args) throws Throwable {

        if (args == null) {
            args = new Object[]{};
        }

        System.out.println(proxy instanceof Query);

        if (abstractMethod.isDefault()) {
            //return abstractMethod.invoke();
        }

        Method method = null;

        try {
            //  传来的 method 是 接口的方法，不能直接 invoke
            method = builder.getClass().getMethod(abstractMethod.getName(), abstractMethod.getParameterTypes());
        } catch (Exception e) {
            throw JadeException.of(
                    String.format("call method[%s] on %s", abstractMethod.getName(), builder),
                    e
            );
        }

        Object ret = method.invoke(builder, args);
        if (ret.equals(builder)) {
            //  链式调用
            return proxy;
        }

        return ret;
    }
}
