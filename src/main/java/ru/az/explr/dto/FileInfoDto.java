package ru.az.explr.dto;

import java.util.Optional;

public record FileInfoDto(
		String name,
		String fullname,
		String parent,
		FileAttrsDto attrs
		) {
	
	public FileInfoDto{
		name = Optional.ofNullable(name).orElse("Noname");
		fullname = Optional.ofNullable(fullname).orElse("UNDEFINED");
		parent = Optional.ofNullable(parent).orElse("UNDEFINED");
		attrs = Optional.ofNullable(attrs).orElse(new FileAttrsDto(null, null, null, null, null, null, null, null, null, null, null, null, null));
	}
	
}
