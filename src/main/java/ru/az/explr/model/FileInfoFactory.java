package ru.az.explr.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.az.explr.config.Params;

@Component
@RequiredArgsConstructor
public class FileInfoFactory {

	private final Params params;
	
	public FileInfo create(Path path) {
		BasicFileAttributes pAttrs = null;
		try {
			pAttrs = params.isLinux()
					? Files.readAttributes(path, BasicFileAttributes.class)
					: Files.readAttributes(path, DosFileAttributes.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new FileInfo(path.toFile().getName(), path.toString(), path.getParent().toString(), pAttrs);
	}	
}
