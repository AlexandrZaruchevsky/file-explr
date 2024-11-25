
package ru.az.explr.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.az.explr.config.Params;
import ru.az.explr.dto.v1.FileInfoDtoFactoryV1;
import ru.az.explr.dto.v1.FileInfoDtoV1;
import ru.az.explr.dto.v1.FolderInfoDtoFactoryV1;
import ru.az.explr.dto.v1.FolderInfoDtoV1;
import ru.az.explr.model.FileInfo;
import ru.az.explr.model.FileInfoFactory;
import ru.az.explr.model.v1.FileInfoFactoryV1;
import ru.az.explr.model.v1.FileInfoV1;

@Component
@RequiredArgsConstructor
public class FileUtil {
	
	private final FileInfoFactory fileInfofactory;
	private final Params params;
	
	private final FileInfoFactoryV1 factoryV1;
	private final FileInfoDtoFactoryV1 dtoFactory;
	private final FolderInfoDtoFactoryV1 folderFactory;
	
	public Stream<FileInfo> getFiles(String path) throws ZIOException{
		return getFiles(Path.of(path));
	}
	
	public Stream<FileInfo> getFiles(Path path) throws ZIOException{
		if (!Files.exists(path))
			throw new ZIOException(HttpStatus.NOT_FOUND, String.format("NOT EXISTS - %s ", path.toString().replace(params.getHomeFolder(), "")));
		if (!Files.isDirectory(path)) 
			throw new ZIOException(HttpStatus.BAD_REQUEST, String.format("NOT FOLDER = %s", path.toString().replace(params.getHomeFolder(), "")));
		try {
			return Files.list(path).map(fileInfofactory::create);
		} catch (IOException e) {
			throw new ZIOException(HttpStatus.BAD_REQUEST, e, String.format("Folder error read [%s]", path.toString().replace(params.getHomeFolder(), "")));
		}
	}
	
	public CompletableFuture<List<FileInfoDtoV1>> getFilesV1(String path){
		return getFilesV1(Path.of(path));
	}
	
	public CompletableFuture<List<FileInfoDtoV1>> getFilesV1(Path path){
		return CompletableFuture.supplyAsync(()->{
			if (!Files.exists(path))
				throw new ZAsyncException(HttpStatus.NOT_FOUND, String.format("NOT EXISTS - %s ", path.toString().replace(params.getHomeFolder(), "")));
			if (!Files.isDirectory(path)) 
				throw new ZAsyncException(HttpStatus.BAD_REQUEST, String.format("NOT FOLDER = %s", path.toString().replace(params.getHomeFolder(), "")));
			try {
				return Files.list(path)
						.map(factoryV1::create)
						.map(dtoFactory::create)
						.toList();
			} catch (IOException e) {
				throw new ZAsyncException(HttpStatus.BAD_REQUEST, e, String.format("Folder error read [%s]", path.toString().replace(params.getHomeFolder(), "")));
			}
		});
	}
	
	public CompletableFuture<FolderInfoDtoV1> getFolderInfoV1(String path){
		return getFolderInfoV1(Path.of(path));
	}
	
	public CompletableFuture<FolderInfoDtoV1> getFolderInfoV1(Path path){
		return CompletableFuture.supplyAsync(()->{
			if (!Files.exists(path))
				throw new ZAsyncException(HttpStatus.NOT_FOUND, String.format("NOT EXISTS - %s ", path.toString().replace(params.getHomeFolder(), "")));
			if (!Files.isDirectory(path)) 
				throw new ZAsyncException(HttpStatus.BAD_REQUEST, String.format("NOT FOLDER = %s", path.toString().replace(params.getHomeFolder(), "")));
			try {
				return Files.list(path)
						.map(factoryV1::create)
						.sorted(fileInfosorted())
						.filter(fi -> !fi.name().startsWith("."))
						.map(dtoFactory::create)
						.toList();
			} catch (IOException e) {
				throw new ZAsyncException(HttpStatus.BAD_REQUEST, e, String.format("Folder error read [%s]", path.toString().replace(params.getHomeFolder(), "")));
			}
		}).thenApply(files -> folderFactory.create(path, files));
	}
	
	
	
	public Comparator<FileInfoV1> fileInfosorted() {
		return new Comparator<FileInfoV1>() {
			@Override
			public int compare(FileInfoV1 o1, FileInfoV1 o2) {
				if (o1.fileType().equals("FOLDER") && !o2.fileType().equals("FOLDER"))
					return -1;
				if (o2.fileType().equals("FOLDER") && !o1.fileType().equals("FOLDER"))
					return 1;
				return o1.name().compareToIgnoreCase(o2.name());
			}
		};
	}
	
}
