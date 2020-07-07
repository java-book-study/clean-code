# 10장 클래스

- 코드의 표현력과 함수에 아무리 신경 쓰더라도 클래스를 신경쓰지 않으면 깨끗한 코드를 얻기 어렵다.  
깨끗한 클래스에 대해서 알아보자.

- **클래스 체계**

    - 변수 목록 -> 함수 목록
    - 변수
      1. static public 
      2. static private 
      3. private
    - 함수
      1. public method1
      2. method1이 호출하는 private method
      3. public method2
      4. method2가 호출하는 private method

    **캡슐화**

    - 변수와 유틸리티 함수는 가능하면 숨기는 것이 좋다.
    - 만약 테스트에 필요하다면 protected로 선언해서 필요한 변수나 함수에 접근할 수 있도록 하자.

- **클래스는 작아야 한다!**

    - 얼마나 작아야 하는가?
      - 함수가 물리적인 행 수로 크기를 측정한다
      - 클래스는 맡은 책임을 기준으로 삼는다.
    - 클래스 이름은 해당 클래스의 책임을 기술한다.
      - 클래스 이름이 모호하다면 클래스의 책임이 많다는 의미다.
    
    **단일 책임 원칙**

    - 클래스나 모듈을 변경할 이유는 단 하나뿐이어야 한다. (SRP, 단일 책임 원칙)
    - 클래스가 크든 작든 봐야할 코드의 양은 비슷하다. 
    > 도구 상자를 어떻게 관리하고 싶은가? 
    작은 서랍을 많이 두고 기능과 이름이 명확한 컴포넌트를 나눠 넣고 싶은가? 
    아니면 큰 서랍 몇 개를 두고 모두를 던져 넣고 싶은가?
    - 다목적 클래스는 당장 알 필요가 없는 사실까지 들이밀어 코드 읽는것을 방해한다.
    - 아래와 같이 하나의 책임만을 가진 클래스는 재사용하기 매우 쉬운 구조이다.
    ``` java
    public class Version {
        public int getMajorVersionNumber()
        public int getMinorVersionNumber()
        public int getBuildNumber()
    }
    ```

    **응집도**

    - 인스턴스 변수의 수는 적어야 한다.
    - 메서드가 변수를 많이 사용할수록 클래스의 응집도가 높다는 것을 의미한다.
    - 몇몇 메서드만이 사용하는 인스턴스 변수가 많다면, 클래스를 분리해야 한다는 신호이다.

    **응집도를 유지하면 작은 클래스 여럿이 나온다**

    - 큰 함수의 일부를 작은 함수로 나눠보자.
      - 작은 함수에서 큰 함수의 변수를 많이 사용해야 한다면, 인수로 넘기지 말고 클래스 인스턴스 변수로 승격하라.
    - 응집력을 잃는다면 클래스를 분리하라.
      - 몇몇 메서드만이 사용하는 인스턴스 변수가 많다면, 해당 메서드들은 독립적인 클래스로 분리하라.
    - 큰 함수를 작은 함수 여럿으로 나누기만 해도 클래스가 많아진다.
      - 재구현을 뜻하는 것이 아니다!
      1. **큰 함수의 동작을 검증하는 테스트 슈트를 작성하라.**
      2. 한번에 하나씩 수 차례에 걸쳐 조금씩 코드를 변경하라.
      3. 코드를 변경할 때마다 테스트를 수행해 원래 프로그램과 동일하게 동작하는지 확인하라. 

  **변경하기 쉬운 클래스**

  - Sql이라는 클래스에 모든 메서드를 넣는것보다는, 아래와 같이 클래스를 분리하는 것이 좋다.
    - 함수 하나를 수정한다고 다른 함수가 망가질 위험이 사라진다.
    - 클래스가 분리되어 테스트 관점에서 모든 논리를 증명하기도 쉬워졌다.
    - 단일 책임 원칙(SRP)과 개방 폐쇄 원칙(OCP)를 따른다.  

  ``` java
  abstract public class Sql {
    public Sql(String table, Column[] columns)
    abstract public String generate();
  }

  public class CreateSql extends Sql {
    public CreateSql(String table, Column[] columns)
    @Override public String generate()
  }

  public class SelectSql extends Sql {
    public SelectSql(String table, Column[] columns)
    @Override public String generate()
  }

  .
  .
  .

  public class PreparedInsertSql extends Sql {
    public PreparedInsertSql(String table, Column[] columns)
    @Override public String generate()
    private String placeholderList(Column[] columns)
  }

  public class Where {
    public Where(String criteria)
    public String generate()
  }

  public class ColumnList {
    public ColumnList(Column[] columns)
    public String generate()
  }
  ```

  **변경으로부터 격리**

  - 요구사항이 변하면 코드도 따라 변한다.
    - 인터페이스와 추상 클래스를 사용하여 구현이 미치는 영향을 격리할 수 있다.
  - 상세한 구현에 의존하는 코드는 테스트가 어렵다.
    - 환율과 같이 변화하는 값을 이용하는 코드의 경우, 수시로 값이 변하기 때문에 API로 테스트 코드를 작성하는 것은 쉽지 않다.
  ``` java
  public interface StockExchange {
    Money currentPrice(String symbol);
  }

  public Class TokyoStockExchange implements StockExchange {
    ...
  }

  public Class Portfolio {
    private StockExchange exchange;
    
    public Portfolio(StockExchange exchange) {
      this.exchange = exchange;
    }
    .
    .
  }
  ```
  - 시스템의 결합도를 낮추면 유연성과 재사용성이 높아지며 테스트가 쉬워진다.
    1. 위 코드에서 TokyoStockExchange 클래스를 흉내내는 테스트용 클래스를 만들어, 고정된 주가를 반환한다.
    2. 테스트용 클래스는 언제나 정해놓은 표 값만 참조하여 전체 포트폴리오 총계가 맞는지 확인하는 테스트 코드를 작성할 수 있게된다. 
  - 이처럼 결합도를 줄이면 클래스 설계 원칙인 의존 역전 원칙(DIP)를 따르는 클래스가 만들어진다.


- **결론**

  - 클래스는 작아야한다.
  - 작다는 기준은 클래스가 가진 책임을 의미한다.