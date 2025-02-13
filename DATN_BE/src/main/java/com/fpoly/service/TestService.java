package com.fpoly.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.entity.Lesson;
import com.fpoly.entity.Question;
import com.fpoly.entity.Section;
import com.fpoly.entity.Test;
import com.fpoly.repository.TestRepository;

@Service
public class TestService {
	@Autowired
	TestRepository testRepo;
	@Autowired
	TestRepository testRepository;

	public Test timTestTheoIdTam(int testId) {
		Test baiKiemTraCuaSection = testRepo.findByTestId(testId);
		return baiKiemTraCuaSection;
	}

	public List<Test> hienThiTestTheoSection(Section sectionEntity) {
		List<Test> listTest = testRepo.findBySection(sectionEntity);
		return listTest;
	}

	// Tìm bài quiz có mã nhỏ nhất của section
	public Test timKiemBaiQuizNhoNhatCuaSection(Section section) {
		return testRepo.findFirstBySectionOrderByTestIdAsc(section);
	}

	// Thêm câu hỏi mới
	public Test addTest(Test test) {
		return testRepo.save(test);
	}
	
	public Test saveTest(Test test) {
		return testRepo.save(test);
	}

	public void saveAll(List<Test> tests) {
        testRepository.saveAll(tests);
    }
}
