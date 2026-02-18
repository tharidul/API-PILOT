package live.lkml.orderservice.service;

import live.lkml.orderservice.client.ProductClient;
import live.lkml.orderservice.dto.request.OrderRequestDTO;
import live.lkml.orderservice.dto.response.OrderResponseDTO;
import live.lkml.orderservice.dto.response.ProductClientResponseDTO;
import live.lkml.orderservice.entity.Order;
import live.lkml.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    public OrderResponseDTO<Order> createOrder(OrderRequestDTO request) {
        ProductClientResponseDTO product = productClient.getProductResponse(request.getProductId());

        if (product == null) {
            return OrderResponseDTO.error("Product not found with id: " + request.getProductId());
        }

        if (product.getStock() < request.getQuantity()) {
            return OrderResponseDTO.error("Insufficient stock for product id: " + request.getProductId());
        }

        Double totalPrice = product.getPrice() * request.getQuantity();

        productClient.reduceStock(request.getProductId(), request.getQuantity());

        Order order = Order.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .totalPrice(totalPrice)
                .build();

        return OrderResponseDTO.success(orderRepository.save(order));
    }

    public OrderResponseDTO<List<Order>> getAllOrders() {
        return OrderResponseDTO.success(orderRepository.findAll());
    }

    public OrderResponseDTO<Order> getById(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order == null) {
            return OrderResponseDTO.error("Order not found with id: " + id);
        }
        return OrderResponseDTO.success(order);
    }

    public OrderResponseDTO<Void> delete(Long id) {
        if (!orderRepository.existsById(id)) {
            return OrderResponseDTO.error("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
        return OrderResponseDTO.success(null);
    }
}