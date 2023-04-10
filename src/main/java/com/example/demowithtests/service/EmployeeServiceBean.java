package com.example.demowithtests.service;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.domain.Photo;
import com.example.demowithtests.repository.EmployeeRepository;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@AllArgsConstructor
@Slf4j
@Service
public class EmployeeServiceBean implements EmployeeService {

    private final String LOG_START = "EmployeeService --> EmployeeServiceBean --> start of method:  ";
    private final String LOG_END = "EmployeeService --> EmployeeServiceBean --> finish of method:  ";

    private final EmployeeRepository employeeRepository;
    private final Mailer mailer;


    //----------------------------------------------------------------------------------------------------
    @Override
    @CustomValidationAnnotations({MarkedAsDeleted.class})
    public Employee getEmployee(Integer id) {
        log.debug(LOG_START + "Employee getById(Integer id = {})", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchEmployeeException("There is no employee with ID=" + id + " in database"));
        changeActiveStatus(employee); // todo: перенести эту обработку в асспект по валидации полей!
        changePrivateStatus(employee);
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
                })
                .orElseThrow(() ->
                        new NoSuchEmployeeException("There is no employee with ID=" + id + " in database"));
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
                })
                .orElseThrow(() ->
                        new NoSuchEmployeeException("There is no employee with ID=" + id + " in database"));
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void deleteEmployee(Integer id) {
        log.debug(LOG_START + "void deleteEmployee(Integer id = {})", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchEmployeeException("There is no employee with ID=" + id + " in database"));
        employeeRepository.delete(employee);
        log.debug(LOG_END + "void deleteEmployee(Integer id): employee {} was deleted", employee);
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void markEmployeeAsDeleted(Integer id) {
        log.debug(LOG_START + "void markEmployeeAsDeleted(Integer id = {}): ", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchEmployeeException("There is no employee with ID=" + id + " in database"));
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
    public Page<Employee> getByCountryAndSort(String country, int page, int size,
            List<String> sortList, String sortOrder) {

        log.debug(
                LOG_START + "Page<Employee> getByCountryAndSort(String country  = {}, int page = {}, int size = {}, " +
                "List<String> sortList = {}, String sortOrder = {}))", country, page, size, sortList, String sortOrder);

        Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
        return employeeRepository.findByCountry(country, pageable);

        Employee result = employeeRepository.save(employee);
        log.debug(LOG_END + "Employee createEmployee(Employee employee): result = {}", result);
        return result;
    }


    //----------------------------------------------------------------------------------------------------
    @Override
    public List<String> getAllEmployeesCountries() {

        log.debug(LOG_START + " : id = {}", id);
        List<Employee> employeeList = employeeRepository.findAll();
        List<String> countries = employeeList.stream()
                .map(country -> country.getCountry())
                .collect(Collectors.toList());
        log.info("getAllEmployeeCountry() - end: countries = {}", countries);
        log.debug(LOG_END + " : id = {}", id);

        return countries;

        Employee result = employeeRepository.save(employee);
        log.debug(LOG_END + "Employee createEmployee(Employee employee): result = {}", result);
        return result;
    }


    //----------------------------------------------------------------------------------------------------
    @Override
    public List<String> getAllEmployeesCountriesSorted() {
        log.debug(LOG_START + " : id = {}", id);
        List<Employee> employeeList = employeeRepository.findAll();
        return employeeList.stream()
                .map(Employee::getCountry)
                .sorted(Comparator.naturalOrder())
                .collect(Collectors.toList());
        log.debug(LOG_END + " : id = {}", id);


        Employee result = employeeRepository.save(employee);
        log.debug(LOG_END + "Employee createEmployee(Employee employee): result = {}", result);
        return result;
    }


    //----------------------------------------------------------------------------------------------------
    @Override
    public Optional<String> getEmails() {
        log.debug(LOG_START + " : id = {}", id);
        var employeeList = employeeRepository.findAll();
        var emails = employeeList.stream().map(Employee::getEmail).collect(Collectors.toList());
        var opt = emails.stream().filter(s -> s.endsWith(".com")).findFirst().orElse("error?");
        log.debug(LOG_END + " : id = {}", id);
        return Optional.ofNullable(opt);


        Employee result = employeeRepository.save(employee);
        log.debug(LOG_END + "Employee createEmployee(Employee employee): result = {}", result);
        return result;
    }


    //----------------------------------------------------------------------------------------------------
    @Override
    public List<Employee> getEmployeesByGenderAndCountry(Gender gender, String country) {
        log.debug(LOG_START + " : id = {}", id);
        var employees = employeeRepository.findByGenderAndCountry(gender.toString(), country);
        log.debug(LOG_END + " : id = {}", id);
        return employees;


        Employee result = employeeRepository.save(employee);
        log.debug(LOG_END + "Employee createEmployee(Employee employee): result = {}", result);
        return result;
    }


    //----------------------------------------------------------------------------------------------------
    @Override
    public Page<Employee> getEmployeesWithActiveAddressesInCountry(String country, Pageable pageable) {
        log.debug(LOG_START + " : id = {}", id);
        log.debug(LOG_END + " : id = {}", id);//!!!!!!!!!!!!!!!!!!!!!!!!!!!
        return employeeRepository.findAllWhoHasActiveAddressesInCountry(country, pageable);


        Employee result = employeeRepository.save(employee);
        log.debug(LOG_END + "Employee createEmployee(Employee employee): result = {}", result);
        return result;
    }


    //---------------------------------------------------------------------------------------
    @Override
    public List<Employee> handleEmployeesWithIsDeletedFieldIsNull() {
        log.debug(LOG_START + " : id = {}", id);
        var employees = employeeRepository.queryEmployeeByIsDeletedIsNull();
        for (Employee employee : employees) employee.setIsDeleted(Boolean.FALSE);
        employeeRepository.saveAll(employees);
        log.debug(LOG_END + " : id = {}", id);
        return employeeRepository.queryEmployeeByIsDeletedIsNull();


        Employee result = employeeRepository.save(employee);
        log.debug(LOG_END + "Employee createEmployee(Employee employee): result = {}", result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    public List<Employee> handleEmployeesWithIsPrivateFieldIsNull() {
        log.debug(LOG_START + " : id = {}", id);
        var employees = employeeRepository.queryEmployeeByIsPrivateIsNull();
        employees.forEach(employee -> employee.setIsPrivate(Boolean.FALSE));
        employeeRepository.saveAll(employees);
        log.debug(LOG_END + " : id = {}", id);
        return employeeRepository.queryEmployeeByIsPrivateIsNull();


        Employee result = employeeRepository.save(employee);
        log.debug(LOG_END + "Employee createEmployee(Employee employee): result = {}", result);
        return result;
    }


    //---------------------------------------------------------------------------------------
    @Override
    public Page<Employee> getAllActive(Pageable pageable) {
        log.debug(LOG_START + " : id = {}", id);
        return employeeRepository.findAllActive(pageable);
        log.debug(LOG_END + " : id = {}", id);


        Employee result = employeeRepository.save(employee);
        log.debug(LOG_END + "Employee createEmployee(Employee employee): result = {}", result);
        return result;
    }

    //---------------------------------------------------------------------------------------
    @Override
    public Page<Employee> getAllDeleted(Pageable pageable) {
        log.debug(LOG_START + " : id = {}", id);
        return employeeRepository.findAllDeleted(pageable);
        log.debug(LOG_END + " : id = {}", id);


        Employee result = employeeRepository.save(employee);
        log.debug(LOG_END + "Employee createEmployee(Employee employee): result = {}", result);
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
                "<a href=\"http://localhost:8087/api/users/" + employee.getId() + "/confirmed" +
                ">Подтвердить регистрацию</a>\n" +
                "Если вы не регистрировались на нашем сайте, проигнорируйте это сообщение.\n");
        log.debug(LOG_END + " : id = {}", id);
    }

    //---------------------------------------------------------------------------------------
    @Override
    public void confirm(Integer id) {
        log.debug(LOG_START + "void sendMailConfirm(Integer id = {})", id);
        Employee employee = employeeRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        employee.setIsConfirmed(Boolean.TRUE);
        employeeRepository.save(employee);
        log.debug(LOG_END + " : id = {}", id);
    }

    //---------------------------------------------------------------------------------------
    @Override
    public void generateEntities(Integer quantity, Boolean clear) {
        log.debug(LOG_START + "void sendMailConfirm(Integer id = {})", id);
        if (clear) employeeRepository.deleteAll();
        List<Employee> employees = new ArrayList<>(1000);
        for (int i = 0; i < quantity; i++) {
            employees.add(Employee.builder().name("Name" + i).email("artemjev.mih@gmail.com").build());
        }
        employeeRepository.saveAll(employees);
        log.debug(LOG_END + " : id = {}", id);
    }

    //---------------------------------------------------------------------------------------
    @Override
    public void massTestUpdate() {
        log.debug(LOG_START + "void sendMailConfirm(Integer id = {})", id);
        List<Employee> employees = employeeRepository.findAll();
        employees.forEach(employee -> employee.setName(employee.getName() + LocalDateTime.now().toString()));
        employeeRepository.saveAll(employees);
        log.debug(LOG_END + " : id = {}", id);
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public List<Employee> findEmployeesWithExpiredPhotos() {
        log.debug(LOG_START + " : id = {}", id);
        List<Employee> resultList = employeeRepository.findAll()
                .stream()
                .filter(employee -> employee.getPhotos()
                        .stream()
                        .flatMap(photo -> Stream.of(isPhotoExpired(photo)))
                        .anyMatch(Boolean.TRUE::equals))
                .collect(Collectors.toList());

        //   .findAll(), если ничего не находит, возвращает пустой список; поидее null бытьне может.
        if (resultList.isEmpty()) {
            throw new NoSuchEmployeeException("There are no employees with expired photos");
        }
        //        return Optional.of(resultList)
        log.debug(LOG_END + " : id = {}", id);
        return resultList;

        Employee result = employeeRepository.save(employee);
        log.debug(LOG_END + "Employee createEmployee(Employee employee): result = {}", result);
        return result;
    }


    //----------------------------------------------------------------------------------------------------
    @Override
    public void sendEmailToEmployeesWhosePhotoIsExpired() {
        log.debug(LOG_START + "void sendMailConfirm(Integer id = {})", id);
        findEmployeesWithExpiredPhotos()
                .stream()
                .forEach(e -> mailer.send(e, "subject",
                        "Пожалуйста, нажмите на ссылку ниже, чтобы подтвердить свою регистрацию:\n" +
                        "<a href=\"http://localhost:8087/api/users/" + e.getId() + "/confirmed" +
                        ">Подтвердить регистрацию</a>\n" +
                        "Если вы не регистрировались на нашем сайте, проигнорируйте это сообщение.\n"));
        log.debug(LOG_END + " : id = {}", id);
    }

    //----------------------------------------------------------------------------------------------------
    private Boolean isPhotoExpired(Photo photo) {
        log.debug(LOG_START + " : id = {}", id);
        return photo.getAddDate()
                .plusYears(5)
                .minusDays(7)
                .isBefore(LocalDateTime.now());
        log.debug(LOG_END + " : id = {}", id);

        Employee result = employeeRepository.save(employee);
        log.debug(LOG_END + "Employee createEmployee(Employee employee): result = {}", result);
        return result;
    }


    //----------------------------------------------------------------------------------------------------
    // private methods
    // todo: хорошо бы, наверное, подобные методы в к какую-то спец.утилиту вынести...
    //----------------------------------------------------------------------------------------------------
    private void changePrivateStatus(Employee employee) {
        log.debug(LOG_START + "void sendMailConfirm(Integer id = {})", id);
        log.info("changePrivateStatus() Service - start: id = {}", employee.getId());
        if (employee.getIsPrivate() == null) {
            employee.setIsPrivate(Boolean.TRUE);
            employeeRepository.save(employee);
        }
        log.info("changePrivateStatus() Service - end: IsPrivate = {}", employee.getIsPrivate());
        log.debug(LOG_END + " : id = {}", id);


        Employee result = employeeRepository.save(employee);
        log.debug(LOG_END + "Employee createEmployee(Employee employee): result = {}", result);
        return result;
    }


    //----------------------------------------------------------------------------------------------------
    private void changeActiveStatus(Employee employee) {

        log.debug(LOG_START + "void sendMailConfirm(Integer id = {})", id);
        if (employee.getIsDeleted() == null) {
            employee.setIsDeleted(Boolean.FALSE);
            employeeRepository.save(employee);
        }
        log.info("changeActiveStatus() Service - end: isVisible = {}", employee.getIsDeleted());
        log.debug(LOG_END + " : id = {}", id);


        Employee result = employeeRepository.save(employee);
        log.debug(LOG_END + "Employee createEmployee(Employee employee): result = {}", result);
        return result;
    }

    //----------------------------------------------------------------------------------------------------
    private List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection) {
        log.debug(LOG_START + " : id = {}", id);

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
        log.debug(LOG_END + "List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection): " +
                  "result = {}", sorts);
        return sorts;
    }
}


        }



