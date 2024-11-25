package ru.az.explr.dto.v1;

import java.util.Optional;

public record DeleteRequestDtoV1(
		String filename
		) {

	public DeleteRequestDtoV1{
		filename = Optional.ofNullable(filename).orElse(filename);
	}
	
}
