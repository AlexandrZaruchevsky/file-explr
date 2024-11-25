package ru.az.explr.dto;

import java.util.Optional;

public record FileRequestDto(
		String folder,
		String filter
		) {
	
	public FileRequestDto{
		folder = Optional.ofNullable(folder).orElse("");
		filter = Optional.ofNullable(filter).orElse("");
	}

}
