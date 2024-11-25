package ru.az.explr.dto.v1;

import java.nio.file.attribute.PosixFilePermission;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.az.explr.config.Params;
import ru.az.explr.model.v1.FileInfoV1;

@Component
@RequiredArgsConstructor
public class FileInfoDtoFactoryV1 {

	private final Params params;

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss.SSS");
	
	public FileInfoDtoV1 create(FileInfoV1 fileInfo) {
		return Optional.ofNullable(fileInfo)
			.map(fi ->  new FileInfoDtoV1(
					fi.name(), 
					clearHomeFolder(fi.fullname()), 
					clearHomeFolder(fi.parent()), 
					Optional.ofNullable(fi.lastModifiedTime()).map(formatter::format).orElse(""), 
					fi.size(), 
					fi.owner(), 
					fi.fileType(), 
					fi.attrs(), 
					fi.permissions().stream().map(PosixFilePermission::name).collect(Collectors.toSet())
			))
			.orElse(createEmpty());
	}
	
	public FileInfoDtoV1 createEmpty() {
		return new FileInfoDtoV1(null, null, null, null, null, null, null, null, null);
	}

	private String clearHomeFolder(String path) {
		return path.replace(params.getHomeFolder(), "");
	}
	
}
