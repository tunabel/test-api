package com.gable.testapi.userservice.config;

import com.gable.testapi.common.constants.enums.MemberType;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MemberTypeConverter implements AttributeConverter<MemberType, String> {

  @Override
  public String convertToDatabaseColumn(MemberType memberType) {
    if (memberType == null) {
      return null;
    }
    return memberType.name();
  }

  @Override
  public MemberType convertToEntityAttribute(String name) {
    if (name == null) {
      return null;
    }

    return MemberType.valueOf(name);
  }
}
