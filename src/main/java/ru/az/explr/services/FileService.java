package ru.az.explr.services;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.az.explr.config.Params;
import ru.az.explr.dto.FileInfoDto;
import ru.az.explr.dto.FileRequestDto;
import ru.az.explr.dto.ResponseFileFactory;
import ru.az.explr.util.FileUtil;
import ru.az.explr.util.StringPath;
import ru.az.explr.util.ZIOException;

@Service
@RequiredArgsConstructor
public class FileService {

	private final FileUtil fileUtil;
	private final Params params;
	private final ResponseFileFactory fileFactory;
	
	public Flux<FileInfoDto> getFiles(FileRequestDto request) throws ZIOException{
		String reqFolder = params.getHomeFolder().concat(StringPath.requestPathNormalize(request.folder()));
		return Flux.fromStream(fileUtil.getFiles(reqFolder))
				.map(fileFactory::createFileInfoDto)
				.map(fid -> fileFactory.clearFolderHome(fid, params.getHomeFolder()));
	}
	
	public Mono<List<FileInfoDto>> getFilesAsMono(FileRequestDto request) throws ZIOException{
		String reqFolder = params.getHomeFolder().concat(StringPath.requestPathNormalize(request.folder()));
		return Mono.just(
				fileUtil.getFiles(reqFolder)
					.map(fileFactory::createFileInfoDto)
					.map(fid -> fileFactory.clearFolderHome(fid, params.getHomeFolder()))
					.toList());
	}
}
