app.controller('headerKhoaController', function ($scope, $location, $http, $rootScope, $window) {
    const tokenUnLogin =
        "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImxlaG9hbmdodXljb2RlckBnbWFpbC5jb20iLCJzdWIiOiJsZWhvYW5naHV5Y29kZXJAZ21haWwuY29tIiwiZXhwIjoxNzM5NDE2NTMxfQ.HV8XuG-nsjmmq8RtlXP-eNGoKcvr5qpQkS9dstpX0xM"; // Thay thế bằng token thực tế của bạn;
    const token = $window.sessionStorage.getItem("jwt_token"); // Lấy token từ sessionStorage
    const base_url = `http://localhost:8080/api/v1/cart/user/${token}`;
    $scope.ListTenKhoaHoc = [];
    $rootScope.listGioHang = [];
    $scope.listDanhMuc = [];
    $scope.isLoggedIn = !!token; // Kiểm tra xem người dùng đã đăng nhập hay chưa
    // Lắng nghe sự thay đổi của giỏ hàng từ $rootScope
    $rootScope.$on('cartUpdated', function (event, data) {
        $rootScope.listGioHang = data; // Cập nhật giỏ hàng
    });

    // Tải danh sách giỏ hàng
    $scope.loadGioHang = function () {
        if (!$scope.isLoggedIn) return; // Nếu chưa đăng nhập, không tải giỏ hàng

        $http.get(base_url, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            $rootScope.listGioHang = resp.data; // Cập nhật giỏ hàng
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu giỏ hàng", err);
        });
    };
    $scope.gotoFavoriteCourses = function () {
        if (!token) {
            Swal.fire("Bạn cần đăng nhập để xem khóa học yêu thích", "", "info");
            $location.path('/assets/views/dang_nhap');
            return;
        }
        $location.path(`/assets/views/user/khoahocyeuthich`);
    };
    $scope.user = {};
    $scope.getUserDataByEmail = function () {
        $http
            .get(`http://localhost:8080/api/v1/user/profile/${token}`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
            .then(function (response) {
                if (response.data) {
                    $scope.user = response.data; // Cập nhật dữ liệu form với phản hồi từ serv;
                    console.log(response.data);
                } else {
                    alert("Không tìm thấy dữ liệu người dùng.");
                }
            })
            .catch(function (error) {
                console.error("Lỗi khi lấy dữ liệu người dùng:", error);
            });
    };
    $scope.getUserDataByEmail();

    // Chuyển đến trang chi tiết khóa học
    // $scope.gotoCourseDetail = function (courseId) {
    //     $('#exampleModal').modal('hide'); // Đóng modal nếu nó đang mở
    //     if (courseId) {
    //         $location.path('/assets/views/user/chi_tiet_khoa_hoc').search({ id: courseId });
    //     }
    // };

    $scope.gotoCourseDetail = function (courseId) {
        $location.path('/assets/views/user/chi_tiet_khoa_hoc').search({ id: courseId });
    };


    $scope.checkEnter = function (event, name) {
        if (event.key === "Enter" || event.keyCode === 13) {
            // Chuyển hướng đến trang mong muốn với nội dung tìm kiếm
            if (name && name.trim() !== '') {
                // Ví dụ: Chuyển hướng đến URL dựa trên tên khóa học
                if (token == null) {
                    if (!name || name.trim() === "") {
                        $scope.ListTenKhoaHoc = []; // Xóa danh sách nếu không có tên
                        return;
                    }
                    const url = `http://localhost:8080/api/course/search?name=${encodeURIComponent(name)}`;
                    $http.get(url, {
                        headers: {
                            'Authorization': `Bearer ${tokenUnLogin}`
                        }
                    }).then(resp => {
                        $scope.ListTenKhoaHoc = resp.data;
                        $location.path('/assets/views/user/danh_muc_khoa_hoc').search({ name: name });
                        $scope.clearSearchResultsNoName();
                    }).catch(err => {
                        console.log("Lỗi khi tìm kiếm khóa học", err);
                    });
                } else {
                    if (!name || name.trim() === "") {
                        $scope.ListTenKhoaHoc = []; // Xóa danh sách nếu không có tên
                        return;
                    }
                    const url = `http://localhost:8080/api/course/search?name=${encodeURIComponent(name)}`;
                    $http.get(url, {
                        headers: {
                            'Authorization': `Bearer ${token}`
                        }
                    }).then(resp => {
                        $scope.ListTenKhoaHoc = resp.data;
                        $location.path('/assets/views/user/danh_muc_khoa_hoc').search({ name: name });
                        $scope.clearSearchResultsNoName();
                    }).catch(err => {
                        console.log("Lỗi khi tìm kiếm khóa học", err);
                    });
                }
            }
        }
    }

    // Tìm khóa học theo tên
    $scope.LoadTenKhoaHoc = function (name) {
        if (!name || name.trim() === "") {
            $scope.ListTenKhoaHoc = []; // Xóa danh sách nếu không có tên
            return;
        }
        const url = `http://localhost:8080/api/course/search?name=${encodeURIComponent(name)}`;
        $http.get(url, {
            headers: {
                'Authorization': `Bearer ${tokenUnLogin}`
            }
        }).then(resp => {
            $scope.ListTenKhoaHoc = resp.data;
        }).catch(err => {
            console.log("Lỗi khi tìm kiếm khóa học", err);
        });
    };

    // Thêm khóa học vào giỏ hàng
    $scope.addCourseToCart = function (courseId) {
        if (!$scope.isLoggedIn) {
            Swal.fire("Bạn cần đăng nhập để thêm khóa học vào giỏ hàng", "", "info");
            $location.path('/assets/views/dang_nhap');
            return;
        }

        $http.post(`http://localhost:8080/api/v1/cart/add/${courseId}`, {}, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(function successCallback(response) {
            Swal.fire("Thêm vào giỏ hàng thành công!", "", "success");
            $scope.loadGioHang(); // Cập nhật giỏ hàng
        }).catch(function errorCallback(error) {
            Swal.fire("Lỗi", "Không thể thêm khóa học vào giỏ hàng", "error");
            console.log("Lỗi thêm giỏ hàng: ", error);
        });
    };

    // Tải danh mục khóa học
    $scope.loadDanhMuc = function () {
        const url = "http://localhost:8080/api/course/getAllCategory";
        $http.get(url, {
            headers: {
                'Authorization': `Bearer ${tokenUnLogin}` // Giữ lại header Authorization
            }
        }).then(resp => {
            $scope.listDanhMuc = resp.data;
        }).catch(err => {
            console.log("Lỗi không thể tải danh mục khóa học", err);
        });
    };

    // Tải danh sách khóa học theo danh mục
    $scope.load_course = function (categoryId) {
        const urlKhoaHoc = `http://localhost:8080/api/course/findCategory/${categoryId}`;
        $http.get(urlKhoaHoc, {
            headers: {
                'Authorization': `Bearer ${tokenUnLogin}`
            }
        }).then(resp => {
            $scope.listKhoaHoc = resp.data; // Cập nhật danh sách khóa học theo danh mục
            $location.path('/assets/views/user/danh_muc_khoa_hoc').search({ categoryId: categoryId });
        }).catch(err => {
            console.log("Lỗi không thể tải danh sách khóa học theo danh mục", err);
        });
    };
    $scope.logout = function () {
        $window.sessionStorage.removeItem("jwt_token");
        $scope.isLoggedIn = false;
        // Điều hướng đến trang home và tải lại trang
        $window.location.reload(); // Tải lại trang
    };

    $scope.clearSearchResults = function () {
        $scope.name = ''; // Xóa nội dung ô input
        $scope.ListTenKhoaHoc = []; // Ẩn kết quả
    };

    $scope.clearSearchResultsNoName = function () {
        $scope.ListTenKhoaHoc = []; // Ẩn kết quả
    };

    // Khởi tạo
    $scope.loadGioHang();
    $scope.loadDanhMuc();
});