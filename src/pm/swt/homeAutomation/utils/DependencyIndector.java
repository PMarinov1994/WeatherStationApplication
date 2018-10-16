package pm.swt.homeAutomation.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class DependencyIndector
{
    private static volatile DependencyIndector instance;
    private static final Object mutex = new Object();

    private final Set<Object> registeredClassSet = new HashSet<>();
    private final Map<String, Object> registedInstanceMap = new HashMap<>();



    private DependencyIndector()
    {
    }



    public static DependencyIndector getInstance()
    {
        DependencyIndector result = instance;
        if (result == null)
        {
            synchronized (mutex)
            {
                result = instance;
                if (result == null)
                    instance = result = new DependencyIndector();
            }
        }

        return result;
    }



    public void registerClass(Class<?> clazz)
    {
        try
        {
            Constructor<?> ctor = clazz.getConstructor();
            Object instance = ctor.newInstance();

            boolean result = this.registeredClassSet.add(instance);
            if (!result)
                System.err.println("Failed to register instance");
        }
        catch (NoSuchMethodException
                | SecurityException
                | InstantiationException
                | IllegalAccessException
                | IllegalArgumentException
                | InvocationTargetException e)
        {
            e.printStackTrace();
        }
    }



    public void registerInstance(String key, Object instance)
    {
        if (key == null || key.isEmpty())
            throw new IllegalArgumentException("Key cannot be empty or null");

        if (instance == null)
            throw new IllegalArgumentException("Instance cannot be null");

        if (this.registedInstanceMap.put(key, instance) != null)
            System.err.println("Instance already registered.");
    }



    public <T> T resolveClass(Class<T> clazz)
    {
        for (Object object : registeredClassSet)
        {
            if (object.getClass().equals(clazz))
            {
                try
                {
                    return clazz.cast(object);

                }
                catch (ClassCastException ex)
                {
                    ex.printStackTrace();
                    return null;
                }
            }
        }

        return null;
    }



    public Object resolveInstance(String key)
    {
        return this.registedInstanceMap.get(key);
    }
}
