package GDSC.ewha.springrestapi.events;

import lombok.*;

import java.time.LocalDateTime;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
public class Event {

    private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime; // 등록 시작 일시
    private LocalDateTime closeEnrollmentDateTime; // 등록 종료 일시
    private LocalDateTime beginEventDateTime; // 이벤트 시작 일시
    private LocalDateTime endEventDateTime; // 이벤트 종료 일시

    private String location; // (optional) 위치 : 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;

    private Integer id; // 식별자
    private boolean offline; // 오프라인 여부
    private boolean free; // 이 모임이 유료인지 무료인지
    private EventStatus eventStatus = EventStatus.DRAFT;
}
