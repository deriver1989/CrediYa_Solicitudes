package co.com.pragma.usecase.solicitud.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RolesTest {

    @Test
    void shouldContainAllRoles() {
        Roles[] values = Roles.values();

        assertThat(values).containsExactly(
                Roles.ADMIN,
                Roles.ASESOR,
                Roles.CLIENTE
        );
    }

    @Test
    void shouldReturnRoleByName() {
        assertThat(Roles.valueOf("ADMIN")).isEqualTo(Roles.ADMIN);
        assertThat(Roles.valueOf("ASESOR")).isEqualTo(Roles.ASESOR);
        assertThat(Roles.valueOf("CLIENTE")).isEqualTo(Roles.CLIENTE);
    }

    @Test
    void shouldThrowExceptionForInvalidRole() {
        assertThrows(IllegalArgumentException.class, () -> Roles.valueOf("INVALIDO"));
    }
}