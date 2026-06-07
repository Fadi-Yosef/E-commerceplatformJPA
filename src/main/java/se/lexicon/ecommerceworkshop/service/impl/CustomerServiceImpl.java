package se.lexicon.ecommerceworkshop.service.impl;

import org.example.entity.Customer;
import org.example.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.ecommerceworkshop.dto.CustomerRequest;
import se.lexicon.ecommerceworkshop.dto.CustomerResponse;
import se.lexicon.ecommerceworkshop.exception.DuplicateResourceException;
import se.lexicon.ecommerceworkshop.exception.ResourceNotFoundException;
import se.lexicon.ecommerceworkshop.mapper.CustomerMapper;
import se.lexicon.ecommerceworkshop.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    @Transactional
    public CustomerResponse register(CustomerRequest request) {
        String email = normalizedEmail(request.email());
        if (customerRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Customer email is already taken: " + email);
        }

        Customer customer = customerMapper.toEntity(request);
        customer.setEmail(email);
        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toResponse(savedCustomer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse findById(Long id) {
        Customer customer = getCustomer(id);
        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional
    public CustomerResponse update(Long id, CustomerRequest request) {
        Customer customer = getCustomer(id);
        String email = normalizedEmail(request.email());

        if (!email.equalsIgnoreCase(customer.getEmail()) && customerRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("Customer email is already taken: " + email);
        }

        customerMapper.updateEntity(customer, request);
        customer.setEmail(email);
        return customerMapper.toResponse(customer);
    }

    private Customer getCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", id));
    }

    private String normalizedEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}
