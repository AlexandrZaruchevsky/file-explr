package ru.az.explr.model.v1;

import java.nio.file.attribute.PosixFilePermission;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public record FileInfoV1(
		String name,
		String fullname,
		String parent,
		LocalDateTime lastModifiedTime,
		Long size,
		String owner,
		String fileType,
		Set<String> attrs,
		Set<PosixFilePermission> permissions
		) {

	public FileInfoV1{
		name = Optional.ofNullable(name).orElse("undefined");
		fullname = Optional.ofNullable(fullname).orElse("undefined");
		parent = Optional.ofNullable(parent).orElse("undefined");
		size = Optional.ofNullable(size).orElse(0l);
		owner = Optional.ofNullable(owner).orElse("undefined");
		fileType = Optional.ofNullable(fileType).orElse("undefined");
		attrs = Optional.ofNullable(attrs).orElse(Set.of());
		permissions = Optional.ofNullable(permissions).orElse(Set.of());
	}
	
}
