const HomeController = function (
  $scope,
  $http,
  $location,
  $routeParams,
  $timeout,
  $window
) {
  const base_url = "http://localhost:8080/api/course/top-rated";
  const token = $window.sessionStorage.getItem("jwt_token");
  const tokenUnLogin =
        "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImxlaG9hbmdodXljb2RlckBnbWFpbC5jb20iLCJzdWIiOiJsZWhvYW5naHV5Y29kZXJAZ21haWwuY29tIiwiZXhwIjoxNzM5NDE2NTMxfQ.HV8XuG-nsjmmq8RtlXP-eNGoKcvr5qpQkS9dstpX0xM"; // Thay thế bằng token thực tế của bạn;

  // Mảng chứa danh sách các khóa học
  $scope.listTopKH = [];

  // Hàm tải dữ liệu khóa học từ API
  $scope.loadTopKH = function () {
    $http.get(base_url, {
      headers: {
        'Authorization': `Bearer ${tokenUnLogin}`
      }
    }).then(resp => {
      console.log("Dữ liệu từ API:", resp.data);
      // Định dạng giá trị tiền tệ trước khi gán vào scope
      $scope.listTopKH = resp.data.map(kh => {
        if (kh.price === 0) {
          kh.formattedPrice = 'Miễn phí'; // Nếu giá bằng 0, gán giá là "Miễn phí"
        } else {
          kh.formattedPrice = new Intl.NumberFormat('vi-VN', {
            style: 'currency',
            currency: 'VND'
          }).format(kh.price);
        }
        return kh;
      });
    }).catch(err => {
      console.log("Lỗi không thể tải dữ liệu", err);
    });
  };


  const base_url_fl = "http://localhost:8080/api/course/follow";
  $scope.listTopFollowKH = [];

  // Hàm tải dữ liệu khóa học từ API
  $scope.loadTopFollowKH = function () {
    $http.get(base_url_fl, {
      headers: {
        'Authorization': `Bearer ${tokenUnLogin}`
      }
    }).then(resp => {
      console.log("Dữ liệu từ API:", resp.data);
      $scope.listTopFollowKH = resp.data.map(fl => {
        fl.formattedPrice = new Intl.NumberFormat('vi-VN', {
          style: 'currency',
          currency: 'VND'
        }).format(fl.price);
        return fl;
      });
    }).catch(err => {
      console.log("Lỗi không thể tải dữ liệu", err);
    });
  };
  $scope.loadTopFollowKH();


  const base_url_user = "http://localhost:8080/api/course/users";
  $scope.listNameCount = [];

  // Hàm tải dữ liệu khóa học từ API
  $scope.loadNameCount = function () {
    $http.get(base_url_user, {
      headers: {
        'Authorization': `Bearer ${tokenUnLogin}`
      }
    }).then(resp => {
      console.log("Dữ liệu từ API:", resp.data);
      $scope.listNameCount = resp.data;  // Lưu dữ liệu khóa học vào scope
    }).catch(err => {
      console.log("Lỗi không thể tải dữ liệu", err);
    });
  };
  $scope.loadNameCount();

  const base_url_course = "http://localhost:8080/api/course/name";
  $scope.listCourseCount = [];

  // Hàm tải dữ liệu khóa học từ API
  $scope.loadCourseCount = function () {
    $http.get(base_url_course, {
      headers: {
        'Authorization': `Bearer ${tokenUnLogin}`
      }
    }).then(resp => {
      console.log("Dữ liệu từ API:", resp.data);
      $scope.listCourseCount = resp.data;  // Lưu dữ liệu khóa học vào scope
    }).catch(err => {
      console.log("Lỗi không thể tải dữ liệu", err);
    });
  };
  $scope.loadCourseCount();


  const base_url_star = "http://localhost:8080/api/course/star";
  $scope.listStarCount = [];

  // Hàm tải dữ liệu khóa học từ API
  $scope.loadStarCount = function () {
    $http.get(base_url_star, {
      headers: {
        'Authorization': `Bearer ${tokenUnLogin}`
      }
    }).then(resp => {
      console.log("Dữ liệu từ API:", resp.data);
      $scope.listStarCount = resp.data;  // Lưu dữ liệu khóa học vào scope
    }).catch(err => {
      console.log("Lỗi không thể tải dữ liệu", err);
    });
  };
  $scope.loadStarCount();


  const base_url_cate = "http://localhost:8080/api/course/cate";
  $scope.listCateCount = [];

  // Hàm tải dữ liệu khóa học từ API
  $scope.loadCateCount = function () {
    $http.get(base_url_cate, {
      headers: {
        'Authorization': `Bearer ${tokenUnLogin}`
      }
    }).then(resp => {
      console.log("Dữ liệu từ API:", resp.data);
      $scope.listCateCount = resp.data;  // Lưu dữ liệu khóa học vào scope
    }).catch(err => {
      console.log("Lỗi không thể tải dữ liệu", err);
    });
  };
  $scope.loadCateCount();

  $scope.loadTopFollowKH();
  // Chuyển đến trang chi tiết khóa học
  $scope.gotoCourseDetail = function (courseId) {
    // Chuyển hướng đến trang chi tiết với query string chứa courseId
    $location.path("/assets/views/user/chi_tiet_khoa_hoc").search({ id: courseId });
    window.scrollTo(0, 0);  // Cuộn trang lên đầu khi chuyển hướng
  };



  const base_url_vc = "http://localhost:8080/api/course/rdVoucher";
  $scope.listVC = [];
  $scope.visibleCount = 4; // Số lượng item hiển thị mặc định
  $scope.selectedVoucher = null; // Biến lưu voucher được chọn
  // Hàm hiển thị toàn bộ voucher
  $scope.showAll = function () {
    $scope.visibleCount = $scope.listVC.length; // Hiển thị tất cả
  };
  
  $scope.collapse = function () {
    $scope.visibleCount = 4; // Thu gọn về 4 item
  };

  // Hàm định dạng ngày tháng dd-mm-yyyy
  function formatDate(dateString) {
    if (!dateString) return ""; // Trả về chuỗi rỗng nếu không có ngày
    const date = new Date(dateString);
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Tháng bắt đầu từ 0
    const year = date.getFullYear();
    return `${day}-${month}-${year}`;
  }

  //Hàm tải dữ liệu voucher từ API
  $scope.loadVC = function () {
    $http.get(base_url_vc, {
      headers: {
        'Authorization': `Bearer ${tokenUnLogin}` // Gửi token trong header
      }
    }).then(resp => {
      console.log("Dữ liệu từ API:", resp.data);

      // Xử lý định dạng ngày tháng cho dữ liệu trả về  
      $scope.listVC = resp.data.map(item => ({
        ...item,
        startDate: formatDate(item.startDate),
        endDate: formatDate(item.endDate),
      }));
      $scope.listVC.sort((a, b) => {
        const aCollected = $scope.isVoucherCollected(a.voucherId);
        const bCollected = $scope.isVoucherCollected(b.voucherId);
        return aCollected - bCollected; // false (0) xếp trước true (1)
      });
      console.log("Danh sách voucher sau khi định dạng:", $scope.listVC);
    }).catch(err => {

    });
  };

  $scope.myVouchers = []; // Danh sách voucher đã nhận của người dùng

  $scope.loadMyVouchers = function () {
    if (token == null) {
      console.log("Token: " + token);
      return;
    }
    $http({
      method: 'GET',
      url: 'http://localhost:8080/api/v1/voucher/myvoucher',
      headers: {
        'Authorization': `Bearer ${token}`
      }
    }).then(function successCallback(response) {
      // Khi thành công, xử lý dữ liệu
      // Giả sử response.data là danh sách voucher đã nhận
      $scope.myVouchers = response.data.map(item => item.voucherId); // Lưu voucherId vào mảng
    }, function errorCallback(error) {
      // Xử lý khi có lỗi

    });
  };

  // Gọi hàm loadMyVouchers để tải danh sách voucher đã nhận
  $scope.loadMyVouchers();

  // Hàm kiểm tra xem voucher đã được nhận hay chưa
  $scope.isVoucherCollected = function (voucherId) {
    return $scope.myVouchers.includes(voucherId);
  };

  // Hàm mở modal voucher chi tiết
  $scope.openModal = function (vc, event) {
    event.preventDefault(); // Ngăn chặn hành động mặc định của thẻ <a>
    $scope.selectedVoucher = vc; // Lưu voucher được chọn
    const myModal = new bootstrap.Modal(document.getElementById('voucherModal'));
    myModal.show(); // Hiển thị modal
  };

  // Hàm thu thập voucher
  $scope.collectVoucher = function (voucherId) {
    if(token == null){
      Swal.fire({
        title: "Bạn cần đăng nhập để thu thập voucher",
        icon: "error",
        confirmButtonText: "OK",
    });
      return;
    }
    const url = `http://localhost:8080/api/v1/voucher/collect/${voucherId}`;
    $http.post(url, {}, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    }).then(function (response) {
      alert("Thu thập voucher thành công!");
      console.log("Phản hồi từ server:", response.data);
      $scope.loadVC(); // Reload danh sách voucher
    }).catch(function (error) {
      const errorMessage = error.data?.message || "Thu thập voucher thành công";
      Swal.fire({
        title: errorMessage,
        icon: "success",
        confirmButtonText: "OK",
    });
    const myModal = bootstrap.Modal.getInstance(document.getElementById('voucherModal'));
        if (myModal) {
            myModal.hide(); // Ẩn modal
        }
    $scope.loadMyVouchers();
    // $window.location.reload();
      // $window.location.reload();
    });
  };

  // Khởi chạy hàm load voucher
  $scope.loadVC();

  // Gọi hàm tải khóa học khi controller được khởi tạo
  $scope.loadTopKH();
};



app.controller("HomeController", HomeController);
