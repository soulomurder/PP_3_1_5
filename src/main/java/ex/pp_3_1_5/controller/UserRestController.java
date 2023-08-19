package ex.pp_3_1_5.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ex.pp_3_1_5.dto.UserDto;
import ex.pp_3_1_5.model.Role;
import ex.pp_3_1_5.model.User;
import ex.pp_3_1_5.repository.RoleRepository;
import ex.pp_3_1_5.service.UserDetailsServiceImpl;
import ex.pp_3_1_5.service.UserService;
import ex.pp_3_1_5.util.UserErrorResponse;
import ex.pp_3_1_5.util.UserNotCreatedException;
import ex.pp_3_1_5.util.UserNotFoundException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserRestController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public UserRestController(UserService userService, ModelMapper modelMapper,
                              RoleRepository roleRepository, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) {
        return new ResponseEntity<>(convertToUserDto(userService.findOne(id)), HttpStatus.OK);
    }

    @GetMapping("/admin")
    public ResponseEntity<List<UserDto>> showAllUsers() {
        List<UserDto> dto = userService.findAll().stream().map(this::convertToUserDto).collect(Collectors.toList());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/admin")
    public ResponseEntity<HttpStatus> create(@Valid @RequestBody UserDto userDto,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError: fieldErrors) {
                errorMsg.append(fieldError.getField())
                        .append("-")
                        .append(fieldError.getDefaultMessage());
            }
            throw new UserNotCreatedException(errorMsg.toString());
        }
        userService.save(convertToUser(userDto));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/admin/{id}")
    public ResponseEntity update(@PathVariable("id") Long id
            , @RequestBody @Valid User user
            , BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError: fieldErrors) {
                errorMsg.append(fieldError.getField())
                        .append("-")
                        .append(fieldError.getDefaultMessage());
            }
            throw new RuntimeException(errorMsg.toString());
        }
        userService.update(id, user);
        return new ResponseEntity<UserDto>(convertToUserDto(user), HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Long id) {
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotFoundException e) {
        UserErrorResponse errorResponse = new UserErrorResponse(
                "User not found!",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); // возвращаем объект ответа с ошибкой в
        // теле и статусом 404 Not Found
    }

    @ExceptionHandler
    private ResponseEntity<UserErrorResponse> handleException(UserNotCreatedException e) {
        UserErrorResponse errorResponse = new UserErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/roles")
    public ResponseEntity<Set<Role>> getRoles() {
        return new ResponseEntity<>(new HashSet<>(roleRepository.findAll()), HttpStatus.OK);
    }

    @GetMapping("/currentuser")
    public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
        return new ResponseEntity<>(convertToUserDto(userDetailsServiceImpl.findByUserName(principal.getName())),
                HttpStatus.OK);
    }

    private User convertToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    private UserDto convertToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
