package com.combatsystem.www.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import com.combatsystem.www.dto.AdminMessageDto;
import com.combatsystem.www.dto.BlockedUsersDto;
import com.combatsystem.www.dto.DecryptedMessageDto;
import com.combatsystem.www.dto.EncryptedMessageDto;
import com.combatsystem.www.dto.MessageDto;
import com.combatsystem.www.dto.UserResponseDto;
import com.combatsystem.www.model.AdminEncryptedMessage;
import com.combatsystem.www.model.IOTOperator;
import com.combatsystem.www.model.Soldier;
import com.combatsystem.www.model.UAVOperator;
import com.combatsystem.www.repository.AdminEncryptedMessageRepo;
import com.combatsystem.www.repository.IOTOperatorRepo;
import com.combatsystem.www.repository.SoldierRepo;
import com.combatsystem.www.repository.UAVOperatorRepo;
import com.combatsystem.www.service.AdminService;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    private final AdminEncryptedMessageRepo adminEncryptedMessageRepo;

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private UAVOperatorRepo uavRepository;
    
    @Autowired
    private SoldierRepo soldierRepository;
    
    @Autowired
    private IOTOperatorRepo iotRepository;


    AdminController(AdminEncryptedMessageRepo adminEncryptedMessageRepo) {
        this.adminEncryptedMessageRepo = adminEncryptedMessageRepo;
    }
    

    @PostMapping("/login-request")
    public ResponseEntity<?> loginRequest(@RequestParam String email, @RequestParam String password) {
        // Debugging logs
        System.out.println("Entered email: " + email);
        System.out.println("Entered password: " + password);

        if (adminService.validateCredentials(email.trim(), password.trim())) {
            adminService.generateAndSendOtp(email.trim());
            return ResponseEntity.ok("OTP sent to email.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        if (adminService.verifyOtp(email.trim(), otp.trim())) {
            return ResponseEntity.ok("OTP verified successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid OTP");
        }
    }
    
    @PutMapping("/approve/{role}/{id}")
    public ResponseEntity<?> approveUser(@PathVariable String role, @PathVariable Long id) {
        switch (role.toUpperCase()) {
            case "UAV_OPERATOR":
                UAVOperator uav = uavRepository.findById(id).orElse(null);
                if (uav != null) {
                    uav.setStatus("APPROVED");
                    uavRepository.save(uav);
                    return ResponseEntity.ok("UAV approved");
                }
                break;

            case "IOT_DEVICE_OPERATOR":
                IOTOperator iot = iotRepository.findById(id).orElse(null);
                if (iot != null) {
                    iot.setStatus("APPROVED");
                    iotRepository.save(iot);
                    return ResponseEntity.ok("IOT approved");
                }
                break;

            case "SOLDIER":
                Soldier soldier = soldierRepository.findById(id).orElse(null);
                if (soldier != null) {
                    soldier.setStatus("APPROVED");
                    soldierRepository.save(soldier);
                    return ResponseEntity.ok("Soldier approved");
                }
                break;
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }



    @PutMapping("/deny/{role}/{id}")
    public ResponseEntity<?> denyUser(@PathVariable String role, @PathVariable Long id) {
        switch (role.toUpperCase()) {
            case "UAV_OPERATOR":
                UAVOperator uav = uavRepository.findById(id).orElse(null);
                if (uav != null) {
                    uav.setStatus("DENIED");
                    uavRepository.save(uav);
                    return ResponseEntity.ok("UAV denied");
                }
                break;

            case "IOT_DEVICE_OPERATOR":
                IOTOperator iot = iotRepository.findById(id).orElse(null);
                if (iot != null) {
                    iot.setStatus("DENIED");
                    iotRepository.save(iot);
                    return ResponseEntity.ok("IOT denied");
                }
                break;

            case "SOLDIER":
                Soldier soldier = soldierRepository.findById(id).orElse(null);
                if (soldier != null) {
                    soldier.setStatus("DENIED");
                    soldierRepository.save(soldier);
                    return ResponseEntity.ok("Soldier denied");
                }
                break;
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
    
    @PostMapping("/block/{role}/{email}")
    public ResponseEntity<?> blockUser(@PathVariable String role, @PathVariable String email)
    {
    	switch(role.toUpperCase())
    	{
    	  case "UAV_OPERATOR" :
    		  UAVOperator uavOperator= uavRepository.findByEmail(email);
    		  if(uavOperator != null) {
    			  uavOperator.setStatus("BLOCKED");
    			  uavRepository.save(uavOperator);
    			  return ResponseEntity.ok("UAV Operator Blocked");
    		  }
    		  break;
    		  
    	  case "IOT_DEVICE_OPERATOR" :
    		  IOTOperator iotOperator= iotRepository.findByEmail(email);
    		  if(iotRepository !=null) {
    			  iotOperator.setStatus("BLOCKED");
    			  iotRepository.save(iotOperator);
    			  return ResponseEntity.ok("IOT Operator Blocked");
    		  }
    		  break;
    		 
    	  case "SOLDIER" :
    		  Soldier soldier= soldierRepository.findByEmail(email);
    		  if(soldierRepository != null)
    		  {
    			  soldier.setStatus("BLOCKED");
    			  soldierRepository.save(soldier);
    			  return ResponseEntity.ok("SOLDIER Blocked");
    		  }
    	}
    	return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
    
    @GetMapping("/fetch-all-users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.fetchAllUsers());
    }
    

    @GetMapping("/decrypt-messages/uav/{id}")
    public ResponseEntity<List<DecryptedMessageDto>> decryptUav(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.decryptUavMessages(id));
    }

    @GetMapping("/decrypt-messages/soldier/{id}")
    public ResponseEntity<List<DecryptedMessageDto>> decryptSoldier(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.decryptSoldierMessages(id));
    }

    @GetMapping("/decrypt-messages/iot/{id}")
    public ResponseEntity<List<DecryptedMessageDto>> decryptIot(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.decryptIotMessages(id));
    }
    
    @GetMapping("/encrypt-messages/uav/{id}")
    public ResponseEntity<List<EncryptedMessageDto>> getUavEncryptedMessages(@PathVariable Long id) {
        List<EncryptedMessageDto> messages = adminService.uavEncryptMessages(id);
        return ResponseEntity.ok(messages);
    }

    
    @GetMapping("/encrypt-messages/iot/{id}")
    public ResponseEntity<List<EncryptedMessageDto>> iotEncryptedMessage(@PathVariable Long id)
    {
    	List<EncryptedMessageDto> messages=adminService.iotEncryptMessages(id);
    	return ResponseEntity.ok(messages);
    }
    
    @GetMapping("/encrypt-messages/soldier/{id}")
    public ResponseEntity<List<EncryptedMessageDto>> soldierEncryptedMessage(@PathVariable Long id)
    {
    	List<EncryptedMessageDto> messages=adminService.soldierEncryptMessages(id);
    	return ResponseEntity.ok(messages);
    }
    
    @PostMapping("/reply/send")
    public ResponseEntity<String> saveAdminEncryptMessage(@RequestBody MessageDto dto)
    {
    	adminService.saveAdminEncryptMessage(dto.getMessage(),dto.getEmail());
    	
    	return ResponseEntity.ok("Message send to user successfully");
    }

    @GetMapping("/reply/view")
    public ResponseEntity<List<AdminMessageDto>> getMessagesForUser(@RequestParam String email)
    {
    	return ResponseEntity.ok(adminService.getMessagesForUser(email));
    }
    
    @GetMapping("/fetch-blocked-users")
    public ResponseEntity<List<BlockedUsersDto>> getAllBlockedUsers(@RequestParam String status) {
        return ResponseEntity.ok(adminService.fetchAllBlockedUsers(status));
    }
    
    @PutMapping("/mark-hacked")
    public ResponseEntity<String> markAsHacked(@RequestParam String timestamp) {
        boolean ok = adminService.markMessageAsHackedByTimestampString(timestamp);
        if (ok) {
            return ResponseEntity.ok("Message status updated to AHACKED");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body("No message found for given timestamp");
        }
    }
    
    @GetMapping("/status")
    public ResponseEntity<List<AdminMessageDto>> getAhackedMessage(@RequestParam String status)
    {
    	return ResponseEntity.ok(adminService.getAhackedMessages(status));
    }

}





