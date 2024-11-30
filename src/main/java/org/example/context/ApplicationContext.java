package org.example.context;

import org.example.annotations.Component;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Component Scan을 기반으로 빈을 등록하고 관리하는 ApplicationContext.
 */
public class ApplicationContext {
    private final Map<String, Object> singletonObjects = new HashMap<>();

    public ApplicationContext(String basePackage) throws Exception {
        scanComponents(basePackage);
    }

    // 지정된 패키지에서 @Component 클래스 스캔
    private void scanComponents(String basePackage) throws Exception {
        String path = basePackage.replace('.', '/');
        File directory = new File(getClass().getClassLoader().getResource(path).toURI());

        for (File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String className = basePackage + "." + file.getName().replace(".class", "");
                Class<?> clazz = Class.forName(className);

                if (clazz.isAnnotationPresent(Component.class)) {
                    String beanName = clazz.getSimpleName();
                    Object instance = createBean(clazz);
                    singletonObjects.put(beanName, instance);
                }
            }
        }
    }

    // 빈 생성
    private Object createBean(Class<?> clazz) throws Exception {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        return constructor.newInstance();
    }

    // 빈 반환
    public Object getBean(String beanName) {
        return singletonObjects.get(beanName);
    }

    // Getter for singletonObjects
    public Map<String, Object> getSingletonObjects() {
        return singletonObjects;
    }
}
