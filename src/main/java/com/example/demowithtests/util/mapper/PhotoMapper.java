package com.example.demowithtests.util.mapper;


import com.example.demowithtests.domain.Photo;
import com.example.demowithtests.dto.photo.PhotoDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PhotoMapper {

    PhotoMapper INSTANCE = Mappers.getMapper(PhotoMapper.class);

    PhotoDto photoToPhotoDto(Photo photo);

    Photo photoDtoToPhoto(PhotoDto photoDto);
}
