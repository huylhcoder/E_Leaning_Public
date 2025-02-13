app.controller('HBaoCourseController', function ($scope, $location, $http, $rootScope, $window) {
    ;
    const token =
        "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImxlaG9hbmdodXljb2RlckBnbWFpbC5jb20iLCJzdWIiOiJsZWhvYW5naHV5Y29kZXJAZ21haWwuY29tIiwiZXhwIjoxNzM5NDE2NTMxfQ.HV8XuG-nsjmmq8RtlXP-eNGoKcvr5qpQkS9dstpX0xM"; // Thay thế bằng token thực tế của bạn;
    const tokenLogin = $window.sessionStorage.getItem("jwt_token"); // Lấy token từ sessionStorage
    const base_url = "http://localhost:8080/api/course";
    const offcanvasElement = document.querySelector('#offcanvasWithBothOptions'); // Lấy offcanvas element
    const offcanvasInstance = bootstrap.Offcanvas.getOrCreateInstance(offcanvasElement); // Lấy offcanvas element
    const radioButtons = offcanvasElement.querySelectorAll('.form-check-input');// Lấy tất cả input radio trong offcanvas
    const relatedButtons = offcanvasElement.querySelectorAll('.tags');// Lấy tất cả các nút trong phần "Tìm kiếm có liên quan"
    $scope.listKhoaHoc = []; // lưu danh sách khóa học vào list
    $scope.listDanhMuc = []; // lưu danh sách danh mục vào list
    $scope.totalCourses = 0; // Đếm tổng khóa học
    $scope.selectedRating = null; // biến lấy số sao 
    $scope.minPrice = 2000000; // Giá trị mặc định cho min
    $scope.maxPrice = 7000000; // Giá trị mặc định cho max
    const minGap = 1500; // Giá trị tăng mỗi lần di chuyển
    const sliderMinValue = 0; // Giá trị tối thiểu
    const sliderMaxValue = 10000000; // Giá trị tối đa
    $scope.itemsPerPage = 15;  // Số lượng khóa học hiển thị trên mỗi trang
    $scope.currentPage = 1;   // Trang hiện tại
    $scope.totalPages = 1;    // Tổng số trang
    let categoryId = 0;
    let hashTagId = 0;
    let name = null;


    if ($location.search().categoryId != undefined) {
        categoryId = $location.search().categoryId;  // Đây là cách lấy id từ URL query string
    }
    if ($location.search().hashTagId != undefined) {
        hashTagId = $location.search().hashTagId;  // Đây là cách lấy id từ URL query string
    }
    if ($location.search().name != undefined) {
        name = $location.search().name;  // Đây là cách lấy id từ URL query string
    }

    // Hàm load khóa học và phân trang
    $scope.loadKhoaHoc = function () {
        console.log("Biến toàn cục: " + $rootScope.listKhoaHoc);
        console.log("ID danh mục: " + categoryId);
        if (categoryId != 0) {
            console.log("Danh mục khác 0: " + categoryId);
            //let urlKhoaHoc = `http://localhost:8080/api/category/cat/${categoryId}`;
            let urlKhoaHoc = `http://localhost:8080/api/course/findCategory/${categoryId}`;
            $http.get(urlKhoaHoc, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            }).then(resp => {
                $scope.listKhoaHoc = resp.data;
                $scope.totalCourses = $scope.listKhoaHoc.length; // Cập nhật số lượng khóa học
            }).catch(err => {
                console.error("Error fetching courses by category:", err);
            });
            return;
        }

        if (hashTagId != 0) {
            console.log("Danh mục khác 0: " + hashTagId);
            let urlKhoaHoc = `http://localhost:8080/api/course/findCourseHashTag/${hashTagId}`;
            $http.get(urlKhoaHoc, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            }).then(resp => {
                $scope.listKhoaHoc = resp.data;
                $scope.totalCourses = $scope.listKhoaHoc.length; // Cập nhật số lượng khóa học
            }).catch(err => {
                console.error("Error fetching courses by category:", err);
            });
            return;
        }

        if (name != null) {
            console.log("Danh mục khác 0: " + name);
            let urlKhoaHoc = `http://localhost:8080/api/course/search?name=${encodeURIComponent(name)}`;
            $http.get(urlKhoaHoc, {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            }).then(resp => {
                $scope.listKhoaHoc = resp.data;
                $scope.totalCourses = $scope.listKhoaHoc.length; // Cập nhật số lượng khóa học
            }).catch(err => {
                console.error("Error fetching courses by category:", err);
            });
            return;
        }

        if ($rootScope.listKhoaHoc != null) {
            console.log("Nhảy vào phương thức sử dụng listKhoaHoc từ $rootScope");
            $scope.listKhoaHoc = $rootScope.listKhoaHoc || [];
            $rootScope.listKhoaHoc = null;
            return;
        }

        // Kiểm tra và gán token thích hợp
        $http.get(base_url, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            const courses = resp.data; // Dữ liệu khóa học trả về từ server
            if (courses.length === 0) {
                $scope.noResults = true;
                $scope.totalCourses = 0;
                $scope.listKhoaHoc = [];
            } else {
                $scope.noResults = false;
                $scope.listKhoaHoc = courses;
                $scope.totalCourses = courses.length;
                $scope.totalPages = Math.ceil($scope.totalCourses / $scope.itemsPerPage);
            }
        }).catch(err => {
            $scope.noResults = true;
            $scope.totalCourses = 0;
            $scope.totalPages = 1;
            console.error("Lỗi không thể tải dữ liệu từ server:", err);
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
    $scope.gotoCourseDetail = function (courseId) {
        $location.path('/assets/views/user/chi_tiet_khoa_hoc').search({ id: courseId });
        window.scrollTo(0, 0); // Cuộn lên đầu trang
    };
    // - Hàm định dạng giá trị thành tiền tệ
    $scope.formatCurrency = function (value) {
        return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".") + " VND";
    };
    // --- SẮP XẾP KHÓA HỌC --- 
    // - Sắp xếp khóa học theo lượt đăng ký 
    $scope.sortByFollow = function () {
        $http.get(`${base_url}/Follow`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            $scope.listKhoaHoc = resp.data;//Set lại list giở hàng
            $scope.totalCourses = $scope.listKhoaHoc.length; // Cập nhật số lượng khóa học
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    }
    // - Sắp xếp khóa học theo lượt đánh giá
    $scope.sortByRating = function () {
        $http.get(`${base_url}/TotalRateDESC`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            $scope.listKhoaHoc = resp.data;//Set lại list giở hàng
            $scope.totalCourses = $scope.listKhoaHoc.length; // Cập nhật số lượng khóa học
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    }
    // - Sắp xếp khóa học theo giá tăng dần 
    $scope.sortByPriceDesc = function () {
        $http.get(`${base_url}/PriceDESC`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            $scope.listKhoaHoc = resp.data;//Set lại list giở hàng
            $scope.totalCourses = $scope.listKhoaHoc.length; // Cập nhật số lượng khóa học
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    }
    // - Sắp xếp khóa học theo giá giảm dần
    $scope.sortByPriceAsc = function () {
        $http.get(`${base_url}/PriceASC`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            $scope.listKhoaHoc = resp.data;//Set lại list giở hàng
            $scope.totalCourses = $scope.listKhoaHoc.length; // Cập nhật số lượng khóa học
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    }
    // --- BỘ LỌC ---
    // - Tìm kiếm khóa học theo giá miễn phí
    $scope.getFreeCourses = function () {
        $http.get(`${base_url}/Free`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            $scope.listKhoaHoc = resp.data;//Set lại list giở hàng
            $scope.totalCourses = $scope.listKhoaHoc.length; // Cập nhật số lượng khóa học
            $scope.totalPages = Math.ceil($scope.totalCourses / $scope.itemsPerPage); // Cập nhật tổng số trang
            $scope.currentPage = 1; // Đặt lại về trang đầu tiên
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    }
    // - Tìm kiếm khóa học giá dưới 500.000 vnd
    $scope.getSmallPriceCourses = function () {
        $http.get(`${base_url}/SmallPrice`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            $scope.listKhoaHoc = resp.data;//Set lại list giở hàng
            $scope.totalCourses = $scope.listKhoaHoc.length; // Cập nhật số lượng khóa học
            $scope.totalPages = Math.ceil($scope.totalCourses / $scope.itemsPerPage); // Cập nhật tổng số trang
            $scope.currentPage = 1; // Đặt lại về trang đầu tiên
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    }
    // - Tìm kiếm khóa học giá trên 500.000 vnd
    $scope.getBigPriceCourses = function () {
        $http.get(`${base_url}/BigPrice`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            $scope.listKhoaHoc = resp.data;//Set lại list giở hàng
            $scope.totalCourses = $scope.listKhoaHoc.length; // Cập nhật số lượng khóa học
            $scope.totalPages = Math.ceil($scope.totalCourses / $scope.itemsPerPage); // Cập nhật tổng số trang
            $scope.currentPage = 1; // Đặt lại về trang đầu tiên
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    }
    // --- TÌM KIẾM KHÓA HỌC THEO GIÁ TỪ MIN ĐẾN MAX ---
    // + Hàm cập nhật vị trí slider
    function setArea() {
        const range = document.querySelector(".slider-track");
        const minTooltip = document.querySelector(".min-tooltip");
        const maxTooltip = document.querySelector(".max-tooltip");

        const minPercentage = ($scope.minPrice / sliderMaxValue) * 100;
        const maxPercentage = ($scope.maxPrice / sliderMaxValue) * 100;

        range.style.left = minPercentage + "%";
        range.style.right = (100 - maxPercentage) + "%";

        minTooltip.style.left = minPercentage + "%"; // Đặt vị trí cho tooltip min
        maxTooltip.style.left = maxPercentage + "%"; // Đặt vị trí cho tooltip max
    }
    // + Cập nhật giá trị min khi di chuyển slider
    function slideMin() {
        let gap = parseInt($scope.maxPrice) - parseInt($scope.minPrice);
        if (gap <= minGap) {
            $scope.minPrice = parseInt($scope.maxPrice) - minGap;
        }
        setArea();
    }
    // + Cập nhật giá trị max khi di chuyển slider
    function slideMax() {
        let gap = parseInt($scope.maxPrice) - parseInt($scope.minPrice);
        if (gap <= minGap) {
            $scope.maxPrice = parseInt($scope.minPrice) + minGap;
        }
        setArea();
    }
    // + Gọi API để lấy khóa học theo giá trong khoảng từ min - max
    $scope.getPriceRangeCourses = function () {
        $http.get(`${base_url}/min/${$scope.minPrice}/max/${$scope.maxPrice}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            $scope.listKhoaHoc = resp.data;//Set lại list giở hàng
            $scope.totalCourses = $scope.listKhoaHoc.length; // Cập nhật số lượng khóa học
            $scope.totalPages = Math.ceil($scope.totalCourses / $scope.itemsPerPage); // Cập nhật tổng số trang
            $scope.currentPage = 1; // Đặt lại về trang đầu tiên
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    }
    // + Theo dõi thay đổi minPrice và maxPrice
    $scope.$watch('minPrice', function (newValue, oldValue) {
        if (newValue !== oldValue) {
            slideMin();
            $scope.getPriceRangeCourses();
        }
    });
    $scope.$watch('maxPrice', function (newValue, oldValue) {
        if (newValue !== oldValue) {
            slideMax();
            $scope.getPriceRangeCourses();
        }
    });
    // + Cập nhật giá trị từ input range
    $scope.updateMinMaxPrice = function () {
        const minInput = document.querySelector('input[type="range"][value="' + $scope.minPrice + '"]');
        const maxInput = document.querySelector('input[type="range"][value="' + $scope.maxPrice + '"]');

        if (minInput && maxInput) {
            $scope.minPrice = minInput.value;
            $scope.maxPrice = maxInput.value;
        }
        setArea();
    };
    // + Cập nhật giá trị min khi người dùng thay đổi ô input
    $scope.setMinInput = function () {
        let minPrice = parseInt($scope.minPrice.toString().replace(/\./g, '').replace(' VND', '')); // Chuyển đổi từ định dạng tiền tệ về số
        if (minPrice < sliderMinValue) {
            $scope.minPrice = sliderMinValue;
        } else {
            $scope.minPrice = minPrice;
        }
        slideMin();
    };
    // + Cập nhật giá trị max khi người dùng thay đổi ô input
    $scope.setMaxInput = function () {
        let maxPrice = parseInt($scope.maxPrice.toString().replace(/\./g, '').replace(' VND', '')); // Chuyển đổi từ định dạng tiền tệ về số
        if (maxPrice > sliderMaxValue) {
            $scope.maxPrice = sliderMaxValue;
        } else {
            $scope.maxPrice = maxPrice;
        }
        slideMax();
    };
    // - Tìm kiếm khóa học theo số sao đánh giá
    $scope.getCourses = function (rating) {
        let url = '';
        switch (rating) {
            case '5':
                url = '/Rate5Star';
                break;
            case '4':
                url = '/Rate4Star';
                break;
            case '3':
                url = '/Rate3Star';
                break;
            case '2':
                url = '/Rate2Star';
                break;
            default:
                return; // Không làm gì nếu không có rating hợp lệ
        }
        $http.get(`${base_url}${url}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            $scope.listKhoaHoc = resp.data;//Set lại list giở hàng
            $scope.totalCourses = $scope.listKhoaHoc.length; // Cập nhật số lượng khóa học
            $scope.totalPages = Math.ceil($scope.totalCourses / $scope.itemsPerPage); // Cập nhật tổng số trang
            $scope.currentPage = 1; // Đặt lại về trang đầu tiên
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    }
    // load danh mục khóa học
    $scope.loadDanhMuc = function () {
        $http.get(`${base_url}/getAllCategory`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            $scope.listDanhMuc = resp.data;//Set lại list giở hàng
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    }
    // - hiển thị toàn bộ danh mục 
    $scope.findCategoryById = function (categoryId) { // Chấp nhận categoryId là tham số
        $http.get(`http://localhost:8080/api/course/findCategory/${categoryId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            console.log(categoryId);
            $scope.listKhoaHoc = resp.data; // Set lại danh sách khóa học
            $scope.totalCourses = $scope.listKhoaHoc.length; // Cập nhật số lượng khóa học
            $scope.itemsPerPage = $scope.itemsPerPage || 10; // Default là 10 nếu chưa có
            $scope.totalPages = Math.ceil($scope.totalCourses / $scope.itemsPerPage); // Cập nhật tổng số trang
            $scope.currentPage = 1; // Đặt lại về trang đầu tiên
        }).catch(err => {
            console.log("Lỗi không thể lấy thông tin chi tiết", err);
        });
    }
    // --- THÊM KHÓA HỌC VÀO GIỎ HÀNG ---
    
    $scope.addCourseToCart = function (courseParam) {
        // Kiểm tra token trước khi gửi yêu cầu
        if (!tokenLogin) {
            Swal.fire({
                title: "Vui lòng đăng nhập để thêm khóa học vào giỏ hàng!",
                icon: "warning"
            });
            return; // Nếu không có token, dừng lại
        }
        $http.post(`http://localhost:8080/api/v1/cart/addCourse/${courseParam}`, {}, {
            headers: {
                Authorization: `Bearer ${tokenLogin}`,
                "Content-Type": undefined, // Để trình duyệt tự động thiết lập Content-Type cho multipart/form-data
            },
        }).then((resp) => {
                //Tăng một đơn vị cho số lượng hàng trong giỏ hàng
                $rootScope.numberOfCourseInCart = $rootScope.numberOfCourseInCart + 1;
                $rootScope.listGioHang.push(resp.data);
                Swal.fire({
                    title: "Thêm Khóa học vào giỏ hàng thành công!",
                    icon: "success"
                });
            })
            .catch((err) => {
                Swal.fire({
                    title: err.data.message,
                    icon: "warning"
                });
                //Swal.fire(err.data.message, "error");
            });
    };
    // Gắn sự kiện click cho mỗi radio button
    radioButtons.forEach(button => {
        button.addEventListener('click', () => {
            // Đóng offcanvas khi click vào bất kỳ nút nào
            offcanvasInstance.hide();
        });
    });
    // Gắn sự kiện click cho các nút này
    relatedButtons.forEach(button => {
        button.addEventListener('click', () => {
            // Đóng offcanvas khi click vào nút
            offcanvasInstance.hide();
        });
    });
    // Lắng nghe sự kiện khi offcanvas đóng
    offcanvasElement.addEventListener('hidden.bs.offcanvas', () => {
        // Xóa lựa chọn của tất cả các input radio
        radioButtons.forEach(button => {
            button.checked = false;
        });

        // Nếu cần, cũng đặt lại các input range hoặc text
        const rangeInputs = offcanvasElement.querySelectorAll('input[type="text"]');
        rangeInputs.forEach(input => {

        });
    });
    // --- CHI TIẾT KHÓA HỌC 
    setArea(); // Khởi tạo giá trị ban đầu cho slider và tooltip
    $scope.loadKhoaHoc();
    $scope.loadDanhMuc();
});

// thêm xong token