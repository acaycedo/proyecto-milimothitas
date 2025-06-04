package com.milimothitas.proyecto.proyecto_milimothitas.model.dto;
//clase usada para enviar los datos de un usuario

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Schema(description = "Respuesta de la información de los usuarios")
@Data
public class UserResponse {
    @Schema(description = "Identificador unico del usuario automatico", example = "1")
    private Long userId;
    @Schema(description = "Nombre del usuario", example = "Nombre Apellido")
    private String name;
    @Schema(description = "Correo unico del usuario", example = "usuario@correo.com")
    private String email;
    @Schema(description = "Contraseña para el ingreso del usuario", example = "admin123")
    private String password;
    @Schema(description = "Role que identifica si es usuario o administrador", example = "ENUM: USER, ADMIN")
    private String role;
    @Schema(description = "Estado del usuario, si es activo o inactivo, automatico", example = "Por Defecto: Active")
    private String state;
    
    
}
