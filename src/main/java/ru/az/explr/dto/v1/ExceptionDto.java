package ru.az.explr.dto.v1;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;

public record ExceptionDto(
		HttpStatus status,
		String message,
		List<String> staketrace
		) {

	public ExceptionDto{
		status = Optional.ofNullable(status).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
		message = Optional.ofNullable(message).orElse("");
		staketrace = Optional.ofNullable(staketrace).orElse(List.of());
	}
	
}
