package dev.fadisarwat.bookstore.dto;

import dev.fadisarwat.bookstore.models.Address;
import dev.fadisarwat.bookstore.models.Book;

import java.io.Serializable;

public record AddressDTO(Long id, String fullName, String country, String street1,
                         String street2, String city, String postalCode) implements Serializable {

    public static AddressDTO fromAddress(Address address) {
        return new AddressDTO(address.getId(), address.getFullName(), address.getCountry(), address.getStreet1(), address.getStreet2(), address.getCity(), address.getPostalCode());
    }

    public String getFullAddress() {
        StringBuilder fullAddress = new StringBuilder();
        if (postalCode() != null)
            fullAddress.append(postalCode())
                    .append(" ");

        fullAddress.append(street1())
                .append(" ");

        if (street2() != null)
            fullAddress.append(street2());

        fullAddress.append(", ")
                .append(city())
                .append(", ")
                .append(country());
        ;

        return fullAddress.toString();
    }
}
