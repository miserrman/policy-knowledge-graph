package com.longyan.policy.util;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BatchUtil {

    public static ConfigurableApplicationContext configurableApplicationContext;

    public static List<Object> batchInvoke(String[] cls, String methodName) {
        WebApplicationContext wec = ContextLoader.getCurrentWebApplicationContext();
        List<Object> objects = new ArrayList<>();
        for (String c : cls) {
            Class aimClass = wec.getBean(c).getClass();
            try {
                Method m = aimClass.getDeclaredMethod(methodName);
                List<Object> res = (List<Object>) m.invoke(wec.getBean(c));
                objects.addAll(res);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return objects;
    }
}
