# :gift: Spring Data Jpa


## :question: Spring Data JPA란?
- Spring에서 제공하는 모듈 중 하나로, 개발자가 JPA를 더 쉽고 편하게 사용할 수 있도록 도와주는 모듈이다.
- 이는 JPA를 한 단계 추상화시킨 Repository라는 인터페이스를 제공함
- 사용자가 Repository 인터페이스에 정해진 규칙대로 메소드를 입력하면, Spring이 알아서 해당 메소드 이름에 적합한 쿼리를 날리는 구현체를 만들어서 Bean으로 등록한다.
- 데이터 접근 계층을 개발할 때 구현 클래스 없이 인터페이스만 작성해도 개발할 수 있다.
