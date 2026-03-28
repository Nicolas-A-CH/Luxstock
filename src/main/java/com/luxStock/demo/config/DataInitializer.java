package com.luxStock.demo.config;

import com.luxStock.demo.entity.Empleado;
import com.luxStock.demo.entity.Rol;
import com.luxStock.demo.entity.Sede;
import com.luxStock.demo.entity.Usuario;
import com.luxStock.demo.repository.EmpleadoRepository;
import com.luxStock.demo.repository.RolRepository;
import com.luxStock.demo.repository.SedeRepository;
import com.luxStock.demo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

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

            // 1. Obtener Rol 1 y Sede 1 (Deben existir en la DB)
            Rol rolAdmin = rolRepository.findById(1).orElseGet(() -> {
                Rol newRol = new Rol(1, "ADMIN");
                return rolRepository.save(newRol);
            });

            Sede sedePrincipal = sedeRepository.findById(1).orElseGet(() -> {
                Sede newSede = new Sede();
                newSede.setIdSede(1);
                newSede.setNombre("Sede Principal");
                newSede.setDireccion("Calle 123");
                return sedeRepository.save(newSede);
            });

            // 2. Crear Empleado Administrador
            Empleado adminEmpleado = new Empleado();
            adminEmpleado.setIdEmpleado(1);
            adminEmpleado.setNombre("Administrador");
            adminEmpleado.setApellido("Sistema");
            adminEmpleado.setDocumento("12345678");
            adminEmpleado.setTelefono("00000000");
            adminEmpleado.setRol(rolAdmin);
            adminEmpleado.setSede(sedePrincipal);
            adminEmpleado = empleadoRepository.save(adminEmpleado);

            // 3. Crear Usuario Administrador
            Usuario adminUsuario = new Usuario();
            adminUsuario.setIdUsuario(1);
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
