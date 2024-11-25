package ru.az.explr.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.az.explr.config.Params;
import ru.az.explr.dto.FileRequestDto;
import ru.az.explr.dto.v1.DeleteRequestDtoV1;
import ru.az.explr.dto.v1.FileInfoDtoFactoryV1;
import ru.az.explr.dto.v1.FileInfoDtoV1;
import ru.az.explr.dto.v1.FolderInfoDtoV1;
import ru.az.explr.model.v1.FileInfoFactoryV1;
import ru.az.explr.util.FileUtil;
import ru.az.explr.util.StringPath;
import ru.az.explr.util.ZAsyncException;

@Service
@RequiredArgsConstructor
public class FileAsyncService {

	private final Params params;
	private final FileUtil fileUtil;
	private final FileInfoFactoryV1 infoFactoryV1;
	private final FileInfoDtoFactoryV1 infoDtoFactoryV1;

	public CompletableFuture<List<FileInfoDtoV1>> getFilesV1(FileRequestDto request) {
		String path = params.getHomeFolder().concat(StringPath.requestPathNormalize(request.folder()));
		return fileUtil.getFilesV1(path);
	}

	public CompletableFuture<FolderInfoDtoV1> getFolderInfoV1(FileRequestDto request) {
		String path = params.getHomeFolder().concat(StringPath.requestPathNormalize(request.folder()));
		return fileUtil.getFolderInfoV1(path);
	}

	public CompletableFuture<List<FileInfoDtoV1>> findFiles(String filter) {
		return params.isFileAccesDenied()
				? findFilesWithTroubles(filter)
				: findFilesWithoutTroubles(filter);
	}

	public CompletableFuture<List<FileInfoDtoV1>> findFilesWithoutTroubles(String filter) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				return Files.walk(Path.of(params.getHomeFolder()), Integer.MAX_VALUE, FileVisitOption.FOLLOW_LINKS)
					.filter(path -> path.toString().toLowerCase().contains(filter))
					.map(infoFactoryV1::create)
					.sorted(fileUtil.fileInfosorted())
					.map(infoDtoFactoryV1::create)
					.toList();
			} catch (IOException e) {
				throw new ZAsyncException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			}
		});
	}

	public CompletableFuture<List<FileInfoDtoV1>> findFilesWithTroubles(String filter) {
		return CompletableFuture.supplyAsync(() -> {
			Set<Path> pathSet = Collections.synchronizedSet(new HashSet<>());
			try {
				Files.walkFileTree(Path.of(params.getHomeFolder()), Set.of(FileVisitOption.FOLLOW_LINKS),
						Integer.MAX_VALUE, new FileVisitor<Path>() {
							@Override
							public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
									throws IOException {
								if (!dir.toFile().getName().startsWith(".")
										&& dir.toFile().getName().toLowerCase().contains(filter.toLowerCase()))
									pathSet.add(dir);
								return dir.toFile().getName().startsWith(".") ? FileVisitResult.SKIP_SUBTREE
										: FileVisitResult.CONTINUE;
							}
							@Override
							public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
								if (!file.toFile().getName().startsWith(".")
										&& file.toFile().getName().toLowerCase().contains(filter.toLowerCase()))
									pathSet.add(file);
								return FileVisitResult.CONTINUE;
							}
							@Override
							public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
								return FileVisitResult.CONTINUE;
							}
							@Override
							public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
								return FileVisitResult.CONTINUE;
							}
						});
			} catch (IOException e) {
				e.printStackTrace();
			}
			return pathSet.stream().map(infoFactoryV1::create).sorted(fileUtil.fileInfosorted())
					.map(infoDtoFactoryV1::create).toList();
		});
	}

	public Mono<Boolean> fileDelete(DeleteRequestDtoV1 request) {
		return Mono.fromFuture(CompletableFuture.supplyAsync(() -> {
			if (!params.isFileDeleted())
				return false;
			if (request.filename().trim().equals(""))
				throw new ZAsyncException("Path is null");
			String fullPath = params.getHomeFolder().concat(StringPath.requestPathNormalize(request.filename()));
			if (Files.exists(Path.of(fullPath))) {
				try {
					if (Files.isDirectory(Path.of(fullPath))) {
						Files.walk(Path.of(fullPath), Integer.MAX_VALUE, FileVisitOption.FOLLOW_LINKS)
								.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
					} else {
						Files.deleteIfExists(Path.of(fullPath));
					}
					return true;
				} catch (IOException e) {
					throw new ZAsyncException(String.format("File not deleted %s", request.filename()));
				}
			}
			return false;
		}));
	}

}
