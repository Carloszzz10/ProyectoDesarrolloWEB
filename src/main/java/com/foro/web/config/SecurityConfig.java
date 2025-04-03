package com.foro.web.config;

import com.foro.web.services.UsuarioServicio;
import com.foro.web.services.BaneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UsuarioServicio usuarioServicio;
    private final BaneoService baneoService;

    @Autowired
    public SecurityConfig(@Lazy UsuarioServicio usuarioServicio, BaneoService baneoService) {
        this.usuarioServicio = usuarioServicio;
        this.baneoService = baneoService;
    }

        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public DaoAuthenticationProvider authenticationProvider() {
            DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
            auth.setUserDetailsService(userDetailsService());
            auth.setPasswordEncoder(passwordEncoder());
            auth.setHideUserNotFoundExceptions(false);
            return auth;
        }

        @Bean
        public UserDetailsService userDetailsService() {
            return username -> {
                // Primero obtener los detalles del usuario desde UsuarioServicio
                org.springframework.security.core.userdetails.User userDetails = 
                    (org.springframework.security.core.userdetails.User) usuarioServicio.loadUserByUsername(username);

                // Verificar si el usuario está baneado
                if (baneoService.usuarioEstaBaneado(username)) {
                    throw new UsernameNotFoundException("Usuario baneado");
                }

                return userDetails;
            };
        }

        @Bean
        public AuthenticationFailureHandler authenticationFailureHandler() {
            return (request, response, exception) -> {
                if (exception instanceof UsernameNotFoundException && 
                    exception.getMessage().equals("Usuario baneado")) {
                    // Redirigir a una página de usuario baneado
                    response.sendRedirect("/usuario-baneado");
                } else {
                    // Manejar otros errores de inicio de sesión
                    response.sendRedirect("/login?error=true");
                }
            };
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(authz -> authz
                    .requestMatchers("/", "/index", "/foro/**", "/Registro**", "/Anuncios**",
                            "/Nosotros**", "/Registroexitoso**", "/login**", "/usuario-baneado",
                            "/Js/**", "/css/**", "/Imagenes/**", "/uploads/**", 
                            "/confirmar-cuenta/**", "/publicacion").permitAll()

                    .requestMatchers("/dashboard", "/dashboard/**", "/publicacion", 
                            "/comunidad/registrar","/comunidad/editar/**", "/comunidad/eliminar/**")
                        .hasAnyAuthority("Moderador", "Administrador")

                    .requestMatchers("/publicar/**", "/comentar/**", "/publicacion", 
                            "/comentarios/**", "/PerfilUsuario").authenticated()

                    .anyRequest().authenticated() 
            )
            .formLogin(login -> login
                    .loginPage("/login")  
                    .failureHandler(new ManejoErroresLogeo())
                    .defaultSuccessUrl("/index", true)
                    .permitAll()
            )
            .logout(logout -> logout
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
            );
            return http.build();
        }
    }
