package com.fpoly.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import com.fpoly.entity.User;

import io.micrometer.common.lang.NonNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

//Tạo thứ 4 sau JWTUtils
//Do mình không thể if else từng cái API như thường
//Có api không cần token => Đăng nhập, đăng ký, xem sản phẩm
//Có api cần token và quyền admin
//Nó extends OncePerRequestFilter mỗi request điều phải qua cái này
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
	@Value("${api.prefix}")
	private String apiPrefix;
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JwtTokenUtils jwtTokenUtil;

	// Như thế nào thì ho đi qua
	// Như thế nào thì kiểm tra
	@Override
	protected void doFilterInternal(
			// Các yêu cầu này nó yêu cầu NOT NULL
			@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
			@NonNull FilterChain filterChain)
			// Nó sẽ ném về 2 cái Exception
			throws ServletException, IOException {
		try {
			// Đoạn này không cần kiểm tra
			if (isBypassToken(request)) {
				filterChain.doFilter(request, response); // enable bypass
				return;// Cho qua luôn không làm tiếp nữa
			}
			final String authHeader = request.getHeader("Authorization");
			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				System.out.println("Chưa nhập token");
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Chưa nhập Token");
				return;
			}
			// Đoạn này cần kiểm tra thì nó làm gì
			// Những cái request khác cần header mới vào được trong postman
			// Nó có một cái thẻ là Authorization
			// Nếu mà nó có giá trị
			if (authHeader != null || authHeader.startsWith("Bearer ")) {
				// Kiểm tra token của anh có hợp lệ chưa
				// Bearer_ có 7 ký tự mình cắt nó đi thì mình sẽ được cái Token
				final String token = authHeader.substring(7);
				final String email = jwtTokenUtil.extractEmail(token);
				//
				if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					// Nó sẽ hiểu đây là trường chính để đăng nhập UserDetailService
					// Nó kế thừa UserDetails nên mình có thể ép kiểu
					User existingUser = (User) userDetailsService.loadUserByUsername(email);
					// Kiểm tra token
					if (jwtTokenUtil.validateToken(token, existingUser)) {
						// Nếu mã hợp lệ thì Authticate với Java Spring
						UsernamePasswordAuthenticationToken authenticationToken =
								//
								new UsernamePasswordAuthenticationToken(
										//
										existingUser,
										//
										null,
										// Set quyền của nó
										existingUser.getAuthorities());
						authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					}
				}
				filterChain.doFilter(request, response); // enable bypass
			}

		} catch (Exception e) {
			System.out.println("JWT Filter - Đã có lỗi xảy ra:");
			System.out.println(e);
			e.printStackTrace(); // Ghi lại thông tin lỗi
			// Khí nó có Exception thì văng ra lỗi 401 thay vì lỗi 500
			// SC = Status Code = mã trạng thái
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Sai Token hoặc đã hết hạn");
		}

	}

	private boolean isBypassToken(@NonNull HttpServletRequest request) {
		// Những Request không cần yêu cầu Token
		final List<Pair<String, String>> bypassTokens = Arrays.asList(
				// Mình truyền thẳng "${api.prefix}/course"), "GET" nó không hiểu
				// Khởi tạo biến và sử dụng nối chuỗi để fix
				// Cho qua thoải mái
				Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
				Pair.of(String.format("%s/users/check-user", apiPrefix), "POST"),
				Pair.of(String.format("%s/users/send-verification-code", apiPrefix), "POST"),
				Pair.of(String.format("%s/users/login", apiPrefix), "POST"),
				Pair.of(String.format("%s/thanhtoanthanhcong", apiPrefix), "GET"), // Thêm vào đây
				Pair.of(String.format("%s/upload-file/download-video-on-server", apiPrefix), "GET"), // Thêm vào đây
				// Test cho public
				Pair.of(String.format("%s/course-level", apiPrefix), "GET"),
				Pair.of(String.format("api/cart/user/**"), "GET"), Pair.of(String.format("api/category"), "GET"),
				Pair.of(String.format("%s/course", apiPrefix), "GET"),
				Pair.of(String.format("%s/category", apiPrefix), "GET"),
				Pair.of(String.format("%s/course-manager-detail/**", apiPrefix), "GET"),
				Pair.of(String.format("%s/course-manager-detail/", apiPrefix), "PUT"),
				Pair.of(String.format("%s/course-manager/draft-course", apiPrefix), "GET"),
				Pair.of(String.format("%s/course-manager/posted-course", apiPrefix), "GET"),
				Pair.of(String.format("%s/course-manager", apiPrefix), "POST"),
				Pair.of(String.format("%s/section-manager/**", apiPrefix), "GET"),
				Pair.of(String.format("%s/section-manager/**", apiPrefix), "POST")
		//
		);
		for (Pair<String, String> bypassToken : bypassTokens) {
			if (request.getServletPath().contains(bypassToken.getFirst())
					&& request.getMethod().equals(bypassToken.getSecond())) {
				return true;
			}
		}
		// Ngoài những cái request trên ra thì trả về sai
		return false;
	}
}