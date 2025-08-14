package com.combatsystem.www.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.combatsystem.www.model.IOTOperator;
import com.combatsystem.www.model.Soldier;
import com.combatsystem.www.model.UAVOperator;
import com.combatsystem.www.repository.IOTOperatorRepo;
import com.combatsystem.www.repository.SoldierRepo;
import com.combatsystem.www.repository.UAVOperatorRepo;

@Service
public class OtpService {

    @Autowired
    private UAVOperatorRepo uavRepository;
    @Autowired
    private IOTOperatorRepo iotRepository;
    @Autowired
    private SoldierRepo soldierRepository;
    @Autowired
    private JavaMailSender mailSender; // ✅ Added for email sending

    private final Map<String, String> otpStorage = new HashMap<>();
    private final Set<String> verifiedEmails = new HashSet<>();

    public void generateAndSendOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);

        boolean userFound = false;

        UAVOperator uavOperator = uavRepository.findByEmail(email);
        if (uavOperator != null) {
            uavOperator.setOtp(otp);
            uavRepository.save(uavOperator);
            userFound = true;
        }

        IOTOperator iotOperator = iotRepository.findByEmail(email);
        if (iotOperator != null) {
            iotOperator.setOtp(otp);
            iotRepository.save(iotOperator);
            userFound = true;
        }

        Soldier soldier = soldierRepository.findByEmail(email);
        if (soldier != null) {
            soldier.setOtp(otp);
            soldierRepository.save(soldier);
            userFound = true;
        }

        // ✅ Always send OTP via email (even if user not yet registered)
        sendEmail(email, otp);

        System.out.println("OTP Sent to " + email + " = " + otp + (userFound ? " (User Found)" : " (New User)"));
    }

    public boolean verifyOtp(String email, String otp) {
        System.out.println("Verifying OTP for: " + email);
        System.out.println("Entered OTP: " + otp);
        System.out.println("Stored OTP: " + otpStorage.get(email));

        if (otpStorage.containsKey(email) && otpStorage.get(email).equals(otp)) {
            verifiedEmails.add(email);
            otpStorage.remove(email);
            System.out.println("✅ OTP verified successfully for: " + email);
            return true;
        }

        System.out.println("❌ OTP verification failed for: " + email);
        return false;
    }

    public boolean isEmailVerified(String email) {
        return verifiedEmails.contains(email);
    }

    public void clearVerification(String email) {
        verifiedEmails.remove(email);
    }

    // ✅ Helper method to send OTP via email
    private void sendEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Your OTP Code");
            message.setText("Your OTP code is: " + otp + "\n\nThis code will expire soon.");
            mailSender.send(message);
            System.out.println("📧 Email sent to: " + toEmail);
        } catch (Exception e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
        }
    }
}
