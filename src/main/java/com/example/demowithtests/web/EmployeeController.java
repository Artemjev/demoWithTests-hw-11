package com.example.demowithtests.web;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.dto.employee.EmployeeCreateDto;
import com.example.demowithtests.dto.employee.EmployeePatchDto;
import com.example.demowithtests.dto.employee.EmployeePutDto;
import com.example.demowithtests.dto.employee.EmployeeReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;


public interface EmployeeController {

    //Получение юзера по id
    EmployeeReadDto getEmployee(Integer id);

    //Добовление юзера в базу данных
    EmployeeReadDto createEmployee(EmployeeCreateDto createDto);

    //Обновление юзера (patch)
    Employee patchEmployee(Integer id, EmployeePatchDto patchDto);

    //Full update юзера (put)
    Employee putEmployee(Integer id, EmployeePutDto putDto);

    //Помечаем юзера c указанным id  как удаленного в бд (setIsDeleted = true).
    void markEmployeeAsDeleted(Integer id);

    //Удаляем юзера c указанным id из бд.
    void deleteEmployee(Integer id);

    //Получение списка всех юзеров
    List<EmployeeReadDto> getAllEmployees();

    //Получение 1 страницы юзеров
    Page<EmployeeReadDto> getPageOfEmployees(int page, int size);

    //Удаление всех юзеров
    void removeAllUsers();

    Page<Employee> getEmployeesByCountry(String country, int page, int size, List<String> sortList, Sort.Direction sortOrder);

    List<String> getAllEmployeesCountries();

    List<String> getAllEmployeesCountriesSorted();

    Optional<String> getAllEmployeesEmails();

    List<Employee> getEmployeesByGenderAndCountry(Gender gender, String country);

    Page<Employee> getEmployeesWithActiveAddressesInCountry(String country, int page, int size);

    List<Employee> handleEmployeesWithIsDeletedFieldIsNull();

    List<Employee> handleEmployeesWithIsPrivateFieldIsNull();

    Page<Employee> getAllActiveUsers(int page, int size);

    Page<Employee> getAllDeletedUsers(int page, int size);

    /**
     * Метод отправляет на почту юзера письмо с запросом на подтверждение.
     * Из письма юзер должен дернуть эндпоинт "/users/{id}/confirmed" (ссылка в тексте письма специальная),
     * который через контроллер вызовет метод сервиса confirm(id) и поменяет поле сущности is_confirmed в БД.
     */
    void sendConfirm(Integer id);

    void confirm(Integer id);

    // точка входа на массовую генерацию
    Long generateEmployees(Integer quantity, Boolean clear);

    Long employeeMassPutUpdate();

    Long employeeMassPatchUpdate();

    List<Employee> getEmployeesWithExpiredPhotos();

    void sendEmailToEmployeesWhosePhotoIsExpired();
}
