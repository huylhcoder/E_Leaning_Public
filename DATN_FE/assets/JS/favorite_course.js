const favorite_courseController = function ($scope, $http, $location, $rootScope, $window) {
    // const userId = 2;   
    const token = $window.sessionStorage.getItem("jwt_token"); // Thay thế bằng token thực tế của bạn;
    const url_favorite_course = `http://localhost:8080/api/favorite_course/user/${token}`;
    const url_khoa_hoc_cua_toi = `http://localhost:8080/api/course/regiteredCourse/${token}`;
    console.log("Token tại trang khóa học của tôi: token" + token);
    $scope.itemsPerPage = 5;  // Số lượng khóa học hiển thị trên mỗi trang
    $scope.totalCourses = 0; // Đếm tổng khóa học
    $scope.currentPage = 1;   // Trang hiện tại
    $scope.totalPages = 1;    // Tổng số trang
    $scope.listYeuThich = [];
    $scope.userTestResult = [];
    $scope.registeredCourses = []; // Khởi tạo mảng để lưu danh sách các khóa học đã đăng ký.
    if (token == null) {
      $window.location.href =
        "http://127.0.0.1:5500/index.html#!/assets/views/dang_nhap";
    }

    //scope
    $scope.getUsetID = function (courseId, registeredCourseId) {
        $('#exampleModal').modal('hide'); // Đóng modal nếu nó đang mở
        if (courseId) {
            $location.path('/assets/views/user/hoc_bai').search({
                id: courseId,
                registeredCourseId: registeredCourseId
            });
            window.scrollTo(0, 0); // Cuộn lên đầu trang
        }
    };
    //
    $scope.gotoLearn = function (courseId, registeredCourseId) {
        $('#exampleModal').modal('hide'); // Đóng modal nếu nó đang mở
        if (courseId) {
            $location.path('/assets/views/user/hoc_bai').search({
                id: courseId,
                registeredCourseId: registeredCourseId
            });
            window.scrollTo(0, 0); // Cuộn lên đầu trang
        }
    };
    $scope.gotoCourseDetail = function (courseId) {
        $location.path('/assets/views/user/chi_tiet_khoa_hoc').search({ id: courseId });
        window.scrollTo(0, 0);
    };
    // Định dạng tiền tệ
    $scope.formatCurrency = function (value) {
        return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".") + " VND";
    };
    // load danh sách khóa học 
    $scope.loadCuaToi = function () {
        $http.get(url_khoa_hoc_cua_toi, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            const courses = resp.data;
            if (courses.length === 0) {
                $scope.noResults = true;
                $scope.totalCourses = 0;
                $scope.registeredCourses = [];
                $scope.filteredCourses = [];
            } else {
                $scope.noResults = false;
                $scope.registeredCourses = courses;
                $scope.filteredCourses = courses; // Khởi tạo danh sách hiển thị
                $scope.totalCourses = courses.length;
                $scope.totalPages = Math.ceil($scope.totalCourses / $scope.itemsPerPage);
            } 
        }).catch(err => {
            console.error("Lỗi không thể tải dữ liệu", err);
        });
    };
    
    
    // Hàm để điều hướng đến trang cụ thể
    $scope.goToPage = function (page) {
        if (page >= 1 && page <= $scope.totalPages) {
            $scope.currentPage = page;
            window.scrollTo(0, 0); // Cuộn lên đầu trang
        }
    };
    // Hàm để lấy danh sách các trang để hiển thị
    $scope.getPages = function () {
        let pages = [];
        for (let i = 1; i <= $scope.totalPages; i++) {
            pages.push(i);
        }
        return pages;
    };
    //
    $scope.loadYeuThich = function () {
        $http.get(`http://localhost:8080/api/v1/favorite_course/user/${token}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            $scope.listYeuThich = resp.data;

        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    }

    //
    $scope.loadTienDo = function () {
        $http.get(`http://localhost:8080/api/test/userTestResult/${token}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
            .then(resp => {
                $scope.userTestResult = resp.data; // Lưu dữ liệu trả về từ API vào $scope.
            })
            .catch(err => {
                console.error("Lỗi không thể tải dữ liệu", err);
            });
    };
    //
    $scope.deleteItem = function (favoriteCourseId) {
        if (window.confirm("Bạn có chắc chắn muốn xóa mục này khỏi yêu thích?")) {
            $http.delete(`http://localhost:8080/api/v1/favorite_course/delete/${favoriteCourseId}`, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            }).then(resp => {
                $scope.loadYeuThich();
                // setInterval(() => {
                //     $scope.loadYeuThich();
                // }, 1000);
            }).catch(err => {
                console.log("Lỗi không thể lấy thông tin chi tiết", err);
            });
        } else {
            console.log("Người dùng đã hủy thao tác xóa.");
        }
    };
    $scope.searchQuery = ""; // Từ khóa tìm kiếm

    $scope.filterRegisteredCourses = function () {
        // Xử lý nếu có từ khóa tìm kiếm
        if ($scope.searchQuery.trim()) {
            const query = $scope.searchQuery.trim().toLowerCase(); // Cắt khoảng trắng và chuyển về chữ thường
    
            $scope.filteredCourses = $scope.registeredCourses.filter(myCourse => {
                const courseName = myCourse.course.name ? myCourse.course.name.toLowerCase() : "";
                const courseDescription = myCourse.course.description ? myCourse.course.description.toLowerCase() : "";
                const courseCategory = myCourse.course.category?.name ? myCourse.course.category.name.toLowerCase() : "";
    
                // Kiểm tra từ khóa có xuất hiện trong tên, mô tả hoặc danh mục không
                return (
                    courseName.includes(query) ||
                    courseDescription.includes(query) ||
                    courseCategory.includes(query)
                );
            });
        } else {
            // Nếu không có từ khóa, hiển thị tất cả
            $scope.filteredCourses = $scope.registeredCourses;
        }
    
        console.log("Danh sách khóa học đã lọc:", $scope.filteredCourses);
    };
    
    // Gọi hàm filter khi searchQuery thay đổi
    $scope.$watch("searchQuery", $scope.filterRegisteredCourses);
    $scope.loadYeuThich();
    $scope.loadCuaToi(); // Gọi hàm để tải dữ liệu.
    $scope.loadTienDo(); // Gọi hàm để tải dữ liệu.
};
app.controller("favorite_courseController", favorite_courseController);
// thêm xong token