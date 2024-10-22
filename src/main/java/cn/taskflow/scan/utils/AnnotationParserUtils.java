package cn.taskflow.scan.utils;

import java.lang.annotation.Annotation;
import java.util.Optional;

public class AnnotationParserUtils {

    /**
     * Get the specified annotation from the target class. If the annotation is not present on the target class,
     * it will try to find the annotation in the superclass or interfaces of the target class.
     *
     * @param targetClass the class to search for the annotation
     * @param annotationType the type of the annotation to find
     * @param <T> the type of the annotation
     * @return an Optional containing the annotation if found, otherwise an empty Optional
     */
    public static <T extends Annotation> Optional<T> getAnnotation(Class<?> targetClass, Class<T> annotationType) {
        Optional<T> optional = Optional.ofNullable(targetClass.getAnnotation(annotationType));
        if (optional.isPresent()) {
            return optional;
        }
        T bindDataSource = tryFindSupperClazz(targetClass, annotationType);
        if (bindDataSource == null) {
            bindDataSource = tryFindInterfaceClazz(targetClass, annotationType);
        }
        return Optional.ofNullable(bindDataSource);
    }

    /**
     * Try to find the specified annotation in the superclass of the given class.
     *
     * @param rawType the class whose superclass will be searched
     * @param annotationType the type of the annotation to find
     * @param <T> the type of the annotation
     * @return the annotation if found, otherwise null
     */
    static <T extends Annotation> T tryFindSupperClazz(Class<?> rawType, Class<T> annotationType) {
        Class<?> supperClazz = rawType.getSuperclass();
        while (supperClazz != null) {
            T annotation = supperClazz.getAnnotation(annotationType);
            if (annotation != null) {
                return annotation;
            }
            supperClazz = supperClazz.getSuperclass();
        }
        return null;
    }

    /**
     * Try to find the specified annotation in the interfaces of the given class.
     *
     * @param rawType the class whose interfaces will be searched
     * @param annotationType the type of the annotation to find
     * @param <T> the type of the annotation
     * @return the annotation if found, otherwise null
     */
    static <T extends Annotation> T tryFindInterfaceClazz(Class<?> rawType, Class<T> annotationType) {
        for (Class<?> inter : rawType.getInterfaces()) {
            T annotation = inter.getAnnotation(annotationType);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }
}
