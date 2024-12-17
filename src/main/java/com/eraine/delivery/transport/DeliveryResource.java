package com.eraine.delivery.transport;

import com.eraine.delivery.model.DeliveryCostRequest;
import com.eraine.delivery.model.DeliveryCostResponse;
import com.eraine.delivery.service.DeliveryCostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Delivery", description = "API for delivery cost calculation")
@RequiredArgsConstructor
@RestController
@RequestMapping("/delivery")
public class DeliveryResource {

    private final DeliveryCostService deliveryCostService;

    @Operation(summary = "Calculate delivery cost", description = "Calculates the cost of delivery based on parcel weight and volume.")
    @PostMapping("/cost")
    public DeliveryCostResponse calculateCost(@Nonnull @RequestBody DeliveryCostRequest request) {
        return deliveryCostService.calculateCost(request);
    }
}
