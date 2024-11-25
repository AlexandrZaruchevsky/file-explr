package ru.az.explr.dto.v1;

import java.util.Optional;
import java.util.Set;

public record FileInfoDtoV1(
		String name,
		String fullname,
		String parent,
		String lastModifiedTime,
		Long size,
		String owner,
		String fileType,
		Set<String> attrs,
		Set<String> permissions
		) {

	public FileInfoDtoV1{
		name = Optional.ofNullable(name).orElse("undefined");
		fullname = Optional.ofNullable(fullname).orElse("undefined");
		parent = Optional.ofNullable(parent).orElse("undefined");
		lastModifiedTime = Optional.ofNullable(lastModifiedTime).orElse("undefined");
		size = Optional.ofNullable(size).orElse(0l);
		owner = Optional.ofNullable(owner).orElse("undefined");
		fileType = Optional.ofNullable(fileType).orElse("undefined");
		attrs = Optional.ofNullable(attrs).orElse(Set.of());
		permissions = Optional.ofNullable(permissions).orElse(Set.of());
	}
	
}
