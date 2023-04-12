package com.example.demowithtests.web;

import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.dto.employee.EmployeeCreateDto;
import com.example.demowithtests.dto.employee.EmployeePatchDto;
import com.example.demowithtests.dto.employee.EmployeePutDto;
import com.example.demowithtests.dto.employee.EmployeeReadDto;
import com.example.demowithtests.dto.photo.PhotoReadDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Tag(name = "Employee", description = "Employee API") public interface EmployeeControllerApiDoc
        extends EmployeeController {

    @Override
    @Operation(summary = "This is endpoint to return employee by his id.",
               description = "Create request to read employee by id", tags = {"Employee"}) @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "OK. Employee has been found"),
                     @ApiResponse(responseCode = "204", description = "NO CONTENT. Employee hasn't been found"),
                     @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                     @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                     @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                     @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    EmployeeReadDto getEmployee(@PathVariable Integer id);

    @Override
    @Operation(summary = "This is endpoint to add a new employee.",
               description = "Create request to add a new employee.", tags = {"Employee"}) @ApiResponses(
            value = {@ApiResponse(responseCode = "201", description = "CREATED. Employee was added"),
                     @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                     @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                     @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                     @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found"),
                     @ApiResponse(responseCode = "409", description = "CONFLICT. Employee already exists")})
    EmployeeReadDto createEmployee(EmployeeCreateDto createDto);

    @Override
    @Operation(summary = "This is endpoint to update employee.", description = "Create request to update employee.",
               tags = {"Employee"}) @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "OK. Employee has been updated"),
                     @ApiResponse(responseCode = "204", description = "NO CONTENT. Employee hasn't been found"),
                     @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                     @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                     @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                     @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    EmployeeReadDto patchEmployee(Integer id, EmployeePatchDto patchDto);

    @Override
    @Operation(summary = "This is endpoint to partially update employee.",
               description = "Create request to update employee.", tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Employee has been updated"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. Employee hasn't been found"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    EmployeeReadDto putEmployee(Integer id, EmployeePutDto putDto);

    @Override
    @Operation(summary = "This is endpoint to mark employee as deleted.",
               description = "Create request to mark employee as deleted.", tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Employee has been marked as deleted"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. Employee hasn't been found"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    void markEmployeeAsDeleted(Integer id);


    @Override
    @Operation(summary = "This is endpoint to delete employee.", description = "Create request to delete employee.",
               tags = {"Employee"}) @ApiResponses(
            value = {@ApiResponse(responseCode = "200", description = "OK. Employee has been deleted"),
                     @ApiResponse(responseCode = "204", description = "NO CONTENT. Employee hasn't been found"),
                     @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                     @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                     @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                     @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    void deleteEmployee(Integer id);


    @Override
    @Operation(summary = "This is endpoint to return all available employees.",
               description = "Create request to get all available employees.", tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Employees have been gotten"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. There are no employees"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    List<EmployeeReadDto> getAllEmployees();

    @Override
    @Operation(summary = "This is endpoint to return page of employees.",
               description = "Create request to get page of employees.", tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Employees have been gotten"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. There are no employees"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    Page<EmployeeReadDto> getPageOfEmployees(int page, int size);

    @Override
    @Operation(summary = "This is endpoint to delete all employees.",
               description = "Create request to delete all employees.", tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Employees have been deleted"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. No employees to delete"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    void removeAllUsers();


    @Override
    @Operation(summary = "This is endpoint to return page of employees by country.",
               description = "Create request to get page of employees by country.", tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Employees have been gotten"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. There are no such employees"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    Page<EmployeeReadDto> getEmployeesByCountry(String country, int page, int size, List<String> sortList,
            Sort.Direction sortOrder);


    @Override
    @Operation(summary = "This is endpoint to return list of all employees' countries.",
               description = "Create request to get list of all employees' countries.", tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Countries list have been gotten"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. No countries found"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    Set<String> getAllEmployeesCountries();

    @Override
    @Operation(summary = "This is an endpoint to return a  sorted list of all employees' countries.",
               description = "Create request to get sorted list of all employees' countries.", tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Countries list have been gotten"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. No countries found"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    List<String> getAllEmployeesCountriesSorted();

    @Override
    @Operation(summary = "This is an endpoint to return list of all employees' emails.",
               description = "Create request to get list of all employees' emails.", tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK.  Emails list have been gotten"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. No emails found"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    Optional<List<String>> getAllEmployeesEmails();


    @Override
    @Operation(summary = "This is endpoint to return employees by gender and country.",
               description = "Create request to get employees by gender and country.", tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Employees have been gotten"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. There are no such employees"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    List<EmployeeReadDto> getEmployeesByGenderAndCountry(Gender gender, String country);


    @Override
    @Operation(summary = "This is endpoint to return page of employees who have active addresses in a given country.",
               description = "Create request to get page of employees who have active addresses in a given country.",
               tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Employees have been gotten"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. There are no such employees"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    Page<EmployeeReadDto> getEmployeesWithActiveAddressesInCountry(String country, int page, int size);


    @Override
    @Operation(summary = "Endpoint is used to set value of field IsDeleted=FALSE for all employees whose field " +
                         "IsDeleted=NULL.",
               description = "Create request to setup IsDeleted=FALSE for all employees whose field IsDeleted=NULL.",
               tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Request was handled successfully"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. No users to process"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    List<EmployeeReadDto> handleEmployeesWithIsDeletedFieldIsNull();

    @Override
    @Operation(summary = "Endpoint is used to set value of field IsPrivate=FALSE for all employees whose field " +
                         "IsPrivate=NULL.",
               description = "Create request to setup IsPrivate=FALSE for all employees whose field IsPrivate=NULL.",
               tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Request was handled successfully"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. No users to process"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    List<EmployeeReadDto> handleEmployeesWithIsPrivateFieldIsNull();


    @Override
    @Operation(summary = "Endpoint is used to set value of field IsConfirmed=FALSE for all employees whose field " +
                         "IsConfirmed=NULL.",
               description = "Create request to setup IsPrivate=FALSE for all employees whose field IsPrivate=NULL.",
               tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Request was handled successfully"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. No users to process"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    List<EmployeeReadDto> handleEmployeesWithIsConfirmedFieldIsNull();

    @Override
    @Operation(summary = "This is endpoint to return page of deleted employees.",
               description = "Create request to get  page of deleted employees.", tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Employees have been gotten"),
                           @ApiResponse(responseCode = "204",
                                        description = "NO CONTENT. There are no deleted employees"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    Page<EmployeeReadDto> getAllActiveUsers(int page, int size);

    @Override
    @Operation(summary = "This is endpoint to return page of active employees.",
               description = "Create request to get  page of active employees.", tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Employees have been gotten"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT.There are no active employees"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    Page<EmployeeReadDto> getAllDeletedUsers(int page, int size);

    @Override
    @Operation(summary = "Endpoint is used to send email to employee with a request to confirm their data.",
               description = "Create request to send email to employee with a request to confirm the data.",
               tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Email have been sent"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. No users to process"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    void sendConfirm(Integer id);

    @Override
    @Operation(summary = "Endpoint is used to set value of field isConfirmed=TRUE for employee.",
               description = "Create request to set value of field isConfirmed=TRUE for employee.", tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Employee is confirmed"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. Employee hasn't been found"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    void confirm(Integer id);

    @Override
    @Operation(summary = "Endpoint is used to generate employees.",
               description = "Create request to generate employees.", tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Employees were created"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    Long generateEmployees(Integer quantity, Boolean clear);

    @Override
    @Operation(summary = "Endpoint is used to mass PUT-update employees.",
               description = "Create request to mass PUT-update employees.", tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Employees were updated"),
                           @ApiResponse(responseCode = "204",
                                        description = "NO CONTENT. There are no employees to update"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    Long employeeMassPutUpdate();

    @Override
    @Operation(summary = "Endpoint is used to mass PATCH-update employees.",
               description = "Create request to mass PATCH-update employees.", tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Employees were updated"),
                           @ApiResponse(responseCode = "204",
                                        description = "NO CONTENT. There are no employees to update"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    Long employeeMassPatchUpdate();

    @Override
    @Operation(summary = "Endpoint is used to get employees whose photos are expired.",
               description = "Create request to get employees whose photos are expired.",
               tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Employees have been gotten"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. There are no such employees"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    List<EmployeeReadDto> getEmployeesWithExpiredPhotos();

    @Override
    @Operation(summary = "Endpoint is used to send a notification email to employees whose photos are expired.",
               description = "Create request to send a notification email to employees whose photos are expired.",
               tags = {"Employee"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Emails have been sent"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. There are no such employees"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    void sendEmailToEmployeesWhosePhotoIsExpired();

    @Override
    @Operation(summary = "This is endpoint to return active photo of employee by his id.",
               description = "Create request to get active photo of employee", tags = {"Photo"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Active photo has been found"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. Employee hasn't been found"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    PhotoReadDto getEmployeeActivePhoto(Integer employeeId);

    @Override
    @Operation(summary = "This is endpoint to return image of active photo of employee by his id.",
               description = "Create request to get image of active photo of employee", tags = {"Photo"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Active photo has been found"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. Employee hasn't been found"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    ResponseEntity<byte[]> getPhotoImage(Integer employeeId);

    @Override
    @Operation(summary = "Endpoint  is used to upload new photo of employee.",
               description = "Create request to upload new photo of employee", tags = {"Photo"})
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "OK. Uploaded"),
                           @ApiResponse(responseCode = "204", description = "NO CONTENT. Employee hasn't been found"),
                           @ApiResponse(responseCode = "400", description = "BAD REQUEST. Invalid input"),
                           @ApiResponse(responseCode = "401", description = "UNAUTHORIZED. Authentication is needed"),
                           @ApiResponse(responseCode = "403", description = "FORBIDDEN. Access denied"),
                           @ApiResponse(responseCode = "404", description = "NOT FOUND. Resource was not found")})
    EmployeeReadDto uploadPhoto(Integer employeeId, MultipartFile file);
}
