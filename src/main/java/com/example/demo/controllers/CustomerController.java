package com.example.demo.controllers;

import com.example.demo.dtos.CustomerDto;
import com.example.demo.dtos.OrderDto;
import com.example.demo.services.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }
   @RequestMapping(value = "/customers", method = RequestMethod.POST)
    public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto customerDto) {

        customerDto = customerService.createCustomer(customerDto);
          return new ResponseEntity<>(customerDto, HttpStatus.CREATED);
    }
}
