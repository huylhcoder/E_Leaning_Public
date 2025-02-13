package com.fpoly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.dto.MyVoucherDTO;
import com.fpoly.entity.MyVoucher;
import com.fpoly.entity.User;
import com.fpoly.entity.Voucher;
import com.fpoly.repository.MyVoucherRepository;

@Service
public class MyVoucherService {
	@Autowired
	MyVoucherRepository myVoucherRepository;
	
	public void save(MyVoucher myVoucher) {
	    myVoucherRepository.save(myVoucher);
	}
	
	public List<MyVoucherDTO> getMyVouchersByUserId(int userId) {
        return myVoucherRepository.findMyVouchersByUserId(userId);
    }
	
	public MyVoucher findMyVoucherByVoucherAndUser(Voucher voucher, User user) {
        return myVoucherRepository.findByVoucherAndUser(voucher, user);
    }

}
