package org.example.context;

import org.example.annotations.Autowired;

import java.lang.reflect.Field;

/**
 * 의존성 주입 처리 클래스.
 */
public class DependencyInjector {
    private final ApplicationContext context;

    public DependencyInjector(ApplicationContext context) {
        this.context = context;
    }

    public void injectDependencies() throws Exception {
        for (Object bean : context.getSingletonObjects().values()) { // Getter 사용
            Field[] fields = bean.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    String dependencyName = field.getType().getSimpleName();
                    Object dependency = context.getBean(dependencyName);
                    field.set(bean, dependency);
                }
            }
        }
    }
}
