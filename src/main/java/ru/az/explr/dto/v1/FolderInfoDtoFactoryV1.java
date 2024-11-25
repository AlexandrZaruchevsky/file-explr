package ru.az.explr.dto.v1;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.az.explr.config.Params;
import ru.az.explr.util.StringPath;

@Component
@RequiredArgsConstructor
public class FolderInfoDtoFactoryV1 {
	
	public final Params params;

	public FolderInfoDtoV1 create(Path path, List<FileInfoDtoV1> files) {
		return Optional.ofNullable(path)
			.map(p -> {
				String currentFolder = StringPath.pathNormalize(path.toString()).replace(params.getHomeFolder(), "");
				String parentFolder = StringPath.pathNormalize(path.getParent().toString());
				parentFolder = parentFolder.startsWith(params.getHomeFolder()) ? parentFolder.replace(params.getHomeFolder(), "") : "";
				return new FolderInfoDtoV1(
						currentFolder.endsWith(System.getProperty("file.separator")) 
							? currentFolder.substring(0, currentFolder.length() - 1)
							: currentFolder, 
						parentFolder.endsWith(System.getProperty("file.separator")) 
							? parentFolder.substring(0, parentFolder.length() - 1)
							: parentFolder, 
						Optional.ofNullable(files).orElse(List.of()));
			}).orElse(createEmpty());
	}
	
	public FolderInfoDtoV1 createEmpty() {
		return new FolderInfoDtoV1(null, null, null);
	}
	
}
