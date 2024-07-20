package com.OtpSending.Dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long userId;
    private String email;
    private String userName;
    private Integer otp;
    private Boolean isVerified;
    private String message;
}
