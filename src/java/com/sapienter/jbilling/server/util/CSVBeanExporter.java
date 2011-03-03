/*
 jBilling - The Enterprise Open Source Billing System
 Copyright (C) 2003-2011 Enterprise jBilling Software Ltd. and Emiliano Conde

 This file is part of jbilling.

 jbilling is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 jbilling is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with jbilling.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.sapienter.jbilling.server.util;

import com.sapienter.jbilling.server.util.db.AbstractDescription;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Converts a list of objects to a CSV string.
 *
 * @author Brian Cowdery
 * @since 02/03/11
 */
public class CSVBeanExporter<T> {
    private static final Logger LOG = Logger.getLogger(CSVBeanExporter.class);

    private static final List<Class<?>> IGNORED_TYPES = Arrays.asList(
            Collection.class,
            Class.class
    );

    private List<PropertyDescriptor> descriptors;

    private CSVBeanExporter(Class<T> type) {
        this.descriptors = describe(type);
    }

    /**
     * Creates a new CSVBeanExporter for the given type. This static factory method avoids
     * the annoying need to specify the same type twice when using a generic typed constrctor
     * (e.g., <code>new CSVBeanExporter<Type>(Type.class)</code>).
     *
     * @param type type of bean exporter
     * @param <T> type T
     * @return new exporter of type T
     */
    public static <T> CSVBeanExporter<T> createExporter(Class<T> type) {
        return new CSVBeanExporter<T>(type);
    }

    /**
     * Describes a class, producing a list of PropertyDescriptor objects for all simple properties
     * of the class. Properties that are arrays, or collections of elements are not described, and
     * will be omitted from the return list.
     *
     * @param type class to describe
     * @return returns a list of simple property descriptors
     */
    public static List<PropertyDescriptor> describe(Class type) {
        List<PropertyDescriptor> descriptors = Arrays.asList(PropertyUtils.getPropertyDescriptors(type));
        descriptors = new ArrayList<PropertyDescriptor>(descriptors);

        // remove arrays and collections of objects, leaving only simple properties
        for (Iterator<PropertyDescriptor> it = descriptors.iterator(); it.hasNext();) {
            PropertyDescriptor descriptor = it.next();

            Class<?> propertyType = descriptor.getPropertyType();
            if (propertyType.isArray()) {
                it.remove();
            }

            if (IGNORED_TYPES.contains(propertyType)) {
                it.remove();
            }

            List<Class<?>> propertyInterfaces = Arrays.asList(propertyType.getInterfaces());
            if (!Collections.disjoint(propertyInterfaces, IGNORED_TYPES)) {
                it.remove();
            }
        }

        return descriptors;
    }

    /**
     * Exports a list of objects as a CSV.
     *
     * @param list list of objects
     * @return csv string
     */
    public String export(List<T> list) {
        StringBuilder csv = new StringBuilder();
        csv.append(getHeader()).append("\n");

        try {
            for (T bean : list) {
                csv.append(getLine(bean)).append("\n");
            }
        } catch (IllegalAccessException e) {
            LOG.error("Property read method is not accessible.", e);
            return null;

        } catch (InvocationTargetException e) {
            LOG.error("An exception occurred while invoking the property read method.", e);
            return null;
        }

        return csv.toString();
    }

    /**
     * Builds a CSV header line for the described type.
     *
     * @return csv header line
     */
    public String getHeader() {
        StringBuilder header = new StringBuilder();

        for (Iterator<PropertyDescriptor> it = descriptors.iterator(); it.hasNext();) {
            header.append(getFieldName(it.next().getReadMethod().getName()));

            if (it.hasNext())
                header.append(",");
        }

        return header.toString();
    }

    /**
     * Builds a CSV row line for the described type and the given object.
     *
     * @param bean bean to build a line from
     * @return csv row line
     * @throws IllegalAccessException thrown if bean getter method is null
     * @throws InvocationTargetException thrown if bean getter method throws an unhandled exception
     */
    public String getLine(T bean) throws IllegalAccessException, InvocationTargetException {
        StringBuilder line = new StringBuilder();

        for (Iterator<PropertyDescriptor> it = descriptors.iterator(); it.hasNext();) {
            Object value = it.next().getReadMethod().invoke(bean);
            line.append(getStringValue(value));

            if (it.hasNext())
                line.append(",");
        }

        return line.toString();
    }

    private String getStringValue(Object value) {
        if (value == null)
            return "";

        // attempt to get a description for the value
        if (value instanceof AbstractDescription)
            return ((AbstractDescription) value).getDescription();

        // attempt to get an ID for the value
        try {
            Integer id = (Integer) PropertyUtils.getProperty(value, "id");
            if (id != null)
                return id.toString();

        } catch (NoSuchMethodException e) {
            /* ignore */
        } catch (InvocationTargetException e) {
            /* ignore */
        } catch (IllegalAccessException e) {
            /* ignore */
        }

        // convert to a string
        return ConvertUtils.convert(value);
    }

    private String getFieldName(String methodName) {
        methodName = methodName.replaceFirst("(get|is)", "");
        return methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
    }
}
