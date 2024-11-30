package org.example.context;

import org.example.annotations.Autowired;
import org.example.annotations.Component;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

public class ApplicationContext {
    private final Map<String, Object> singletonObjects = new HashMap<>(); // Singleton 저장소
    private final Map<Class<?>, Class<?>> beanDefinitions = new HashMap<>(); // Bean 정의 저장소
    private final Map<Class<?>, List<Class<?>>> dependencyGraph = new HashMap<>(); // 의존 객체 그래프
    private final Set<Class<?>> visiting = new HashSet<>(); // 탐색 중인 Bean
    private final Set<Class<?>> visited = new HashSet<>(); // 탐색 완료된 Bean

    public ApplicationContext(String basePackage) throws Exception {
        scanComponents(basePackage); // @Component 클래스 스캔
        initializeBeans(); // 의존 객체를 순서에 따라 초기화
    }

    // 지정된 패키지에서 @Component 클래스를 스캔하여 Bean 정의 저장
    private void scanComponents(String basePackage) throws Exception {
        String path = basePackage.replace('.', '/');
        File directory = new File(getClass().getClassLoader().getResource(path).toURI());

        for (File file : directory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".class")) {
                String className = basePackage + "." + file.getName().replace(".class", "");
                Class<?> clazz = Class.forName(className);

                if (clazz.isAnnotationPresent(Component.class)) {
                    beanDefinitions.put(clazz, clazz);
                    dependencyGraph.put(clazz, new ArrayList<>()); // 의존 객체 그래프 초기화
                }
            }
        }

        // 의존 객체 그래프 생성
        for (Class<?> clazz : beanDefinitions.keySet()) {
            Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
            for (Class<?> parameterType : constructor.getParameterTypes()) {
                dependencyGraph.get(clazz).add(parameterType);
            }
        }
    }

    // 의존 객체를 순서에 따라 초기화
    private void initializeBeans() throws Exception {
        for (Class<?> clazz : beanDefinitions.keySet()) {
            if (!visited.contains(clazz)) {
                dfsInitialize(clazz);
            }
        }
    }

    // DFS를 사용하여 의존 객체 초기화
    private void dfsInitialize(Class<?> clazz) throws Exception {
        if (visited.contains(clazz)) {
            return; // 이미 초기화된 경우 종료
        }

        if (visiting.contains(clazz)) {
            throw new IllegalStateException("Circular dependency detected for Bean: " + clazz.getSimpleName());
        }

        visiting.add(clazz); // 현재 탐색 중인 상태로 표시

        // 의존 객체부터 초기화
        for (Class<?> dependency : dependencyGraph.get(clazz)) {
            dfsInitialize(dependency);
        }

        // 현재 Bean 생성 및 등록
        createAndInitializeBean(clazz);

        visiting.remove(clazz); // 탐색 중 상태 제거
        visited.add(clazz); // 탐색 완료 상태로 표시
    }

    // Bean 생성 및 초기화
    private void createAndInitializeBean(Class<?> clazz) throws Exception {
        String beanName = clazz.getSimpleName();

        // 이미 초기화된 경우 반환
        if (singletonObjects.containsKey(beanName)) {
            return;
        }

        // Bean 생성
        Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
        Object[] parameters = Arrays.stream(constructor.getParameterTypes())
                .map(paramType -> singletonObjects.get(paramType.getSimpleName()))
                .toArray();

        Object instance = constructor.newInstance(parameters);

        // 의존성 주입
        injectDependencies(instance);

        // Singleton 저장소에 등록
        singletonObjects.put(beanName, instance);
    }

    // @Autowired 어노테이션 기반 의존성 주입
    private void injectDependencies(Object bean) throws Exception {
        for (Method method : bean.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Autowired.class)) {
                Class<?> parameterType = method.getParameterTypes()[0];
                Object dependency = singletonObjects.get(parameterType.getSimpleName());
                if (dependency == null) {
                    throw new IllegalStateException("Dependency not satisfied: " + parameterType.getSimpleName());
                }
                method.setAccessible(true);
                method.invoke(bean, dependency);
            }
        }
    }

    // Bean 가져오기
    public <T> T getBean(Class<T> beanClass) {
        Object bean = singletonObjects.get(beanClass.getSimpleName());
        if (bean == null) {
            throw new IllegalStateException("No bean found for class: " + beanClass.getName());
        }
        return beanClass.cast(bean);
    }
}
