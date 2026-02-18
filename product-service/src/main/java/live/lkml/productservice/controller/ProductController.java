package live.lkml.productservice.controller;

import live.lkml.productservice.dto.request.ProductRequestDTO;
import live.lkml.productservice.dto.response.ProductResponseDTO;
import live.lkml.productservice.entity.Product;
import live.lkml.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO<Product>> create(@RequestBody ProductRequestDTO product) {
        ProductResponseDTO<Product> response = productService.create(product);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ProductResponseDTO<List<Product>>> getAll() {
        return ResponseEntity.ok(productService.getAll());
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO<Product>> getById(@PathVariable Long id){
        ProductResponseDTO<Product> response = productService.getById(id);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO<Product>> update(@PathVariable Long id,
                                                              @RequestBody ProductRequestDTO request) {
        ProductResponseDTO<Product> response = productService.update(id, request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponseDTO<Void>> delete(@PathVariable Long id) {
        ProductResponseDTO<Void> response = productService.delete(id);
        if (!response.isSuccess()) {
            return ResponseEntity.status(404).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/reduce-stock")
    public ResponseEntity<ProductResponseDTO<Void>> reduceStock(@PathVariable Long id,
                                                                @RequestParam Integer quantity) {
        ProductResponseDTO<Void> response = productService.reduceStock(id, quantity);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok().build();
    }

    // For internal Feign calls only
    @GetMapping("/internal/{id}")
    public ResponseEntity<Product> getByIdInternal(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id).getData());
    }
}
