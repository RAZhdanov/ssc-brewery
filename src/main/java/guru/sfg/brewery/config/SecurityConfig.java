package guru.sfg.brewery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
        return new LdapShaPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("spring")
                .password("guru")
                .roles("ADMIN")
                .and()
                .withUser("user")
                .password("{SSHA}D+7zeOntEUF9c6PrQ04G0bpTMOMEGJNn8uA0qw==")
                .roles("USER")
                .and()
                .withUser("scott")
                .password("tiger")
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
