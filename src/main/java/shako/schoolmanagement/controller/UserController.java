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
    public ResponseEntity<String> register(@RequestBody @Valid StudentUserDto studentUserDto) {
        userService.register(studentUserDto);
        return ResponseEntity.ok("Student registered successfully");
    }

    @PostMapping("/activate")
    public ResponseEntity<String> activate(@RequestBody @Valid ActivationCodeDto activationCode) {
        userService.activateUser(activationCode);
        return ResponseEntity.ok("Activation successful");
    }

    @PostMapping("/resend-activation")
    public ResponseEntity<String> resendActivation(@RequestParam String neptunCode) {
        userService.resendActivationCode(neptunCode);
        return ResponseEntity.ok("If the account exists and is not yet active, a new code has been sent");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody @Valid ForgotPasswordDto dto) {
        userService.forgotPassword(dto);
        return ResponseEntity.ok("If an account with that email exists, a reset link has been sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordDto dto) {
        userService.resetPassword(dto);
        return ResponseEntity.ok("Password reset successfully");
    }
}
