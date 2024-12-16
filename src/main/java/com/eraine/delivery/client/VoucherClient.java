package com.eraine.delivery.client;

import com.eraine.delivery.config.VoucherApiConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@Service
public class VoucherClient {

    private final RestClient restClient;
    private final VoucherApiConfig restConfig;

    public BigDecimal getDiscount(String voucherCode) {
        URI uri = UriComponentsBuilder
                .fromUriString(restConfig.getBaseUrl())
                .path("/voucher/{voucherCode}")
                .queryParam("key", restConfig.getApiKey())
                .build(voucherCode);

        VoucherResponse voucherResponse = restClient.get()
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

    record VoucherResponse(
            String code,
            BigDecimal discount,
            String expiry) {
    }
}
