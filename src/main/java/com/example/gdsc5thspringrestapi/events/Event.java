package com.example.gdsc5thspringrestapi.events;

import lombok.*;

import java.time.LocalDateTime;

@Builder //입력하는 값이 무엇인지 명확하게 알 수 있는 장점을 가짐
@AllArgsConstructor  //모든 args를 가진 생성자
@NoArgsConstructor  //기본 생성자
@Getter
@Setter
@EqualsAndHashCode(of = "id") //객체 간의 연간관계가 있을 때 서로 상호참조하는 관계가 되면 equals와 hashcode로 구현한 코드 안에서 stackoverflow 발생할 수 있음. 따라서 id만 가지고 equals와 hashcode를 비교하겠다. 절대 다른 엔티티와 연관된 필드를 넣지 말기
public class Event {
    private Integer id;
    private boolean offline;
    private boolean free;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location;
    private int maxPrice;
    private int limitOfEnrollment;
    private EventStatus eventStatus;
}
