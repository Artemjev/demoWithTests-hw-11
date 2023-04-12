package com.example.demowithtests.web;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.domain.Photo;
import com.example.demowithtests.dto.employee.EmployeeCreateDto;
import com.example.demowithtests.dto.employee.EmployeePatchDto;
import com.example.demowithtests.dto.employee.EmployeePutDto;
import com.example.demowithtests.dto.employee.EmployeeReadDto;
import com.example.demowithtests.dto.photo.PhotoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface EmployeeController {

    EmployeeReadDto getEmployee(Integer id);

    EmployeeReadDto createEmployee(EmployeeCreateDto createDto);

    EmployeeReadDto patchEmployee(Integer id, EmployeePatchDto patchDto);

    EmployeeReadDto putEmployee(Integer id, EmployeePutDto putDto);

    void markEmployeeAsDeleted(Integer id);

    void deleteEmployee(Integer id);

    List<EmployeeReadDto> getAllEmployees();

    Page<EmployeeReadDto> getPageOfEmployees(int page, int size);

    void removeAllUsers();

    Page<EmployeeReadDto> getEmployeesByCountry(String country, int page, int size, List<String> sortList,
            Sort.Direction sortOrder);

    Set<String> getAllEmployeesCountries();

    List<String> getAllEmployeesCountriesSorted();

    Optional<List<String>> getAllEmployeesEmails();

    List<EmployeeReadDto> getEmployeesByGenderAndCountry(Gender gender, String country);

    Page<EmployeeReadDto> getEmployeesWithActiveAddressesInCountry(String country, int page, int size);

    List<EmployeeReadDto> handleEmployeesWithIsDeletedFieldIsNull();

    List<EmployeeReadDto> handleEmployeesWithIsPrivateFieldIsNull();

    List<EmployeeReadDto> handleEmployeesWithIsConfirmedFieldIsNull();

    Page<EmployeeReadDto> getAllActiveUsers(int page, int size);

    Page<EmployeeReadDto> getAllDeletedUsers(int page, int size);

    void sendConfirm(Integer id);

    void confirm(Integer id);

    Long generateEmployees(Integer quantity, Boolean clear);

    Long employeeMassPutUpdate();

    Long employeeMassPatchUpdate();

    List<EmployeeReadDto> getEmployeesWithExpiredPhotos();

    void sendEmailToEmployeesWhosePhotoIsExpired();

    //---------------------------------------------------------------------------------\
    //    todo дщбавить описание в свагер
//    Photo getPhotoDetails(Integer employeeId);

    PhotoDto getPhotoDetails(Integer employeeId);

    ResponseEntity<byte[]> getPhoto(Integer employeeId);

    ResponseEntity<String> uploadPhoto(Integer employeeId, MultipartFile file);
}
