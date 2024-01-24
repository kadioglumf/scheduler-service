package com.kadioglumf.scheduler.adepter;

import com.kadioglumf.scheduler.exceptions.SchedulerServiceException;
import com.kadioglumf.scheduler.payload.response.error.BaseErrorResponse;
import com.kadioglumf.scheduler.payload.response.error.ErrorType;
import com.kadioglumf.scheduler.utils.AppUtils;
import com.kadioglumf.scheduler.utils.ConvertUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class TestServiceAdapter {

    private final static String BASE_BLOG_SERVICE_URL = "http://test-service";

    private final RestTemplate restTemplate;


    public void process(String path, HttpMethod httpMethod) {
        String url = BASE_BLOG_SERVICE_URL + path;
        try {
            restTemplate.exchange(url, httpMethod, new HttpEntity<>(AppUtils.getHttpHeader()), Void.class);
        } catch (HttpClientErrorException ex) {
            BaseErrorResponse response = ConvertUtils.convertJsonDataToObject(ex.getResponseBodyAsString(), BaseErrorResponse.class);
            if (response == null) {
                response = new BaseErrorResponse(ex.getStatusCode().value(), ErrorType.TEST_SERVICE_ERROR ,ex.getMessage());
            }
            throw new SchedulerServiceException(response);
        } catch (Exception ex) {
            throw new SchedulerServiceException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorType.TEST_SERVICE_ERROR, ex.getMessage());
        }
    }
}
