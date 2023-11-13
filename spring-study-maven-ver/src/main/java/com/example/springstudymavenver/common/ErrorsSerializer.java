package com.example.springstudymavenver.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;

import java.io.IOException;


@JsonComponent //Object Mapper에 등록해야 함 -> 요 어노테이션이 해줌. 이제 object mapper는 Errors를 serialize할 때 얘를 사용함
public class ErrorsSerializer extends JsonSerializer<Errors> {
    @Override
    public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {

        gen.writeStartArray();
        //Field에러 담아주기
        errors.getFieldErrors().stream().forEach(e-> {//각각 에러 마다
            try {
                //Json object 채우기!
                gen.writeStartObject();
                gen.writeStringField("field", e.getField());
                gen.writeStringField("objectName", e.getObjectName());
                gen.writeStringField("code", e.getCode());
                gen.writeStringField("defaultMessage", e.getDefaultMessage());
                Object rejectedValue = e.getRejectedValue();//있을수도 없을수도
                if(rejectedValue!=null){ //있으면 넣어줘!
                    gen.writeStringField("rejectedValue", rejectedValue.toString());
                }
                gen.writeEndObject();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } );

        //글로벌 메세지 채우기
        errors.getGlobalErrors().forEach(e->{
            try {
                gen.writeStartObject();
                gen.writeStringField("objectName", e.getObjectName());
                gen.writeStringField("code", e.getCode());
                gen.writeStringField("defaultMessage", e.getDefaultMessage());
                gen.writeEndObject();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        gen.writeEndArray();

    }

}
