package com.eraine.delivery.transport;

import com.eraine.delivery.model.DeliveryCostRequest;
import com.eraine.delivery.model.DeliveryCostResponse;
import com.eraine.delivery.model.ParcelRejectedException;
import com.eraine.delivery.service.DeliveryCostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJson;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeliveryResource.class)
@AutoConfigureJson
class DeliveryResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DeliveryCostService deliveryCostService;

    private final DeliveryCostRequest request = DeliveryCostRequest.builder()
            .weight(new BigDecimal("10"))
            .height(new BigDecimal("10"))
            .width(new BigDecimal("10"))
            .length(new BigDecimal("10"))
            .voucherCode("MYNT")
            .build();

    @Test
    void whenCalculateCost_thenShouldReturn200Response() throws Exception {
        var response = DeliveryCostResponse.builder()
                .classification("Small Parcel")
                .currency(Currency.getInstance("PHP"))
                .cost(new BigDecimal("30.00"))
                .build();

        when(deliveryCostService.calculateCost(request)).thenReturn(response);

        mockMvc.perform(post("/delivery/cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }

    @Test
    void givenParcelRejectedException_whenCalculateCost_thenShouldReturn400() throws Exception {
        when(deliveryCostService.calculateCost(request)).thenThrow(new ParcelRejectedException("Reject"));

        mockMvc.perform(post("/delivery/cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenException_whenCalculateCost_thenShouldReturn500() throws Exception {
        when(deliveryCostService.calculateCost(request)).thenThrow(new RuntimeException("Something broke"));

        mockMvc.perform(post("/delivery/cost")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());
    }

}
