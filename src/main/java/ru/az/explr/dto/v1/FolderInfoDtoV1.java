package ru.az.explr.dto.v1;

import java.util.List;
import java.util.Optional;

public record FolderInfoDtoV1(
		String folderCurrent,
		String folderParent,
		List<FileInfoDtoV1> files
		) {

	public FolderInfoDtoV1{
		folderCurrent = Optional.ofNullable(folderCurrent).orElse("undefined");
		folderParent = Optional.ofNullable(folderParent).orElse("undefined");
		files = Optional.ofNullable(files).orElse(List.of());
	}
	
}
