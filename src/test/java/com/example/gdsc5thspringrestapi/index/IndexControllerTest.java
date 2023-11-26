package com.example.gdsc5thspringrestapi.index;

import com.example.gdsc5thspringrestapi.common.BaseControllerTest;
import com.example.gdsc5thspringrestapi.common.RestDocsConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc //mockMVC를 쓰기 위해
//@AutoConfigureRestDocs  //RestDocs 적용
//@Import(RestDocsConfiguration.class)  //다른 spring bean 설정 파일을 읽어와서 사용
//@ActiveProfiles("test") //테스트용 설정파일 적용
public class IndexControllerTest extends BaseControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    public void index() throws Exception{
        this.mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("_links.events").exists());
    }
}
