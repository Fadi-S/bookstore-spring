package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.models.Address;

import java.util.List;

public interface AddressDAO {
    void saveAddress(Address address);
    Address getAddress(Long id);
    List<Address> getAddresses(Long userId);
    void deleteAddress(Long id);
}
