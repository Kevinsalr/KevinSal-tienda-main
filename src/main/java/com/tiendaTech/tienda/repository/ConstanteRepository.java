package com.tiendaTech.tienda.repository;

import com.tiendaTech.tienda.domain.Constante;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ConstanteRepository extends JpaRepository<Constante, Integer> {

    // Consulta derivada (IMPORTANTE en el PDF)
    public Optional<Constante> findByAtributo(String atributo);
}