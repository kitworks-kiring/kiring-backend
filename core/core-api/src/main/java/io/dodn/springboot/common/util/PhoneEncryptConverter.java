package io.dodn.springboot.common.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PhoneEncryptConverter implements AttributeConverter<String, String> {

    @Override
    public String convertToDatabaseColumn(String attribute) {
        // DB에 저장할 때: 평문(attribute)을 받아 암호화된 문자열을 반환
        if (attribute == null) return null;
        return EncryptUtil.encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        // DB 데이터를 자바 객체로 변환할 때: 암호화된 문자열(dbData)을 받아 복호화된 평문을 반환
        if (dbData == null) return null;
        return EncryptUtil.decrypt(dbData);
    }
}