package ru.az.explr.dto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import ru.az.explr.config.Params;
import ru.az.explr.model.FileInfo;
import ru.az.explr.util.StringPath;

@Component
@RequiredArgsConstructor
public class ResponseFileFactory {
	
	private final Params params;
	
	public FileInfoDto createFileInfoDto(FileInfo fileInfo) {
		return Optional.ofNullable(fileInfo)
				.map(fi -> {
					return new FileInfoDto(
								fi.name(), 
								fi.fullname(),
								fi.parent(),
								createFileAttrsDto(fi.attrs())
							);
				})
				.orElse(new FileInfoDto(null, null, null, createEmptyFileAttrsDto()));
	}
	
	public FileInfoDto clearFolderHome(@Nonnull FileInfoDto fileInfo, @Nonnull String folderHome) {
		return new FileInfoDto(
				fileInfo.name().replace(folderHome, ""), 
				fileInfo.fullname().replace(folderHome, ""),
				StringPath.pathNormalize(fileInfo.parent()).replace(folderHome, ""),
				fileInfo.attrs()); 
	}
	
	public FileAttrsDto createFileAttrsDto(BasicFileAttributes attrs) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		return Optional.ofNullable(attrs)
				.map(pa -> {
					return new FileAttrsDto(
							System.getProperty("os.name"),
							fileType(pa),
							pa.size(),
							format.format(LocalDateTime.ofInstant(pa.creationTime().toInstant(), ZoneId.systemDefault())),
							format.format(LocalDateTime.ofInstant(pa.lastModifiedTime().toInstant(), ZoneId.systemDefault())),
							format.format(LocalDateTime.ofInstant(pa.lastAccessTime().toInstant(), ZoneId.systemDefault())),
							params.isLinux() ? ((PosixFileAttributes) pa).owner().getName() : null,
							params.isLinux() ? ((PosixFileAttributes) pa).group().getName() : null,
							params.isLinux() 
								? ((PosixFileAttributes) pa).permissions().stream()
										.map(PosixFilePermission::name).collect(Collectors.toSet()) 
								: null,
							!params.isLinux() ? ((DosFileAttributes) pa).isArchive() : false,
							!params.isLinux() ? ((DosFileAttributes) pa).isHidden() : false,
							!params.isLinux() ? ((DosFileAttributes) pa).isReadOnly() : false,
							!params.isLinux() ? ((DosFileAttributes) pa).isSystem() : false
							);
				})
				.orElse(createEmptyFileAttrsDto());
	}
	
	public FileAttrsDto createEmptyFileAttrsDto() {
		return new FileAttrsDto(null, null, null, null, null, null, null, null, null, null, null, null, null);
	}
	
	public FileInfoAsyncDto createAsync(Path path) {
		long size = 0l;
		try {
			size = Files.size(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new FileInfoAsyncDto(
				path.toFile().getName(), 
				path.toString(), 
				Files.isDirectory(path) ? "FOLDER": "OTHER", 
				"UNDEFINED", 
				Long.valueOf(size));
	}
	
	private static String fileType(BasicFileAttributes pAttrs) {
		if (pAttrs.isDirectory()) return "FOLDER";
		if (pAttrs.isRegularFile()) return "FILE";
		if (pAttrs.isSymbolicLink()) return "SYMBOL_LINK";
		return "OTHER";
	}
	
	
	
}
