package com.eraine.delivery.client;

import com.eraine.delivery.config.VoucherApiConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadGateway;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(VoucherClient.class)
class VoucherClientTest {

    private static final String VOUCHER_BASE_URI = "https://mynt-exam.mocklab.io";
    private static final String GET_VOUCHER_ENDPOINT = "/voucher/%s?key=%s";

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private VoucherClient voucherClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VoucherApiConfig restConfig;

    @BeforeEach
    void setUp() {
        when(restConfig.getBaseUrl()).thenReturn(VOUCHER_BASE_URI);
        when(restConfig.getApiKey()).thenReturn("123456");
    }

    @Test
    void shouldReturnDiscount() throws JsonProcessingException {
        var response = VoucherClient.VoucherResponse.builder()
                .code("MYNT")
                .discount(new BigDecimal("10"))
                .expiry(Instant.now().toString())
                .build();

        var endpoint = VOUCHER_BASE_URI + GET_VOUCHER_ENDPOINT.formatted("MYNT", "123456");

        mockServer.expect(requestTo(endpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(objectMapper.writeValueAsString(response), MediaType.APPLICATION_JSON));

        BigDecimal result = voucherClient.getDiscount("MYNT");

        assertNotNull(result);
        assertEquals(new BigDecimal("10"), result);
    }

    @Test
    void shouldReturnZeroDiscount_whenResponseIsNull() {
        var endpoint = VOUCHER_BASE_URI + GET_VOUCHER_ENDPOINT.formatted("MYNT", "123456");

        mockServer.expect(requestTo(endpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("", MediaType.APPLICATION_JSON));

        BigDecimal result = voucherClient.getDiscount("MYNT");

        assertNotNull(result);
        assertEquals(BigDecimal.ZERO, result);
    }

    @Test
    void shouldThrowRestClientExceptionWhenApiReturnsError() {
        var endpoint = VOUCHER_BASE_URI + GET_VOUCHER_ENDPOINT.formatted("MYNT", "123456");

        mockServer.expect(requestTo(endpoint))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withBadGateway());

        assertThrows(RestClientException.class,
                () -> voucherClient.getDiscount("MYNT"));
    }

    @TestConfiguration
    static class TestRestConfig {

        @Bean
        public RestClient restClient(RestClient.Builder builder) {
            return builder.build();
        }

    }
}
