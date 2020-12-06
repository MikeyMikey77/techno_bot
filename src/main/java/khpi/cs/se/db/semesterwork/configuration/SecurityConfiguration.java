package khpi.cs.se.db.semesterwork.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;


@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity config) throws Exception {
        config.csrf().disable();
        String[] pages = {"/webhook/bot", "/css/**", "/fonts/**","/js/**","/sass/**", "/about", "/help"};
            config
                    .authorizeRequests()
                        .antMatchers(pages).permitAll()
                        .anyRequest().hasRole("ADMIN")
                        .and()
                    .formLogin()
                        .loginPage("/login")
                        .permitAll()
                        .and()
                    .logout().
                        logoutUrl("/logout").
                        permitAll();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
        builder.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("select username, password, is_active from usr where username=?")
                .authoritiesByUsernameQuery("select username, title as role from usr, user_role where usr.id_user_role = user_role.id_role and username=?");
    }
}
