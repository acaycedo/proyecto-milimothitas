package com.milimothitas.proyecto.proyecto_milimothitas.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import lombok.RequiredArgsConstructor;

import com.milimothitas.proyecto.proyecto_milimothitas.services.UserService;
import com.milimothitas.proyecto.proyecto_milimothitas.exceptions.UserNotFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.UserRequest;
import com.milimothitas.proyecto.proyecto_milimothitas.model.dto.UserResponse;

@Tag(name = "Usuarios", description = "EndPoint para gestionar los usuarios de Milimothitas")
// RequiredArgsConstructor es una anotacion de lombok que genera un constructor
// con todos los argumentos finales
@RequiredArgsConstructor
// RestController es una anotacion de spring que indica que la clase es un
// controlador rest
@RestController
// RequestMapping es una anotacion de spring que indica el path de la url
@RequestMapping("api/users")
public class UserController {
    // UserService es una interfaz que define los servicios de usuario
    // Se establece final ya que es constante y es el llamado a dicho servicio
    private final UserService userService;

    @Operation(summary = "Lista Usuarios creados", description = "Lista todos los usuarios creados con sus datos existentes en el sistema. Incluye activos e inactivos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class)))
    })
    // Metodo para obtener todos los usuarios
    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAll();
    }

    @Operation(summary = "Obtener usuario por ID", description = "Obtiene un usuario específico por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID de usuario inválido (ej. menor a 1)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserNotFoundException.class)))
    })
    // @Valid valida si el id buscado es mayor o igual a 1 para que no ingresen
    // valores negativos o 0
    @GetMapping("{id}")
    public UserResponse getUserById(
            @Parameter(description = "ID del usuario a buscar", required = true, example = "1") @Valid @Min(1) @PathVariable("id") Long id) {
        return userService.getById(id);
    }

    @Operation(summary = "Buscar usuarios por nombre", description = "Busca usuarios por una parte de su nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuarios encontrada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetro 'nombre' vacío o inválido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @GetMapping("search")
    // valida si esta vacio dar error
    public List<UserResponse> getUsersByName(
            @Parameter(description = "Nombre o parte del nombre del usuario a buscar", required = true, example = "Juan") @Valid @NotBlank @RequestParam("nombre") String name) {
        return userService.getByName(name);
    }

    @Operation(summary = "Crear un nuevo usuario", description = "Crea un nuevo usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de usuario inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Indica que la respuesta es 201 Created
    // valida los campos en el DTP Request
    public UserResponse createUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos del usuario a crear", required = true, content = @Content(schema = @Schema(implementation = UserRequest.class))) @Valid @RequestBody UserRequest user) {
        return userService.create(user);
    }

    @Operation(summary = "Actualizar un usuario existente", description = "Actualiza la información de un usuario por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400", description = "ID de usuario o datos de actualización inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserNotFoundException.class)))
    })
    @PutMapping("{id}")
    // valida que el update deba tener un id positivo mayor o igual a 1
    public UserResponse updateUser(
            @Parameter(description = "ID del usuario a actualizar", required = true, example = "1") @Valid @Min(1) @PathVariable("id") Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Nuevos datos del usuario", required = true, content = @Content(schema = @Schema(implementation = UserRequest.class))) @RequestBody UserRequest user) {
        return userService.update(id, user);
    }

    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario del sistema por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "ID de usuario inválido (ej. menor a 1)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserNotFoundException.class)))
    })
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Indica que la respuesta es 204 No Content
    // valida si es positivo mayor o igual a 1
    public void deleteUser(
            @Parameter(description = "ID del usuario a eliminar", required = true, example = "1") @Valid @Min(1) @PathVariable("id") Long id) {
        userService.delete(id);
    }
}
