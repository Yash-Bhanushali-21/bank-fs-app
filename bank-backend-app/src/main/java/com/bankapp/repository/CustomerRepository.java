package com.bankapp.repository;

import com.bankapp.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
    //run time, execute the search query . => derived by name property.
    List<Customer> findByEmail(String email);
}
