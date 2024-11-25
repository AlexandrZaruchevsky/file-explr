package ru.az.explr.model;

import java.nio.file.attribute.BasicFileAttributes;

public record FileInfo(
		String name,
		String fullname,
		String parent,
		BasicFileAttributes attrs
		) {
	
}
