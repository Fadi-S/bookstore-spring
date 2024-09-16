package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.dao.AddressDAO;
import dev.fadisarwat.bookstore.dto.AddressDTO;
import dev.fadisarwat.bookstore.models.Address;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressDAO addressDAO;

    public AddressServiceImpl(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }

    @Override
    @Transactional
    public void saveAddress(Address address) {
        addressDAO.saveAddress(address);
    }

    @Override
    @Transactional
    public Address getAddress(Long id) {
        return addressDAO.getAddress(id);
    }

    @Override
    @Transactional
    public void deleteAddress(Long id) {
        addressDAO.deleteAddress(id);
    }

    @Override
    @Transactional
    public List<AddressDTO> getAddresses(Long userId) {
        return addressDAO.getAddresses(userId)
                .stream()
                .map(AddressDTO::fromAddress)
                .collect(Collectors.toList());
    }
}
