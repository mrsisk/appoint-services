package mrsisk.github.io.appointservice.dto;

import java.util.List;

public record User(
        String id,
        String username,
        long createdTimestamp,
        boolean enabled,
        boolean totp,
        boolean emailVerified,
        String firstName,
        String lastName,
        String email,
        List<String> requiredActions
) {
}
