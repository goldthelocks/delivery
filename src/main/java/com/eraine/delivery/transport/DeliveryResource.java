package com.eraine.delivery.transport;

import com.eraine.delivery.model.DeliveryCostRequest;
import com.eraine.delivery.model.DeliveryCostResponse;
import com.eraine.delivery.service.DeliveryCostService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/delivery")
public class DeliveryResource {

    private final DeliveryCostService deliveryCostService;

    @PostMapping("/cost")
    public DeliveryCostResponse calculateCost(@Nonnull @RequestBody DeliveryCostRequest request) {
        return deliveryCostService.calculateCost(request);
    }
}
