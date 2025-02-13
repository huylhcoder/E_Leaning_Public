package com.fpoly.security;

import static org.springframework.http.HttpMethod.*;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fpoly.entity.Role;

//Cái này là cái thứ 2 phải có sau cái Security Config
//Nó yêu cầu quyền từ các Request gửi tới
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
	@Autowired
	private JwtTokenFilter jwtTokenFilter;
	@Value("${api.prefix}")
	private String apiPrefix;

	// Bảo vệ khi request gửi đến
	// Có đã đủ giấy tờ chưa
	// Ông có quyền gì để vào
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
				.authorizeHttpRequests(requests -> {
					requests
							// Category
							// CourseLevel
							// Course
							.requestMatchers(GET,
									// Course Level
									String.format("%s/course-level", apiPrefix),
									// Category
									String.format("api/category"),
									// Course
									String.format("%s/course-manager-detail/**", apiPrefix),
									String.format("%s/course-manager/draft-course", apiPrefix),
									String.format("%s/course-manager/posted-course", apiPrefix),
									// Section
									String.format("%s/section-manager/**", apiPrefix))
							.permitAll()
							//Payment
							.requestMatchers(GET, String.format("%s/thanhtoanthanhcong", apiPrefix)).permitAll() // Cho phép truy cập
							.requestMatchers(GET, String.format("%s/upload-file/download-video-on-server", apiPrefix)).permitAll() // Cho phép truy cập
							.requestMatchers(GET, String.format("%s/upload-file/download-video", apiPrefix)).permitAll()
							// Course
							// Account
							.requestMatchers(POST, String.format("%s/users/register", apiPrefix),
									String.format("%s/users/check-user", apiPrefix),
									String.format("%s/users/send-verification-code", apiPrefix),
									String.format("%s/users/login", apiPrefix),
									String.format("%s/course-manager", apiPrefix),
									String.format("%s/section-manager/**", apiPrefix))
							.permitAll()
							//
							.requestMatchers(PUT, String.format("%s/course-manager-detail/**", apiPrefix)).permitAll()
							// Test
							.requestMatchers(GET, String.format("%s/roles**", apiPrefix)).permitAll()

							.requestMatchers(GET, String.format("%s/categories**", apiPrefix)).permitAll()

							.requestMatchers(POST, String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)

							.requestMatchers(PUT, String.format("%s/categories/**", apiPrefix)).hasAnyRole(Role.ADMIN)

							.requestMatchers(DELETE, String.format("%s/categories/**", apiPrefix))
							.hasAnyRole(Role.ADMIN)
//
//							.requestMatchers(GET, String.format("%s/products**", apiPrefix)).permitAll()
//
//							.requestMatchers(GET, String.format("%s/products/images/*", apiPrefix)).permitAll()
//
//							.requestMatchers(POST, String.format("%s/products**", apiPrefix)).hasAnyRole(Role.ADMIN)
//
//							.requestMatchers(PUT, String.format("%s/products/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//
//							.requestMatchers(DELETE, String.format("%s/products/**", apiPrefix)).hasAnyRole(Role.ADMIN)
//
//							.requestMatchers(POST, String.format("%s/orders/**", apiPrefix)).hasAnyRole(Role.USER)
//
//							.requestMatchers(GET, String.format("%s/orders/**", apiPrefix)).permitAll()
//
//							.requestMatchers(PUT, String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
//
//							.requestMatchers(DELETE, String.format("%s/orders/**", apiPrefix)).hasRole(Role.ADMIN)
//
//							.requestMatchers(POST, String.format("%s/order_details/**", apiPrefix))
//							.hasAnyRole(Role.USER)
//
//							.requestMatchers(GET, String.format("%s/order_details/**", apiPrefix)).permitAll()
//
//							.requestMatchers(PUT, String.format("%s/order_details/**", apiPrefix)).hasRole(Role.ADMIN)
//
//							.requestMatchers(DELETE, String.format("%s/order_details/**", apiPrefix))
//							.hasRole(Role.ADMIN)

							.anyRequest().authenticated();
					// .anyRequest().permitAll();

				}).csrf(AbstractHttpConfigurer::disable);
		http.cors(new Customizer<CorsConfigurer<HttpSecurity>>() {
			@Override
			public void customize(CorsConfigurer<HttpSecurity> httpSecurityCorsConfigurer) {
				CorsConfiguration configuration = new CorsConfiguration();
				configuration.setAllowedOrigins(List.of("*"));
				configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
				configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
				configuration.setExposedHeaders(List.of("x-auth-token"));
				UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
				source.registerCorsConfiguration("/**", configuration);
				httpSecurityCorsConfigurer.configurationSource(source);
			}
		});

		return http.build();
	}
}
