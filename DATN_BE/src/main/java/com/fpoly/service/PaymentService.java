package com.fpoly.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.dto.PaymentDTO;
import com.fpoly.entity.Course;
import com.fpoly.entity.Payment;
import com.fpoly.entity.RegisteredCourse;
import com.fpoly.repository.AnswerRepository;
import com.fpoly.repository.CourseRepository;
import com.fpoly.repository.PaymentRepository;

@Service
public class PaymentService {
	@Autowired
	PaymentRepository paymentRepository;

	public Payment savePayment(Payment payment) {
		return paymentRepository.save(payment);
	}

	public Optional<Payment> getPaymentById(int paymentId) {
		return paymentRepository.findById(paymentId);
	}
}
