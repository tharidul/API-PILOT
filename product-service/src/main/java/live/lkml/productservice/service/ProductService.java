package live.lkml.productservice.service;

import live.lkml.productservice.dto.request.ProductRequestDTO;
import live.lkml.productservice.dto.response.ProductResponseDTO;
import live.lkml.productservice.entity.Product;
import live.lkml.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponseDTO<Product> create(ProductRequestDTO requestDTO) {
        if (productRepository.existsByName(requestDTO.getName())) {
            return ProductResponseDTO.error("Product already exists with name: " + requestDTO.getName());
        }
        Product product = Product.builder()
                .name(requestDTO.getName())
                .price(requestDTO.getPrice())
                .description(requestDTO.getDescription())
                .stock(requestDTO.getStock())
                .build();
        return ProductResponseDTO.success(productRepository.save(product));
    }

    public ProductResponseDTO<List<Product>> getAll(){
        return ProductResponseDTO.success(productRepository.findAll());
    }

    public ProductResponseDTO<Product> getById(Long id){
        Product product = productRepository.findById(id).orElse(null);
        if (product == null){
            return ProductResponseDTO.error("Product not found with id: " + id);
        }
        return ProductResponseDTO.success(product);
    }


    public ProductResponseDTO<Void> delete(Long id) {
        if (!productRepository.existsById(id)) {
            return ProductResponseDTO.error("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
        return ProductResponseDTO.success(null);
    }

    public ProductResponseDTO<Product> update(Long id, ProductRequestDTO requestDTO) {
        Product existing = productRepository.findById(id)
                .orElse(null);
        if (existing == null) {
            return ProductResponseDTO.error("Product not found with id: " + id);
        }
        existing.setName(requestDTO.getName());
        existing.setPrice(requestDTO.getPrice());
        existing.setDescription(requestDTO.getDescription());
        existing.setStock(requestDTO.getStock());
        return ProductResponseDTO.success(productRepository.save(existing));
    }

    public ProductResponseDTO<Void> reduceStock(Long id, Integer quantity) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ProductResponseDTO.error("Product not found with id: " + id);
        }
        if (product.getStock() < quantity) {
            return ProductResponseDTO.error("Insufficient stock for product id: " + id);
        }
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
        return ProductResponseDTO.success(null);
    }
}
