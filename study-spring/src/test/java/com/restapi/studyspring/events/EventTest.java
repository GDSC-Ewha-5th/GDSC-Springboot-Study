package com.restapi.studyspring.events;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class EventTest {

    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Inflearn Strping REST API")
                .description("REST API development with Spring")
                .build();
        assertThat(event).isNull();
    }

    @Test
    public void javaBean() {
        // Given
        String name = "Event";
        String descroption = "Spring";

        // When
        Event event = new Event();
        event.setName(name);
        event.setDescription(descroption);

        // Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(descroption);
    }
}