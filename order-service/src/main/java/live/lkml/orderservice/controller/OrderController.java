package live.lkml.orderservice.controller;

import live.lkml.orderservice.dto.request.OrderRequestDTO;
import live.lkml.orderservice.dto.response.OrderResponseDTO;
import live.lkml.orderservice.entity.Order;
import live.lkml.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO<Order>> createOrder(@RequestBody OrderRequestDTO request) {
        OrderResponseDTO<Order> response = orderService.createOrder(request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<OrderResponseDTO<List<Order>>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO<Order>> getById(@PathVariable Long id) {
        OrderResponseDTO<Order> response = orderService.getById(id);
        if (!response.isSuccess()) {
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OrderResponseDTO<Void>> delete(@PathVariable Long id) {
        OrderResponseDTO<Void> response = orderService.delete(id);
        if (!response.isSuccess()) {
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }
}