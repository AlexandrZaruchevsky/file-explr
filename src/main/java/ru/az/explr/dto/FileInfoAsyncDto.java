package ru.az.explr.dto;

public record FileInfoAsyncDto(
		String name,
		String fullname,
		String parent,
		String type,
		Long lenght
		) {
	
	public FileInfoAsyncDto{
		
	}

}
