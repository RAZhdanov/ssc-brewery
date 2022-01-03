package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorize -> {
                authorize.antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll();
                authorize.antMatchers("/beers/**").permitAll();
                authorize.antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll();
                authorize.mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();
            })
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .formLogin().and()
            .httpBasic();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("spring")
                .password("$2a$10$0Q1c6Clz12EQ44fyVknVZumA.upWBQP0UcQtaknCje9TzwnoN0nU2")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("$2a$10$mV5kLaj/zCRt0mwD6Zcg4.GUl51IX/HVH/2YmdKk/PWn/79eN1b3m")
                .roles("USER")
                .and()
                .withUser("scott")
                .password("$2a$10$kTg79MUji77mTfxnkPsCZufD6.XwdSTi4G7gt0kl1junMKNTOL7HO")
                .roles("CUSTOMER");
    }

    //    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("spring")
//                .password("guru")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }
}
