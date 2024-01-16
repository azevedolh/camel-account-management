package br.com.teste.camelaccountmanagement.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DTOTester {

    private static final String DTO_PACKAGE = ".dto";
    private static final String ENTITY_PACKAGE = ".entity";
    private static final String MODEL_PACKAGE = ".model";
    private static final String GETTER_METHOD_PREFIX = "get";
    private static final String IS_METHOD_PREFIX = "is";
    private static final String SETTER_METHOD_PREFIX = "set";
    private static final String DOUBLE_PRIMITIVE = "double";
    private static final String INT_PRIMITIVE = "int";
    private static final String BOOLEAN_PRIMITIVE = "boolean";
    private static final String FLOAT_PRIMITIVE = "float";
    private static final String LONG_PRIMITIVE = "long";
    private static final String CHAR_PRIMITIVE = "char";
    private static final String STRING_OBJECT = "String";
    private static final String DOUBLE_OBJECT = "Double";
    private static final String INTEGER_OBJECT = "Integer";
    private static final String BIG_DECIMAL_OBJECT = "BigDecimal";
    private static final String BOOLEAN_OBJECT = "Boolean";
    private static final String FLOAT_OBJECT = "Float";
    private static final String CHARACTER_OBJECT = "Character";
    private static final int DEFAULT_LIST_SIZE = 20;
    private static final String BUILDER_METHOD = "builder";
    private static final String BUILD_METHOD = "build";

    public static <T> void executeGetterSetterConstructor(Class<T> clazz) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        String packageName = clazz.getPackageName();
        if (packageName.contains(DTO_PACKAGE) || packageName.contains(ENTITY_PACKAGE) || packageName.contains(MODEL_PACKAGE)) {
            Constructor<T> constructor = clazz.getConstructor();
            T obj = constructor.newInstance();
            Method[] methods = clazz.getDeclaredMethods();
            List<Method> listMethods = Arrays.asList(methods);

            List<Method> getters = listMethods.stream()
                    .filter(m -> m.getName().startsWith(GETTER_METHOD_PREFIX) || m.getName().startsWith(IS_METHOD_PREFIX))
                    .collect(Collectors.toList());
            List<Method> setters = listMethods.stream()
                    .filter(m -> m.getName().startsWith(SETTER_METHOD_PREFIX))
                    .collect(Collectors.toList());

            for (Method setter : setters) {
                executeSetter(obj, setter);
            }

            for (Method getter : getters) {
                executeGetter(obj, getter);
            }

            executeConstructors(obj);
            executeBuilder(obj);

            obj.toString();
        }
    }

    private static void executeBuilder(Object obj) throws InvocationTargetException, IllegalAccessException {
        Field[] fields = obj.getClass().getDeclaredFields();
        List<Field> listFields = Arrays.asList(fields);
        List<String> fieldNames = listFields.stream().map(f -> f.getName()).collect(Collectors.toList());

        Method[] methods = obj.getClass().getDeclaredMethods();

        List<Method> listMethods = Arrays.asList(methods);
        Optional<Method> builderOpt = listMethods.stream().filter(m -> BUILDER_METHOD.equals(m.getName())).findFirst();

        if (builderOpt.isPresent()) {
            Method builderMethod = builderOpt.get();
            Object builder = builderMethod.invoke(obj);
            builder.toString();

            Method[] builderObjMethods = builder.getClass().getDeclaredMethods();
            List<Method> listObjMethods = Arrays.asList(builderObjMethods);
            Optional<Method> buildMethodOpt = listObjMethods.stream().filter(m -> BUILD_METHOD.equals(m.getName())).findFirst();

            if (buildMethodOpt.isPresent()) {
                List<Method> listBuilderMethods = listObjMethods.stream().filter(m -> fieldNames.contains(m.getName())).collect(Collectors.toList());

                builder = prepareParameters(builder, listBuilderMethods);
                Method buildMethod = buildMethodOpt.get();
                buildMethod.invoke(builder);
            }
        }
    }

    private static Object prepareParameters(Object builder, List<Method> methods) throws InvocationTargetException, IllegalAccessException {
        for (Method m : methods) {
            Parameter[] parameters = m.getParameters();
            List<Object> parameterList = prepareParameters(parameters);

            builder = m.invoke(builder, parameterList.toArray());
        }

        return builder;
    }

    private static void executeConstructors(Object obj) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?>[] constructors = obj.getClass().getConstructors();

        for(Constructor<?> c : constructors) {
            Class<?>[] parameterTypes = c.getParameterTypes();

            if (parameterTypes.length == 0) {
                c.newInstance();
            } else {
                List<Object> parametersInstance = new ArrayList<>(DEFAULT_LIST_SIZE);
                for (Class<?> p : parameterTypes) {
                    Object param = null;
                    String className = p.getSimpleName();
                    boolean primitiveType = isPrimitiveType(className);

                    if (primitiveType) {
                        param = getValueByPrimitiveType(className);
                    } else {
                        param = getInstanceByObjectType(className);
                    }

                    parametersInstance.add(param);
                }

                c.newInstance(parametersInstance.toArray());
            }
        }
    }

    private static void executeSetter(Object obj, Method setter) throws InvocationTargetException, IllegalAccessException {
        Parameter[] parameters = setter.getParameters();
        List<Object> parameterList = prepareParameters(parameters);

        setter.invoke(obj, parameterList.toArray());
    }

    private static List<Object> prepareParameters(Parameter[] parameters) {
        List<Object> parameterList = new ArrayList<>(DEFAULT_LIST_SIZE);
        for (Parameter p : parameters) {
            Object param = null;

            String typeName = p.getType().getSimpleName();
            boolean primitiveType = isPrimitiveType(typeName);

            if (primitiveType) {
                param = getValueByPrimitiveType(typeName);
            } else {
                param = getInstanceByObjectType(typeName);
            }

            parameterList.add(param);
        }

        return parameterList;
    }

    private static Object getInstanceByObjectType(String typeName) {
        Object param = null;

        switch (typeName) {
            case STRING_OBJECT:
                param = "";
                break;
            case DOUBLE_OBJECT:
                param = 00;
                break;
            case INTEGER_OBJECT:
                param = 0;
                break;
            case BIG_DECIMAL_OBJECT:
                param = BigDecimal.ZERO;
                break;
            case BOOLEAN_OBJECT:
                param = Boolean.TRUE;
                break;
            case FLOAT_OBJECT:
                param = 0f;
                break;
            case CHARACTER_OBJECT:
                param = 'a';
                break;
            default:
                break;
        }

        return param;
    }

    private static Object getValueByPrimitiveType(String typeName) {
        Object param = null;

        switch (typeName) {
            case DOUBLE_PRIMITIVE:
                param = 00;
                break;
            case INT_PRIMITIVE:
                param = 0;
                break;
            case BOOLEAN_PRIMITIVE:
                param = true;
                break;
            case FLOAT_PRIMITIVE:
                param = 0f;
                break;
            case LONG_PRIMITIVE:
                param = 0l;
                break;
            case CHAR_PRIMITIVE:
                param = 'a';
                break;
            default:
                break;
        }

        return param;
    }

    private static boolean isPrimitiveType(String typeName) {
        char characters = typeName.charAt(0);
        return Character.isLowerCase(characters);
    }

    private static void executeGetter(Object obj, Method getter) throws InvocationTargetException, IllegalAccessException {
        getter.invoke(obj);
    }


}
