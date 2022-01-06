package guru.sfg.brewery.config;

import guru.sfg.brewery.security.JpaUserDetailsService;
import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlParamAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@EnableWebSecurity
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
                authorize.antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll();
                authorize.antMatchers("/beers/**").permitAll();
                authorize.antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll();
                authorize.mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();
                authorize.antMatchers("/h2-console/**").permitAll();
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

    //@Autowired
    //JpaUserDetailsService jpaUserDetailsService;

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//        //auth.userDetailsService(this.jpaUserDetailsService).passwordEncoder(passwordEncoder());
//
//
//
////        auth.inMemoryAuthentication()
////                .withUser("spring")
////                .password("{bcrypt}$2a$10$ZJFn6AaXUcmRoyplH6gDIOJPi7a0AQM/ErBN.EKYbd529wrB5OeIO")
////                .roles("ADMIN")
////                .and()
////                .withUser("user")
////                .password("{sha256}08f906b13525187f17d649c0279be5b9c70fe7c45e318187784ddac1f69543d65f62da31446e0cbe")
////                .roles("USER")
////                .and()
////                .withUser("scott")
////                .password("{bcrypt15}$2a$15$lYw9uKBsObHOd0.kTVvk2uZwthzq1zkfxcpa5/1iZtqJq9p0HeOC2")
////                .roles("CUSTOMER");
//    }

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
