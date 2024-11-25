package ru.az.explr.model.v1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.az.explr.util.StringPath;

@Component
@RequiredArgsConstructor
public class FileInfoFactoryV1 {
	
	public FileInfoV1 create(Path path) {
		String name = path.toFile().getName();
		String fullname = path.toString();
		String parent = StringPath.pathNormalize(path.getParent().toString());
		LocalDateTime lastModifiedTime = null;
		long size = 0l;
		String owner = null;
		Set<String> attrs = Set.of();
		Set<PosixFilePermission> permissions = Set.of();
		try {
			lastModifiedTime = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(), ZoneId.systemDefault());
			size = Files.size(path);
			owner = Files.getOwner(path).getName();
			attrs = getAttrs(path);
			if (System.getProperty("os.name").toLowerCase().contains("linux"))
				permissions = Files.getPosixFilePermissions(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String fileType = getFileType(path);
		return new FileInfoV1(name, fullname, parent, lastModifiedTime, size, owner, fileType, attrs, permissions);
	}
	
	public FileInfoV1 createEmpty(Path path) {
		return new FileInfoV1(null, null, null, null, null, null, null, null, null);
	}
	
	private String getFileType(Path path) {
		if (Files.isDirectory(path)) return "FOLDER";
		if (Files.isRegularFile(path)) return "FILE";
		if (Files.isSymbolicLink(path)) return "SYMBOL_LINK";
		return "OTHER";
	}
	
	private Set<String> getAttrs(Path path) throws IOException{
		Set<String> attrs = new HashSet<>();
		if (Files.isHidden(path))
			attrs.add("HIDDEN");
		if (Files.isExecutable(path))
			attrs.add("HIDDEN");
		if (Files.isReadable(path))
			attrs.add("READABLE");
		if (Files.isWritable(path))
			attrs.add("WRITABLE");
		return attrs;
	}

}
