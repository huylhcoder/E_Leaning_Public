package com.fpoly.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.fpoly.dto.MyVoucherDTO;
import com.fpoly.entity.MyVoucher;
import com.fpoly.entity.User;
import com.fpoly.entity.Voucher;

@Repository
public interface MyVoucherRepository extends JpaRepository<MyVoucher, Integer> {
	@Query("SELECT new com.fpoly.dto.MyVoucherDTO(m.myVoucherId, m.status, v.name, v.description, v.voucherCode, v.percentSale, v.startDate, v.endDate) " +
	           "FROM MyVoucher m JOIN m.voucher v WHERE m.user.id = :userId AND v.status = true AND m.status = true")
	    List<MyVoucherDTO> findMyVouchersByUserId(@Param("userId") int userId);
	
	MyVoucher findByVoucherAndUser(Voucher vourcher, User user);
}
