package com.simms.healthcare.util;

import java.util.stream.Stream;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.simms.healthcare.service.ActivationStatusCode;

/**
 * JPA converter for ActivationStatusCode.
 * @author thoma
 *
 */
@Converter(autoApply = true)
public class ActivationStatusConverter implements AttributeConverter<ActivationStatusCode, String> {

	@Override
	public String convertToDatabaseColumn(ActivationStatusCode status) {
		if (status == null) {
			return null;
		}
		return status.getCode();
	}

	@Override
	public ActivationStatusCode convertToEntityAttribute(String code) {
		if (code == null) {
			return null;
		}

		return Stream.of(ActivationStatusCode.values()).filter(
				status -> status.getCode().equals(code)).findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

}
