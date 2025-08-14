package com.combatsystem.www.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.combatsystem.www.dto.IOTOperatorDto;
import com.combatsystem.www.dto.MessageDto;
import com.combatsystem.www.dto.UserResponseDto;
import com.combatsystem.www.model.IOTOperator;
import com.combatsystem.www.model.IotEncryptedMessage;
import com.combatsystem.www.repository.IOTOperatorRepo;
import com.combatsystem.www.repository.IotEncryptedMessageRepository;
import com.combatsystem.www.service.IOTOperatorService;
import com.combatsystem.www.service.OtpService;


@RestController
@RequestMapping("/iot")
@CrossOrigin(origins = "http://localhost:3000")
public class IOTOperatorController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private IOTOperatorService operatorService;

    @Autowired
    private IOTOperatorRepo iotRepository;
    
    @Autowired
    private IOTOperatorService iotService;

    @Autowired
    private IotEncryptedMessageRepository repo;
    
    // Send OTP
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        otpService.generateAndSendOtp(email);
        return ResponseEntity.ok("OTP sent to " + email);
    }

    @PostMapping("/send-login-otp")
    public ResponseEntity<String> sendLoginOtp(@RequestParam String email) {
        String response = iotService.sendLoginOtp(email);
        return ResponseEntity.ok(response);
    }
    
    // Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
    	System.out.println("Verifying OTP for email: " + email + ", OTP: " + otp);
        if (otpService.verifyOtp(email, otp)) {
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
        }
    }


    // Register Operator
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody IOTOperator operator) {
        if (operatorService.emailExists(operator.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already registered");
        }

        if (!otpService.isEmailVerified(operator.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not verified");
        }

        operator.setStatus("PENDING");
        operatorService.registerOperator(operator);
        otpService.clearVerification(operator.getEmail());

        return ResponseEntity.ok("Registration request submitted. Awaiting admin approval.");
    }

    // Get all operators
    @GetMapping("/all")
    public List<IOTOperator> getAllUavOperators() {
        return iotRepository.findAll();
    }

    // Get pending operators
    @GetMapping("/pending")
    public ResponseEntity<List<IOTOperatorDto>> getPendingOperators() {
        return ResponseEntity.ok(operatorService.getPendingIOTOperators());
    }

    // Get approved operators
    @GetMapping("/approved")
    public ResponseEntity<List<IOTOperatorDto>> getApprovedOperators() {
        return ResponseEntity.ok(operatorService.getApprovedIOTOperators());
    }

    // Get denied operators
    @GetMapping("/denied")
    public ResponseEntity<List<IOTOperatorDto>> getDeniedOperators() {
        return ResponseEntity.ok(operatorService.getDeniedIOTOperators());
    }

    // Reusable: Get operators by any status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<IOTOperator>> getOperatorsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(iotRepository.findByStatus(status.toUpperCase()));
    }
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String password = request.get("password");
            String otp = request.get("otp");

            String response = operatorService.login(email, password, otp);
            
            if ("SUCCESS".equals(response)) {
                return ResponseEntity.ok("Login Successful");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Something went wrong on the server.");
        }
    }

    @PostMapping("/login-request")
    public ResponseEntity<String> loginRequest(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");

        System.out.println("Received email: " + email);
        System.out.println("Received password: " + password);

        if (email == null || password == null) {
            return ResponseEntity.badRequest().body("Email and password are required");
        }

        IOTOperator operator = iotRepository.findByEmail(email);

        if (operator == null || !operator.getPassword().equals(password)) {
            return ResponseEntity.badRequest().body("Invalid email or password");
        }

        System.out.println("DB email: " + operator.getEmail());
        System.out.println("DB password: " + operator.getPassword());

        // Send OTP if credentials are correct
        otpService.generateAndSendOtp(email);
        return ResponseEntity.ok("OTP sent to " + email);
    }

    @PostMapping("/sendinfo")
    public ResponseEntity<String> receiveEncryptedInfo(@RequestBody MessageDto info) {
        if (info.getMessage() == null || info.getLatitude() == null || info.getLongitude() == null) {
            return ResponseEntity.badRequest().body("All fields (message, latitude, longitude) are required.");
        }
        iotService.saveEncryptedMessage(info.getMessage(), info.getLatitude(), info.getLongitude(), info.getEmail());
        return ResponseEntity.ok("Encrypted Message Stored Successfully");
    }
    
    @PostMapping("otp/send")
    public ResponseEntity<String> messageSendOtp(@RequestParam String email) {
        otpService.generateAndSendOtp(email);
        return ResponseEntity.ok("OTP sent successfully to " + email);
    }

    // Verify OTP
    @PostMapping("otp/verify")
    public ResponseEntity<String> messageVerifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = otpService.verifyOtp(email, otp);
        if (isValid) {
            return ResponseEntity.ok("OTP verified successfully");
        }
        return ResponseEntity.badRequest().body("Invalid OTP");
    }
    
    @GetMapping("/get")
    public ResponseEntity<IOTOperatorDto> getIotByEmail(@RequestParam String email) {
        IOTOperatorDto iot = iotService.getIotByEmail(email);
        if (iot != null) {
            return ResponseEntity.ok(iot);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/update")
    public IOTOperatorDto updateIot(@RequestBody IOTOperatorDto iotDto) {
        return iotService.updateIot(iotDto);
    }
}
