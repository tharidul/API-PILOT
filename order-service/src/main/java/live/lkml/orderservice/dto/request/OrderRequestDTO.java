package live.lkml.orderservice.dto.request;

import lombok.Data;

@Data
public class OrderRequestDTO {
    private Long productId;
    private Integer quantity;
}