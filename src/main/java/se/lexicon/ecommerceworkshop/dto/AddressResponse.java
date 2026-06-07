package se.lexicon.ecommerceworkshop.dto;

public record AddressResponse(
        Long id,
        String street,
        String city,
        String zipCode
) {
}
