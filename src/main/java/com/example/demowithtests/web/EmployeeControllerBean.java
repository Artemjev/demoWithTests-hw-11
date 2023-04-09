package com.example.demowithtests.web;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.dto.employee.EmployeeCreateDto;
import com.example.demowithtests.dto.employee.EmployeePatchDto;
import com.example.demowithtests.dto.employee.EmployeePutDto;
import com.example.demowithtests.dto.employee.EmployeeReadDto;
import com.example.demowithtests.service.EmployeeService;
import com.example.demowithtests.util.mapper.EmployeeMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class EmployeeControllerBean implements EmployeeControllerApiDoc {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    //---------------------------------------------------------------------------------------
    //Получение юзера по id
    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeReadDto getEmployee(@PathVariable Integer id) {
        return employeeMapper.employeeToEmployeeReadDto(employeeService.getEmployee(id));
    }

    //---------------------------------------------------------------------------------------
    //Добовление юзера в базу данных
    @Override
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeReadDto createEmployee(@Valid @RequestBody EmployeeCreateDto createDto) {
        Employee employee = employeeMapper.employeeCreateDtoToEmployee(createDto);
        return employeeMapper.employeeToEmployeeReadDto(employeeService.createEmployee(employee));
    }

    //---------------------------------------------------------------------------------------
    //Обновление юзера (patch)
    @Override
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Employee patchEmployee(@PathVariable("id") Integer id, @RequestBody EmployeePatchDto patchDto) {
        Employee employee = employeeMapper.employeePatchDtoToEmployee(patchDto);
        System.err.println("\"Controller -> patchEmployee() employee =   " + employee);
        return employeeService.patchEmployee(id, employee);
    }

    //---------------------------------------------------------------------------------------
    //Full update юзера (put)
    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Employee putEmployee(@PathVariable("id") Integer id, @Valid @RequestBody EmployeePutDto putDto) {
        Employee employee = employeeMapper.employeePutDtoToEmployee(putDto);
        System.err.println("putDto = " + putDto);
        return employeeService.updateEmployee(id, employee);
    }

    //---------------------------------------------------------------------------------------
    //Помечаем юзера c указанным id  как удаленного в бд (setIsDeleted = true).
    @Override
    @PatchMapping("/{id}/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markEmployeeAsDeleted(@PathVariable Integer id) {
        employeeService.markEmployeeAsDeleted(id);
    }

    //---------------------------------------------------------------------------------------
    //Удаляем юзера c указанным id из бд.
    @Override
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
    }

    //---------------------------------------------------------------------------------------
    //Получение списка всех юзеров
    @Override
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> getAllEmployees() {
        return employeeService.getAll().stream().map(employeeMapper::employeeToEmployeeReadDto)
                .collect(Collectors.toList());
    }

    //---------------------------------------------------------------------------------------
    //Получение 1 страницы юзеров
    @Override
    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    public Page<EmployeeReadDto> getPageOfEmployees(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Employee> employees = employeeService.getAllWithPagination(paging);
        return employees.map(employeeMapper::employeeToEmployeeReadDto);
    }

    //---------------------------------------------------------------------------------------
    //Удаление всех юзеров
    @Override
    @DeleteMapping("/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAllUsers() {
        employeeService.deleteAll();
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/country")
    @ResponseStatus(HttpStatus.OK)
    public Page<Employee> getEmployeesByCountry(@RequestParam(required = false) String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "") List<String> sortList,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {

        return employeeService.getByCountryAndSort(country, page, size, sortList, sortOrder.toString());
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/countries")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllEmployeesCountries() {
        return employeeService.getAllEmployeesCountries();
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/sortedCountries")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllEmployeesCountriesSorted() {
        return employeeService.getAllEmployeesCountriesSorted();
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/emails")
    @ResponseStatus(HttpStatus.OK)
    public Optional<String> getAllEmployeesEmails() {
        return employeeService.getEmails();
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/byGenderAndCountry")
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> getEmployeesByGenderAndCountry(@RequestParam Gender gender, @RequestParam String country) {
        return employeeService.getEmployeesByGenderAndCountry(gender, country);
    }

    //---------------------------------------------------------------------------------------
    @Override
    //    получаем сотрудников у которох есть активные адреса в заданной стране
    //    get employees who have active addresses in a given country
    @GetMapping("/hasActiveAddressInCountry")
    @ResponseStatus(HttpStatus.OK)
    public Page<Employee> getEmployeesWithActiveAddressesInCountry(@RequestParam String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        return employeeService.getEmployeesWithActiveAddressesInCountry(country, pageable);
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/proc-is-deleted")
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> handleEmployeesWithIsDeletedFieldIsNull() {
        return employeeService.handleEmployeesWithIsDeletedFieldIsNull();
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/proc-is-private")
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> handleEmployeesWithIsPrivateFieldIsNull() {
        return employeeService.handleEmployeesWithIsPrivateFieldIsNull();
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public Page<Employee> getAllActiveUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        return employeeService.getAllActive(pageable);
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/deleted")
    @ResponseStatus(HttpStatus.OK)
    public Page<Employee> getAllDeletedUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        return employeeService.getAllDeleted(pageable);
    }

    //---------------------------------------------------------------------------------------

    /**
     * Метод отправляет на почту юзера письмо с запросом на подтверждение.
     * Из письма юзер должен дернуть эндпоинт "/users/{id}/confirmed" (ссылка в тексте письма специальная),
     * который через контроллер вызовет метод сервиса confirm(id) и поменяет поле сущности is_confirmed в БД.
     */
    @Override
    @GetMapping("/{id}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public void sendConfirm(@PathVariable Integer id) {
        employeeService.sendMailConfirm(id);
    }

    //---------------------------------------------------------------------------------------
    // @PatchMapping("/users/{id}/confirmed")
    // Get - костыль, так из письма проще этот эндпоинт дергать.
    @Override
    @GetMapping("/{id}/confirmed")
    @ResponseStatus(HttpStatus.OK)
    public void confirm(@PathVariable Integer id) {
        employeeService.confirm(id);
    }

    //---------------------------------------------------------------------------------------
    // точка входа на массовую генерацию
    @Override
    @PostMapping("/generate/{quantity}")
    @ResponseStatus(HttpStatus.CREATED)
    public Long generateEmployees(@PathVariable Integer quantity,
            @RequestParam(required = false, defaultValue = "false") Boolean clear) {
        LocalDateTime timeStart = LocalDateTime.now();
        log.info("Controller -> generateEmployees(Integer, Boolean) -> start: time={}", timeStart);
        employeeService.generateEntities(quantity, clear);
        LocalDateTime timeStop = LocalDateTime.now();
        log.info("Controller -> generateEmployee(Integer, Boolean) -> stop: time={}", timeStop);
        log.info("Controller -> generateEmployee(Integer, Boolean) -> method execution, ms: duration={}",
                Duration.between(timeStart, timeStop).toMillis());
        return Duration.between(timeStart, timeStop).toMillis();
    }

    //---------------------------------------------------------------------------------------
    @Override
    @PutMapping("/mass-test-update")
    @ResponseStatus(HttpStatus.OK)
    public Long employeeMassPutUpdate() {
        LocalDateTime timeStart = LocalDateTime.now();
        log.info("Controller -> employeeMassPutUpdate() method start: time={}", timeStart);
        employeeService.massTestUpdate();
        LocalDateTime timeStop = LocalDateTime.now();
        log.info("Controller -> employeeMassPutUpdate() method start: time={}", timeStop);
        log.info("Controller -> employeeMassPutUpdate() method execution, ms: duration={}",
                Duration.between(timeStart, timeStop).toMillis());
        return Duration.between(timeStart, timeStop).toMillis();
    }

    //---------------------------------------------------------------------------------------
    @Override
    @PatchMapping("/mass-test-update")
    @ResponseStatus(HttpStatus.OK)
    public Long employeeMassPatchUpdate() {
        LocalDateTime timeStart = LocalDateTime.now();
        log.info("Controller -> employeeMassPatchUpdate() method start: time={}", timeStart);
        employeeService.massTestUpdate();
        LocalDateTime timeStop = LocalDateTime.now();
        log.info("Controller -> employeeMassPatchUpdate() method start: time={}", timeStop);
        log.info("Controller -> employeeMassPatchUpdate() method execution, ms: duration={}",
                Duration.between(timeStart, timeStop).toMillis());
        return Duration.between(timeStart, timeStop).toMillis();
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/expired-photos")
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> getEmployeesWithExpiredPhotos() {
        return employeeService.findEmployeesWithExpiredPhotos();
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/expired-photos/send-notification")
    @ResponseStatus(HttpStatus.OK)
    public void sendEmailToEmployeesWhosePhotoIsExpired() {
        employeeService.sendEmailToEmployeesWhosePhotoIsExpired();
    }

}
