package com.example.demowithtests.web;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.domain.Photo;
import com.example.demowithtests.dto.employee.EmployeeCreateDto;
import com.example.demowithtests.dto.employee.EmployeePatchDto;
import com.example.demowithtests.dto.employee.EmployeePutDto;
import com.example.demowithtests.dto.employee.EmployeeReadDto;
import com.example.demowithtests.dto.photo.PhotoDto;
import com.example.demowithtests.service.EmployeeService;
import com.example.demowithtests.util.exception.NoPhotoEmployeeException;
import com.example.demowithtests.util.mapper.EmployeeMapper;
import com.example.demowithtests.util.mapper.PhotoMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private final PhotoMapper photoMapper;

    private final String LOG_START = "EmployeeController --> EmployeeControllerBean --> start of method:  ";
    private final String LOG_END = "EmployeeController --> EmployeeControllerBean --> finish of method:  ";


    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/{employeeId}/getPhotoDetails")
    public PhotoDto getPhotoDetails(@PathVariable Integer employeeId) {
        log.info(LOG_START + "PhotoDto getPhotoDetails(Integer employeeId = {})", employeeId);

        Photo photo = employeeService.getPhoto(employeeId)
                .orElseThrow(() -> new NoPhotoEmployeeException("Employee has no photo!"));

        PhotoDto result = photoMapper.photoToPhotoDto(photo);

        log.info(LOG_START + "PhotoDto getPhotoDetails(Integer employeeId = {}): result = {}", employeeId, result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/{employeeId}/getPhoto")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Integer employeeId) {
        log.info(LOG_START + "ResponseEntity<byte[]> getPhoto(Integer employeeId = {})", employeeId);

        Photo photo = employeeService.getPhoto(employeeId)
                .orElseThrow(() -> new NoPhotoEmployeeException("Employee has no photo!"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        ResponseEntity<byte[]> result = new ResponseEntity<>(photo.getData(), headers, HttpStatus.OK);

        log.info(LOG_END + "ResponseEntity<byte[]> getPhoto(Integer employeeId = {}): result = {}", employeeId, result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @PatchMapping("/{employeeId}/uploadPhoto")
    public EmployeeReadDto uploadPhoto(@PathVariable Integer employeeId, @RequestParam("file") MultipartFile file) {
        log.info(LOG_START + "ResponseEntity<String> uploadPhoto(Integer employeeId = {}, MultipartFile file = {})",
                employeeId, file);

        Employee updatedEmployee = employeeService.addPhoto(employeeId, file);

        EmployeeReadDto result = employeeMapper.employeeToEmployeeReadDto(updatedEmployee);

        log.info(LOG_END + "ResponseEntity<String> uploadPhoto(Integer employeeId = {}, MultipartFile file = {}):" +
                 " result = {}", employeeId, file, result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeReadDto getEmployee(@PathVariable Integer id) {
        log.info(LOG_START + "EmployeeReadDto getEmployee(Integer id = {})", id);
        EmployeeReadDto result = employeeMapper.employeeToEmployeeReadDto(employeeService.getEmployee(id));
        log.info(LOG_END + "EmployeeReadDto getEmployee(Integer id = {}): result = {}", id, result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeReadDto createEmployee(@Valid @RequestBody EmployeeCreateDto createDto) {
        log.info(LOG_START + "EmployeeReadDto createEmployee(EmployeeCreateDto createDto = {})", createDto);
        Employee employee = employeeMapper.employeeCreateDtoToEmployee(createDto);
        EmployeeReadDto result = employeeMapper.employeeToEmployeeReadDto(employeeService.createEmployee(employee));
        log.info(LOG_END + "EmployeeReadDto createEmployee(EmployeeCreateDto createDto = {}): result = {}",
                createDto, result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeReadDto putEmployee(@PathVariable("id") Integer id, @Valid @RequestBody EmployeePutDto putDto) {
        log.info(LOG_START + "EmployeeReadDto putEmployee(Integer id  = {}, EmployeePutDto putDto = {})",
                id, putDto);
        Employee employee = employeeMapper.employeePutDtoToEmployee(putDto);
        EmployeeReadDto result = employeeMapper.employeeToEmployeeReadDto(
                employeeService.updateEmployee(id, employee));

        log.info(LOG_END + "EmployeeReadDto putEmployee(Integer id  = {}, EmployeePutDto putDto = {}): result = {}",
                id, putDto, result);

        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EmployeeReadDto patchEmployee(@PathVariable("id") Integer id, @RequestBody EmployeePatchDto patchDto) {
        log.info(LOG_START + "EmployeeReadDto patchEmployee(Integer id  = {}, EmployeePatchDto patchDto = {})",
                id, patchDto);
        Employee employee = employeeMapper.employeePatchDtoToEmployee(patchDto);
        EmployeeReadDto result = employeeMapper.employeeToEmployeeReadDto(
                employeeService.patchEmployee(id, employee));

        log.info(
                LOG_END + "EmployeeReadDto patchEmployee(Integer id  = {}, EmployeePatchDto patchDto = {}): result = {}",
                id, patchDto, result);

        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @PatchMapping("/{id}/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void markEmployeeAsDeleted(@PathVariable Integer id) {
        log.info(LOG_START + "void markEmployeeAsDeleted(Integer id  = {})", id);
        employeeService.markEmployeeAsDeleted(id);
        log.info(LOG_END + "void markEmployeeAsDeleted(Integer id  = {})", id);
    }

    //---------------------------------------------------------------------------------------
    @Override
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Integer id) {
        log.info(LOG_START + "void deleteEmployee(Integer id  = {})", id);
        employeeService.deleteEmployee(id);
        log.info(LOG_END + "void deleteEmployee(Integer id  = {})", id);

    }

    //---------------------------------------------------------------------------------------
    @Override
    @DeleteMapping("/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAllUsers() {
        log.info(LOG_START + "void removeAllUsers()");
        employeeService.deleteAll();
        log.info(LOG_END + "void removeAllUsers()");
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> getAllEmployees() {
        log.info(LOG_START + "List<EmployeeReadDto> getAllEmployees()");
        List<EmployeeReadDto> result = employeeService.getAll()
                .stream()
                .map(employeeMapper::employeeToEmployeeReadDto)
                .collect(Collectors.toList());
        log.info(LOG_END + "List<EmployeeReadDto> getAllEmployees(): result = {}", result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    public Page<EmployeeReadDto> getPageOfEmployees(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        log.info(LOG_START + "Page<EmployeeReadDto> getPageOfEmployees(int page = {}, int size = {})", page, size);
        Pageable paging = PageRequest.of(page, size);
        Page<Employee> employees = employeeService.getAllWithPagination(paging);
        Page<EmployeeReadDto> result = employees.map(employeeMapper::employeeToEmployeeReadDto);
        log.info(LOG_END + "Page<EmployeeReadDto> getPageOfEmployees(int page = {}, int size = {}): result = {}",
                page, size, result);
        return result;
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
        log.info(LOG_START + "Page<EmployeeReadDto> getEmployeesByCountry(String country = {}, int page = {}, " +
                 "int size = {}, List<String> sortList = {}, Sort.Direction sortOrder = {})", country, page, size,
                sortList, sortOrder);
        Page<EmployeeReadDto> result =
                employeeService.getByCountryAndSort(country, page, size, sortList, sortOrder.toString())
                        .map(employeeMapper::employeeToEmployeeReadDto);

        log.info(LOG_END + "Page<EmployeeReadDto> getEmployeesByCountry(String country = {}, int page = {}, " +
                 "int size = {}, List<String> sortList = {}, Sort.Direction sortOrder = {}): result = {}",
                country, page, size, sortList, sortOrder, result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/countries")
    @ResponseStatus(HttpStatus.OK)
    public Set<String> getAllEmployeesCountries() {
        log.info(LOG_START + "Set<String> getAllEmployeesCountries()");
        Set<String> result = employeeService.getAllEmployeesCountries();
        log.info(LOG_END + "Set<String> getAllEmployeesCountries(): result = {}", result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/sortedCountries")
    @ResponseStatus(HttpStatus.OK)
    public List<String> getAllEmployeesCountriesSorted() {
        log.info(LOG_START + "List<String> getAllEmployeesCountriesSorted()");
        List<String> result = employeeService.getAllEmployeesCountriesSorted();
        log.info(LOG_END + "List<String> getAllEmployeesCountriesSorted(): result = {}", result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/emails")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<String>> getAllEmployeesEmails() {
        log.info(LOG_START + "Optional<List<String>> getAllEmployeesEmails()");
        Optional<List<String>> result = employeeService.getEmails();
        log.info(LOG_END + "Optional<List<String>> getAllEmployeesEmails(): result = {}", result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/byGenderAndCountry")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> getEmployeesByGenderAndCountry(@RequestParam Gender gender,
            @RequestParam String country) {
        log.info(LOG_START + "List<EmployeeReadDto> getEmployeesByGenderAndCountry(Gender gender = {}, " +
                 "String country = {})", gender, country);
        List<EmployeeReadDto> result = employeeService.getEmployeesByGenderAndCountry(gender, country)
                .stream()
                .map(employeeMapper::employeeToEmployeeReadDto)
                .collect(Collectors.toList());
        log.info(LOG_END + "List<EmployeeReadDto> getEmployeesByGenderAndCountry(Gender gender = {}, " +
                 "String country = {}): result = {}", gender, country, result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/hasActiveAddressInCountry")
    @ResponseStatus(HttpStatus.OK)
    public Page<EmployeeReadDto> getEmployeesWithActiveAddressesInCountry(@RequestParam String country,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        log.info(LOG_START + "Page<EmployeeReadDto> getAllActiveUsers(String country = {}, " +
                 "int page = {}, int size  = {})", country, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<EmployeeReadDto> result = employeeService.getEmployeesWithActiveAddressesInCountry(country, pageable)
                .map(employeeMapper::employeeToEmployeeReadDto);
        log.info(
                LOG_END + "Page<EmployeeReadDto> getAllActiveUsers(String country = {}, int page = {}, int size  = {})"
                + ": result = {}", country, page, size, result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/proc-is-deleted")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> handleEmployeesWithIsDeletedFieldIsNull() {
        log.info(LOG_START + "List<EmployeeReadDto> handleEmployeesWithIsDeletedFieldIsNull()");
        List<EmployeeReadDto> result = employeeService.handleEmployeesWithIsDeletedFieldIsNull()
                .stream()
                .map(employeeMapper::employeeToEmployeeReadDto)
                .collect(Collectors.toList());
        log.info(LOG_END + "List<EmployeeReadDto> handleEmployeesWithIsDeletedFieldIsNull(): result = {}", result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/proc-is-private")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> handleEmployeesWithIsPrivateFieldIsNull() {
        log.info(LOG_START + "List<EmployeeReadDto> handleEmployeesWithIsPrivateFieldIsNull()");
        List<EmployeeReadDto> result = employeeService.handleEmployeesWithIsPrivateFieldIsNull()
                .stream()
                .map(employeeMapper::employeeToEmployeeReadDto)
                .collect(Collectors.toList());
        log.info(LOG_END + "List<EmployeeReadDto> handleEmployeesWithIsPrivateFieldIsNull(): result = {}", result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/proc-is-confirmed")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> handleEmployeesWithIsConfirmedFieldIsNull() {
        log.info(LOG_START + "List<EmployeeReadDto> handleEmployeesWithIsConfirmedFieldIsNull()");
        List<EmployeeReadDto> result = employeeService.handleEmployeesWithIsConfirmedFieldIsNull()
                .stream()
                .map(employeeMapper::employeeToEmployeeReadDto)
                .collect(Collectors.toList());
        log.info(LOG_END + "List<EmployeeReadDto> handleEmployeesWithIsConfirmedFieldIsNull(): result = {}",
                result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public Page<EmployeeReadDto> getAllActiveUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        log.info(LOG_START + "Page<EmployeeReadDto> getAllActiveUsers(int page  = {}, int size  = {})", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<EmployeeReadDto> result = employeeService.getAllActive(pageable)
                .map(employeeMapper::employeeToEmployeeReadDto);
        log.info(LOG_END + "Page<EmployeeReadDto> getAllActiveUsers(int page  = {}, int size  = {}): result = {}",
                page, size, result);
        return result;

    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/deleted")
    @ResponseStatus(HttpStatus.OK)
    public Page<EmployeeReadDto> getAllDeletedUsers(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        log.info(LOG_START + "Page<EmployeeReadDto> getAllDeletedUsers(int page  = {}, int size  = {})", page,
                size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "name"));
        Page<EmployeeReadDto> result = employeeService.getAllDeleted(pageable)
                .map(employeeMapper::employeeToEmployeeReadDto);
        log.info(LOG_END + "Page<EmployeeReadDto> getAllDeletedUsers(int page  = {}, int size  = {}): result = {}",
                page, size, result);
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
        log.info(LOG_START + "void sendConfirm(Integer id  = {})", id);
        employeeService.sendMailConfirm(id);
        log.info(LOG_END + "void sendConfirm(Integer id  = {})", id);
    }

    //---------------------------------------------------------------------------------------
    // @PatchMapping("/users/{id}/confirmed")
    // Get - костыль, так из письма проще этот эндпоинт дергать.
    @Override
    @GetMapping("/{id}/confirmed")
    @ResponseStatus(HttpStatus.OK)
    public void confirm(@PathVariable Integer id) {
        log.info(LOG_START + "void confirm(Integer id  = {})", id);
        employeeService.confirm(id);
        log.info(LOG_END + "void confirm(Integer id  = {})", id);
    }


    //---------------------------------------------------------------------------------------
    // точка входа на массовую генерацию
    @Override
    @PostMapping("/generate/{quantity}")
    @ResponseStatus(HttpStatus.CREATED)
    public Long generateEmployees(@PathVariable Integer quantity,
            @RequestParam(required = false, defaultValue = "false") Boolean clear) {
        log.info(LOG_START + "Long generateEmployees(Integer quantity = {},  Boolean clear = {})", quantity, clear);
        LocalDateTime timeStart = LocalDateTime.now();
        log.info("... -> generateEmployees(Integer, Boolean) -> start: time={}", timeStart);
        employeeService.generateEntities(quantity, clear);
        LocalDateTime timeStop = LocalDateTime.now();
        log.info("... -> generateEmployee(Integer, Boolean) -> stop: time={}", timeStop);
        log.info("... -> generateEmployee(Integer, Boolean) -> method execution, ms: duration={}",
                Duration.between(timeStart, timeStop).toMillis());
        Long result = Duration.between(timeStart, timeStop).toMillis();
        log.info(LOG_END + "Long generateEmployees(Integer quantity = {},  Boolean clear = {}): result = {}",
                quantity, clear, result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @PutMapping("/mass-test-update")
    @ResponseStatus(HttpStatus.OK)
    public Long employeeMassPutUpdate() {
        log.info(LOG_START + "Long employeeMassPutUpdate()");
        LocalDateTime timeStart = LocalDateTime.now();
        log.info("... -> employeeMassPutUpdate() method start: time={}", timeStart);
        employeeService.massTestUpdate();
        LocalDateTime timeStop = LocalDateTime.now();
        log.info("... -> employeeMassPutUpdate() method start: time={}", timeStop);
        log.info("... -> employeeMassPutUpdate() method execution, ms: duration={}",
                Duration.between(timeStart, timeStop).toMillis());
        Long result = Duration.between(timeStart, timeStop).toMillis();
        log.info(LOG_END + "Long employeeMassPutUpdate(): result = {}", result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @PatchMapping("/mass-test-update")
    @ResponseStatus(HttpStatus.OK)
    public Long employeeMassPatchUpdate() {
        log.info(LOG_START + "Long employeeMassPatchUpdate()");
        LocalDateTime timeStart = LocalDateTime.now();
        log.info("... -> employeeMassPatchUpdate() method start: time={}", timeStart);
        employeeService.massTestUpdate();
        LocalDateTime timeStop = LocalDateTime.now();
        log.info("... -> employeeMassPatchUpdate() method start: time={}", timeStop);
        log.info("... -> employeeMassPatchUpdate() method execution, ms: duration={}",
                Duration.between(timeStart, timeStop).toMillis());
        Long result = Duration.between(timeStart, timeStop).toMillis();
        log.info(LOG_END + "Long employeeMassPatchUpdate(): result = {}", result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/expired-photos")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeReadDto> getEmployeesWithExpiredPhotos() {
        log.info(LOG_START + "List<EmployeeReadDto> getEmployeesWithExpiredPhotos()");
        List<EmployeeReadDto> result = employeeService.findEmployeesWithExpiredPhotos()
                .stream()
                .map(employeeMapper::employeeToEmployeeReadDto)
                .collect(Collectors.toList());
        log.info(LOG_END + "List<EmployeeReadDto> getEmployeesWithExpiredPhotos(): result = {}", result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    @GetMapping("/expired-photos/send-notification")
    @ResponseStatus(HttpStatus.OK)
    public void sendEmailToEmployeesWhosePhotoIsExpired() {
        log.info(LOG_START + "void sendEmailToEmployeesWhosePhotoIsExpired()");
        employeeService.sendEmailToEmployeesWhosePhotoIsExpired();
        log.info(LOG_END + "void sendEmailToEmployeesWhosePhotoIsExpired()");
    }

    //---------------------------------------------------------------------------------------
}
