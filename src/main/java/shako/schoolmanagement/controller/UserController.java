package shako.schoolmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shako.schoolmanagement.dto.*;
import shako.schoolmanagement.service.inter.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid StudentUserDto studentUserDto) {
        userService.register(studentUserDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/activate")
    public ResponseEntity<Void> activate(@RequestBody @Valid ActivationCodeDto activationCode) {
        userService.activateUser(activationCode);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend-activation")
    public ResponseEntity<Void> resendActivation(@RequestParam String neptunCode) {
        userService.resendActivationCode(neptunCode);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordDto dto) {
        userService.forgotPassword(dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordDto dto) {
        userService.resetPassword(dto);
        return ResponseEntity.ok().build();
    }
}
