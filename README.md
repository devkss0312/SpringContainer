# **Spring 스타일의 경량 DI 프레임워크**

이 프로젝트는 **Spring의 Component Scan과 의존성 주입(`@Autowired`)을 모방한 경량 DI(Dependency Injection) 프레임워크입니다. 순수 Java로 구현된 이 프레임워크는 학습 목적으로 설계되었으며, Spring의 핵심 개념을 간단히 재현합니다.


## **주요 기능**

- **컴포넌트 스캔**: 특정 패키지의 `@Component`로 정의된 클래스를 자동으로 스캔하여 빈으로 등록.
- **의존성 주입**: `@Autowired`를 사용하여 필드에 의존성을 주입.
- **경량 설계**: Spring의 복잡한 설정 없이 DI 및 빈 관리 기능 제공.
- **확장 가능성**: 라이프사이클 관리, 스코프, 사용자 정의 어노테이션 등의 기능 추가 가능.


## **동작 원리**
### 1. 컴포넌트 스캔
- 기본 패키지 설정: ApplicationContext는 지정된 패키지 경로에서 @Component로 어노테이션된 모든 클래스를 탐색합니다.
- 클래스 탐색 및 등록: 지정된 경로의 클래스 파일을 스캔합니다.
- @Component 어노테이션이 붙어있는 클래스를 식별합니다.
- 해당 클래스를 인스턴스화하여 빈으로 등록합니다.
- 빈 관리: 생성된 객체는 singletonObjects라는 맵에 저장되어 관리되며, 빈 이름은 클래스의 단순 이름을 사용합니다.
### 2. 의존성 주입
- 빈 순회: DependencyInjector는 ApplicationContext에서 관리하는 모든 빈을 순회합니다.
- 필드 탐색:
각 빈에서 @Autowired 어노테이션이 붙은 필드를 찾습니다.
- 해당 필드의 타입에 맞는 빈을 ApplicationContext에서 검색합니다.
### 3. 주입 수행:
- 리플렉션을 사용하여 필드에 의존성을 주입합니다.
- 이 과정은 접근 제어자를 무시하고(private 필드 등) 주입을 가능하게 합니다.
- 의존성 해결: 모든 빈의 의존성이 해결되면 애플리케이션 실행 준비가 완료됩니다.
### 4. 실행
- 빈 가져오기: ApplicationContext에서 필요한 빈을 가져옵니다.
- 로직 실행: 의존성이 주입된 객체의 메서드를 호출하여 애플리케이션의 주요 로직을 실행합니다.
- 예시 흐름:
  - ServiceA가 ServiceB를 의존성으로 주입받습니다.
  - ServiceA의 execute() 메서드를 호출하면, 내부적으로 ServiceB의 execute() 메서드가 호출됩니다.
- 이를 통해 빈 간의 의존성이 실제로 동작함을 확인할 수 있습니다.


## **설치 및 빌드**
- 필요 조건
  - Java 17 이상
  - Gradle 8.0 이상
- 빌드
```bash
gradle clean build
```
- 애플리케이션 실행
```bash
gradle run
```

## **확장 가능성

- 빈 스코프: 싱글톤 외에 프로토타입, 요청 스코프 등 다양한 스코프 지원.
- 라이프사이클 관리: @PostConstruct 및 @PreDestroy와 같은 라이프사이클 콜백 지원.
- 순환 의존성 감지: 두 개 이상의 빈이 서로를 의존할 때 발생하는 문제를 탐지하고 해결.
- 사용자 정의 어노테이션: @Service, @Repository 등 역할 기반 어노테이션 추가로 세분화된 빈 관리.
- 예외 처리 개선: 빈 생성 및 의존성 주입 과정에서 발생할 수 있는 다양한 예외 상황에 대한 상세한 처리.