package com.springboot.rest.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.springboot.rest.filter.JsonUsernamePasswordAuthenticationFilter;

/**
 * Security config.
 * 
 * @author takaseshota
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;

	@Value("${front-path}")
	private String path;

	/**
	 * Configure.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// アクセス制限の設定
		http
				.authorizeRequests().mvcMatchers("/api/login")
				.permitAll()
				.mvcMatchers("/api/user/regist-user")
				.permitAll()
				.anyRequest().authenticated()
				.and()
				.csrf()
				.ignoringAntMatchers("/api/login")
				.ignoringAntMatchers("/api/user/regist-user")
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.and()
				.cors()
				.configurationSource(corsConfigurationSource());

		http.formLogin();

		// ログインパラメーターの設定
		JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter = new JsonUsernamePasswordAuthenticationFilter(
				authenticationManager());
		jsonUsernamePasswordAuthenticationFilter.setUsernameParameter("email");
		jsonUsernamePasswordAuthenticationFilter.setPasswordParameter("password");
		// ログイン後にリダイレクトのリダイレクトを抑制
		jsonUsernamePasswordAuthenticationFilter
				.setAuthenticationSuccessHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK));
		// ログイン失敗時のリダイレクト抑制
		jsonUsernamePasswordAuthenticationFilter
				.setAuthenticationFailureHandler((req, res, ex) -> res.setStatus(HttpServletResponse.SC_UNAUTHORIZED));

		// FormログインのFilterを置き換える
		http.addFilterAt(jsonUsernamePasswordAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		// Spring Securityデフォルトでは、アクセス権限（ROLE）設定したページに未認証状態でアクセスすると403を返すので、
		// 401を返すように変更
		http.exceptionHandling().authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
		// 今回は、403エラー時にHTTP Bodyを返さないように設定
		http.exceptionHandling().accessDeniedHandler((req, res, ex) -> res.setStatus(HttpServletResponse.SC_FORBIDDEN));

		// ログアウト
		http
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/api/logout", "GET"))
				// ログアウト時のリダイレクト抑制
				.logoutSuccessHandler((req, res, auth) -> res.setStatus(HttpServletResponse.SC_OK))
				.deleteCookies("JSESSIONID")
				.invalidateHttpSession(true);

	}

	/**
	 * Detail service configure.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
				.passwordEncoder(passwordEncoder());
	}

	/**
	 * Set encoding type.
	 * 
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new Pbkdf2PasswordEncoder();
	}

	/**
	 * Set cors.
	 * 
	 * @return
	 */
	private CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.addAllowedOrigin(path);
		corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
		corsConfiguration.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
		corsSource.registerCorsConfiguration("/**", corsConfiguration);

		return corsSource;
	}

}
