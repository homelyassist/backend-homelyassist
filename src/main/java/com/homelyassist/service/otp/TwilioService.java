package com.homelyassist.service.otp;

import com.homelyassist.config.TwilioConfig;
import com.homelyassist.model.TwilioOtpData;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TwilioService {

    private final TwilioConfig twilioConfig;


    @Autowired
    public TwilioService(TwilioConfig twilioConfig) {
        this.twilioConfig = twilioConfig;
    }


    public void sendOtp(TwilioOtpData data) {
        PhoneNumber from = new PhoneNumber(twilioConfig.getSystemNumber());
        PhoneNumber to = new PhoneNumber(data.getPhoneNumber());
        Message message = Message.creator(
                to,
                from,
                data.getBody()
        ).create();
    }
}
