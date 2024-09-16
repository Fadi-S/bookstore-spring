package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.dto.AddressDTO;
import dev.fadisarwat.bookstore.dto.BookForListDTO;
import dev.fadisarwat.bookstore.helpers.Filter;
import dev.fadisarwat.bookstore.helpers.Sort;
import dev.fadisarwat.bookstore.models.Address;
import dev.fadisarwat.bookstore.models.Book;

import java.util.List;

public interface AddressService {
    void saveAddress(Address address);
    Address getAddress(Long id);
    void deleteAddress(Long id);
    List<AddressDTO> getAddresses(Long userId);
}
