package com.mickeytheq.hades.core.model.entity;

import com.mickeytheq.hades.core.model.common.Distance;
import com.mickeytheq.hades.core.model.common.Statistic;
import com.mickeytheq.hades.core.model.image.ImageProxy;
import com.mickeytheq.hades.core.project.configuration.CollectionInfo;
import com.mickeytheq.hades.core.project.configuration.EncounterSetInfo;
import com.mickeytheq.hades.serialise.discriminator.EmptyEntityDiscriminator;
import com.mickeytheq.hades.serialise.discriminator.EmptyValueDiscriminator;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.*;

public class AnnotatedEntityMetadataBuilder {
    private final Deque<Function<Object, Object>> flattenFunctions = new LinkedList<>();

    public static EntityMetadata build(Class<?> clazz) {
        return new AnnotatedEntityMetadataBuilder().createEntityMetadata(clazz);
    }

    private EntityMetadata createEntityMetadata(Class<?> clazz) {
        List<PropertyMetadata> propertyMetadata = createPropertyMetadatas(clazz);

        return new EntityMetadataImpl(clazz, propertyMetadata);
    }

    private List<PropertyMetadata> createPropertyMetadatas(Class<?> clazz) {
        BeanInfo beanInfo;
        try {
            beanInfo = Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }

        List<PropertyMetadata> propertyMetadataList = new ArrayList<>();

        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            Method readMethod = propertyDescriptor.getReadMethod();

            if (readMethod == null)
                continue;

            Property property = readMethod.getAnnotation(Property.class);

            if (property == null)
                continue;

            if (property.flatten()) {
                propertyMetadataList.addAll(createFlattenedPropertyMetadata(propertyDescriptor));
                continue;
            }

            if (isValueType(propertyDescriptor)) {
                propertyMetadataList.add(createValuePropertyMetadata(clazz, propertyDescriptor, property));
            }
            else {
                propertyMetadataList.add(createEntityPropertyMetadata(propertyDescriptor));
            }
        }

        return propertyMetadataList;
    }

    private String getPropertyName(PropertyDescriptor propertyDescriptor) {
        Property property = propertyDescriptor.getReadMethod().getAnnotation(Property.class);

        return property.value();
    }

    private List<PropertyMetadata> createFlattenedPropertyMetadata(PropertyDescriptor propertyDescriptor) {
        Function<Object, Object> flattenPropertyReadFunction = createReadFunctionFromReadMethod(propertyDescriptor.getReadMethod());

        flattenFunctions.addFirst(flattenPropertyReadFunction);
        try {
            return createPropertyMetadatas(propertyDescriptor.getPropertyType());
        }
        finally {
            flattenFunctions.removeFirst();
        }
    }

    private PropertyMetadata createValuePropertyMetadata(Class<?> owningClass, PropertyDescriptor propertyDescriptor, Property propertyAnnotation) {
        String propertyName = getPropertyName(propertyDescriptor);

        if (propertyDescriptor.getWriteMethod() == null)
            throw new RuntimeException("Declared property '" + propertyName + "' in class '" + owningClass.getName() + "' appears to be a value type but has no corresponding setter method");

        return new ValuePropertyMetadata(propertyName, propertyDescriptor.getPropertyType(),
                createReadFunction(propertyDescriptor),
                createWriteFunction(propertyDescriptor),
                createValueDiscriminator(propertyAnnotation.discriminator())
        );
    }

    private Function<Object, Object> createReadFunction(PropertyDescriptor propertyDescriptor) {
        Method readMethod = propertyDescriptor.getReadMethod();

        Function<Object, Object> readFunction = createReadFunctionFromReadMethod(readMethod);

        readFunction = createFlatteningFunction().andThen(readFunction);

        return readFunction;
    }

    private Function<Object, Object> createReadFunctionFromReadMethod(Method readMethod) {
        return o -> {
            try {
                return readMethod.invoke(o);
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                throw new RuntimeException("Error invoking read method '" + readMethod.getName() + "' in class '" + readMethod.getDeclaringClass().getName() + "'", e);
            }
        };
    }

    private BiConsumer<Object, Object> createWriteFunction(PropertyDescriptor propertyDescriptor) {
        Method writeMethod = propertyDescriptor.getWriteMethod();

        Function<Object, Object> flatteningFunction = createFlatteningFunction();

        BiConsumer<Object, Object> writeFunction = (owner, newValue) -> {
            Object actualOwner = flatteningFunction.apply(owner);

            try {
                writeMethod.invoke(actualOwner, newValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        };

        return writeFunction;
    }

    private Function<Object, Object> createFlatteningFunction() {
        Function<Object, Object> totalFlattenFunction = Function.identity();

        // add any flatten functions to perform the necessary navigation into composed objects
        // we want the function 'added' first to be the first in the function chain
        // the combination of adding new functions to the front (so the 'deeper' function is always at the start of the list)
        // and using compose() below we build the function stack backwards
        for (Function<Object, Object> flattenFunction : flattenFunctions) {
            totalFlattenFunction = totalFlattenFunction.compose(flattenFunction);
        }

        return totalFlattenFunction;
    }

    private EmptyValueDiscriminator createValueDiscriminator(Class<? extends EmptyValueDiscriminator> discriminatorClass) {
        try {
            return discriminatorClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private PropertyMetadata createEntityPropertyMetadata(PropertyDescriptor propertyDescriptor) {
        // when a transition occurs down the entity stack to a new entity the flatten stack must be cleared
        // before the new entity metadata is created, and then restored afterwards
        Deque<Function<Object, Object>> flattenFunctionsStackCopy = new LinkedList<>(flattenFunctions);
        flattenFunctions.clear();

        EntityMetadata entityMetadata;
        try {
            entityMetadata = createEntityMetadata(propertyDescriptor.getPropertyType());
        }
        finally {
            flattenFunctions.addAll(flattenFunctionsStackCopy);
        }

        Function<Object, Object> readFunction = createReadFunction(propertyDescriptor);

        return new EntityPropertyMetadataImpl(getPropertyName(propertyDescriptor), entityMetadata, readFunction);
    }

    private boolean isValueType(PropertyDescriptor propertyDescriptor) {
        Class<?> propertyType = propertyDescriptor.getPropertyType();

        if (propertyType.isPrimitive())
            return true;

        if (propertyType.isEnum())
            return true;

        if (String.class.isAssignableFrom(propertyType))
            return true;

        if (URL.class.isAssignableFrom(propertyType))
            return true;

        if (Number.class.isAssignableFrom(propertyType))
            return true;

        if (Boolean.class.isAssignableFrom(propertyType))
            return true;

        if (ImageProxy.class.isAssignableFrom(propertyType))
            return true;

        if (EncounterSetInfo.class.isAssignableFrom(propertyType))
            return true;

        if (CollectionInfo.class.isAssignableFrom(propertyType))
            return true;

        if (Distance.class.isAssignableFrom(propertyType))
            return true;

        if (Statistic.class.isAssignableFrom(propertyType))
            return true;

        return false;
    }

    static class EntityMetadataImpl implements EntityMetadata {
        private final Class<?> entityClass;
        private final List<PropertyMetadata> propertyMetadataList;

        public EntityMetadataImpl(Class<?> entityClass, List<PropertyMetadata> propertyMetadataList) {
            this.entityClass = entityClass;
            this.propertyMetadataList = propertyMetadataList;
        }

        @Override
        public Class<?> getEntityClass() {
            return entityClass;
        }

        @Override
        public List<PropertyMetadata> getProperties() {
            return propertyMetadataList;
        }
    }

    static class EntityPropertyMetadataImpl implements EntityPropertyMetadata {
        private final String name;
        private final EntityMetadata entityMetadata;
        private final Function<Object, Object> readFunction;

        public EntityPropertyMetadataImpl(String name, EntityMetadata entityMetadata, Function<Object, Object> readFunction) {
            this.name = name;
            this.entityMetadata = entityMetadata;
            this.readFunction = readFunction;
        }

        @Override
        public EntityMetadata getEntityMetadata() {
            return entityMetadata;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Class<?> getPropertyClass() {
            return entityMetadata.getEntityClass();
        }

        @Override
        public boolean shouldInclude(Object value) {
            if (value instanceof EmptyEntityDiscriminator)
                return !((EmptyEntityDiscriminator)value).isEmpty();

            return true;
        }

        @Override
        public Object getPropertyValue(Object parent) {
            return readFunction.apply(parent);
        }

        @Override
        public void setPropertyValue(Object parent, Object newValue) {
            throw new UnsupportedOperationException("Setting entity values is not supported. Default constructors of owning classes should initialise any child entities");
        }
    }

    static class ValuePropertyMetadata implements PropertyMetadata {
        private final String name;
        private final Class<?> propertyClass;
        private final Function<Object, Object> readFunction;
        private final BiConsumer<Object, Object> writeFunction;
        private final EmptyValueDiscriminator emptyValueDiscriminator;

        public ValuePropertyMetadata(String name, Class<?> propertyClass, Function<Object, Object> readFunction, BiConsumer<Object, Object> writeFunction, EmptyValueDiscriminator emptyValueDiscriminator) {
            this.name = name;
            this.propertyClass = propertyClass;
            this.readFunction = readFunction;
            this.writeFunction = writeFunction;
            this.emptyValueDiscriminator = emptyValueDiscriminator;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isValue() {
            return true;
        }

        @Override
        public boolean shouldInclude(Object value) {
            if (value instanceof EmptyEntityDiscriminator)
                return !((EmptyEntityDiscriminator)value).isEmpty();

            return !emptyValueDiscriminator.isEmpty(value);
        }

        @Override
        public boolean isEntity() {
            return false;
        }

        @Override
        public boolean isList() {
            return false;
        }

        @Override
        public Class<?> getPropertyClass() {
            return propertyClass;
        }

        @Override
        public Object getPropertyValue(Object parent) {
            return readFunction.apply(parent);
        }

        @Override
        public void setPropertyValue(Object parent, Object newValue) {
            writeFunction.accept(parent, newValue);
        }
    }
}
