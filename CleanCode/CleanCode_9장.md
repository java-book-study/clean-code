# 9장 테스트

- 애자일과 TDD 덕분에 단위 테스트를 자동화하는 프로그래머들이 많아졌고, 점점 늘어나는 추세이다.

- **TDD 법칙 세 가지**

    1. ​실패하는 단위 테스트를 작성할 때까지 실제 코드를 작성하지 않는다.
    2. 컴파일은 실패하지 않으면서 실행이 실패하는 정도로만 단위 테스트를 작성한다.
    3. 현재 실패하는 테스트를 통과할 정도로만 실제 코드를 작성한다.  

    - 하지만 이렇게 코드를 작성한다면, 실제 코드를 전부 테스트하는 테스트 케이스가 나온다.         
    - 실제 코드와 맞먹을 정도로 방대한 테스트 코드는 심각한 관리 문제를 유발하기도 한다.

- **깨끗한 테스트 코드 유지하기**

    - **테스트 코드가 지저분하면 코드를 변경하는 능력이 떨어지며 코드 구조를 개선하는 능력도 떨어진다.**  
    - 실제 코드가 변경되면, 테스트 코드도 변해야 한다.  
    - 즉, 테스트 코드는 실제 코드 못지않게 중요하다.  
    - 테스트 케이스가 있다면 코드를 변경했을 때 버그가 숨어들까 걱정하지 않아도 된다.  
    - 즉, 깨끗한 단위테스트를 작성하여 실제 코드에 유연성, 유지보수성, 재사용성을 제공할 수 있다.
    
    <br>

    1. **깨끗한 테스트 코드**

     - 깨끗한 테스트 코드에는 **가독성**이 필요하다.   
     - 가독성을 높이기 위해서는 3가지, **명료성, 단순성, 풍부한 표현력**이 필요하다.

    SerializedPageResponderTest.java
     ```java
    SimpleResponse response;

    public void testGetPageHieratchyAsXml() throws Exception {

        makePages("PageOne", "PageOne.ChildOne", "PageTwo");

        submitRequest("root", "type:pages");

        assertResponseIsXML();
        assertResponseContains(
            "<name>PageOne</name>", "<name>PageTwo</name>", "<name>ChildOne</name>"
        );
    } 

    private void makePages(String... elements) throws Exception {
        for(String element : elements) {
            crawler.addPage(root, PathParser.parse(element));
        }
    }

    private void submitRequest(resource, input) throws Exception {
        request.setResource(resource);
        StringTokenizer stringTokenizer = new StringTokenizer(input, ":");

        if(stringTokenizer.countTokens() != 2)
            throw new Exception();

        request.addInput(stringTokenizer.nextToken(), stringTokenizer.nextToken());

        Responder responder = new SerializedPageResponder();
        this.response = (SimpleResponse) responder.makeResponse(
            new FitNesseContext(resource), request
        );
    }

    private void assertResponseIsXML() throws Exception {
        assertEquals("text/xml", response.getContentType());
    }

    private void assertResponseContains(String... elements) throws Exception {
        String xml = this.response.getContent();

        for(String element : elements) {
            assertSubString(element, xml);
        }
    }
     ```
    makePages(String... elements): 테스트 자료를 만든다.  
    submitRequest(resource, input): 테스트 자료를 조작한다.  
    assert~~: 조작한 결과가 올바른지 확인한다.  
    
    코드를 읽는 사람은 세세한 코드를 알 필요없이, testGetPageHieratchyAsXml 메서드만 읽으면 된다.

    build-operate-check pattern
    > Build: 테스트 데이터를 빌드  
    Operate: 테스트 데이터 조작  
    Check: 조작을 검증  
    
    <br>

    2. **도메인에 특화된 언어**
    
    - 시스템 조작 API를 한번 감싼 함수와 유틸리티를 사용하여 테스트 코드를 작성하거나 읽기 쉽게 만들자.
    - 이렇게 구현한 코드는 테스트 코드에서 사용하는 특수 API가 되어 작성자나 구독자 모두를 도와주는 테스트 언어가 된다.  

    DSL( Domain-Specific-Languages )
    > 관련 특정 분야에 최적화된 프로그래밍 언어.  
    즉, 어떤 목적이 있고 그 목적만을 달성할 수 있는 언어
    
    3. **이중 표준**

    - 테스트 환경은 런타임 환경과 다르게 자원이 제한적일 가능성이 낮다.  
    즉, 테스트 코드는 단순하고 간결하고, 표현력이 풍부해야하지만 효율적일 필요는 없다.
    - 런타임 환경에서는 아래 코드를 StringBuffer를 사용하는 코드로 바꿔야 하지만, 테스트 환경에서는 바꿀 필요가 없다.
    ``` java
    public String getState() {
        String state = "";
        state += header ? "H" : "h";
        state += blower ? "B" : "b";
        state += cooler ? "C" : "c";
        state += hiTempAlarm ? "H" : "h";
        state += loTempAlarm ? "L" : "l";
        return state;
    }
    ```

- **테스트당 assert 하나**

    - Junit으로 테스트 코드를 짤 때는 함수마다 assert문을 단 하나만 사용해야 한다.
    - '단일 assert문'은 훌륭한 지침이지만, 필요하다면 여러 개의 assert문을 사용해도 좋다.  
    하지만, assert문 갯수를 최대한 줄여야 한다는 건 잊지말자.
    - 위에서 작성한 testGetPageHieratchyAsXml 메서드는 2 종류의 assert문이 있는데, 아래와 같이 나눌 수 있다.  
    중복된 코드가 생기는 것이 거슬린다면, Template Method 패턴을 이용하여 중복을 줄일 수 있다.
    
    SerializedPageResponderTest.java
    ``` java
    public void testGetPageHierarchyAsXml() throws Exception {
        givenPages("PageOne", "PageOne.ChildOne", "PageTwo");
        whenRequestIsIssued("root", "type:pages");
        thenResponseShouldBeXML();
    }

    public void testGetPageHierarchyHasRightTags() throws Exception {
        givenPages("PageOne", "PageOne.ChildOne", "PageTwo");
        whenRequestIsIssued("root", "type:pages");
        thenResponseShouldContain(
            "<name>PageOne</name>", "<name>PageTwo</name>", "<name>ChildOne</name>"
        );
    }
    ```

    <small>참고) Template Method Pattern</small>  
    GOF
    > 알고리즘의 구조를 메소드에 정의하고, 하위 클래스에서 알고리즘 구조의 변경없이 알고리즘을 재정의 하는 패턴이다. 알고리즘이 단계별로 나누어 지거나, 같은 역할을 하는 메소드이지만 여러곳에서 다른형태로 사용이 필요한 경우 유용한 패턴이다.

    토비
    >상속을 통해 슈퍼클래스의 기능을 확장할 때 사용하는 가장 대표적인 방법. 변하지 않는 기능은 슈퍼클래스에 만들어두고 자주 변경되며 확장할 기능은 서브클래스에서 만들도록 한다. 

    <br>

    1. **테스트당 개념 하나**

    - 테스트 함수마다 한 개념만 테스트하라. 한 함수에 여러 개념이 있으면 독자가 읽기 힘들어진다.
    ``` java
    public void testAddMonths() {
        SerialDate d1 = SerialDate.createInstance(31, 5, 2004);

        SerialDate d2 = SerialDate.addMonths(1, d1);
        // d2가 2004년, 6월, 30일이 맞는지 확인하는 assert문

        SerialDate d3 = SerialDate.addMonths(2, d1);
        // d3가 2004년, 7월, 31일이 맞는지 확인하는 assert문

        SerialDate d4 = SerialDate.addMonths(1, SerialDate.addMonths(1, d1));
        // d4가 2004년, 7월, 30일이 맞는지 확인하는 assert문
    }
    ```

    ```java
    public void addMonthTest_LastDay_31_30_Is_30() {
        SerialDate d1 = SerialDate.createInstance(31, 5, 2004);
        SerialDate serialDate = SerialDate.addMonths(1, d1);
        // serialDate가 2004년, 6월, 30일이 맞는지 확인하는 assert문
    }

    public void addMonthTest_LastDay_31_31_Is_31() {
        SerialDate d1 = SerialDate.createInstance(31, 5, 2004);
        SerialDate serialDate = SerialDate.addMonths(2, d1);
        // serialDate가 2004년, 7월, 31일이 맞는지 확인하는 assert문
    }

    public void addMonthTest_LastDay_31_30_31_Is_30() {
        SerialDate d1 = SerialDate.createInstance(31, 5, 2004);
        SerialDate serialDate = SerialDate.addMonths(1, SerialDate.addMonths(1, d1));
        // serialDate가 2004년, 7월, 30일이 맞는지 확인하는 assert문
    }
    ```
    - assert문이 여러 개인건 문제가 되지 않는다.  
    하지만, 한 테스트 함수에서 여러 개념을 테스트하는 것은 지양하자.

    - 결론은, **개념당 assert 문을 최소로 줄이고 테스트 함수 하나는 개념 하나만 테스트하라**

- F.I.R.S.T.

    깨끗한 테스트는 다음 다섯가지 규칙을 따른다.
    - Fast: 테스트는 빨라야 한다.  
    - Independent: 각 테스트는 서로 의존하면 안된다. 테스트는 독립적으로 실행해야 한다.
    - Repeatable: 테스트는 **어떤 환경**에서도 반복(실행) 가능해야 한다. 
    - Self-Validating: 테스트는 T/F 결과를 내야한다. 수작업 평가가 필요한 테스트를 만들면 안된다.
    - Timely: 테스트 코드는 실제 코드를 구현하기 직전에 작성한다.

- **결론**

    - 테스트 코드는 지속적으로 깨끗하게 관리하자.  
    - 표현력을 높이고 간결하게 정리하자.  
    - 테스트 API를 구현해 DSL을 만들자.