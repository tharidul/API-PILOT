package live.lkml.orderservice.service;

import live.lkml.orderservice.client.ProductClient;
import live.lkml.orderservice.dto.request.OrderRequest;
import live.lkml.orderservice.dto.response.ProductResponse;
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

    public Order createOrder(OrderRequest orderRequest) {
        System.out.println("DEBUG productId: " + orderRequest.getProductId());
        System.out.println("DEBUG quantity: " + orderRequest.getQuantity());
        ProductResponse product = productClient.getProductResponse(orderRequest.getProductId());

        if (product == null){
            throw new RuntimeException("Product not found with id: " + orderRequest.getProductId());
        }

        Double totalPrice = product.getPrice() * orderRequest.getQuantity();

        productClient.reduceStock(orderRequest.getProductId(), orderRequest.getQuantity());

        Order order = Order.builder()
                .productId(orderRequest.getProductId())
                .quantity(orderRequest.getQuantity())
                .totalPrice(totalPrice)
                .build();
        return orderRepository.save(order);
    }
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }
    public Order getOrderById(Long id){
        return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }
    public void deleteOrderById(Long id){
        orderRepository.deleteById(id);
    }
}
