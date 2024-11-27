package ru.az.explr.api;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import ru.az.explr.services.VideoStremingService;

@RestController
@RequestMapping("api/stream/video")
@RequiredArgsConstructor
public class VideoStreamingController {

	private final VideoStremingService videoService;
	
	@GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public Mono<ResponseEntity<Resource>> streamVideo(
			@RequestParam(required = true) String path 
	) {
		System.out.println(path);
		
		return Mono.just(
				ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(videoService.getVideo(path))
				);
	}
	
}
