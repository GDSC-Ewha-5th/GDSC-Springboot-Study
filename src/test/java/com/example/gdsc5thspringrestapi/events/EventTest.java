package com.example.gdsc5thspringrestapi.events;


import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.assertj.core.api.Assertions;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;

import java.util.stream.Stream;

@RunWith(JUnitParamsRunner.class)
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


    @ParameterizedTest
    @MethodSource("provideStringFortestFree")
    public void testFree(int basePrice, int maxPrice, boolean isFree){
        //given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();

        //when
        event.update();

        //then
        Assertions.assertThat(event.isFree()).isEqualTo(isFree);

    }

//    //junit5 에서는 실행 안되는 애들
//    @Test
//    @Parameters(method = "parametersForTestFree2")
//    public void testFree2(int basePrice, int maxPrice, boolean isFree){
//        //given
//        Event event = Event.builder()
//                .basePrice(basePrice)
//                .maxPrice(maxPrice)
//                .build();
//
//        //when
//        event.update();
//
//        //then
//        Assertions.assertThat(event.isFree()).isEqualTo(isFree);
//
//    }

    private Object[] parametersForTestFree2(){
        return new Object[]{
                new Object[] {0,0,true},
                new Object[] {100,0,false},
                new Object[] {0, 100, false},
                new Object[] {100, 200, false}
        };
    }


    @ParameterizedTest
    @MethodSource("provideStringFortestOffline")
    public void testOffline(String location, boolean isOffline){
        //given
        Event event = Event.builder()
                .location("강남역 네이버 D2 스타텁 팩토리")
                .build();

        //when
        event.update();

        //then
        Assertions.assertThat(event.isOffline()).isTrue();

    }

    private static Stream<Arguments> provideStringFortestFree(){
        return Stream.of(
                Arguments.of(0,0,true),
                Arguments.of(100,0,false),
                Arguments.of(0,100,false),
                Arguments.of(100, 200, false)
        );
    }

    private static Stream<Arguments> provideStringFortestOffline(){
        return Stream.of(
                Arguments.of("강남", true),
                Arguments.of(null, false),
                Arguments.of("  ", false)
        );
    }

}