package com.fpoly.vnpay;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpoly.dto.PaymentDTO;
import com.fpoly.entity.Course;
import com.fpoly.entity.Payment;
import com.fpoly.entity.RegisteredCourse;
import com.fpoly.entity.User;
import com.fpoly.entity.Voucher;
import com.fpoly.repository.MyVoucherRepository;
import com.fpoly.security.JwtTokenUtils;
import com.fpoly.service.CourseService;
import com.fpoly.service.PaymentService;
import com.fpoly.service.RegisteredCourseService;
import com.fpoly.service.UserService;
import com.fpoly.service.VoucherService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

@CrossOrigin("*")
@RestController()
public class AjaxVnpay {

	@Autowired
	private JwtTokenUtils jwtTokenUtils;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private UserService userService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private RegisteredCourseService registeredCourseService;
	@Autowired
	private VoucherService voucherService;

	@PostMapping("/api/v1/vnpayajax")
	protected void doPost(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam("tokenString") String tokenString, @RequestParam("tienThanhToan") int tienThanhToan,
			@RequestParam("ListCourseId") String listCourseIdJson, @RequestParam("voucherCode") Optional<String> voucherCode)
			throws ServletException, IOException, JSONException {
		// Chuyển đổi listCourseIdJson thành List<Integer>
		System.out.println("--------------------Ajax-------------------");
		List<Integer> listCourseId = new ArrayList<>();
		JSONArray jsonArray = new JSONArray(listCourseIdJson);
		for (int i = 0; i < jsonArray.length(); i++) {
			listCourseId.add(jsonArray.getInt(i));
		}
		//
		System.out.println(tokenString);
		System.out.println(tienThanhToan);
		System.out.println(listCourseId);
		System.out.println(voucherCode);

		//
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(tokenString);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			System.out.println("Thanh toán thất bại lỗi token sai");
			resp.sendRedirect("http://127.0.0.1:5500/index.html#!/assets/views/payment/thanh_toan_that_bai");
		}

		User user = userService.getUserByEmailToan(email);
		if (user == null) {
			System.out.println("Thanh toán thất bại lỗi user null");
			resp.sendRedirect("http://127.0.0.1:5500/index.html#!/assets/views/payment/thanh_toan_that_bai");
		}
		// Check voucherCode
		boolean checkVoucherCode = false;
		if (voucherCode.isPresent() && !voucherCode.get().isEmpty()) {
		    System.out.println(voucherCode.get());
		    checkVoucherCode = true;
		}else {
			voucherCode = Optional.ofNullable("no");
			checkVoucherCode = false;
		}
		Voucher voucher = new Voucher();
		// Kiểm tra khuyến mãi
		if (checkVoucherCode) {
			System.out.println("VourcherCode khác null");
			voucher = voucherService.findVoucherByCode_Huy(voucherCode.orElse("no"));
			if (voucher == null) {
				System.out.println("Không tìm thấy mã khuyến mãi");
				resp.sendRedirect("http://127.0.0.1:5500/index.html#!/assets/views/payment/thanh_toan_that_bai");
			}

			if (voucher.getQuantity() == 0) {
				resp.sendRedirect("http://127.0.0.1:5500/index.html#!/assets/views/payment/thanh_toan_that_bai");
			}

			Date currentDate = new Date();
			if (!voucher.isStatus() || currentDate.before(voucher.getStartDate())
					|| currentDate.after(voucher.getEndDate())) {
				resp.sendRedirect("http://127.0.0.1:5500/index.html#!/assets/views/payment/thanh_toan_that_bai");
			}
		}
		

		// Thêm một payment mới với trạng thái là false
		Payment payment = new Payment();
		payment.setTransactionStatus(false);
		payment.setUser(user);
		payment.setCreateAt(new Date());
		Payment paymentCreate = paymentService.savePayment(payment);
		// Thêm khóa học đăng ký
		for (Integer item : listCourseId) {
			Course course = courseService.timKhoaHocTheoMaKhoaHocHuy(item).orElse(null);
			if (course == null) {
				System.out.println("Thanh toán thất bại lỗi course null");
				resp.sendRedirect("http://127.0.0.1:5500/index.html#!/assets/views/payment/thanh_toan_that_bai");
			}
			//
			RegisteredCourse registeredCourse = new RegisteredCourse();
			registeredCourse.setCourse(course);
			registeredCourse.setCreateAt(new Date());
			registeredCourse.setPayment(paymentCreate);
			if (checkVoucherCode) {
				registeredCourse.setPrice(course.getPrice() - (course.getPrice() * voucher.getPercentSale() / 100));
				System.out.println(course.getPrice() - (course.getPrice() * voucher.getPercentSale() / 100));
			} else {
				registeredCourse.setPrice(course.getPrice());
			}
			registeredCourse.setStatusPayment(false);
			registeredCourse.setUser(user);
			registeredCourseService.saverRgisteredCourse(registeredCourse);
		}
		//
		String soTienCanThanhToan = tienThanhToan + "";
		soTienCanThanhToan = soTienCanThanhToan.replaceAll("[^0-9\\.]", "");
		//
		String vnp_Version = "2.1.0";
		String vnp_Command = "pay";
		String orderType = "other";
		System.out.println("--------------------Tổng tiền cần thanh toán-------------------" + soTienCanThanhToan);
		long amount = (long) (Float.parseFloat(soTienCanThanhToan) * 100);
		String bankCode = req.getParameter("bankCode");

		String vnp_TxnRef = Config.getRandomNumber(8);
		String vnp_IpAddr = Config.getIpAddress(req);

		String vnp_TmnCode = Config.vnp_TmnCode;

		Map<String, String> vnp_Params = new HashMap<>();
		vnp_Params.put("vnp_Version", vnp_Version);
		vnp_Params.put("vnp_Command", vnp_Command);
		vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
		vnp_Params.put("vnp_Amount", String.valueOf(amount));// vnp_Amount tại tảng return
		vnp_Params.put("vnp_CurrCode", "VND");

		if (bankCode != null && !bankCode.isEmpty()) {
			vnp_Params.put("vnp_BankCode", bankCode);
		}
		vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
		vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
		vnp_Params.put("vnp_OrderType", orderType);

		String locate = req.getParameter("language");
		if (locate != null && !locate.isEmpty()) {
			vnp_Params.put("vnp_Locale", locate);
		} else {
			vnp_Params.put("vnp_Locale", "vn");
		}
		String voucherCodeValue = voucherCode.orElse("no").trim();
		System.out.println(Config.vnp_ReturnUrl + "?user=" + tokenString + "&paymentId="
				+ paymentCreate.getPaymentId() + "&voucherCode=" + voucherCodeValue.trim());
		vnp_Params.put("vnp_ReturnUrl", Config.vnp_ReturnUrl + "?user=" + tokenString + "&paymentId="
				+ paymentCreate.getPaymentId() + "&voucherCode=" + voucherCodeValue.trim());
		vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String vnp_CreateDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

		cld.add(Calendar.MINUTE, 15);
		String vnp_ExpireDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

		List fieldNames = new ArrayList(vnp_Params.keySet());
		Collections.sort(fieldNames);
		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();
		Iterator itr = fieldNames.iterator();
		while (itr.hasNext()) {
			String fieldName = (String) itr.next();
			String fieldValue = (String) vnp_Params.get(fieldName);
			if ((fieldValue != null) && (fieldValue.length() > 0)) {
				// Build hash data
				hashData.append(fieldName);
				hashData.append('=');
				hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				// Build query
				query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
				query.append('=');
				query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
				if (itr.hasNext()) {
					query.append('&');
					hashData.append('&');
				}
			}
		}
		String queryUrl = query.toString();
		String vnp_SecureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
		queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
		String paymentUrl = Config.vnp_PayUrl + "?" + queryUrl;
		com.google.gson.JsonObject job = new JsonObject();
		job.addProperty("code", "00");
		job.addProperty("message", "success");
		job.addProperty("data", paymentUrl);
		Gson gson = new Gson();
		resp.getWriter().write(gson.toJson(job));
		System.out.println("--------------------Hết Ajax-------------------");
	}

	private void vnpayajax() {
		// TODO Auto-generated method stub

	}
}
