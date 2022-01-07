package guru.sfg.brewery.config;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlParamAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager){
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public RestUrlParamAuthFilter restUrlParameterAuthFilter(AuthenticationManager authenticationManager){
        RestUrlParamAuthFilter filter = new RestUrlParamAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(
                restHeaderAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class
        ).csrf().disable();

        http.addFilterBefore(
                restUrlParameterAuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class
        );

        http
            .authorizeRequests(authorize -> {
                authorize
                        .antMatchers("/h2-console/**").permitAll()
                        .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                        .antMatchers("/beers/find", "/beers*").hasAnyRole("ADMIN", "CUSTOMER", "USER")
                        .antMatchers(HttpMethod.GET, "/api/v1/beer/**").hasAnyRole("ADMIN", "CUSTOMER", "USER")
                        .mvcMatchers(HttpMethod.DELETE, "/api/v1/beer/**").hasRole("ADMIN")
                        .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").hasAnyRole("ADMIN", "CUSTOMER", "USER")
                        .antMatchers("/brewery/breweries/**").hasAnyRole("ADMIN","CUSTOMER")
                        .mvcMatchers(HttpMethod.GET, "/brewery/api/v1/breweries").hasAnyRole("ADMIN","CUSTOMER")
                        .mvcMatchers("/beers/find", "/beers/{beerId}").hasAnyRole("ADMIN", "CUSTOMER", "USER");

            })
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .formLogin().and()
            .httpBasic();

        http.headers().frameOptions().sameOrigin();
    }


    @Bean
    PasswordEncoder passwordEncoder() {
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
