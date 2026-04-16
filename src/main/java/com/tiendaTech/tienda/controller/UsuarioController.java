package com.tiendaTech.tienda.controller;

import com.tiendaTech.tienda.domain.Usuario;
import com.tiendaTech.tienda.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final MessageSource messageSource;

    public UsuarioController(UsuarioService usuarioService,
            MessageSource messageSource) {
        this.usuarioService = usuarioService;
        this.messageSource = messageSource;
    }

    // 🔹 LISTADO
    @GetMapping("/listado")
    public String inicio(Model model) {
        var usuarios = usuarioService.getUsuarios(false);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());
        return "/usuario/listado";
    }

    // 🔹 GUARDAR
    @PostMapping("/guardar")
    public String guardar(@Valid Usuario usuario,
            BindingResult bindingResult,
            @RequestParam MultipartFile imagenFile,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("usuario.error04", null, Locale.getDefault()));

            if (usuario.getIdUsuario() == null) {
                return "redirect:/usuario/listado";
            }

            return "redirect:/usuario/modificar/" + usuario.getIdUsuario();
        }

        usuarioService.save(usuario, imagenFile, true);

        redirectAttributes.addFlashAttribute("todoOk",
                messageSource.getMessage("mensaje.actualizado",
                        null, Locale.getDefault()));

        return "redirect:/usuario/listado";
    }

    // 🔹 ELIMINAR
    @PostMapping("/eliminar")
    public String eliminar(@RequestParam Integer idUsuario,
            RedirectAttributes redirectAttributes) {

        try {
            usuarioService.delete(idUsuario);

            redirectAttributes.addFlashAttribute("todoOk",
                    messageSource.getMessage("mensaje.eliminado", null,
                            Locale.getDefault()));

        } catch (IllegalArgumentException e) {

            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("usuario.error01", null,
                            Locale.getDefault()));

        } catch (IllegalStateException e) {

            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("usuario.error02", null,
                            Locale.getDefault()));

        } catch (NoSuchMessageException e) {

            redirectAttributes.addFlashAttribute("error",
                    messageSource.getMessage("usuario.error03", null,
                            Locale.getDefault()));
        }

        return "redirect:/usuario/listado";
    }

    // 🔹 MODIFICAR
    @GetMapping("/modificar/{idUsuario}")
    public String modificar(@PathVariable("idUsuario") Integer idUsuario,
            Model model, RedirectAttributes redirectAttributes) {

        Optional<Usuario> usuarioOpt = usuarioService.getUsuario(idUsuario);

        if (usuarioOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error",
                    "El usuario no fue encontrado.");
            return "redirect:/usuario/listado";
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setPassword("");

        model.addAttribute("usuario", usuario);

        // 🔥 IMPORTANTE: lista de roles para la vista
        model.addAttribute("roles", usuarioService.getRolesNombres());

        return "/usuario/modifica";
    }

    // =========================================================
    // 🔥 SECCIÓN DE ROLES (LO DE LA IMAGEN)
    // =========================================================

    // 🔹 AGREGAR ROL A USUARIO
    @GetMapping("/agregarRol")
    public String agregarRol(@RequestParam String username,
            @RequestParam Integer idRol,
            RedirectAttributes redirectAttributes) {

        usuarioService.agregarRol(username, idRol);

        redirectAttributes.addFlashAttribute("todoOk",
                messageSource.getMessage("mensaje.actualizado", null, Locale.getDefault()));

        return "redirect:/usuario/listado";
    }

    // 🔹 ELIMINAR ROL A USUARIO
    @GetMapping("/eliminarRol")
    public String eliminarRol(@RequestParam String username,
            @RequestParam Integer idRol,
            RedirectAttributes redirectAttributes) {

        usuarioService.eliminarRol(username, idRol);

        redirectAttributes.addFlashAttribute("todoOk",
                messageSource.getMessage("mensaje.eliminado", null, Locale.getDefault()));

        return "redirect:/usuario/listado";
    }
}