package com.fpoly.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.fpoly.entity.MailInfo;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender emailSender;
	List<MimeMessage> dsEmail = new ArrayList();
	
	public void sendEmail(String to, String code) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Mã xác nhận đăng ký");
		message.setText("Mã xác nhận của bạn là: " + code);
		emailSender.send(message);
	}
	
	// GỬI MÃ OTP
	public void sendOTP(MailInfo mail) {
		MimeMessage message = emailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper (message, true, "utf-8");
			helper.setFrom(mail.getFrom());//người gửi
			helper.setTo(mail.getTo());//người nhận
			helper.setSubject(mail.getSubject());//tiêu đề
			helper.setText(mail.getBody(), true);//nội dung
			helper.setReplyTo (mail.getFrom());//người gửi
			if(mail.haveCc()) {//nếu có đính kèm CC (carbon copy)
				for(String email: mail.getCc()) {
					helper.addCc(email);
				}
			}
			if(mail.haveBcc()) {//nếu có đính kèm BCC (Blind carbon copy)
				for(String email: mail.getBcc()) { 
					helper.addBcc(email);
				}
			}
			if(mail.haveAttachments()) {//nếu có định kèm tệp
				for(String path: mail.getAttachments()) {
					File file = new File(path);
					helper.addAttachment(file.getName(), file);
				}
			}
			emailSender.send(message);;//thực hiện gửi mail
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// ĐÓNG GÓP Ý KIẾN
	public void sendComment(MailInfo mail) { 
	    MimeMessage message = emailSender.createMimeMessage();
	    try {
	        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
	        // Người gửi được lấy từ form
	        helper.setFrom(mail.getFrom()); 
	        // Người nhận gán cứng
	        helper.setTo("khoatdpc07807@fpt.edu.vn"); // Thay bằng email người nhận cố định
	        // Tiêu đề
	        helper.setSubject(mail.getSubject());
	        // Nội dung
	        helper.setText(mail.getBody(), true);
	        // Đặt người gửi làm email để trả lời
	        helper.setReplyTo(mail.getFrom());
	        // Nếu có đính kèm CC (Carbon Copy)
	        if (mail.haveCc()) {
	            for (String email : mail.getCc()) {
	                helper.addCc(email);
	            }
	        }
	        // Nếu có đính kèm BCC (Blind Carbon Copy)
	        if (mail.haveBcc()) {
	            for (String email : mail.getBcc()) {
	                helper.addBcc(email);
	            }
	        }
	        // Gửi email
	        emailSender.send(message);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	// GỬI CHỨNG CHỈ
	public void sendCertificateWithAttachment(MailInfo mailInfo, String attachmentPath) {
	    MimeMessage message = emailSender.createMimeMessage();
	    try {
	        // Sử dụng MimeMessageHelper để tạo email với định dạng UTF-8
	        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
	        
	        // Cài đặt thông tin cơ bản của email
	        helper.setFrom(mailInfo.getFrom());
	        helper.setTo(mailInfo.getTo());
	        helper.setSubject(mailInfo.getSubject());
	        helper.setText(mailInfo.getBody(), true); // Nội dung hỗ trợ HTML
	        
	        // Nếu có CC
	        if (mailInfo.haveCc()) {
	            for (String email : mailInfo.getCc()) {
	                helper.addCc(email);
	            }
	        }

	        // Nếu có BCC
	        if (mailInfo.haveBcc()) {
	            for (String email : mailInfo.getBcc()) {
	                helper.addBcc(email);
	            }
	        }

	        // Thêm tệp đính kèm (tệp chứng chỉ)
	        File attachmentFile = new File(attachmentPath);
	        if (attachmentFile.exists()) {
	            helper.addAttachment(attachmentFile.getName(), attachmentFile);
	        }

	        // Gửi email
	        emailSender.send(message);
	        System.out.println("Chứng chỉ đã được gửi thành công tới " + mailInfo.getTo());
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Lỗi khi gửi email với chứng chỉ: " + e.getMessage());
	    }
	}

	// Gửi phản hồi có file
	public void sendCommentFile(MailInfo mailInfo, String attachmentPath) {
	    MimeMessage message = emailSender.createMimeMessage();
	    try {
	        // Sử dụng MimeMessageHelper để tạo email với định dạng UTF-8
	        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
	        
	        // Cài đặt thông tin cơ bản của email
	        helper.setFrom(mailInfo.getFrom());
	        helper.setTo(mailInfo.getTo());
	        helper.setSubject(mailInfo.getSubject());
	        helper.setText(mailInfo.getBody(), true); // Nội dung hỗ trợ HTML
	        
	        // Nếu có CC
	        if (mailInfo.haveCc()) {
	            for (String email : mailInfo.getCc()) {
	                helper.addCc(email);
	            }
	        }

	        // Nếu có BCC
	        if (mailInfo.haveBcc()) {
	            for (String email : mailInfo.getBcc()) {
	                helper.addBcc(email);
	            }
	        }

	        // Thêm tệp đính kèm (tệp chứng chỉ)
	        File attachmentFile = new File(attachmentPath);
	        if (attachmentFile.exists()) {
	            helper.addAttachment(attachmentFile.getName(), attachmentFile);
	        }

	        // Gửi email
	        emailSender.send(message);
	        System.out.println("Phản hồi đã được gửi thành công tới " + mailInfo.getTo());
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Lỗi khi gửi email với phản hồi: " + e.getMessage());
	    }
	}
	
	// Gửi lý do chặn người dùng 
	public void sendBlockingReason(MailInfo mailInfo, String attachmentPath) {
	    MimeMessage message = emailSender.createMimeMessage();
	    try {
	        // Sử dụng MimeMessageHelper để tạo email với định dạng UTF-8
	        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
	        
	        // Cài đặt thông tin cơ bản của email
	        helper.setFrom(mailInfo.getFrom());
	        helper.setTo(mailInfo.getTo());
	        helper.setSubject(mailInfo.getSubject());
	        helper.setText(mailInfo.getBody(), true); // Nội dung hỗ trợ HTML
	        
	        // Nếu có CC
	        if (mailInfo.haveCc()) {
	            for (String email : mailInfo.getCc()) {
	                helper.addCc(email);
	            }
	        }

	        // Nếu có BCC
	        if (mailInfo.haveBcc()) {
	            for (String email : mailInfo.getBcc()) {
	                helper.addBcc(email);
	            }
	        }

	        // Thêm tệp đính kèm (tệp chứng chỉ)
	        File attachmentFile = new File(attachmentPath);
	        if (attachmentFile.exists()) {
	            helper.addAttachment(attachmentFile.getName(), attachmentFile);
	        }

	        // Gửi email
	        emailSender.send(message);
	        System.out.println("Lý do chặn tài khoản đã được gửi thành công tới " + mailInfo.getTo());
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("Lỗi khi gửi email với lý do chặn tài khoản: " + e.getMessage());
	    }
	}
}
