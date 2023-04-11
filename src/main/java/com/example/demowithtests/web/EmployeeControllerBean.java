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
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class EmployeeControllerBean implements EmployeeControllerApiDoc {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    private final String LOG_START = "EmployeeController --> EmployeeControllerBean --> start of method:  ";
    private final String LOG_END = "EmployeeController --> EmployeeControllerBean --> finish of method:  ";

    //---------------------------------------------------------------------------------------
    //Получение юзера по id
    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeReadDto getEmployee(@PathVariable Integer id) {
        log.info(LOG_START + "EmployeeReadDto getEmployee(Integer id = {})", id);
        EmployeeReadDto result = employeeMapper.employeeToEmployeeReadDto(employeeService.getEmployee(id));

        return result;
    }

    //---------------------------------------------------------------------------------------
    //Добовление юзера в базу данных
    @Override
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeReadDto createEmployee(@Valid @RequestBody EmployeeCreateDto createDto) {
        log.info(LOG_START + "EmployeeReadDto createEmployee(EmployeeCreateDto createDto = {})", createDto);
        Employee employee = employeeMapper.employeeCreateDtoToEmployee(createDto);
        EmployeeReadDto result = employeeMapper.employeeToEmployeeReadDto(employeeService.createEmployee(employee));

        return result;
    }

    //---------------------------------------------------------------------------------------
    //Обновление юзера (patch)
    @Override
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeReadDto patchEmployee(@PathVariable("id") Integer id, @RequestBody EmployeePatchDto patchDto) {
        log.info(LOG_START + "EmployeeReadDto patchEmployee(Integer id  = {}, EmployeePatchDto patchDto = {})",
                id, patchDto);
        Employee employee = employeeMapper.employeePatchDtoToEmployee(patchDto);
        EmployeeReadDto result = employeeMapper.employeeToEmployeeReadDto(employeeService.patchEmployee(id, employee));

        return result;
    }

    //---------------------------------------------------------------------------------------
    //Full update юзера (put)
    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeReadDto putEmployee(@PathVariable("id") Integer id, @Valid @RequestBody EmployeePutDto putDto) {
        log.info(LOG_START + "EmployeeReadDto putEmployee(Integer id  = {}, EmployeePatchDto putDto = {})",
                id, putDto);
        Employee employee = employeeMapper.employeePutDtoToEmployee(putDto);
        EmployeeReadDto result = employeeMapper.employeeToEmployeeReadDto(employeeService.updateEmployee(id, employee));

        return result;
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

        List<EmployeeReadDto> result = employeeService.getAll()
                .stream()
                .map(employeeMapper::employeeToEmployeeReadDto)
                .collect(Collectors.toList());

        return result;
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
        Page<EmployeeReadDto> result = employees.map(employeeMapper::employeeToEmployeeReadDto);

        return result;
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
    public Page<EmployeeReadDto> getEmployeesByCountry(@RequestParam(required = false) String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "") List<String> sortList,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortOrder) {

        Page<EmployeeReadDto> result =
                employeeService.getByCountryAndSort(country, page, size, sortList, sortOrder.toString())
                        .map(employeeMapper::employeeToEmployeeReadDto);

        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/countries")
    @ResponseStatus(HttpStatus.OK)
    public Set<String> getAllEmployeesCountries() {

        Set<String> result = employeeService.getAllEmployeesCountries();

        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/sortedCountries")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllEmployeesCountriesSorted() {
        List<String> result = employeeService.getAllEmployeesCountriesSorted();
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/emails")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<String>> getAllEmployeesEmails() {

        Optional<List<String>> result = employeeService.getEmails();

        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/byGenderAndCountry")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> getEmployeesByGenderAndCountry(@RequestParam Gender gender,
            @RequestParam String country) {

        List<EmployeeReadDto> result = employeeService.getEmployeesByGenderAndCountry(gender, country)
                .stream()
                .map(employeeMapper::employeeToEmployeeReadDto)
                .collect(Collectors.toList());
        ;

        return result;
    }

    //---------------------------------------------------------------------------------------
    //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @Override
    //    получаем сотрудников у которох есть активные адреса в заданной стране
    //    get employees who have active addresses in a given country
    @GetMapping("/hasActiveAddressInCountry")
    @ResponseStatus(HttpStatus.OK)
    public Page<EmployeeReadDto> getEmployeesWithActiveAddressesInCountry(@RequestParam String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<EmployeeReadDto> result = employeeService.getEmployeesWithActiveAddressesInCountry(country, pageable)
                .map(employeeMapper::employeeToEmployeeReadDto);

        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/proc-is-deleted")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> handleEmployeesWithIsDeletedFieldIsNull() {

        List<EmployeeReadDto> result = employeeService.handleEmployeesWithIsDeletedFieldIsNull()
                .stream()
                .map(employeeMapper::employeeToEmployeeReadDto)
                .collect(Collectors.toList());

        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/proc-is-private")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> handleEmployeesWithIsPrivateFieldIsNull() {

        List<EmployeeReadDto> result = employeeService.handleEmployeesWithIsPrivateFieldIsNull()
                .stream()
                .map(employeeMapper::employeeToEmployeeReadDto)
                .collect(Collectors.toList());

        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/proc-is-confirmed")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> handleEmployeesWithIsConfirmedFieldIsNull() {

        List<EmployeeReadDto> result = employeeService.handleEmployeesWithIsConfirmedFieldIsNull()
                .stream()
                .map(employeeMapper::employeeToEmployeeReadDto)
                .collect(Collectors.toList());

        return result;
    }


    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public Page<EmployeeReadDto> getAllActiveUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<EmployeeReadDto> result = employeeService.getAllActive(pageable)
                .map(employeeMapper::employeeToEmployeeReadDto);

        return result;

    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/deleted")
    @ResponseStatus(HttpStatus.OK)
    public Page<EmployeeReadDto> getAllDeletedUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));

        Page<EmployeeReadDto> result = employeeService.getAllDeleted(pageable)
                .map(employeeMapper::employeeToEmployeeReadDto);

        return result;
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
    public List<EmployeeReadDto> getEmployeesWithExpiredPhotos() {
        return employeeService.findEmployeesWithExpiredPhotos()
                .stream()
                .map(e -> employeeMapper.employeeToEmployeeReadDto(e))
                .collect(Collectors.toList());
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/expired-photos/send-notification")
    @ResponseStatus(HttpStatus.OK)
    public void sendEmailToEmployeesWhosePhotoIsExpired() {
        employeeService.sendEmailToEmployeesWhosePhotoIsExpired();
    }

}
