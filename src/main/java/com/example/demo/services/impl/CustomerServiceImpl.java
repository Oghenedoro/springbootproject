package com.example.demo.services.impl;

import com.example.demo.dtos.CustomerDto;
import com.example.demo.entities.Customer;
import com.example.demo.mappers.CustomerMapper;
import com.example.demo.repositories.CustomerRepository;
import com.example.demo.services.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerDto createCustomer(CustomerDto dto) {
        Customer customer = customerMapper.mapFromDtoToCustomerEntity(dto);
        customer = customerRepository.save(customer);
        CustomerDto customerDto = customerMapper.mapFromCustomerEntityToDTO(customer);
        return customerDto;
    }
}
