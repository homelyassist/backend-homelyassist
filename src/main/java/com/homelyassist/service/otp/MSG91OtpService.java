package com.homelyassist.service.otp;

import com.homelyassist.model.MSG91OtpData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MSG91OtpService {

    private final RestTemplate restTemplate;

    @Autowired
    public MSG91OtpService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String send(MSG91OtpData otpData) {
        String url = "https://control.msg91.com/api/v5/otp?template_id=6659d48dd6fc053d25337b19&mobile="+ otpData.getPhoneNumber() +"&authkey=423073AxLeO2186659d5cbP1";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/JSON");

        HttpEntity<MSG91OtpData.OTP> entity = new HttpEntity<>(otpData.getOpt(), headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }
}
