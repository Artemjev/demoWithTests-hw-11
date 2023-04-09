package com.example.demowithtests.util.mapper;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.dto.employee.EmployeeCreateDto;
import com.example.demowithtests.dto.employee.EmployeePatchDto;
import com.example.demowithtests.dto.employee.EmployeePutDto;
import com.example.demowithtests.dto.employee.EmployeeReadDto;
import org.mapstruct.factory.Mappers;
import org.mapstruct.Mapper;

import java.util.HashSet;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    //    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);
    //    - закоментил шоб не юзать в коде, потом, наверное,  можно и раскоментить

    //-------------------------------------------------------------------------------------------
    //Переопределял метод маппера, что бы корректно отображать фронту приватных сотрудников (скрываем данные, если
    // isPrivate=true).
    // Наверное, это было проще реализовать используя АОП (CustomValidationAspect) и кастомную аннотацию
    // @IsBooleanFieldValid для валидирования логических полей (механизм валидации сущности в сервисе написан).
    // Но этот вариант сокрытия данных приватных сотрудников (через маппер) был написан ранее. Пусть отстается как
    // альтернативный вариант, для демонстрации гибкости и разнообразия способов))
    default EmployeeReadDto employeeToEmployeeReadDto(Employee employee) {
        return
                (employee.getIsPrivate() == Boolean.FALSE) ?
                EmployeeReadDto.builder()
                        .id(employee.getId())
                        .name(employee.getName())
                        .country(employee.getCountry())
                        .email(employee.getEmail())
                        .addresses(employee.getAddresses().stream()


                                .map(address -> AddressMapper.INSTANCE.addressToAddressDto(address))
                                .collect(Collectors.toSet()))
                        .gender(employee.getGender())
                        .isDeleted(employee.getIsDeleted())
                        .isPrivate(employee.getIsPrivate())
                        .isConfirmed(employee.getIsConfirmed())
                        .photos(employee.getPhotos().stream()
                                .map(photo -> PhotoMapper.INSTANCE.photoToPhotoDto(photo))
                                .collect(Collectors.toSet()))
                        .build()
                                                           :
                EmployeeReadDto.builder()
                        .id(employee.getId())
                        .name("is hidden")
                        .country("is hidden")
                        .email("is hidden")
                        .addresses(new HashSet<>())
                        .isPrivate(employee.getIsPrivate())
                        .isDeleted(employee.getIsDeleted())
                        .isConfirmed(employee.getIsConfirmed())
                        .photos(new HashSet<>())
                        .build();
    }

    Employee employeeCreateDtoToEmployee(EmployeeCreateDto employeeCreateDto);

    Employee employeePatchDtoToEmployee(EmployeePatchDto employeePatchDto);

    Employee employeePutDtoToEmployee(EmployeePutDto employeePutDto);
    //    Employee employeeReadDTOToEmployee(EmployeeCreateDto employeeReadDto);
    //    EmployeeCreateDto employeeToEmployeeCreateDTO(Employee employee);
    //    EmployeePatchDto employeeToEmployeePatchDto(Employee employee);
}