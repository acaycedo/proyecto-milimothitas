package com.milimothitas.proyecto.proyecto_milimothitas.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
@Schema(description = "Solicitud de la información de los usuarios")

@Data
//Clase usada para recibir los datos de un usuario
public class UserRequest {
    @Schema(description = "Nombre del usuario", example = "Nombre Apellido")
    @NotBlank(message = "El campo Nombre es obligatiorio")
    @Size(min=3, message = "El campo Name debe tener minimo 3 caracteres")
    private String name;
    @Schema(description = "Correo único del usuario", example = "usuario@correo.com")
    @NotBlank(message = "El campo Email es obligatiorio")
    private String email;
    @Schema(description = "Contraseña para el ingreso del usuario", example = "admin123")
    @NotBlank(message = "El campo Contraseña es obligatiorio")
    private String password;
    @Schema(description = "Rol que identifica si es usuario o administrador", example = "USER")
    @NotBlank(message = "El campo Role es obligatiorio")
    private String role;
    @Schema(description = "Estado del usuario, si es activo o inactivo", example = "Active")
    private String state;
}
