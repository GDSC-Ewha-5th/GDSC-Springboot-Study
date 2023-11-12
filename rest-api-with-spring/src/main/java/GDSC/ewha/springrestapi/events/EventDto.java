package GDSC.ewha.springrestapi.events;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EventDto {

    @NotEmpty
    private String name;
    @NotEmpty
    private String description;
    @NotNull
    private LocalDateTime beginEnrollmentDateTime; // 등록 시작 일시
    @NotNull
    private LocalDateTime closeEnrollmentDateTime; // 등록 종료 일시
    @NotNull
    private LocalDateTime beginEventDateTime; // 이벤트 시작 일시
    @NotNull
    private LocalDateTime endEventDateTime; // 이벤트 종료 일시
    private String location; // (optional) 위치 : 이게 없으면 온라인 모임
    @Min(0) // 최소 0 이상
    private int basePrice; // (optional)
    @Min(0)
    private int maxPrice; // (optional)
    @Min(0)
    private int limitOfEnrollment;
}
