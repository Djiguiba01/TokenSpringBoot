package io.getarrays.userservice.security;

import io.getarrays.userservice.filter.CustomAuthenticationFilter;
import io.getarrays.userservice.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        /*auth.inMemoryAuthentication()
                .withUser("springuser").password(passwordEncoder().encode("spring123"))
                .roles("ROLE_USER")
                .and()
                .withUser("springadmin").password(passwordEncoder().encode("admin123"))
                .roles("ROLE_ADMIN", "ROLE_USER");*/
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/login");
        http.csrf().disable();
        //http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests()
                //.antMatchers("/api/admin/").hasRole("ROLE_ADMIN")
                //.antMatchers("/api/user/").hasRole("ROLE_USER")
                .antMatchers("/api/login/**").permitAll()
                //.antMatchers("/api/test/**").permitAll()
                //.antMatchers(GET, "/api/test/user/**").hasAnyAuthority("ROLE_USER")
                //.antMatchers(GET, "/api/collaborateurs/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
        .antMatchers(POST, "/api/collaborateur/save/**").hasAnyAuthority("ROLE_ADMIN")
        //.antMatchers(PUT, "/api/collaborateur/update/{ColId}/**").hasAnyAuthority("ROLE_ADMIN")
                //.antMatchers(DELETE, "/api/collaborateur/delete/{userId}/**").hasAnyAuthority("ROLE_ADMIN")
        .anyRequest().authenticated()
                .and()
        .formLogin()////Connexion avec login par defaut de spring securite
                .and()
                .oauth2Login(); //Connexion avec Oauth2
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /*@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

}
