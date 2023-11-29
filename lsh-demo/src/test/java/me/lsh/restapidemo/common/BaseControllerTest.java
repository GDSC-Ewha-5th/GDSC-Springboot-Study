package me.lsh.restapidemo.common;

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
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
@Ignore //테스트를 가지고 있는 클래스로 간주되지 않도록(실행하려고 하면 안 됨)
public class BaseControllerTest {

    @Autowired
    protected MockMvc mockMvc; //가짜 요청 만들어서 보내고 응답 확인할 수 있는 Test 만들수있음.
    // Slicing Test 웹과 관련된 빈들만 테스트. dispatch sublet이란거 만들어야 함, 웹서버 띄우지x

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected ModelMapper modelMapper;

}
