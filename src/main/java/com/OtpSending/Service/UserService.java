package com.OtpSending.Service;

import com.OtpSending.Dto.UserDTO;
import com.OtpSending.Repositories.UserRepo;
import com.OtpSending.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
   private  EmailService emailService;



    private final Map<String, OtpData> otpCache = new ConcurrentHashMap<>(); // Using ConcurrentHashMap for thread safety

    // Your existing registerUser method
    public User registerUser(UserDTO userDTO) {
            User userResponse = new User();
            User existingUser = userRepo.findByEmail(userDTO.getEmail());
            String otp = generateOTP(); // Generate OTP here to use later

            if (existingUser != null) {

                    userResponse.setMessage("User already exists");
                    otpCache.put(existingUser.getEmail(), new OtpData(otp, System.currentTimeMillis())); // Store OTP and creation time in cache
                    otpCache.entrySet().stream().forEach(e -> System.out.println(e.getKey() + ":" + e.getValue()));
                    sendOTP(existingUser.getEmail(), otp);

            } else {
                User newUser = new User();
                newUser.setUserName(userDTO.getUserName());
                newUser.setEmail(userDTO.getEmail());
                newUser.setIsVerified(false);
                newUser.setOtp(otp);
                User newuser = userRepo.save(newUser);
                otpCache.put(newuser.getEmail(), new OtpData(otp, System.currentTimeMillis())); // Store OTP and creation time in cache
                sendOTP(newuser.getEmail(), otp);
                userResponse.setUserName(newuser.getUserName());
                userResponse.setEmail(newuser.getEmail());
                userResponse.setMessage("OTP sent successfully");

            }

        return userResponse;

    }

    // Your existing generateOTP method
    private String generateOTP() {
        Random random = new Random();
        return String.format("%06d", random.nextInt(1000000));
    }

    // Method to send OTP via email (You can replace this with your actual email sending logic)
    private void sendOTP(String email, String otp) {
        String subject = "Email verification";
        String body = "Your 6 digit otp code is  " + otp;
        emailService.sendMail(email, subject, body);
    }

    // Your existing otpVerified method
    public String otpVerified(String email, int otp) {
        String res = "";
        OtpData otpData = otpCache.get(email);
        if (otpData != null && !otpData.isExpired() && String.valueOf(otp).equals(otpData.getOtp())) {
            User user = userRepo.findByEmail(email);
            user.setIsVerified(true);
            userRepo.save(user);
            res = "User verified successfully";
        } else {
            res = "User not verified";
        }
        return res;
    }

    private static class OtpData {
        private final String otp;
        private final long creationTime;

        public OtpData(String otp, long creationTime) {
            this.otp = otp;
            this.creationTime = creationTime;
        }

        public String getOtp() {
            return otp;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() - creationTime > 2 * 60 * 1000; // 2 minutes in milliseconds
        }
    }
}
//
//    public User registerUser( UserDTO userDTO){
//        User userResponse=new User();
//       User existingUser=userRepo.findByEmail(userDTO.getEmail());
//       if(existingUser!=null){
//
//           userResponse.setMessage("user already  exist");
//           String otp=generateOTP();
//           existingUser.setOtp(otp);
//           String subject="Email verification";
//           String body="your verification otp "+otp;
//           emailService.sendMail(existingUser.getEmail(), subject,body);
//
//       }
//       else{
//           User newUser=new User();
//           String otp=generateOTP();
//           newUser.setUserName(userDTO.getUserName());
//           newUser.setEmail(userDTO.getEmail());
//           newUser.setIsVerified(false);
//           newUser.setOtp(otp);
//           User newuser = userRepo.save(newUser);
//           String subject="Email verification";
//           String body="your verification otp "+otp;
//
//           emailService.sendMail(newuser.getEmail(), subject,body);
//           userResponse.setUserName(newuser.getUserName());
//           userResponse.setEmail(newuser.getEmail());
//           userResponse.setMessage("otp sent successfully");
//
//
//       }
//
//       return userResponse;
//
//    }
//
//    private String generateOTP() {
//        Random random = new Random();
//        return String.format("%06d", random.nextInt(1000000));
//    }
//    public String otpVerified(String email,int otp){
//        String res="";
//        User user=userRepo.findByEmail(email);
//        if(user!=null && user.getIsVerified()){
//            res="user is already verified";
//        } else if (String.valueOf(otp).equals(user.getOtp())) {
//            user.setIsVerified(true);
//            userRepo.save(user);
//            res="user verified succesfully";
//        }
//        else{
//            res="user not verified";
//        }
//
//        return res;
//    }
//
//
//}
