package ru.az.explr.services;

import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.az.explr.config.Params;
import ru.az.explr.util.StringPath;

@Service
@RequiredArgsConstructor
public class VideoStremingService {

	private final Params params;
	private final ResourceLoader resourceLoader;

	public Resource getVideo(String path) {
//		String fullPath = StringPath.getFullPathAsResource(params.getHomeFolder(), path);
//		Resource resource = resourceLoader.getResource(fullPath);
		return new FileSystemResource(StringPath.getFullPath(params.getHomeFolder(), path));
	}

}
