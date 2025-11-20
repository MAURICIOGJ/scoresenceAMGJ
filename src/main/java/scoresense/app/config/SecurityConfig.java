package scoresense.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                auth -> auth
                        .requestMatchers("/signin", "/signup").permitAll()
                        .requestMatchers("/api/v1/coaches/**").hasRole("ADMIN")
                        .requestMatchers("/api/v3/learningResources/**").hasRole("USER")
                        .requestMatchers("/graphql/**").hasRole("ADMIN")
                        .requestMatchers("/services/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(withDefaults()).csrf(csrf -> csrf.disable())
                .formLogin(withDefaults())
                .rememberMe(withDefaults())
                .logout(logout -> logout.logoutUrl("/signout").permitAll());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var userDetailsManager = new InMemoryUserDetailsManager();
        userDetailsManager.createUser(User.withUsername("usePrueba")
                .password(passwordEncoder().encode("userPrueba"))
                .roles("USER")
                .build());
        userDetailsManager.createUser(User.withUsername("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .build());
        return userDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
