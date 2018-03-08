package net.romatic.jade;

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
    public Object invoke(Object proxy, Method AbstractMethod, Object[] args) throws Throwable {

        if (args == null) {
            args = new Object[]{};
        }

        Method method;
        try {
            //  传来的 method 是 接口的方法，不能直接 invoke
            method = builder.getClass().getMethod(AbstractMethod.getName(), AbstractMethod.getParameterTypes());
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("call method[%s] on %s", AbstractMethod.getName(), builder),
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
