package me.chaerryos.demoinflearnrestapi.Events;

import lombok.*;

import java.time.LocalDateTime;

// lombok annotation 은 메타 annotation 사용이 불가능
// entity 에는 Data annotation 안 쓰는 게 좋다. 
@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
public class Event {

    private Integer id;
    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // optional
    private int basePrice;
    private int maxPrice;
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    private EventStatus eventStatus;

}
