# 7장 오류 처리

- 깨끗한 코드와 오류 처리는 확실히 연관성이 있다.

- 오류 처리는 중요하다. 하지만 오류 처리 코드로 인해 프로그램 논리를 이해하기 어려워진다면 깨끗한 코드라 부르기 어렵다.

- **오류코드보다 예외를 사용하라**

  1. ​	예외를 지원하지 않는 프로그래밍 언어가 많았다. 예외를 지원하지 않는 언어는 오류를 처리하고 보고하는 방법이 제한적이었다.

     

     목록 7-1은 이와 같은 방법을 보여준다

     ```java
     public class DeviceCntroller{
     	...
     	public void sendShutDown(){
     		DeviceHandle handle = getHandle(DEV1);
     		// 디바이스 상태를 점검한다.
             if (handle != DeviceHandle.INVALID){
                 // 레코드 필드에 디바이스 상태를 저장한다.
                 retrieveDeviceRecord(handle);
                 //디바이스가 일시정지 상태가 아니라면 종료한다.
                 if (record.getStatus() != DEVICE_SUSPENDED){
                     pauseDevice(handle);
                     clearDeviceWorkQueue(handle);
                     closeDevice(handle);
                 }else{
                     logger.log("Device suspended. Unable to shut down");
                 }
             }else{
                 logger.log("Invalid handle for: " + DEV1.toString());
         
             }
     	}
         ...
     }
     ```

     

     위와 같은 방법을 사용하면 호출자 코드가 복잡해 진다. 함수를 호출한 즉시 오류를 확인해야 하기 때문이다. 오류가 발생하면 예외를 던지는 편이 낫다. 그러면 호출자 코드가 더 깔끔해진다. 논리와 오류 처리 코드가 뒤섞이지 않으니까...

     

     **목록 7-2 **

     ```java
     public class DeviceController{
         ...
         public void sendShutDown(){
             try{
                 tryToShutDown(); // 함수로 분리
             }catch (DeviceShutDownError e){
                 logger.log(e); // 예외처리
             }
         }
         
         private void tryToShutDown() throws DeviceShutDownError{
             DeviceHandle handle = getHandle(DEV1); // 디바이스 상태점검
             DeviceRecord record = retrieveDeviceRecord(handle); 
             // 레코드 필드에 디바이스 상태 저장
             pauseDevice(handle); // 디바이스 정지상태 확인
             clearDeviceWorkQueue(handle); 
             closeDevice(handle); // 디바이스 종료
         }
         
         private DeviceHandle getHandle(DeviceID id){
             ...
             throw new DeviceShutDownError("Invalid handle for: " + id.toString());
             // 디바이스가 이상이 있으면 에러를 생성해서 던진다.
             ...
         }
         ...
     }
     ```

     코드가 깨끗해지고 품질이 나아졌다. 앞서 뒤섞였던 개념,  종료하는 알고리즘과 오류를 처리하는 알고리즘을 분리했기 때문이다. 

     

- **Try - Catch - Finally 문부터 작성하라**

  Try 블록에 들어가는 코드를 실행하면 어느 시점에서든 실행이 중단 된 후 Catch 블록으로 넘어갈 수 있다.

  <u>어떤 면에서 Try 블록은 트랜잭션과 비슷하다. Try 블록에서 무슨 일이 생기든지 Catch 블록은 프로그램 상태를 일관성 있게 유지해야 한다.</u>

  예외가 발생할 코드를 짤 때는 Try-Catch-Finally 문으로 시작하는 편이 낫다. 그러면 Try 블록에서 무슨일이 생기든지 호출자가 기대하는 상태를 정의하기 쉬워진다.

  1.  처음에 먼저 try - catch 구조로 강제로 예외를 일으키는 테스트 케이스를 작성한 후 테스트를 통과하게 코드를 작성하는 방법을 권장한다. 그러면 자연스럽게 try 블록의 트랜잭션 범위부터 구현하게 되므로 트랜잭션 본질을 유지하기 쉬워진다.

     ```java
     public List<RecordedGrip> retrieveSection(String sectionName){
         try{
             FileInputStream = new FileInputStream(sectionName);
             // 나머지 논리.. 오류나 예외가 전혀 발생하지 않는다고 가정
             stream.close();
         }catch(FileNotFoundException e){
             throw new StorageException("retrieval error", e);
         }
         return new ArrayList<RecordedGrip>();
     }
     ```

  

- **미확인 예외를 사용하라**

  

  **미확인 예외란?**

  *checked 예외* 는 컴파일 단계에서 확인되며 반드시 처리해야 하는 예외입니다.

  - IOException
  - SQLException

  *Unchecked 예외* 는 실행 단계에서 확인되며 명시적인 처리를 강제하지는 않는 예외입니다.

  - NullPointerException
  - IllegalArgumentException
  - IndexOutOfBoundException
  - SystemException

  #### 미확인 예외의 단점

  - 메서드를 선언할 때 메서드가 반환할 예외를 모두 열거해야 하기 때문에 *메서드 유형의 일부* 가 됨

  - OCP (Open Closed Principle)을 위반

    - 확인된 예외는 예상되는 모든 예외를 사전에 처리할 수 있다는 장점이 있지만, 일반적인 애플리케이션은 의존성이라는 비용이 이익보다 더 크다.

    > 소프트웨어 개발 작업에 이용된 많은 모듈 중에 하나에 수정을 가할 때 그 모듈을 이용하는 다른 모듈을 줄줄이 고쳐야 한다면, 이와 같은 프로그램은 수정하기가 어렵다. 개방-폐쇄 원칙은 시스템의 구조를 올바르게 재조직(리팩토링)하여 나중에 이와 같은 유형의 변경이 더 이상의 수정을 유발하지 않도록 하는 것이다. 개방-폐쇄 원칙이 잘 적용되면, 기능을 추가하거나 변경해야 할 때 이미 제대로 동작하고 있던 원래 코드를 변경하지 않아도, 기존의 코드에 새로운 코드를 추가함으로써 기능의 추가나 변경이 가능하다.

  1. 아래 코드는 단순한 출력을 하는 메소드이다.

     ```java
      public void printA(bool flag) {
          if(flag)
              System.out.println("called");
      }
     
      public void func(bool flag) {
          printA(flag);
      }
     ```

  2. 문득 아 프린트를 안할 때 *NotPrintException* 을 던지기로 구현을 변경했을 때,

     ```java
      public void printA(bool flag) throws NotPrintException {
          if(flag)
              System.out.println("called");
          else
              throw new NotPrintException();
      }
     
      public void func(bool flag) throws NotPrintException {
          printA(flag);
      }
     ```

  해당 함수 뿐만이 아니라 호출하는 함수도 수정을 해줘야 하기 때문에 *OCP* 를 위반하게 된다.



- **예외에 의미를 제공하라**

  - 예외를 던질 때는 전후 상황을 충분히 덧붙인다. 그러면 오류가 발생한 원인과 위치를 찾기가 쉬워진다.  오류 메세지에 정보를 담아 예외와 함께 던지는 것이 좋다.

    

- **호출자를 고려해 예외 클래스를 정의하라**

  - 오류를 분류하는 방법은 수없이 많다.

  - 아래 코드는 외부 라이브러리를 호출하고 모든 예외를 호출자가 잡아내고 있습니다.

    ```java
     ACMEPort port = new ACMEPort(12);
    
     try {
         port.open();
     } catch (DeviceResponseException e) {
         reportPortError(e);
         logger.log("Device response exception", e);
     } catch (ATM1212UnlockedException e) {
         reportPortError(e);
         logger.log("Unlock exception", e);
     } catch (GMXError e) {
         reportPortError(e);
         logger.log("Device response exception");
     } finally {
         ...
     }
    ```

  - 호출 라이브러리 API를 감싸 한가지 예외 유형을 반환하는 방식으로 단순화 할 수 있다.

    위 경우는 예외에 대응하는 방식이 예외 유형과 무관하게 거의 동일함

    ```java
     LocalPort port = new LocalPort(12);
     try {
         port.open();
     } catch (PortDeviceFailure e) {
         reportError(e);
         logger.log(e.getMessage(), e);
     } finally {
         ...
     }
    ```

    ```java
     public class LocalPort {
         private ACMEPort innerPort;
    
         public LocalPort(int portNumber) {
             innerPort = new ACMEPort(portNumber);
         }
    
         public void open() {
             try {
                 innerPort.open();
             } catch (DeviceResponseException e) {
                 throw new PortDeviceFailure(e);
             } catch (ATM1212UnlockedException e) {
                 throw new PortDeviceFailure(e);
             } catch (GMXError e) {
                 throw new PortDeviceFailure(e);
             }
         }
         ...
     }
    ```

  - 외부 API를 감싸면 아래와 같은 장점이 있다.
    1. 에러 처리가 간결해짐
    2. 외부 라이브러리와 프로그램 사이의 의존성이 크게 줄어듦
    3. 프로그램 테스트가 쉬워짐
    4. 외부 API 설계 방식에 의존하지 않아도 됨

  

- **정상 흐름을 정의하라**

  다음은 총계를 계산하는 허술한.. 코드입니다.

  ```java
   try {
       MealExpenses expenses = expenseReportDAO.getMeals(employee.getID());
       m_total += expenses.getTotal();
   } catch(MealExpencesNotFound e) {
       m_total += getMealPerDiem();
   }
  ```

  1. 식비를 비용으로 청구했다면 직원이 청구한 식비를 총계에 더한다. (Try문)
  2. 식비를 비용으로 청구하지 않았다면 일일 기본 식비를 총계에 더한다. (Catch문)

  

  *getTotal* 메소드에 예외 시 처리를 넣어 클라이언트 코드를 간결하게 처리합니다.

  ```java
   public class PerDiemMealExpenses implements MealExpenses {
       public int getTotal() {
           // 청구한 식비가 없다면 기본값으로 일일 기본 식비를 반환한다.
           // (예외가 아닌)
       }
   }
  ```

  ```java
   MealExpenses expenses = expenseReportDAO.getMeals(employee.getID());
   m_total += expenses.getTotal();
  ```

  이렇게 처리 하는 것을 *특수 사례 패턴* 이라고 합니다. 클래스를 만들거나 객체를 조작해 특수 사례를 처리하는 방식이다. 그러면 클라이언트 코드가 예외적인 상황을 처리할 필요가 없어진다. 클래스나 객체가 예외적인 상황을 캡슐화 해서 처리하니까...

  

- **null을 반환하지 마라**

  - null을 반환하는 습관을 하면 안된다.

```java
public void registerItem(Item item) {
	if (item != null) {
		ItemRegistry registry = peristentStore.getItemRegistry();
		if (registry != null) {
			Item existing = registry.getItem(item.getID());
			if (existing.getBillingPeriod().hasRetailOwner()) {
				existing.register(item);
			}
		}
	}
}
```

위 코드는 나쁜코드다.

1. null을 반환하는 코드는 일거리를 늘릴 뿐만 아니라 호출자에게 문제를 떠넘긴다.
2. 누구라도 null 확인을 빼먹는다면 애플리케이션이 통제 불능에 빠질지도 모른다.
3. null 확인이 너무 많아서 문제



차라리 예외를 던지거나 특수 사례 객체를 반환하는 것이 좋습니다.

```java
// bad
List<Employee> employees = getEmployees();
if(employees != null) {
	for(Employee e : employees) {
		totalPay += e.getPay();
	}
}

// good
List<Employee> employees = getEmployees();
for(Employee e : employees) {
	totalPay += e.getPay();
}

public List<Employee> getEmployees() {
	if (..직원이 없다면..) // 특수 사례 객체 반환
		return Collections.emptyList();
}
```



- **null을 전달하지 마라**

  1. null을 반환하는 방식 보다 메서드로 null을 전달하는 방식은 더 나쁘다.
  2. 정상적인 인수로 null을 기대하는 API가 아니라면 메서드로 null을 전달하는 코드는 최대한 피한다.

  ```java
  public class MetricsCalculator{
      public double xProjection(Point p1, Point p2){
          return (p2.x - p1.x) * 1.5;
      }
      ...
  }
  ```

  누군가 null을 인수로 전달하면?

  ```java
  calculator.xProjection(null, new Point(12, 13));
  ```

  **NullPointorException** 발생

  이런 경우 assert 문이나 메서드 안에 조건으로 null 이 인수로 들어오면 InvalidArgumentException을 처리할 수 있겠지만 애초에 null을 넘기지 못하도록 금지하는 정책이 합리적이다.

- **결론**

  깨끗한 코드는 읽기도 좋아야 하지만 안정성도 높아야 한다. 이 둘은 상충하는 목표가 아니다. 오류처리를 프로그램 논리와 분리하면 독립적인 추론이 가능해 지며 코드 유지보수성도 크게 높아진다.



# 8장 경계

시스템에 들어가는 모든 소프트웨어를 직접 개발하는 경우는 드물다. 때로는 패키지를 사고, 때로는 오픈소스를 이용한다. 어떤 식으로든 이 외부코드를 우리코드에 깔끔하게 통합해야만 한다. 이 장에서는 소프트웨어 경계를 깔끔하게 처리하는 기법과 기교를 살펴본다.



- **외부코드 사용하기**

패키지 제공자나 프레임워크 제공자는 적용성을 최대한 넓히려 애쓴다. 더 많은 환경에서 돌아가야 더 많은 고객이   구매하기 때문이다. 하지만 사용자는 자신의 요구에 집중하는 인터페이스를 바란다. 이런 차이로 인해 시스템 경계에서 문제가 생길 소지가 많다.



*Map*은 굉장히 다양한 인터페이스로 수많은 기능을 제공한다. Map이 제공하는 기능성과 유연성은 확실히 유용하지만 그만큼 위험도 크다.

1.  프로그램에서 Map을 만들어 여기저기 넘긴다면, 다른 Map을 지울 수도 있다.
2. 설계시 특정 객체 유형만 저장하기로 결정했지만, Map 자체는 객체 유형을 제한하지 않는다. 마음만 먹으면 사용자는 다른 어떤 객체 유형도 추가할 수 있다.



Sensor 라는 객체를 담는 Map을 만들려면 다음과 같이 Map을 생성한다.

```java
Map sensors = new HashMap();
```

Sensor 객체가 필요한 코드는 다음과 같이 Sensor 객체를 가져온다.

```java
Sensor s = (Sensor)sensors.get(sensorId);
```



위와 같은 코드가 한번이 아니라 여러 차례 나온다. 즉, Map이 반환하는 Object를 올바른유형으로 변환할 책임은 Map을 사용하는 클라이언트에 있다. 다음과 같이 제네릭스를 사용하면 코드 가독성이 높아진다.



```java
Map<String, Sensor> sensors = new HashMap<Sensor>();
...
Sensor s = sensors.get(sensorId);
```

하지만 위 방법도 사용자에게 필요하지 않은 기능까지 제공한다는 문제는 해결하지 못한다.

인터페이스가 변할 가능성이 없다 여길 지도 모르지만 자바 5가 제네릭스를 지원하면서 Map 인터페이스가 변했다



다음은 Map을 좀 더 깔끔하게 사용한 코드다.

```java
public class Sensors{
    private Map sensors = new HashMap();
    
    public Sensor getById(String id){
        return (Sensor) sensors.get(id);
    }
    
    // 이하 생략
}
```

경계 인터페이스인 Map을 Sensors 안으로 숨긴다. 이제 제네릭스를 사용하든 않든 문제가 안된다.

Sensors 클래스 안에서 객체 유형을 관리하고 변환하기 때문이다. 

Map 클래스를 사용할 때마다 캡슐화를 하라는 소리가 아니다 Map을(혹은 유사한 경계 인터페이스를) 여기저기 넘기지 말라는 뜻이다.



- **경계 살피고 익히기**

외부 코드를 익히기는 어렵다. 외부 코드를 통합하기도 어렵다. 두가지를 동시에 하기는 두배나 어렵다. 다르게 접근하면 우리쪽 코드를 작성해 외부 코드를 호출하는 대신 먼저 간단한 테스트 케이스르 작성해 외부 코드를 익히는게 낫다. 짐 뉴커크는 이를 *학습테스트* 라 부른다. 학습테스트는 API를 사용하려는 목적에 초점을 맞춘다.



- **log4j 익히기**



로깅 기능을 직접 구현하는 대신 아파치의 *log4j* 패키지를 사용하려 한다고 가정하자.

1. 패키지를 내려 받아 소개 페이지를 연다.

2. *문서를 자세히 읽기 전에*  첫 번째 테스트 케이스를 작성한다.

   ```java
    // 화면에 "hello"를 출력하는 테스트 케이스이다.
    @Test
    public void testLogCreate() {
        Logger logger = Logger.getLogger("MyLogger");
        logger.info("hello");
    }
   ```

3. 테스트 케이스를 돌려본다.

   - Appender라는 뭔가가 필요하다는 오류가 발생한다.

4. 문서를 읽어보니 *ConsoleAppender* 라는 클래스가 있다. 그래서 *ConsoleAppender* 를 생선한 후 테스트 케이스를 다시 돌린다.

   ```java
    @Test
    public void testLogAddAppender() {
        Logger logger = Logger.getLogger("MyLogger");
        ConsoleAppender appender = new ConsoleAppender();
        logger.addAppender(appender);
        logger.info("hello");
    }
   ```

   Appender에 출력 스트림이 없다는 사실을 발견한다.

5. 구글을 검색한 후 아래와 같이 시도한다.

   ```java
    @Test
    public void testLogAddAppender() {
        Logger logger = Logger.getLogger("MyLogger");
        logger.removeAllAppenders();
        logger.addAppender(new ConsoleAppender(
            new PatternLayout("%p %t %m%n"),
            ConsoleAppender.SYSTEM_OUT));
        logger.info("hello");
    }
   ```

   잘 돌아간다.

6. 테스트 케이스를 짜는 과정에서 *log4j* 의 동작을 많이 이해했고 이 지식을 바탕으로 단위 테스트 케이스 몇 개를 작성한다.

   ```java
    public class LogTest {
        private Logger logger;
   
        @Before
        public void initialize() {
            logger = Logger.getLogger("logger");
            logger.removeAllAppenders();
            Logger.getRootLogger().removeAllAppenders();
        }
   
        @Test
        public void basicLogger() {
            BasicConfigurator.configure();
            logger.info("basicLogger");
        }
   
        @Test
        public void addAppenderWithStream() {
            logger.addAppender(new ConsoleAppender(
                new PatternLayout("%p %t %m%n"),
                ConsoleAppender.SYSTEM_OUT));
            logger.info("addAppenderWithStream");
        }
   
        @Test
        public void addAppenderWithoutStream() {
            logger.addAppender(new ConsoleAppender(
                new PatternLayout("%p %t %m%n")));
            logger.info("addAppenderWithoutStream");
        }
    }
   ```

7. 모든 지식을 통해 Logger 클래스로 캡슐화한다.



- **학습 테스트는 공짜 이상이다.**

  *학습 테스트* 는 드는 비용은 없지만 필요한 지식만 확보할 수 있는 손쉬운 방법입니다.

  - 패키지가 예상대로 도는지 검증하고, 통합 이후에도 주기적으로 검증이 가능
    - 패키지의 새 버전이 나오면 학습 테스트만 돌려 차이가 있는지만 확인하면 동작을 보장할 수도 있음
    - 호환되지 않을 경우 코드를 수정하든 패키지를 수정하든 조치를 미리 취할 수 있음
  - 경계 테스트와 함께라면 버전 변경도 두렵지 않다.
    - 필요 이상으로 낡은 버전을 사용하려는 유혹에서 빠져나올 수 있음



- **아직 존재하지 않는 코드를 사용하기**

  지금 알지 못하는 코드 영역을 개발할 때도 경계는 유용하게 쓰일 수 있습니다.

  - 필요한 인터페이스를 정의/구현하면 전적으로 통제가 가능해짐
  - 테스트도 간편하게 진행할 수 있음



- **깨끗한 경계**
  1.  소프트 웨어 설계가 우수하다면 변경하는데 많은 투자와 재작업이 필요하지 않다.
  2. 경계에 위치하는 코드는 깔끔하게 분리한다. 또한 기대치를 정의하는 테스트케이스를 작성
  3. 통제 불가능한 외부 패키지에 의존하는 대신 통제가 가능한 우리 코드에 의존하는 편이 좋다
  4. 외부 패키지를 호출하는 코드를 가능한 줄여 경계를 관리하자
  5. 새로운 클래스로 경계를 감싸거나 Adapter 패턴을 사용해 우리가 원하는 인터페이스를 패키지가 제공하는 인터페이스로 변환하자