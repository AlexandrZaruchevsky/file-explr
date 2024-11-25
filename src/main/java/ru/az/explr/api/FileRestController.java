package ru.az.explr.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.az.explr.dto.FileRequestDto;
import ru.az.explr.dto.v1.DeleteRequestDtoV1;
import ru.az.explr.dto.v1.FileInfoDtoV1;
import ru.az.explr.dto.v1.FolderInfoDtoV1;
import ru.az.explr.services.FileAsyncService;

@RestController
@RequestMapping("api/files")
@RequiredArgsConstructor
@CrossOrigin("*")
public class FileRestController {

	private final FileAsyncService fileAsyncService;
	
	@GetMapping
	public ResponseEntity<Mono<FolderInfoDtoV1>> getFilesAsync(FileRequestDto request){
		return ResponseEntity.ok(Mono.fromFuture(fileAsyncService.getFolderInfoV1(request)));
	}
	
	@GetMapping("filter")
	public ResponseEntity<Mono<List<FileInfoDtoV1>>> filter(
			@RequestParam(defaultValue = "", required = false) String filter
	){
		return ResponseEntity.ok(Mono.fromFuture(fileAsyncService.findFiles(filter)));
	}
	
	@PostMapping("/delete")
	public Mono<?> delete(
			@RequestBody DeleteRequestDtoV1 request
	){
		return fileAsyncService.fileDelete(request);
	}
	
}
