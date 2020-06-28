Spring
=============

## JPA

**> 왜 JPA를 학습해야 하는가**
1. 도메인 주도 개발 가능
* 애플리케이션의 코드가 SQL 데이터베이스 관련 코드에 잠식 당하는 것을
방지하고 도메인 기반의 프로그래밍으로 비즈니스 로직을 구현하는데 집중할
수 있습니다.

2. 그리고 개발 생산성에 좋으며, 데이터베이스에 독립적인 프로그래밍이 가능하고, 타입
세이프한 쿼리 작성 그리고 Persistent Context가 제공하는 캐시 기능으로 성능
최적화까지 가능합니다.

**> JDBC란?**
* 데이터베이스에 접속 할때 데이터베이스서버와 클라이언트서버를 이어주는 연결고리
1. JDBC를 사용할 때 쓰는 것
* DataSource / DriverManager 데이터베이스의 드라이버에 접근하기 위한 DriverManager
* Connection 으로 데이터베이스 정보와 연결
* PreparedStatement로 미리 쿼리문을 작성함으로써 코드를 간결하게 유지
* **EX)코드**
```
@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		DataSource ds = new DataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://localhost/spring-Test?characterEncoding=utf8");
		ds.setUsername("spring5");
		ds.setPassword("spring5");
		ds.setInitialSize(2);
		ds.setMaxActive(10);
		ds.setTestWhileIdle(true);
		ds.setMinEvictableIdleTimeMillis(60000 * 3);
		ds.setTimeBetweenEvictionRunsMillis(10 * 1000);
		
		return ds;
	}
  ```
**> JDBC의 단점**
1. SQL을 실행하는 비용이 비싸다.
2. SQL이 데이터베이스 마다 다르다.
3. 스키마를 바꿨더니 코드가 너무 많이 바뀌네...
4. 반복적인 코드가 너무 많아.
5. 당장은 필요가 없는데 언제 쓸 줄 모르니까 미리 다 읽어와야 하나...

**> JPA를 사용하는 이유(도메인 모델)**
1. 객체 지향 프로그래밍의 장점을 활용하기 좋으니까.
2. 각종 디자인 패턴
3. 코드 재사용
4. 비즈니스 로직 구현 및 테스트 편함.

**> ORM이란?**
+ 애플리케이션의 클래스와 SQL 데이터베이스의 테이블 사이의 맵핑 정보를 기술한
메타데이터 를 사용하여, 자바 애플리케이션의 객체를 SQL 데이터베이스의 테이블에
자동으로 (또 깨끗하게) 영속화 해주는 기술입니다.

**> JPA의 장단점**
1. 생산성 : 코드를 간결하게 표현할 수 있고, 중복되는 코드를 줄일 수 있다.
2. 유지보수성 : 코드가 간결하기 때문에 변경해야 될 경우에도 수정하는 부분이 적다.
3. 성능 : 순수한 성능측면에서는 JPA가 느릴 수도 있지만, 쿼리의 내용이 동일한 경우 ex) password 변경을 하는데 전과 동일한 password 값을 날린 경우 JPA가 동일한 값의 쿼리는 실행하지 않아 불필요한 실행을 방지함
4. 벤더 독립성 : DB 마다 SQL 문법이 조금씩 차이가 있는데 DB를 바꿀때 마다 조금씩 수정해 주어야되는 불편함이 있었지만, JPA는 어떤 DB를 쓰는지 알려주기만 하면 JPA가 변환해 준다.

+단점.. : 학습비용이 크다.. 여러가지 알아야하는 사항이 많다(하지만 장점이 너무 크다 배우는게 좋다^^)


[백기선의 JPA를 학습하고 정리한 내용입니다.](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-%EB%8D%B0%EC%9D%B4%ED%84%B0-jpa/dashboard)
