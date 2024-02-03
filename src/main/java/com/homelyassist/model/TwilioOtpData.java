package com.homelyassist.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TwilioOtpData {
    private String phoneNumber;

    private String body;
}
