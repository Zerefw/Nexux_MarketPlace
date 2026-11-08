package application.adapter.in.web;

import application.adapter.in.web.dto.ApiResponse;
import application.adapter.in.web.dto.ProductResponseDTO;
import application.domain.model.entity.Product;
import application.domain.ports.in.catalog.ManageProductUseCase;
import application.domain.ports.in.command.catalog.CreateProductCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST (Adaptador de entrada) para la administracion de productos del catalogo.
 */
@RestController
@RequestMapping("/api/v1/catalog/products")
@RequiredArgsConstructor
public class ProductController {

    private final ManageProductUseCase manageProductUseCase;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDTO>> createProduct(@RequestBody CreateProductCommand command) {
        Product createdProduct = manageProductUseCase.createProduct(command);
        ProductResponseDTO responseDto = ProductResponseDTO.fromDomain(createdProduct);
        return ResponseEntity.ok(ApiResponse.ok("Product successfully added to catalog", responseDto));
    }
}
