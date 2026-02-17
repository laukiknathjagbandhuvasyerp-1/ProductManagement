package org.example.product.repo;

import org.example.product.model.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantRepo extends JpaRepository<Variant,Long> {

}
