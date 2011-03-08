package de.jos.labelgenerator.beaninfo;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BeanInfoUtil {

	private static final Logger LOGGER = Logger.getLogger(BeanInfoUtil.class.getName());

	/**
	 * Checks the given classes for transient attributes and updates the
	 * BeanInfo accordingly. This is used to prevent the BSAF LocalStorage to
	 * serialize the transient attributes.
	 * 
	 * @param classes
	 */
	public static void markTransientAttributes(Class<?>... classes) {
		for (final Class<?> tmpClazz : classes) {
			try {
				final BeanInfo info = Introspector.getBeanInfo(tmpClazz);
				final PropertyDescriptor[] propertyDescriptors = info.getPropertyDescriptors();
				for (int i = 0; i < propertyDescriptors.length; ++i) {
					final PropertyDescriptor pd = propertyDescriptors[i];
					if (pd.getReadMethod().isAnnotationPresent(Transient.class)) {
						pd.setValue("transient", Boolean.TRUE);
						LOGGER.log(Level.INFO, "Marked {0} as transient.", pd.getName());
					}
				}
			} catch (IntrospectionException e) {
				LOGGER.log(Level.SEVERE, "Introspection of bean class {0} failed", tmpClazz.getName());
			}
		}
	}

}
