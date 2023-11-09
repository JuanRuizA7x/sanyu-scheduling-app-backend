package com.segurosbolivar.sanyuschedulingapp.service.client;

import com.segurosbolivar.sanyuschedulingapp.dto.response.HolidayResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class HolidayService {

    @Value("${holiday-api.url}")
    private String apiURL;
    private final RestTemplate restTemplate;

    public HolidayService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Retrieves a list of holidays for a specific year and country code from an external holiday API.
     *
     * @param year The year for which to retrieve holidays.
     * @param countryCode The country code for which to retrieve holidays (e.g., "CO").
     * @return A list of HolidayResponseDTO objects representing the retrieved holidays.
     */
    public List<HolidayResponseDTO> getHolidaysByYearAndCountryCode(Integer year, String countryCode) {
        String requestUrl = this.apiURL + "/api/v3/publicholidays/" + year + "/" + countryCode;
        HolidayResponseDTO[] response = restTemplate.getForObject(requestUrl, HolidayResponseDTO[].class);
        return (response != null) ? Arrays.asList(response) : Collections.emptyList();
    }

}