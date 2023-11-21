package com.example.gdsc5thspringrestapi.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;

@JsonComponent   //objectMapper에 등록
public class ErrorsSerializer extends JsonSerializer<Errors> {//errors를 json으로 변환. errors는 bean의 스펙을 따르지 않으므로
    @Override
    public void serialize(Errors errors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeFieldName("errors"); //https://www.inflearn.com/questions/72123/%EC%9D%B8%EB%8D%B1%EC%8A%A4-%EB%A7%8C%EB%93%A4%EA%B8%B0-%EC%97%90%EC%84%9C-errorsresource-%EB%B6%80%EB%B6%84-%EC%A7%88%EB%AC%B8%EC%9E%85%EB%8B%88%EB%8B%A4
        jsonGenerator.writeStartArray();
        errors.getFieldErrors().stream().forEach(e -> {
            try{
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("field", e.getField());
                jsonGenerator.writeStringField("objectName", e.getObjectName());
                jsonGenerator.writeStringField("code", e.getCode());
                jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());
                Object rejectedValue = e.getRejectedValue();
                if (rejectedValue != null){
                    jsonGenerator.writeStringField("rejectedValue", rejectedValue.toString());
                }
                jsonGenerator.writeEndObject();
            }catch(IOException e1){
                e1.printStackTrace();
            }
        });

        errors.getGlobalErrors().forEach(e -> {
            try{
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("objectName", e.getObjectName());
                jsonGenerator.writeStringField("code", e.getCode());
                jsonGenerator.writeStringField("defaultMessage", e.getDefaultMessage());
                jsonGenerator.writeEndObject();
            }catch(IOException e1){
                e1.printStackTrace();
            }

        });
        jsonGenerator.writeEndArray();
    }


}
