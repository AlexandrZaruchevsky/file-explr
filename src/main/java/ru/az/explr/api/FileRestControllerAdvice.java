package ru.az.explr.api;

import java.util.stream.Stream;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import reactor.core.publisher.Mono;
import ru.az.explr.dto.v1.ExceptionDto;
import ru.az.explr.util.ZAsyncException;
import ru.az.explr.util.ZIOException;

@RestControllerAdvice
public class FileRestControllerAdvice {
	
	@ExceptionHandler(ZIOException.class)
	public Mono<ResponseEntity<ExceptionDto>> throwZIOException(ZIOException ex){
		return Mono.just(
				ResponseEntity.status(ex.getStatus())
					.body(
						new ExceptionDto(
								ex.getStatus(), 
								ex.getMessage(), 
								Stream.of(ex.getStackTrace())
									.map(Object::toString)
									.toList())
					)
				);
	}

	@ExceptionHandler(ZAsyncException.class)
	public Mono<ResponseEntity<ExceptionDto>> throwZIOException(ZAsyncException ex){
		return Mono.just(
				ResponseEntity.status(ex.getStatus())
					.body(
						new ExceptionDto(
								ex.getStatus(), 
								ex.getMessage(), 
								Stream.of(ex.getStackTrace())
									.map(Object::toString)
									.toList())
					)
				);
	}

}
