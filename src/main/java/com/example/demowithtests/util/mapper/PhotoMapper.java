package com.example.demowithtests.util.mapper;


import com.example.demowithtests.domain.Photo;
import com.example.demowithtests.dto.photo.PhotoCreateDto;
import com.example.demowithtests.dto.photo.PhotoReadDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring")
public interface PhotoMapper {

    PhotoMapper INSTANCE = Mappers.getMapper(PhotoMapper.class);

    PhotoReadDto photoToPhotoDto(Photo photo);

    Photo photoCreateDtoToPhoto(PhotoCreateDto photoCreateDto);

    //    default Photo photoCreateDtoToPhoto(PhotoCreateDto photoCreateDto) {
    //        MultipartFile file = photoCreateDto.file;
    //        //        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    //        Photo photo = new Photo();
    //        photo.setDescription(photoCreateDto.getDescription());
    //        photo.setFileName(StringUtils.cleanPath(file.getOriginalFilename()));
    //        photo.setFileType(file.getContentType());
    //        try {
    //            photo.setData(file.getBytes());
    //        } catch (IOException e) {
    //            throw new RuntimeException(e);//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!todo
    //        }
    //        return photo;
    //    }

}
