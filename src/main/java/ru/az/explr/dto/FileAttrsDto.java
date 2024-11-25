package ru.az.explr.dto;

import java.util.Optional;
import java.util.Set;

public record FileAttrsDto(
		String osType,
		String type,
		Long size,
		String creationTime,
		String lastModifiedTime,
		String lastAccessTime,
		String owner,
		String group,
		Set<String> permissions,
		Boolean isArchive,
		Boolean isHidden,
		Boolean isReadOnly,
		Boolean isSystem
		) {

	public FileAttrsDto{
		osType = Optional.ofNullable(osType).orElse("UNDEFINED");
		type = Optional.ofNullable(type).orElse("UNDEFINED");
		size = Optional.ofNullable(size).orElse(0L);
		creationTime = Optional.ofNullable(creationTime).orElse("UNDEFINED");
		lastModifiedTime = Optional.ofNullable(lastModifiedTime).orElse("UNDEFINED");
		lastAccessTime = Optional.ofNullable(lastAccessTime).orElse("UNDEFINED");
		owner = Optional.ofNullable(owner).orElse("UNDEFINED");
		group = Optional.ofNullable(group).orElse("UNDEFINED");
		permissions = Optional.ofNullable(permissions).orElse(Set.of());
	}
	
}
