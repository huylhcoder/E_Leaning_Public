package com.fpoly.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.fpoly.entity.User;
import com.fpoly.repository.UserRepository;

//Cái này là cái đầu tiên mình phải tạo sau khi tùy chỉnh User Entity
@Configuration
public class SecurityConfig {
	@Autowired
	private UserRepository userRepository;

	// user's detail object
	// Đối tượng này là chuẩn của Java Spring
	// Khi đăng nhập nó sẽ tạo ra đối tượng này
	// Vì vậy chúng ta phải làm sao để User Entity nó Config với UserDetails Spring
	// Lúc đó mình mới sử dụng được Authentication của Spring Security

	// Cái UserDetailsService này là một Inteface nó sẽ trả về một cái Function
	@Bean
	public UserDetailsService userDetailsService() {
		// Thông qua một cái trường duy nhất của mình
		// Trường hợp này là đăng nhập bằng Email duy nhất và mật khẩu
		// Trường Email sẽ được lưu vào trong Username của UserDetails
		return email -> {
			// Tìm cái email
			// Nếu tìm thấy thì trả về giá trị
			// Còn nếu không thì ném ra một cái Exception
			User existingUser = userRepository.findByEmail(email)
					.orElseThrow(() -> new UsernameNotFoundException("Không thể tìm thấy user với email = " + email));
			return existingUser;
		};
	}

	// Đối tượng mã hóa mật khẩu
	// SHA256 hoặc SHA512, MD5 là cũ rồi
	@Bean
	public PasswordEncoder passwordEncoder() {
		// Trong này nó đã thực thi các phương thức đó cho mình rồi khỏi cần viết lại
		// dài dòng
		return new BCryptPasswordEncoder();
	}

	// Có 2 hàm trên sẽ tạo được AuthenticationProvider
	// Chạy hàm này thì nó sẽ gọi 2 thằng ở trên
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	// Đầu vào của nó là Authentication Config
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
//	@Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.cors() // Bật CORS
//            .and()
//            .csrf().disable()
//            .authorizeRequests()
//            .anyRequest().permitAll(); // Cấu hình quyền truy cập
//        return http.build();
//    }
	
//	@Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://127.0.0.1:5500"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        configuration.setAllowedHeaders(Arrays.asList("*"));
//        configuration.setAllowCredentials(true);
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
}
