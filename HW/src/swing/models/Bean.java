package swing.models;

import java.beans.*;
import java.lang.reflect.*;
import javax.swing.event.*;

public class Bean {
    private final Object bean;
    private final Method getMethod;
    private final Method setMethod;

    public Bean(Object bean, String propertyName) {
        PropertyDescriptor descriptor = getProperty(bean, propertyName);

        this.bean = bean;
        getMethod = descriptor.getReadMethod();
        setMethod = descriptor.getWriteMethod();
    }

    public Object getValue() {
        try {
            return getMethod.invoke(bean);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setValue(Object value) {
        try {
            setMethod.invoke(bean, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PropertyDescriptor getProperty(Object bean, String propertyName) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
                if (propertyName.equals(descriptor.getName())) {
                    return descriptor;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException(
            String.format("class %s does not have property %s", 
                bean.getClass().getName(),
                propertyName
            )
        );
    }

    public void addChangeListener(ChangeListener listener) {
        try {
            Method method = bean.getClass().getMethod("addChangeListener", ChangeListener.class);
            method.invoke(bean, listener);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}