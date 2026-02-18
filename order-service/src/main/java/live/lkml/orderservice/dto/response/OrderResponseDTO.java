package live.lkml.orderservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> OrderResponseDTO<T> success(T data) {
        return new OrderResponseDTO<>(true, "Success", data);
    }

    public static <T> OrderResponseDTO<T> error(String message) {
        return new OrderResponseDTO<>(false, message, null);
    }
}