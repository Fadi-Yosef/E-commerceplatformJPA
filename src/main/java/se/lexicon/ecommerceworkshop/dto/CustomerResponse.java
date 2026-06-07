package se.lexicon.ecommerceworkshop.dto;

public record CustomerResponse(
        Long id,
        String fullName,
        String email,
        AddressResponse addressResponse
) {
}
