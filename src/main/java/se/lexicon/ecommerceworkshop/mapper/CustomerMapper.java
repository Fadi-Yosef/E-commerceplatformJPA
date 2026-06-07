package se.lexicon.ecommerceworkshop.mapper;

import org.example.entity.Address;
import org.example.entity.Customer;
import org.springframework.stereotype.Component;
import se.lexicon.ecommerceworkshop.dto.AddressResponse;
import se.lexicon.ecommerceworkshop.dto.CustomerRequest;
import se.lexicon.ecommerceworkshop.dto.CustomerResponse;

@Component
public class CustomerMapper {

    public CustomerResponse toResponse(Customer customer) {
        if (customer == null) {
            return null;
        }

        return new CustomerResponse(
                customer.getId(),
                fullName(customer),
                customer.getEmail(),
                toAddressResponse(customer.getAddress())
        );
    }

    public Customer toEntity(CustomerRequest request) {
        if (request == null) {
            return null;
        }

        Customer customer = new Customer();
        customer.setFirstName(trim(request.firstName()));
        customer.setLastName(trim(request.lastName()));
        customer.setEmail(trim(request.email()));
        customer.setAddress(toAddress(request));
        return customer;
    }

    public void updateEntity(Customer customer, CustomerRequest request) {
        if (customer == null || request == null) {
            return;
        }

        customer.setFirstName(trim(request.firstName()));
        customer.setLastName(trim(request.lastName()));
        customer.setEmail(trim(request.email()));

        Address address = customer.getAddress();
        if (address == null) {
            address = new Address();
            customer.setAddress(address);
        }
        address.setStreet(trim(request.street()));
        address.setCity(trim(request.city()));
        address.setZipCode(trim(request.zipCode()));
    }

    private AddressResponse toAddressResponse(Address address) {
        if (address == null) {
            return null;
        }

        return new AddressResponse(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getZipCode()
        );
    }

    private Address toAddress(CustomerRequest request) {
        Address address = new Address();
        address.setStreet(trim(request.street()));
        address.setCity(trim(request.city()));
        address.setZipCode(trim(request.zipCode()));
        return address;
    }

    private String fullName(Customer customer) {
        return (customer.getFirstName() + " " + customer.getLastName()).trim();
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }
}
