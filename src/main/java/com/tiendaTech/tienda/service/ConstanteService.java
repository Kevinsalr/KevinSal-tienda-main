package com.tiendaTech.tienda.service;

import com.tiendaTech.tienda.domain.Constante;
import com.tiendaTech.tienda.repository.ConstanteRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConstanteService {

    private final ConstanteRepository constanteRepository;

    public ConstanteService(ConstanteRepository constanteRepository) {
        this.constanteRepository = constanteRepository;
    }

    // LISTAR TODAS
    @Transactional(readOnly = true)
    public List<Constante> getConstantes() {
        return constanteRepository.findAll();
    }

    // BUSCAR POR ID
    @Transactional(readOnly = true)
    public Constante getConstante(Integer idConstante) {
        return constanteRepository.findById(idConstante)
                .orElseThrow(() -> new NoSuchElementException("Constante no encontrada con ID: " + idConstante));
    }

    // GUARDAR (INSERT o UPDATE)
    @Transactional
    public Constante save(Constante constante) {
        return constanteRepository.save(constante);
    }

    // ELIMINAR
    @Transactional
    public void delete(Integer idConstante) {
        constanteRepository.deleteById(idConstante);
    }

    @Transactional(readOnly = true)
    public Constante getByAtributo(String atributo) {
        return constanteRepository.findByAtributo(atributo)
                .orElseThrow(() -> new NoSuchElementException("Constante no encontrada con atributo: " + atributo));
    }
}