package com.challenge.controllers;

import com.challenge.model.Order;
import com.challenge.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Tag(name = "Order")
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('VIEW_ORDER', 'ADMIN')")
    @Operation(
            summary = "Searches for orders based on the filters provided.",
            description = "Searches on all available orders based on the filters provided.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order[].class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Failed to validate token"),
                    @ApiResponse(responseCode = "403", description = "User not permission to view orders"),
            }
    )
    public Optional<Order> get(
            @PathVariable String id) {
        return orderService.getOrderById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('VIEW_ORDERS', 'ADMIN')")
    @Operation(
            summary = "Searches for orders based on the filters provided.",
            description = "Searches on all available orders based on the filters provided.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Order[].class))),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Failed to validate token"),
                    @ApiResponse(responseCode = "403", description = "User not permission to view orders"),
            }
    )
    public Page<Order> getAll(@PageableDefault(size = 50) Pageable pageable) {
        return orderService.getAllOrders(pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Save order.",
            description = "Saver the order and calculate total product values.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Failed to validate token"),
                    @ApiResponse(responseCode = "403", description = "User not permission"),
                    @ApiResponse(responseCode = "404", description = "Order with matching ID not found"),
            }
    )
    public void save(@RequestHeader("Idempotency-Key") String idempotencyKey,
                     @RequestBody Order order) {
        orderService.saveOrder(idempotencyKey, order);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Hides an article based on its ID.",
            description = "Sets the field 'hiddenAt' for the article found by the articleId param.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Success"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Failed to validate token"),
                    @ApiResponse(responseCode = "403", description = "User not permission to hide articles"),
                    @ApiResponse(responseCode = "404", description = "Article with matching ID not found"),
            }
    )
    public void deleteAll() {
        orderService.deleteAll();
    }

}