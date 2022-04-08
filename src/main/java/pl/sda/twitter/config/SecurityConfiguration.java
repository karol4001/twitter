package pl.sda.twitter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import pl.sda.twitter.service.UserAppService;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserAppService userAppService;

    public SecurityConfiguration(UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .cors().disable()
                .httpBasic()
                .and()
                .csrf()
                .disable()
                .headers()
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/user/add").permitAll()
                .antMatchers(HttpMethod.POST, "/api/**").authenticated()

                .antMatchers(HttpMethod.DELETE, "/api/**").authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                 password 1234
//                .withUser("jan").password("$2a$12$xyKIti7SOmJngrj3ZpcsKusZ4MF5G3/K0miPTX6isJj1rn9uFyGVy").roles("USER")
//                .and()
//                .passwordEncoder(new BCryptPasswordEncoder());
        auth.userDetailsService(userAppService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
