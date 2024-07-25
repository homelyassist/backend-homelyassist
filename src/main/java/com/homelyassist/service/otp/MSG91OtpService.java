package com.homelyassist.service.otp;

import com.homelyassist.config.MSG91Config;
import com.homelyassist.model.MSG91OtpData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class MSG91OtpService {

    private final MSG91Config msg91Config;
    private final RestTemplate restTemplate;

    @Autowired
    public MSG91OtpService(MSG91Config msg91Config, RestTemplate restTemplate) {
        this.msg91Config = msg91Config;
        this.restTemplate = restTemplate;
    }

    public String send(MSG91OtpData otpData) {
        if(!msg91Config.isEnabled()) {
            log.info("MSG 91 is disabled skipping opt send");
            return null;
        }
        String url = "https://control.msg91.com/api/v5/otp?template_id=" + msg91Config.getTemplateId() + "&mobile=" + otpData.getPhoneNumber() + "&authkey=" + msg91Config.getAuthKey();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/JSON");

        HttpEntity<MSG91OtpData.OTP> entity = new HttpEntity<>(otpData.getOpt(), headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }
}
