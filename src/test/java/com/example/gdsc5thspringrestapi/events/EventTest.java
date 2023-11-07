package com.example.gdsc5thspringrestapi.events;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class EventTest {

    @Test
    public void builder(){  //빌더가 있는지 확인하는 테스트
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API development with Spring")
                .build();
        Assertions.assertThat(event).isNotNull(); //assertj 사용
    }

    @Test
    public void javaBean(){
        //lombok에 의해 기본생성자 만들어지지 x. 모든 인자를 가진 생성자만 만들어짐. public이 아님 default임 -> 다른 패키지에서 Event에 대한 객체를 만들기 애매함

        //Given
        String name = "Event";
        String description = "Spring";

        //When
        Event event = new Event();
        event.setName(name);
        event.setDescription(description);

        //Then
        Assertions.assertThat(event.getName()).isEqualTo(name);
        Assertions.assertThat(event.getDescription()).isEqualTo(description);
    }

}