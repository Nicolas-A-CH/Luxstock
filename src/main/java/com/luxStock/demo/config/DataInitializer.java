package com.luxStock.demo.config;

import com.luxStock.demo.model.entity.Empleado;
import com.luxStock.demo.model.entity.Rol;
import com.luxStock.demo.model.entity.Sede;
import com.luxStock.demo.model.entity.Usuario;
import com.luxStock.demo.repository.EmpleadoRepository;
import com.luxStock.demo.repository.RolRepository;
import com.luxStock.demo.repository.SedeRepository;
import com.luxStock.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final EmpleadoRepository empleadoRepository;
    private final RolRepository rolRepository;
    private final SedeRepository sedeRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Verificar si el usuario administrador ya existe
        if (usuarioRepository.findByUsername("admin").isEmpty()) {

            List<String> rolesNecesarios = Arrays.asList("Administrador", "Cajero", "Mesero");

            for (String nombreRol : rolesNecesarios) {
                rolRepository.findByNombre(nombreRol).orElseGet(() -> {
                    Rol newRol = new Rol();
                    newRol.setNombre(nombreRol);
                    return rolRepository.save(newRol);
                });
            }

            Rol rolAdmin = rolRepository.findByNombre("Administrador")
                    .orElseThrow(() -> new RuntimeException("Error: No se pudo encontrar el rol Administrador"));

            Sede sedePrincipal = sedeRepository.findById(1).orElseGet(() -> {
                Sede newSede = new Sede();
                newSede.setNombre("Sede Principal");
                newSede.setDireccion("Calle 123");
                return sedeRepository.save(newSede);
            });

            // 2. Crear Empleado Administrador
            Empleado adminEmpleado = new Empleado();
            adminEmpleado.setNombre("Administrador");
            adminEmpleado.setApellido("Sistema");
            adminEmpleado.setDocumento("12345678");
            adminEmpleado.setTelefono("00000000");
            adminEmpleado.setRol(rolAdmin);
            adminEmpleado.setSede(sedePrincipal);
            adminEmpleado = empleadoRepository.save(adminEmpleado);

            // 3. Crear Usuario Administrador
            Usuario adminUsuario = new Usuario();
            adminUsuario.setUsername("admin");
            adminUsuario.setPassword(passwordEncoder.encode("pruebas"));
            adminUsuario.setEmpleado(adminEmpleado);
            
            usuarioRepository.save(adminUsuario);

            System.out.println(">>> Usuario administrador creado exitosamente: admin / pruebas");
        } else {
            System.out.println(">>> El usuario administrador ya existe en la base de datos.");
        }
    }
}
