package com.example.demowithtests.service;

import com.example.demowithtests.domain.Employee;
import com.example.demowithtests.domain.Gender;
import com.example.demowithtests.dto.employee.EmployeeReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface EmployeeService {
    Employee getEmployee(Integer id);

    Employee createEmployee(Employee employee);

    Employee patchEmployee(Integer id, Employee employee);

    Employee updateEmployee(Integer id, Employee employee);

    void markEmployeeAsDeleted(Integer id);

    void deleteEmployee(Integer id);

    void deleteAll();

    List<Employee> getAll();

    Page<Employee> getAllWithPagination(Pageable pageable);

    Page<Employee> getAllActive(Pageable pageable);

    Page<Employee> getAllDeleted(Pageable pageable);

    List<Employee> getEmployeesByGenderAndCountry(Gender gender, String country);

    Page<Employee> getEmployeesWithActiveAddressesInCountry(String country, Pageable pageable);

    /**
     * @param country   Filter for the country if required
     * @param page      number of the page returned
     * @param size      number of entries in each page
     * @param sortList  list of columns to sort on
     * @param sortOrder sort order. Can be ASC or DESC
     * @return Page object with employees after filtering and sorting
     */
    Page<Employee> getByCountryAndSort(String country, int page, int size, List<String> sortList, String sortOrder);


    /**
     * Get all the countries of all the employees.
     *
     * @return A list of all the countries that employees are from.
     */
    Set<String> getAllEmployeesCountries();


    /**
     * It returns a list of countries sorted by name.
     *
     * @return A list of countries in alphabetical order.
     */
    List<String> getAllEmployeesCountriesSorted();


    Optional<List<String>> getEmails();

    List<Employee> handleEmployeesWithIsDeletedFieldIsNull();

    List<Employee> handleEmployeesWithIsPrivateFieldIsNull();

    List<Employee> handleEmployeesWithIsConfirmedFieldIsNull();

    void sendMailConfirm(Integer id);

    void confirm(Integer id);

    void generateEntities(Integer quantity, Boolean clear);

    void massTestUpdate();

    List<Employee> findEmployeesWithExpiredPhotos();

    void sendEmailToEmployeesWhosePhotoIsExpired();

    Employee addPhoto (Integer id, MultipartFile file);
}
