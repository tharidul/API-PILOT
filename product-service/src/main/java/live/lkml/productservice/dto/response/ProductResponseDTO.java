package live.lkml.productservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> ProductResponseDTO<T> success(T data) {
        return new ProductResponseDTO<>(true, "Success", data);
    }

    public static <T> ProductResponseDTO<T> error(String message) {
        return new ProductResponseDTO<>(false, message, null);
    }
}