package com.example.demowithtests.service;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.domain.Photo;
import com.example.demowithtests.dto.employee.EmployeeReadDto;
import com.example.demowithtests.repository.EmployeeRepository;
import com.example.demowithtests.util.exception.NoPhotoEmployeeException;
import com.example.demowithtests.util.exception.NoSuchEmployeeException;
import com.example.demowithtests.util.exception.ResourceNotFoundException;
import com.example.demowithtests.util.mail.Mailer;
import com.example.demowithtests.util.validation.annotation.custom.CountryMatchesAddressesVerification;
import com.example.demowithtests.util.validation.annotation.custom.CustomValidationAnnotations;
import com.example.demowithtests.util.validation.annotation.custom.MarkedAsDeleted;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@AllArgsConstructor
@Slf4j @Service
public class EmployeeServiceBean implements EmployeeService {

    private final String LOG_START = "EmployeeService --> EmployeeServiceBean --> start of method:  ";
    private final String LOG_END = "EmployeeService --> EmployeeServiceBean --> finish of method:  ";

    private final EmployeeRepository employeeRepository;
    private final Mailer mailer;


    //----------------------------------------------------------------------------------------------------
    @Override
    //    public Optional<Photo> getPhoto(Integer employeeId) {
    public Optional<Photo> getPhoto(Integer employeeId) {

        log.debug(LOG_START + "Photo getPhoto(Integer employeeId = {})", employeeId);

        System.err.println(LOG_START + "Photo getPhoto(Integer employeeId=" + employeeId);

        Employee employee = getEmployee(employeeId);

        System.err.println("!!!!!!!!!!!!!!!!!! employee = " + employee);

        System.err.println("!!!!!!!!!!!!!!!!!! employee.getPhotos() = " + employee.getPhotos());


        //        Photo photo = employee.getPhotos()
        //                .stream()
        //                //                .findFirst()
        //                .findAny()
        //                .orElseThrow(() -> new NoPhotoEmployeeException("Employee has no photo!"));


        Optional<Photo> photoOpt = employee.getPhotos()
                .stream()
                //                .findFirst()
                .findAny();
        //                .orElseThrow(() -> new NoPhotoEmployeeException("Employee has no photo!"));

        System.err.println("!!!!!!!!!!!!!!!!!! photoOpt = " + photoOpt);

        //        Photo result;
        //        Photo result;

        //        if (photoOpt.isPresent()){
        //            System.err.println("!!!!!!!!!!!!!!!!!! (photoOpt.isPresent()++++++");
        //            result = photoOpt.orElseThrow(() -> new NoPhotoEmployeeException("Employee has no photo!"));
        //        }else {
        //            System.err.println("!!!!!!!!!!!!!!!!!! (photoOpt.isPresent()------");
        //            throw new NoPhotoEmployeeException("Employee has no photo!");
        //        }

        //        Photo result = photoOpt.orElseThrow(() -> new NoPhotoEmployeeException("Employee has no photo!"));

        //        System.err.println("!!!!!!!!!!!!!!!!!! photo = " + photo);


        Optional<Photo> result = photoOpt;

        log.debug(LOG_END + "Photo getPhoto(Integer employeeId = {}): result = {}", employeeId, result);
        System.err.println(LOG_END + "Photo getPhoto(Integer employeeId = {}): result = " + result);

        return result;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public Employee addPhoto(Integer id, MultipartFile file) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        Photo photo = null;
        try {
            photo = new Photo();
            photo.setFileName(fileName);
            photo.setFileType(file.getContentType());
            photo.setData(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NoSuchEmployeeException("There is no employee with ID=" + id + " in database"));
        employee.getPhotos().add(photo);
        Employee result = employeeRepository.save(employee);

        return result;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    @CustomValidationAnnotations({MarkedAsDeleted.class})
    public Employee getEmployee(Integer id) {
        log.debug(LOG_START + "Employee getById(Integer id = {})", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchEmployeeException("There is no employee with ID=" + id + " in database"));
        setIsActiveStatusTrueIfNull(employee); // todo: перенести эту обработку в асспект по валидации полей!
        setIsPrivateStatusTrueIfNull(employee);
        log.debug(LOG_END + "Employee getById(Integer id): result = {}", employee);
        return employee;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public Employee createEmployee(Employee employee) {
        log.debug(LOG_START + "Employee createEmployee(Employee employee = {})", employee);
        Employee result = employeeRepository.save(employee);
        log.debug(LOG_END + "Employee createEmployee(Employee employee): result = {}", result);
        return result;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    @Transactional
    @CustomValidationAnnotations({MarkedAsDeleted.class, CountryMatchesAddressesVerification.class})
    public Employee patchEmployee(Integer id, Employee employee) {
        log.debug(LOG_START + "Employee patchEmployee(Integer id = {}, Employee employee = {})", id, employee);
        return employeeRepository.findById(id).map(e -> {
            if (employee.getName() != null && !employee.getName().equals(e.getName())) {
                e.setName(employee.getName());
            }
            if (employee.getEmail() != null && !employee.getEmail().equals(e.getEmail())) {
                e.setEmail(employee.getEmail());
            }
            if (employee.getCountry() != null && !employee.getCountry().equals(e.getCountry())) {
                e.setCountry(employee.getCountry());
            }
            if (employee.getAddresses() != null && !employee.getAddresses().equals(e.getAddresses())) {
                e.setAddresses(employee.getAddresses());
            }
            if (employee.getIsDeleted() != null && !employee.getIsDeleted().equals(e.getIsDeleted())) {
                e.setIsDeleted(employee.getIsDeleted());
            }
            if (employee.getIsPrivate() != null && !employee.getIsPrivate().equals(e.getIsPrivate())) {
                e.setIsPrivate(employee.getIsPrivate());
            }
            if (employee.getIsConfirmed() != null && !employee.getIsConfirmed().equals(e.getIsConfirmed())) {
                e.setIsConfirmed(employee.getIsConfirmed());
            }
            if (employee.getPhotos() != null && !employee.getPhotos().equals(e.getPhotos())) {
                e.setPhotos(employee.getPhotos());
            }
            //todo:
            // Код выше (как я понимаю, реализует patch, не put, лайтово мержит), што-то мне подсказывает,
            // можно не писать для каждой сущности, а написать некую утилиту c методом, который принимает
            // описание класса сущности (напр, Employee.class) + саму сущность и с помощью рефлексии
            // бегает по полям, проверяетЮ, сетит.
            // Еще что-то мне подсказывает, что у спринга должна быть какая-то дефолтная реализация подобного(?)
            //                    log.debug("updateById(Integer id, Employee employee) Service end: e = {}", e);
            Employee result = employeeRepository.save(e);
            log.debug(LOG_END + "Employee patchEmployee(Integer id, Employee employee): result = {}", result);
            return result;
        }).orElseThrow(() -> new NoSuchEmployeeException("There is no employee with ID=" + id + " in database"));
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    @Transactional
    @CustomValidationAnnotations({MarkedAsDeleted.class})
    public Employee updateEmployee(Integer id, Employee employee) {
        log.debug(LOG_START + "Employee updateEmployee(Integer id = {}, Employee employee = {})", id, employee);
        return employeeRepository.findById(id).map(e -> {
            e.setName(employee.getName());
            e.setEmail(employee.getEmail());
            e.setCountry(employee.getCountry());
            e.setAddresses(employee.getAddresses());
            e.setIsDeleted(employee.getIsDeleted());
            e.setIsPrivate(employee.getIsPrivate());
            e.setIsConfirmed(employee.getIsConfirmed());
            e.setPhotos(employee.getPhotos());
            Employee result = employeeRepository.save(e);
            log.debug(LOG_END + "Employee updateEmployee(Integer id, Employee employee): result = {}", result);
            return result;
        }).orElseThrow(() -> new NoSuchEmployeeException("There is no employee with ID=" + id + " in database"));
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void deleteEmployee(Integer id) {
        log.debug(LOG_START + "void deleteEmployee(Integer id = {})", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchEmployeeException("There is no employee with ID=" + id + " in database"));
        employeeRepository.delete(employee);
        log.debug(LOG_END + "void deleteEmployee(Integer id): employee {} was deleted", employee);
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void markEmployeeAsDeleted(Integer id) {
        log.debug(LOG_START + "void markEmployeeAsDeleted(Integer id = {}): ", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(
                        () -> new NoSuchEmployeeException("There is no employee with ID=" + id + " in database"));
        employee.setIsDeleted(Boolean.TRUE);
        employeeRepository.save(employee);
        log.debug(LOG_END + "void markEmployeeAsDeleted(Integer id): employee = {} was marked as delete", employee);
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public List<Employee> getAll() {
        log.debug(LOG_START + "List<Employee> getAll().");
        List<Employee> result = employeeRepository.findAll();
        log.debug(LOG_END + "List<Employee> getAll(): result = {}", result);
        return result;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public Page<Employee> getAllWithPagination(Pageable pageable) {
        log.debug(LOG_START + "Page<Employee> getAllWithPagination(Pageable pageable = {})", pageable);
        Page<Employee> result = employeeRepository.findAll(pageable);
        log.debug(LOG_END + "Employee createEmployee(Employee employee): result = {}", result);
        return result;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void deleteAll() {
        log.debug(LOG_START + "void deleteAll()");
        employeeRepository.deleteAll();
        log.debug(LOG_END + "void deleteAll()");
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public Page<Employee> getByCountryAndSort(String country, int page, int size, List<String> sortList,
            String sortOrder) {
        log.debug(LOG_START + "Page<Employee> getByCountryAndSort(String country  = {}, " + "int page = {}, " +
                  "int size = {}, List<String> sortList = {}, " + "String sortOrder = {}))", country, page, size,
                sortList, sortOrder);
        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
        Page<Employee> result = employeeRepository.findByCountry(country, pageable);
        log.debug(LOG_END + "Page<Employee> getByCountryAndSort(String country, int page, int size, " +
                  "List<String> sortList, String sortOrder):result = {} ", result);
        return result;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public Set<String> getAllEmployeesCountries() {
        log.debug(LOG_START + "List<String> getAllEmployeesCountries()");
        Set<Employee> employeeList = new HashSet<>(employeeRepository.findAll());
        Set<String> result = employeeList.stream().map(country -> country.getCountry()).collect(Collectors.toSet());
        log.debug(LOG_END + "List<String> getAllEmployeesCountries(): result = {}", result);
        return result;
    }


    //----------------------------------------------------------------------------------------------------
    @Override
    public List<String> getAllEmployeesCountriesSorted() {
        log.debug(LOG_START + "List<String> getAllEmployeesCountriesSorted()");
        List<Employee> employeeList = employeeRepository.findAll();
        List<String> result = employeeList.stream().map(Employee::getCountry).sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        log.debug(LOG_END + "getAllEmployeesCountriesSorted(): result = {}", result);
        return result;
    }


    //----------------------------------------------------------------------------------------------------
    @Override
    public Optional<List<String>> getEmails() {
        log.debug(LOG_START + "Optional<List<String>> getEmails()");
        List<Employee> employeeList = employeeRepository.findAll();
        List<String> emails = employeeList.stream().map(Employee::getEmail).collect(Collectors.toList());
        Optional<List<String>> result = Optional.ofNullable(emails);
        log.debug(LOG_END + "Optional<String> getEmails(): result = {}", result);
        return result;
    }


    //----------------------------------------------------------------------------------------------------
    @Override
    public List<Employee> getEmployeesByGenderAndCountry(Gender gender, String country) {
        log.debug(
                LOG_START + "List<Employee> getEmployeesByGenderAndCountry(Gender gender = {}, String country = {})",
                gender, country);
        List<Employee> result = employeeRepository.findByGenderAndCountry(gender.toString(), country);
        log.debug(LOG_END +
                  "List<Employee> getEmployeesByGenderAndCountry(Gender gender, String country): result = {}",
                result);
        return result;
    }


    //----------------------------------------------------------------------------------------------------
    @Override
    public Page<Employee> getEmployeesWithActiveAddressesInCountry(String country, Pageable pageable) {
        log.debug(LOG_START + "Page<Employee> getEmployeesWithActiveAddressesInCountry(String country  = {}, " +
                  "Pageable pageable  = {})", country, pageable);
        Page<Employee> result = employeeRepository.findAllWhoHasActiveAddressesInCountry(country, pageable);
        log.debug(LOG_END + "Page<Employee> getEmployeesWithActiveAddressesInCountry(String country, " +
                  "Pageable pageable): result = {}", result);
        return result;
    }


    //---------------------------------------------------------------------------------------
    @Override
    public List<Employee> handleEmployeesWithIsDeletedFieldIsNull() {
        log.debug(LOG_START + "List<Employee> handleEmployeesWithIsDeletedFieldIsNull()");
        List<Employee> employees = employeeRepository.queryEmployeeByIsDeletedIsNull();
        employees.forEach(employee -> employee.setIsDeleted(Boolean.FALSE));
        employeeRepository.saveAll(employees);
        List<Employee> result = employeeRepository.queryEmployeeByIsDeletedIsNull();
        log.debug(LOG_END + "List<Employee> handleEmployeesWithIsDeletedFieldIsNull(): result = {}", result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    public List<Employee> handleEmployeesWithIsPrivateFieldIsNull() {
        log.debug(LOG_START + "List<Employee> handleEmployeesWithIsPrivateFieldIsNull()");
        List<Employee> employees = employeeRepository.queryEmployeeByIsPrivateIsNull();
        employees.forEach(employee -> employee.setIsPrivate(Boolean.FALSE));
        employeeRepository.saveAll(employees);
        List<Employee> result = employeeRepository.queryEmployeeByIsPrivateIsNull();
        log.debug(LOG_END + "List<Employee> handleEmployeesWithIsPrivateFieldIsNull(): result = {}", result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    public List<Employee> handleEmployeesWithIsConfirmedFieldIsNull() {
        log.debug(LOG_START + "List<Employee> handleEmployeesWithIsPrivateFieldIsNull()");
        List<Employee> employees = employeeRepository.queryEmployeeByIsConfirmedNull();
        employees.stream()
                .filter(e -> e.getIsConfirmed() == null)
                .forEach(e -> e.setIsConfirmed(Boolean.FALSE));
        employeeRepository.saveAll(employees);
        List<Employee> result = employeeRepository.queryEmployeeByIsPrivateIsNull();
        log.debug(LOG_END + "List<Employee> handleEmployeesWithIsPrivateFieldIsNull(): result = {}", result);
        return result;
    }


    //---------------------------------------------------------------------------------------
    @Override public Page<Employee> getAllActive(Pageable pageable) {
        log.debug(LOG_START + "Page<Employee> getAllActive(Pageable pageable  = {})", pageable);
        Page<Employee> result = employeeRepository.findAllActive(pageable);
        log.debug(LOG_END + "Page<Employee> getAllActive(Pageable pageable): result = {}", result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    public Page<Employee> getAllDeleted(Pageable pageable) {
        log.debug(LOG_START + "Page<Employee> getAllDeleted(Pageable pageable  = {})", pageable);
        Page<Employee> result = employeeRepository.findAllDeleted(pageable);
        log.debug(LOG_END + "Page<Employee> getAllDeleted(Pageable pageable): result = {}", result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    public void sendMailConfirm(Integer id) {
        log.debug(LOG_START + "void sendMailConfirm(Integer id = {})", id);
        Employee employee = employeeRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        mailer.send(employee,
                "Provided data confirmation",
                "Пожалуйста, нажмите на ссылку ниже, чтобы подтвердить свою регистрацию:\n" +
                "<a href=\"http://localhost:8087/api/users/" + employee.getId() + "/confirmed>" +
                "Подтвердить регистрацию</a>\n" +
                "Если вы не регистрировались на нашем сайте, проигнорируйте это сообщение.\n");
        log.debug(LOG_END + "void sendMailConfirm(Integer id): email confirmation was sent to employee {}",
                employee);
    }

    //---------------------------------------------------------------------------------------
    @Override
    public void confirm(Integer id) {
        log.debug(LOG_START + "void confirm(Integer id = {})", id);
        Employee employee = employeeRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        employee.setIsConfirmed(Boolean.TRUE);
        employeeRepository.save(employee);
        log.debug(LOG_END + "void confirm(Integer id): employee {} is confirmed", employee);
    }

    //---------------------------------------------------------------------------------------
    @Override
    public void generateEntities(Integer quantity, Boolean clear) {
        log.debug(LOG_START + "void generateEntities(Integer quantity = {}, Boolean clear = {})", quantity, clear);
        if (clear) employeeRepository.deleteAll();
        List<Employee> employees = new ArrayList<>(1000);
        for (int i = 0; i < quantity; i++) {
            employees.add(Employee.builder().name("Name" + i).email("artemjev.mih@gmail.com").build());
        }
        employeeRepository.saveAll(employees);
        log.debug(LOG_END +
                  "void generateEntities(Integer quantity, Boolean clear): {} entities were created;", quantity);
    }

    //---------------------------------------------------------------------------------------
    @Override
    public void massTestUpdate() {
        log.debug(LOG_START + "massTestUpdate()");
        List<Employee> employees = employeeRepository.findAll();
        employees.forEach(employee -> employee.setName(employee.getName() + LocalDateTime.now().toString()));
        employeeRepository.saveAll(employees);
        log.debug(LOG_END + "void massTestUpdate(): done.");
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public List<Employee> findEmployeesWithExpiredPhotos() {
        log.debug(LOG_START + "List<Employee> findEmployeesWithExpiredPhotos()");
        List<Employee> result = employeeRepository.findAll()
                //.findAll(), если ничего не находит, возвращает пустой список; поидее null бытьне может.
                .stream()
                .filter(employee -> employee.getPhotos().stream().flatMap(photo -> Stream.of(isPhotoExpired(photo)))
                        .anyMatch(Boolean.TRUE::equals)).collect(Collectors.toList());

        //.findAll(), если ничего не находит, возвращает пустой список; поидее null бытьне может.
        if (result.isEmpty()) {
            throw new NoSuchEmployeeException("There are no employees with expired photos");
        }
        //        return Optional.of(result)
        log.debug(LOG_END + "List<Employee> findEmployeesWithExpiredPhotos(): result = {}", result);
        return result;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void sendEmailToEmployeesWhosePhotoIsExpired() {
        log.debug(LOG_START + "void sendEmailToEmployeesWhosePhotoIsExpired()");
        findEmployeesWithExpiredPhotos().stream().forEach(e -> mailer.send(e, "subject",
                "Пожалуйста, нажмите на ссылку ниже, чтобы подтвердить свою регистрацию:\n" +
                "<a href=\"http://localhost:8087/api/users/" + e.getId() + "/confirmed>Подтвердить регистрацию</a>\n" +
                "Если вы не регистрировались на нашем сайте, проигнорируйте это сообщение.\n"));
        log.debug(LOG_END + "void sendEmailToEmployeesWhosePhotoIsExpired(): emails were sent.");
    }

    //----------------------------------------------------------------------------------------------------
    private Boolean isPhotoExpired(Photo photo) {
        log.debug(LOG_START + "private Boolean isPhotoExpired(Photo photo  = {})", photo);
        Boolean result = photo.getAddDate()
                .plusYears(5)
                .minusDays(7)
                .isBefore(LocalDateTime.now());
        log.debug(LOG_END + "private Boolean isPhotoExpired(Photo photo): result = {}", result);
        return result;
    }

    //----------------------------------------------------------------------------------------------------
    // private methods
    // todo: хорошо бы, наверное, подобные методы в к какую-то спец.утилиту вынести...
    //----------------------------------------------------------------------------------------------------
    private void setIsPrivateStatusTrueIfNull(Employee employee) {
        log.debug(LOG_START + "private void setIsPrivateStatusTrueIfNull(Employee employee = {})", employee);
        if (employee.getIsPrivate() == null) {
            employee.setIsPrivate(Boolean.TRUE);
            employeeRepository.save(employee);
        }
        log.debug(LOG_END + "private void setIsPrivateStatusTrueIfNull(Employee employee): done.");
    }

    //----------------------------------------------------------------------------------------------------
    private void setIsActiveStatusTrueIfNull(Employee employee) {
        log.debug(LOG_START + "private void setIsActiveStatusTrueIfNull(Employee employee = {})", employee);
        if (employee.getIsDeleted() == null) {
            employee.setIsDeleted(Boolean.FALSE);
            employeeRepository.save(employee);
        }
        log.debug(LOG_END + "private void setIsActiveStatusTrueIfNull(Employee employee): done.");
    }

    //----------------------------------------------------------------------------------------------------
    private List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection) {
        log.debug(LOG_START + "private List<Sort.Order> createSortOrder(List<String> sortList = {}, " +
                  "String sortDirection = {})", sortList, sortDirection);
        List<Sort.Order> sorts = new ArrayList<>();
        Sort.Direction direction;
        for (String sort : sortList) {
            if (sortDirection != null) {
                direction = Sort.Direction.fromString(sortDirection);
            } else {
                direction = Sort.Direction.DESC;
            }
            sorts.add(new Sort.Order(direction, sort));
        }
        log.debug(LOG_END + "List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection): "
                  + "result = {}", sorts);
        return sorts;
    }

    //----------------------------------------------------------------------------------------------------
}






