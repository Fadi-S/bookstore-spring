package dev.fadisarwat.bookstore.controllers;

import dev.fadisarwat.bookstore.dto.AddressDTO;
import dev.fadisarwat.bookstore.models.Address;
import dev.fadisarwat.bookstore.models.User;
import dev.fadisarwat.bookstore.services.AddressService;
import dev.fadisarwat.bookstore.services.UserService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final UserService userService;
    private final AddressService addressService;

    AddressController(UserService userService, AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
    }

    @GetMapping
    public List<AddressDTO> index() {
        User user = this.userService.loadAddresses(User.getCurrentUser());

        return user.getAddresses().stream().map(AddressDTO::fromAddress).toList();
    }

    @PostMapping
    public AddressDTO store(@RequestBody Address address) {
        User user = this.userService.loadAddresses(User.getCurrentUser());
        user.addAddress(address);
        this.userService.saveUser(user);

        return AddressDTO.fromAddress(address);
    }

    @PatchMapping("/{addressId}")
    public AddressDTO update(@RequestBody Address address, @PathVariable Long addressId) {
        User user = this.userService.loadAddresses(User.getCurrentUser());
        address.setId(addressId);
        address.setUser(user);

        if (user.getAddresses().stream().noneMatch(a -> a.getId().equals(addressId))) {
            throw new NotFoundException("Address not found");
        }

        this.addressService.saveAddress(address);
        return AddressDTO.fromAddress(address);
    }

    @DeleteMapping("/{addressId}")
    public Map<String, Object> destroy(@PathVariable Long addressId) {
        Address address = this.addressService.getAddress(addressId);
        User user = User.getCurrentUser();

        if (address == null || !Objects.equals(address.getUser().getId(), user.getId())) {
            throw new NotFoundException("Address not found");
        }

        this.addressService.deleteAddress(addressId);

        return Map.of("message", "Deleted address successfully");
    }
}
