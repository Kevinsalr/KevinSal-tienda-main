package com.tiendaTech.tienda.controller;

import com.tiendaTech.tienda.domain.Constante;
import com.tiendaTech.tienda.service.ConstanteService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/constante")
public class ConstanteController {

    private final ConstanteService constanteService;
    private final MessageSource messageSource;

    public ConstanteController(ConstanteService constanteService, MessageSource messageSource) {
        this.constanteService = constanteService;
        this.messageSource = messageSource;
    }

    // 🔹 LISTADO
    @GetMapping("/listado")
    public String listado(Model model) {
        List<Constante> lista = constanteService.getConstantes();
        model.addAttribute("constantes", lista);
        model.addAttribute("totalConstantes", lista.size());
        return "/constante/listado";
    }

    // 🔹 GUARDAR
    @PostMapping("/guardar")
    public String guardar(@Valid Constante constante, RedirectAttributes redirectAttributes) {
        constanteService.save(constante);
        redirectAttributes.addFlashAttribute(
                "exito",
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault())
        );
        return "redirect:/constante/listado";
    }

    // 🔹 ELIMINAR
    @GetMapping("/eliminar/{idConstante}")
    public String eliminar(@PathVariable Integer idConstante, RedirectAttributes redirectAttributes) {
        constanteService.delete(idConstante);
        redirectAttributes.addFlashAttribute(
                "exito",
                messageSource.getMessage("mensaje.eliminado", null, Locale.getDefault())
        );
        return "redirect:/constante/listado";
    }

    // 🔹 MOSTRAR FORMULARIO PARA EDITAR
    @GetMapping("/modificar/{idConstante}")
    public String modificar(@PathVariable Integer idConstante, Model model) {
        Constante constante = constanteService.getConstante(idConstante);
        model.addAttribute("constante", constante);
        return "/constante/modifica";
    }

    // 🔹 NUEVA CONSTANTE
    @GetMapping("/nuevo")
    public String nuevo(Constante constante) {
        return "/constante/modifica";
    }
}