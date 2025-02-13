app.controller('StatisticalController', function ($scope, $http, $timeout, $window) {
    const base_url_user = "http://localhost:8080/api/course_progress";
    const base_url_course = "http://localhost:8080/api/course";
    const token =
        "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImxlaG9hbmdodXljb2RlckBnbWFpbC5jb20iLCJzdWIiOiJsZWhvYW5naHV5Y29kZXJAZ21haWwuY29tIiwiZXhwIjoxNzM5NDE2NTMxfQ.HV8XuG-nsjmmq8RtlXP-eNGoKcvr5qpQkS9dstpX0xM"; // Thay thế bằng token thực tế của bạn;
    const tokenLogin = $window.sessionStorage.getItem("jwt_token");
    $scope.listKhoaHoc = []; // lưu danh sách khóa học vào list
    $scope.listNguoiDung = []; // lưu danh sách người dùng vào list
    $scope.listKhoaHocDaBan = []; // Lưu danh sách khóa học đã bán vào 
    $scope.listKhoaHocHoanThanh = []; // lưu danh sách khóa học hoàn thành vào list
    $scope.userStatsByMonth = []; // lưu dữ liệu thống kê người dùng theo tháng
    $scope.userStatsByYear = []; // lưu dữ liệu thống kê người dùng theo tháng
    $scope.revenueStatsByMonth = []; // lưu dữ liệu thống kê người dùng theo tháng
    $scope.revenueStatsByYear = []; // lưu dữ liệu thống kê người dùng theo năm
    $scope.totalCourses = 0; // Đếm tổng khóa học
    $scope.totalUser = 0; // Đếm tổng Người dùng
    $scope.totalRevenue = 0; // Biến để lưu tổng doanh thu
    $scope.totalCoursesComplete = 0; // Đếm số khóa học hoàn thành
    $scope.currentRevenueView = 'month'; // Xác định chế độ hiển thị doanh thu ('month' hoặc 'year')
    $scope.currentUserStatsView = 'month'; // Xác định chế độ hiển thị Thống kê người dùng ('month' hoặc 'year')

    

    // --- CODE PHẦN KHÓA HỌC ---
    // Hiển thị danh sách khóa học và tổng số khóa học
    $scope.loadKhoaHoc = function () {
        if (tokenLogin == null) {
            $http.get(base_url_course, {
                headers: {
                    'Authorization': `Bearer ${tokenLogin}`
                }
            }).then(resp => {
                $scope.listKhoaHoc = resp.data;//Set lại list giở hàng
                $scope.totalCourses = $scope.listKhoaHoc.length;
            }).catch(err => {
                console.log("Lỗi không thể tải dữ liệu", err);
            });
        } else {
            $http.get(base_url_course, {
                headers: {
                    'Authorization': `Bearer ${tokenLogin}`
                }
            }).then(resp => {
                $scope.listKhoaHoc = resp.data;//Set lại list giở hàng
                $scope.totalCourses = $scope.listKhoaHoc.length;
            }).catch(err => {
                console.log("Lỗi không thể tải dữ liệu", err);
            });
        }

    };
    // Hiển thị tổng số khóa học đã hoàn thành (số khóa học hoàn thành = số chứng chỉ đã phát)
    $scope.loadKhoaHocHoanThanh = function () {
        $http.get(`http://localhost:8080/api/course_progress/TotalComplete`, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            $scope.listKhoaHocHoanThanh = resp.data;//Set lại list giở hàng
            $scope.totalCoursesComplete = $scope.listKhoaHocHoanThanh.length;
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    };
    // Hiển thị danh sách Khóa học bán chạy 
    $scope.sortByFollow = function () {
        $http.get(`${base_url_course}/Follow`, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            $scope.listKhoaHoc = resp.data;//Set lại list giở hàng
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    }
    // Hiển thị danh sách Khóa học miễn phí
    $scope.getFreeCourses = function () {
        $http.get(`${base_url_course}/Free`, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            $scope.listKhoaHoc = resp.data;//Set lại list giở hàng
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    }
    // Hiển thị danh sách khóa học tính phí
    $scope.getUnFreeCourses = function () {
        $http.get(`${base_url_course}/UnFree`, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            $scope.listKhoaHoc = resp.data;//Set lại list giở hàng
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    }

    // --- CODE PHẦN USER ---
    // Hiển thị danh sách người dùng và tổng số người dùng
    $scope.loadNguoiDung = function () {
        $http.get(`${base_url_user}/userAdmin`, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            $scope.listNguoiDung = resp.data;//Set lại list giở hàng
            $scope.totalUser = $scope.listNguoiDung.length;
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    };
    // Hàm chuyển đổi chế độ hiển thị thống kê người dùng
    $scope.changeUserStatsView = function (viewType) {
        $scope.currentUserStatsView = viewType;
        if (viewType === 'month') {
            $scope.UserStatsChartTitle = "Biểu đồ gia tăng học viên theo tháng";
            $scope.loadUserStatsByMonth();
        } else if (viewType === 'year') {
            $scope.UserStatsChartTitle = "Biểu đồ gia tăng học viên theo năm";
            $scope.loadUserStatsByYear();
        }
    };
    // Lấy dữ liệu thống kê người dùng theo tháng
    $scope.loadUserStatsByMonth = function () {
        $http.get(`${base_url_user}/userStatsByMonth`, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            $scope.userStatsByMonth = resp.data;
            // Tạo nhãn và dữ liệu theo tháng
            const labels = ['T1', 'T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'T8', 'T9', 'T10', 'T11', 'T12'];
            const data = Array(12).fill(0);
            $scope.userStatsByMonth.forEach(stat => {
                const monthIndex = stat.month - 1;
                data[monthIndex] = stat.userCount;
            });
            // Vẽ biểu đồ người dùng với dữ liệu theo tháng
            $scope.ChartNguoiDung(labels, data);
        }).catch(err => {
            console.log("Lỗi khi tải dữ liệu thống kê người dùng theo tháng", err);
        });
    };
    // Lấy dữ liệu thống kê người dùng theo năm
    $scope.loadUserStatsByYear = function () {
        $http.get(`${base_url_user}/userStatsByYear`, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            $scope.userStatsByYear = resp.data;
            // Kiểm tra nếu không có dữ liệu
            if (!$scope.userStatsByYear.length) {
                console.log("No data for userStatsByYear");
                return;
            }
            // Tạo nhãn và dữ liệu theo năm
            const labels = $scope.userStatsByYear.map(stat => stat.year);
            const data = $scope.userStatsByYear.map(stat => stat.userCount);
            // Vẽ biểu đồ người dùng với dữ liệu theo năm
            $scope.ChartNguoiDung(labels, data);
        }).catch(err => {
            console.log("Lỗi khi tải dữ liệu thống kê người dùng theo năm", err);
        });
    };
    // Khởi tạo và cập nhật biểu đồ người dùng
    $scope.ChartNguoiDung = function (labels, data) {
        const ctxLine = document.getElementById('myLineChart').getContext('2d');

        // Hủy biểu đồ cũ nếu tồn tại
        if ($scope.myLineChart) {
            $scope.myLineChart.destroy();
        }

        // Khởi tạo biểu đồ mới
        $scope.myLineChart = new Chart(ctxLine, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [{
                    label: $scope.currentUserStatsView === 'month' ? 'Số học viên theo tháng' : 'Số học viên theo năm',
                    data: data,
                    borderWidth: 2,
                    borderColor: '#42A5F5',
                    backgroundColor: 'rgba(66, 165, 245, 0.2)',
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    };

    // --- CODE PHẦN DOANH THU ---
    // Gọi danh sách khóa học đã đăng ký và tính Tổng doanh thu
    $scope.loadKhoaHocDaDK = function () {
        $http.get(`${base_url_user}/totalRevenue`, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            const registeredCourses = resp.data;
            $scope.totalRevenue = registeredCourses.reduce((sum, course) => {
                return sum + (course.price || 0); // Thay course.price bằng giá khóa học tương ứng
            }, 0);

            // Nếu bạn muốn định dạng lại tổng doanh thu
            $scope.totalRevenueFormatted = $scope.totalRevenue.toLocaleString('vi-VN') + " VND";
            console.log("Formatted Revenue:", $scope.totalRevenueFormatted); // Kiểm tra log
        }).catch(err => {
            console.log("Lỗi khi tải dữ liệu thống kê người dùng theo tháng", err);
        });

    }
    // Hàm chuyển đổi chế độ hiển thị doanh thu
    $scope.changeRevenueView = function (viewType) {
        $scope.currentRevenueView = viewType;
        if (viewType === 'month') {
            $scope.revenueChartTitle = "Biểu đồ doanh thu theo tháng";
            $scope.loadRevenueStatsByMonth();
        } else if (viewType === 'year') {
            $scope.revenueChartTitle = "Biểu đồ doanh thu theo năm";
            $scope.loadRevenueStatsByYear();
        }
    };
    // Biểu đồ doanh thu theo tháng
    $scope.loadRevenueStatsByMonth = function () {
        $http.get(`${base_url_user}/courseStatsByMonth`, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            $scope.revenueStatsByMonth = resp.data;
            const labels = $scope.revenueStatsByMonth.map(item => `T${item.month}`);
            const data = $scope.revenueStatsByMonth.map(item => item.recordCount);
            $scope.ChartDoanhThu(labels, data);
        }).catch(err => {
            console.log("Lỗi khi tải dữ liệu doanh thu theo tháng", err);
        });
    };
    // Hàm tải dữ liệu doanh thu theo năm
    $scope.loadRevenueStatsByYear = function () {
        $http.get(`${base_url_user}/courseStatsByYear`, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            $scope.revenueStatsByYear = resp.data;
            // Kiểm tra log dữ liệu nhận từ API
            console.log("Dữ liệu doanh thu theo năm:", $scope.revenueStatsByYear);
            const labels = $scope.revenueStatsByYear.map(item => item.year);
            const data = $scope.revenueStatsByYear.map(item => item.recordCount);
            // Kiểm tra log labels và data trước khi truyền vào biểu đồ
            console.log("Labels:", labels);
            console.log("Data:", data);
            $scope.ChartDoanhThu(labels, data);
        }).catch(err => {
            console.log("Lỗi khi tải dữ liệu doanh thu theo năm", err);
        });
    };
    // Khởi tạo và cập nhật biểu đồ doanh thu
    $scope.ChartDoanhThu = function (labels, data) {
        const ctx = document.getElementById('myChart').getContext('2d');

        // Xóa biểu đồ cũ nếu tồn tại
        if ($scope.myChart) {
            $scope.myChart.destroy();
        }

        // Khởi tạo biểu đồ mới
        $scope.myChart = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Doanh thu',
                    data: data,
                    borderWidth: 1,
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    borderColor: 'rgba(75, 192, 192, 1)'
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    };

    //--- CODE PHẦN CÁC KHÓA HỌC ĐÃ BÁN VÀ SỐ LƯỢT BÁN
    $scope.loadKhoaHocDaBan = function () {
        $http.get(`${base_url_user}/listRegisteredCourse`, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            $scope.listKhoaHocDaBan = resp.data;
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    };

    // --- XUẤT EXCEL ---
    // XUẤT DANH SÁCH KHÓA HỌC RA FILE EXCEL
    $scope.exportCourseToExcel = function () {
        // Lấy dữ liệu khóa học bán chạy
        $http.get(`${base_url_course}/Follow`, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            const popularCourses = resp.data;

            // Lấy dữ liệu khóa học miễn phí
            $http.get(`${base_url_course}/Free`, {
                headers: {
                    'Authorization': `Bearer ${tokenLogin}`
                }
            }).then(resp => {
                const freeCourses = resp.data;

                // Lấy dữ liệu khóa học tính phí
                $http.get(`${base_url_course}/UnFree`, {
                    headers: {
                        'Authorization': `Bearer ${tokenLogin}`
                    }
                }).then(resp => {
                    const paidCourses = resp.data;

                    // Chuẩn bị dữ liệu cho các worksheet
                    let allCourses = $scope.listKhoaHoc.map((course, index) => ({
                        STT: index + 1,
                        "Mã khóa học": course.courseId,
                        "Tên khóa học": course.name,
                        "Số bài học": course.numberOfLesson,
                        Giá: course.price == 0 ? "Miễn phí" : course.price.toLocaleString('vi-VN') + " VND",
                        "Số học viên": course.follow
                    }));

                    let popularCourseData = popularCourses.map((course, index) => ({
                        STT: index + 1,
                        "Mã khóa học": course.courseId,
                        "Tên khóa học": course.name,
                        "Số bài học": course.numberOfLesson,
                        Giá: course.price == 0 ? "Miễn phí" : course.price.toLocaleString('vi-VN') + " VND",
                        "Số học viên": course.follow
                    }));

                    let freeCourseData = freeCourses.map((course, index) => ({
                        STT: index + 1,
                        "Mã khóa học": course.courseId,
                        "Tên khóa học": course.name,
                        "Số bài học": course.numberOfLesson,
                        Giá: "Miễn phí",
                        "Số học viên": course.follow
                    }));

                    let paidCourseData = paidCourses.map((course, index) => ({
                        STT: index + 1,
                        "Mã khóa học": course.courseId,
                        "Tên khóa học": course.name,
                        "Số bài học": course.numberOfLesson,
                        Giá: course.price.toLocaleString('vi-VN') + " VND",
                        "Số học viên": course.follow
                    }));

                    // Sheet thống kê tổng số lượng khóa học
                    let summaryData = [
                        { "Loại khóa học": "Tổng số khóa học", "Số lượng": $scope.listKhoaHoc.length },
                        { "Loại khóa học": "Số khóa học miễn phí", "Số lượng": freeCourses.length },
                        { "Loại khóa học": "Số khóa học tính phí", "Số lượng": paidCourses.length }
                    ];

                    // Tạo các worksheet
                    let workbook = XLSX.utils.book_new();
                    let worksheetAll = XLSX.utils.json_to_sheet(allCourses);
                    let worksheetPopular = XLSX.utils.json_to_sheet(popularCourseData);
                    let worksheetFree = XLSX.utils.json_to_sheet(freeCourseData);
                    let worksheetPaid = XLSX.utils.json_to_sheet(paidCourseData);
                    let worksheetSummary = XLSX.utils.json_to_sheet(summaryData);

                    // Thêm worksheet vào workbook
                    XLSX.utils.book_append_sheet(workbook, worksheetAll, "Tất cả khóa học");
                    XLSX.utils.book_append_sheet(workbook, worksheetPopular, "Khóa học bán chạy");
                    XLSX.utils.book_append_sheet(workbook, worksheetFree, "Khóa học miễn phí");
                    XLSX.utils.book_append_sheet(workbook, worksheetPaid, "Khóa học tính phí");
                    XLSX.utils.book_append_sheet(workbook, worksheetSummary, "Tổng quan");

                    // Định dạng kích thước cột tự động vừa với nội dung
                    [worksheetAll, worksheetPopular, worksheetFree, worksheetPaid, worksheetSummary].forEach(sheet => {
                        let range = XLSX.utils.decode_range(sheet['!ref']);
                        for (let C = range.s.c; C <= range.e.c; ++C) {
                            let maxWidth = 10; // Chiều rộng tối thiểu
                            for (let R = range.s.r; R <= range.e.r; ++R) {
                                let cellAddress = XLSX.utils.encode_cell({ c: C, r: R });
                                let cell = sheet[cellAddress];
                                if (cell && cell.v) {
                                    let cellValue = cell.v.toString();
                                    maxWidth = Math.max(maxWidth, cellValue.length + 2); // Cộng thêm 2 ký tự cho khoảng cách
                                }
                            }
                            if (!sheet['!cols']) sheet['!cols'] = [];
                            sheet['!cols'][C] = { width: maxWidth };
                        }
                    });

                    // Xuất workbook dưới dạng file Excel
                    XLSX.writeFile(workbook, "DanhSachKhoaHoc.xlsx");

                }).catch(err => console.error("Lỗi tải dữ liệu khóa học tính phí:", err));
            }).catch(err => console.error("Lỗi tải dữ liệu khóa học miễn phí:", err));
        }).catch(err => console.error("Lỗi tải dữ liệu khóa học bán chạy:", err));
    };
    // XUẤT DANH SÁCH NGƯỜI DÙNG RA FILE EXCEL
    $scope.exportUserToExcel = function () {
        let promises = $scope.listNguoiDung.map((user, index) => {
            return $http.get(`${base_url_user}/courseRegister/${user.userId}`, {
                headers: {
                    'Authorization': `Bearer ${tokenLogin}`
                }
            }).then(resp => {
                // Kiểm tra dữ liệu trả về
                console.log("Dữ liệu trả về cho userId:", user.userId, resp.data);
                let courseNames = Array.isArray(resp.data) && resp.data.length > 0 
                    ? resp.data.map(course => course.course.name).join(", ")
                    : "Người dùng chưa tham gia khóa học";
                return {
                    STT: index + 1,
                    "User  ID": user.userId,
                    "Tên người dùng": user.name,
                    "Số điện thoại": user.phone,
                    "Email": user.email,
                    "Các khóa học đã tham gia": courseNames
                };
            }).catch(err => {
                console.error("Lỗi khi gọi API cho userId:", user.userId, err);
                return {
                    STT: index + 1,
                    "User  ID": user.userId,
                    "Tên người dùng": user.name,
                    "Số điện thoại": user.phone,
                    "Email": user.email,
                    "Các khóa học đã tham gia": "Người dùng chưa tham gia khóa học"
                };
            });
        });
    
        // Chờ tất cả các Promise hoàn thành
        Promise.all(promises).then(tableData => {
            let worksheet = XLSX.utils.json_to_sheet(tableData);
    
            // Đặt độ rộng cho từng cột
            worksheet['!cols'] = [
                { wpx: 40 },  // Cột STT (40px)
                { wpx: 80 },  // Cột User ID (80px)
                { wpx: 150 }, // Cột Tên người dùng (150px)
                { wpx: 100 }, // Cột Số điện thoại (100px)
                { wpx: 200 }, // Cột Email (200px)
                { wpx: 300 }  // Cột Các khóa học đã tham gia (300px)
            ];
    
            // Tạo workbook và thêm worksheet
            let workbook = XLSX.utils.book_new();
            XLSX.utils.book_append_sheet(workbook, worksheet, "Danh sách người dùng");
    
            // Xuất workbook thành file Excel
            let now = new Date();
            let formattedDate = `${now.getFullYear()}-${now.getMonth() + 1}-${now.getDate()}`;
            XLSX.writeFile(workbook, `DanhSachNguoiDung_${formattedDate}.xlsx`);
        }).catch(error => {
            console.error("Lỗi trong Promise.all:", error);
        });
    };    
    // XUẤT DANH SÁCH KHÓA HỌC ĐÃ BÁN VÀ SỐ LƯỢT BÁN 
    $scope.exportKhoaHocDaBanToExcel = function () {
        // Tạo dữ liệu cho Excel từ listKhoaHocDaBan
        let tableData = $scope.listKhoaHocDaBan.map((itemUser, index) => ({
            STT: index + 1,
            "Mã khóa học": itemUser[0], // Mã khóa học (ID)
            "Tên Khóa học": itemUser[1], // Tên khóa học
            "Số người theo dõi": itemUser[2], // Số người theo dõi
            "Giá": itemUser[3] == 0 ? 'Miễn phí' : (itemUser[3].toLocaleString('vi-VN') + ' VND'), // Giá
            "Số lượt bán": itemUser[4] // Số lượt bán
        }));
    
        // Tạo một worksheet từ dữ liệu
        let worksheet = XLSX.utils.json_to_sheet(tableData);
    
        // Đặt độ rộng cho các cột
        worksheet['!cols'] = [
            { wpx: 50 },  // Cột STT (50px)
            { wpx: 100 }, // Cột Mã khóa học (100px)
            { wpx: 250 }, // Cột Tên khóa học (250px)
            { wpx: 150 }, // Cột Số người theo dõi (150px)
            { wpx: 150 }, // Cột Giá (150px)
            { wpx: 100 }  // Cột Số lượt bán (100px)
        ];
    
        // Tạo workbook và thêm worksheet vào
        let workbook = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(workbook, worksheet, "Danh sách khóa học đã bán");
    
        // Xuất workbook thành file Excel
        XLSX.writeFile(workbook, "DanhSachKhoaHocDaBan.xlsx");
    };
    

    // lấy khóa học đã đăng ký theo userId
    $scope.getKhoaHocDaDangKy = function (userId) {
        console.log({ userId });
        $http.get(`${base_url_user}/courseRegister/${userId}`, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            $scope.listRegisterCourseByUser = resp.data;
            $scope.listChiTietNguoiDung = resp.data; // Gán lại giá trị cho listChiTietNguoiDung
            $('#userDetailModalUserStatistical').modal('show');
            console.log(resp.data);
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    }


    $scope.loadKhoaHoc();
    $scope.loadNguoiDung();
    $scope.loadKhoaHocDaDK();
    $scope.loadKhoaHocDaBan();
    $scope.loadKhoaHocHoanThanh();
    $scope.loadUserStatsByMonth();
    $scope.loadRevenueStatsByMonth();
    $scope.changeRevenueView('month'); // Mặc định là doanh thu theo tháng
    $scope.changeUserStatsView('month'); // Mặc định là thống kê theo tháng
});
// Thêm token xong