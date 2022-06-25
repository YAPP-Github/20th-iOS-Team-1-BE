package com.yapp.pet.global.exception.common.response;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.yapp.pet.global.exception.common.ExceptionStatus;
import lombok.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionResponse {

	private String message;
	private String status;
	private List<ExceptionDetailResponse> errors;

	private ExceptionResponse(ExceptionStatus exceptionStatus, List<ExceptionDetailResponse> errors) {
		this.message = exceptionStatus.getMessage();
		this.status = String.valueOf(exceptionStatus.getStatus());
		this.errors = errors;
	}

	public static ExceptionResponse of(ExceptionStatus code, BindingResult bindingResult) {
		return new ExceptionResponse(code, ExceptionDetailResponse.from(bindingResult));
	}

	public static ExceptionResponse of(MethodArgumentTypeMismatchException ex) {
		String value = ex.getValue() == null ? "" : ex.getValue().toString();
		List<ExceptionDetailResponse> errors = ExceptionDetailResponse.of(ex.getName(), value, ex.getErrorCode());
		return new ExceptionResponse(ExceptionStatus.INVALID_TYPE_VALUE_EXCEPTION, errors);
	}

	public static ExceptionResponse of(InvalidFormatException ex) {
		String field = Arrays.stream(Objects.requireNonNull(ex.getTargetType().getFields()))
				.map(Field::getName)
				.collect(Collectors.joining(", "));

		String getTargetType = ex.getTargetType().toString();

		List<ExceptionDetailResponse> errors = null;

		if (ex.getPath().size() != 0) {
			errors = ExceptionDetailResponse.of(
					"지원 Enum = " + field,
					ex.getValue().toString(),
					"지원하지 않는 Enum 입니다.");
		}

		if (ex.getPath().size() == 0) {
			errors = ExceptionDetailResponse.of(
					ex.getPath().size() == 0 ? "지원 Enum = " + field : ex.getPath().get(0).getFieldName(),
					ex.getValue().toString(),
					getTargetType.contains("$") ? getTargetType.substring('$' + 1) : getTargetType);
		}

		return new ExceptionResponse(ExceptionStatus.INVALID_FORMAT_EXCEPTION, errors);
	}

	public static ExceptionResponse of(HttpRequestMethodNotSupportedException ex) {
		String supportedMethods = Arrays.stream(Objects.requireNonNull(ex.getSupportedMethods()))
				.map(String::toString)
				.collect(Collectors.joining(", "));

		List<ExceptionDetailResponse> details = ExceptionDetailResponse.of(ex.getLocalizedMessage(),
				"입력한 HTTP Method = " + ex.getMethod(),
				"지원 가능한 HTTP Method = " + supportedMethods);

		return new ExceptionResponse(ExceptionStatus.METHOD_NOT_SUPPORT_EXCEPTION, details);
	}

	public static ExceptionResponse of(EntityNotFoundException ex){

		List<ExceptionDetailResponse> errors = ExceptionDetailResponse.of(
				ex.getLocalizedMessage(),
				"",
				"해당 엔티티를 찾을 수 없음"
		);

		return new ExceptionResponse(ExceptionStatus.ENTITY_NOT_FOUND_EXCEPTION, errors);
	}

}
