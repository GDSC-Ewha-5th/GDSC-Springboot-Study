package com.example.springstudymavenver.events;


import junitparams.JUnitParamsRunner;
import org.junit.Test;

import junitparams.Parameters;
import org.junit.runner.RunWith;


import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API developement with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBeen(){
        Event event = new Event();
        String name = "Event";
        String description = "Spring";

        event.setName(name);
        event.setDescription(description);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }


    @Test
    @Parameters
    public void testFree(int basePrice, int maxPrice, boolean isFree){
        //Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        //When
        event.update();

        //Then
        assertThat(event.isFree()).isEqualTo(isFree);

    }

    private Object[] parametersForTestFree(){
        return new Object[]{
                new Object[]{0,0,true},
                new Object[]{100,0,false},
                new Object[]{0,100,false},
                new Object[]{100,200,false},
        };
    }


    //오프라인여부
    @Test
    @Parameters
    public void testOffLine(String location, boolean isOffline){
        //Given
        Event event = Event.builder()
                .location(location)
                .build();
        //When
        event.update();

        //Then
        assertThat(event.isOffline()).isEqualTo(isOffline);

    }

    private Object[] parametersForTestOffLine(){
        return new Object[]{
                new Object[]{"강남", true},
                new Object[]{null,false},
                new Object[]{"      ",false}
        };
    }

}