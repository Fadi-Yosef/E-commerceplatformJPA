package org.example.repository;

import java.util.List;
import org.example.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByZipCode(String zipCode);

    List<Address> findByCityIgnoreCase(String city);

    List<Address> findByStreetContainingIgnoreCase(String street);

    List<Address> findByZipCodeStartingWith(String prefix);

    @Query("select count(c) from Customer c where c.address.zipCode = :zipCode")
    long countCustomersByZipCode(@Param("zipCode") String zipCode);
}
