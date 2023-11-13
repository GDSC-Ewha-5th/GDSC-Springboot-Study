package com.example.springstudymavenver.events;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Entity
public class Event {

    @Id
    @GeneratedValue
    private Integer id; //식별자

    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임 private int basePrice; // (optional)
    private int basePrice;
    private int maxPrice; // (optional)
    private int limitOfEnrollment;

    //추가 필드
    private boolean offline;
    private boolean free;

    @Enumerated(EnumType.STRING) //기본값이 ordinal(순서대로 정수값으로 저장됨- 추후 enum의 순서가 바뀌면 꼬일수 있음) 인데 String으로 바꿔주는게 좋음
    private EventStatus eventStatus = EventStatus.DRAFT;

}
