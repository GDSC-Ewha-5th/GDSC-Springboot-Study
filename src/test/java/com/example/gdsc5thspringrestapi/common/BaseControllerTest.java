package com.example.gdsc5thspringrestapi.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc //mockMVC를 쓰기 위해
@AutoConfigureRestDocs  //RestDocs 적용
@Import(RestDocsConfiguration.class)  //다른 spring bean 설정 파일을 읽어와서 사용
@ActiveProfiles("test") //테스트용 설정파일 적용
@Ignore  //test를 가지고 있는 class로 간주되지 않도록
public class BaseControllerTest {
    @Autowired
    protected MockMvc mockMvc;  //mocking되어 있는 dispatcherServlet을 상대로 가짜 요청을 만들어서 응답을 확인할 수 있음. 계층별로 나누어서 웹과 관련된 Bean만 등록함. 더 빠름.


    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ModelMapper modelMapper;
}
