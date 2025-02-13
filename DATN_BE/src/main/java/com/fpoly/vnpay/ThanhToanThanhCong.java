package com.fpoly.vnpay;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fpoly.dto.PaymentDTO;
import com.fpoly.entity.Course;
import com.fpoly.entity.MyVoucher;
import com.fpoly.entity.Payment;
import com.fpoly.entity.RegisteredCourse;
import com.fpoly.entity.User;
import com.fpoly.entity.Voucher;
import com.fpoly.security.JwtTokenUtils;
import com.fpoly.service.CartService;
import com.fpoly.service.CourseService;
import com.fpoly.service.MyVoucherService;
import com.fpoly.service.PaymentService;
import com.fpoly.service.RegisteredCourseService;
import com.fpoly.service.UserService;
import com.fpoly.service.VoucherService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@CrossOrigin("*")
@RestController
@RequestMapping("${api.prefix}")
public class ThanhToanThanhCong {
	@Autowired
	private JwtTokenUtils jwtTokenUtils;
	@Autowired
	private UserService userService;
	@Autowired
	private PaymentService paymentService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private MyVoucherService myVoucherService;
	@Autowired
	private CartService cartService;
	@Autowired
	private RegisteredCourseService registeredCourseService;

	@GetMapping("/thanhtoanthanhcong")
	public void thanhToanThanhCong(@RequestParam("user") String tokenString, @RequestParam("paymentId") int paymentId,
			@RequestParam("voucherCode") String voucherCode, @RequestParam("vnp_Amount") long amount,
			@RequestParam("vnp_BankCode") String bankCode, @RequestParam("vnp_BankTranNo") String bankTranNo,
			@RequestParam("vnp_CardType") String cardType, @RequestParam("vnp_OrderInfo") String orderInfo,
			@RequestParam("vnp_PayDate") String payDate, @RequestParam("vnp_ResponseCode") String responseCode,
			@RequestParam("vnp_TmnCode") String tmnCode, @RequestParam("vnp_TransactionNo") long transactionNo,
			@RequestParam("vnp_TransactionStatus") String transactionStatus, @RequestParam("vnp_TxnRef") long txnRef,
			@RequestParam("vnp_SecureHash") String secureHash, HttpServletResponse response, HttpServletRequest request)
			throws IOException {
		System.out.println("--------------------Vào trang thanh toán thành công-------------------");
		System.out.println(tokenString);
		System.out.println(paymentId);
		System.out.println(voucherCode);
		// Thực hiển thao tác với khuyến mãi và khuyến mãi của tôi nếu có mã khuyến mãi
		// pass
		// Giảm số lượng
		// Cập nhật trạng thái khuyến mãi của tôi
		
		boolean checkVoucherCode = false;
		System.out.println("Giá trị voucherCode: " + voucherCode);
		if (!voucherCode.equals("no")) {
		    System.out.println(voucherCode);
		    checkVoucherCode = true;
		}
		
		Voucher voucher = new Voucher();
		// Kiểm tra khuyến mãi
		if (checkVoucherCode) {
		    voucher = voucherService.findVoucherByCode_Huy(voucherCode);
		    if (voucher != null) {
		        voucher.setQuantity(voucher.getQuantity() - 1);
		    } else {
		        System.out.println("Không tìm thấy voucher với mã: " + voucherCode);
		    }
		}

		// Kiểm tra token
		String email = "";
		try {
			email = jwtTokenUtils.extractEmail(tokenString);
			System.out.println("Email: " + email);
		} catch (Exception e) {
			System.out.println("Thanh toán thất bại lỗi token sai");
			response.sendRedirect("http://127.0.0.1:5500/index.html#!/assets/views/payment/thanh_toan_that_bai");
		}
		User user = userService.getUserByEmailToan(email);
		if (user == null) {
			System.out.println("Thanh toán thất bại lỗi user null");
			response.sendRedirect("http://127.0.0.1:5500/index.html#!/assets/views/payment/thanh_toan_that_bai");
		}

		// Cập nhật trạng thái vourcher của người dùng
		if (checkVoucherCode) {
		    voucher = voucherService.findVoucherByCode_Huy(voucherCode);
		    if (voucher != null) {
		        voucher.setQuantity(voucher.getQuantity() - 1);
		        voucherService.save(voucher); // Lưu đối tượng Voucher sau khi cập nhật
		    } else {
		        System.out.println("Không tìm thấy voucher với mã: " + voucherCode);
		    }
		}

		// Tìm kiếm payment để cập nhật
		Payment payment = paymentService.getPaymentById(paymentId).orElse(null);
		if (payment == null) {
			System.out.println("Thanh toán thất bại lỗi payment null");
			response.sendRedirect("http://127.0.0.1:5500/index.html#!/assets/views/payment/thanh_toan_that_bai");
		}
		payment.setAmount(amount);
		payment.setBankCode(bankCode);
		payment.setBanktranNo(bankTranNo);
		payment.setTransactionNo(transactionNo + "");
		payment.setTransactionStatus(true);
		payment.setTxnRef(txnRef + "");
		payment.setUser(user);
		Payment paymentUpdate = paymentService.savePayment(payment);
		// findRegisterCourseByPayment
		List<RegisteredCourse> listRegisteredCourses = registeredCourseService
				.findRegisterCourseByPayment(paymentUpdate);
		// Thêm khóa học đăng ký
		for (RegisteredCourse registeredCourseItem : listRegisteredCourses) {
			registeredCourseItem.setStatusPayment(true);
			registeredCourseService.saverRgisteredCourse(registeredCourseItem);
			
			// Xóa các cart có khóa học giống với khóa học đã đăng ký
	        int courseId = registeredCourseItem.getCourse().getCourseId(); // Giả sử bạn có phương thức getCourseId()
	        cartService.deleteCartsByUserIdAndCourseId(user.getUserId(), courseId); // Giả sử bạn có phương thức này
		}
		// session.removeAttribute("paymentDTO");
		response.sendRedirect("http://127.0.0.1:5500/index.html#!/assets/views/payment/thanh_toan_thanh_cong");
		System.out.println("--------------------Hết trang thanh toán thành công-------------------");
	}
}
