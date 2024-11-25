package ru.az.explr.util;

import java.util.Optional;

public enum StringPath {
	;
	public static String pathNormalize(String folder) {
		return Optional.ofNullable(folder)
				.map(f -> f.endsWith(System.getProperty("file.separator")) 
						? folder
						: f.concat(System.getProperty("file.separator")))
				.orElse(System.getProperty("user.dir").concat(System.getProperty("file.separator")));
	}
	
	public static String requestPathNormalize(String path) {
		return Optional.ofNullable(path)
				.map(p -> p.startsWith(System.getProperty("file.separator"))
						? p.substring(1)
						: p)
				.orElse("");
	}
	
}