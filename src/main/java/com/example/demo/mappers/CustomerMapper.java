package com.example.demo.mappers;

import com.example.demo.dtos.CustomerDto;
import com.example.demo.entities.Customer;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CustomerMapper {

    CustomerDto mapFromCustomerEntityToDTO(Customer customer);
    Customer mapFromDtoToCustomerEntity(CustomerDto dto);

}
