package ru.az.explr.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import ru.az.explr.util.StringPath;

@Component
public class Params {

	@Value("${params.folder-home}")
	@Getter
	private String homeFolder;
	
	@Value("${params.thread.pool-size:1}")
	@Getter
	private Integer threadPoolSize;

	@Value("${params.file.deleted:false}")
	@Getter
	private boolean fileDeleted;

	@Value("${params.file.access-denied:false}")
	@Getter
	private boolean fileAccesDenied;
	@Getter
	private final boolean linux = System.getProperty("os.name").toLowerCase().contains("linux");
	
	@PostConstruct
	private void init() {
		this.homeFolder = StringPath.pathNormalize(this.homeFolder);
	}
	
}
