package com.eraine.delivery.client;

import com.eraine.delivery.config.VoucherApiConfig;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class VoucherClient {

    private final RestClient restClient;
    private final VoucherApiConfig restConfig;

    public BigDecimal getDiscount(String voucherCode) {
        var uri = UriComponentsBuilder
                .fromUriString(restConfig.getBaseUrl())
                .path("/voucher/{voucherCode}")
                .queryParam("key", restConfig.getApiKey())
                .build(voucherCode);

        var voucherResponse = restClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("Call to voucher API failed with status code {}", res.getStatusCode());
                    throw new RestClientException("Failed to call voucher API");
                })
                .body(VoucherResponse.class);

        return voucherResponse == null ? BigDecimal.ZERO : voucherResponse.discount();
    }

    @Builder
    record VoucherResponse(
            String code,
            BigDecimal discount,
            String expiry) {
    }
}
