const donHangController = function (
  $scope,
  $http,
  $location,
  $window,
  $rootScope
) {
  window.scrollTo(0, 0);
  
  const token = $window.sessionStorage.getItem("jwt_token");
  if (token == null) {
    $location.path("/assets/views/dang_nhap");
  }
  console.log("trang đơn hàng");
  console.dir("Biến toàn cục: " + $rootScope.don_hang);
  $scope.listCourse = [];
  $scope.listCourse = $rootScope.don_hang;
  $scope.listCourseId = [];
  $scope.tienChuaGiamGia = 0;
  $scope.tienGiam = 0;
  $scope.tongTien = 0;
  $scope.soLuongKhoaHoc = 0;
  $scope.voucherCode = null;
  $scope.vourcherObj = null; // vourcher object
  $scope.listKhuyenMaiCuaToi = null;

  // Hiển thị danh sách đơn hàng và tính chi tiết
  $scope.load_listKhoaHoc = function () {
    // Khởi tạo biến nếu chưa có
    $scope.tienChuaGiamGia = 0;
    $scope.soLuongKhoaHoc = 0;
    $scope.tienGiam = 0; // Reset tiền giảm

    if ($scope.listCourse == null || $scope.listCourse.length === 0) {
      return;
    }

    for (var i = 0; i < $scope.listCourse.length; i++) {
      $scope.tienChuaGiamGia += $scope.listCourse[i].price; // Đúng tiền chưa giảm giá
      $scope.soLuongKhoaHoc++;
      $scope.listCourseId.push($scope.listCourse[i].courseId);
    }
    $scope.tongTien = $scope.tienChuaGiamGia - $scope.tienGiam; // Đúng tổng tiền
  };

  if ($scope.listCourse != null && $scope.listCourse.length > 0) {
    $scope.load_listKhoaHoc();
  }

  $scope.loadSalesUser  = function () {
    $http({
      method: "GET",
      url: "http://localhost:8080/api/v1/voucher/myvoucher",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }).then(
      function successCallback(response) {
        // Khi thành công, xử lý dữ liệu
        $scope.listKhuyenMaiCuaToi = response.data;
        console.log(
          "Danh sách vourcher người dùng có:",
          $scope.listKhuyenMaiCuaToi
        );
      },
      function errorCallback(error) {
        // Xử lý khi có lỗi
        $scope.listKhuyenMaiCuaToi = null;
        console.error("Lỗi khi gọi API:", error);
      }
    );
  };
  $scope.loadSalesUser ();

  $scope.lblVourcherError = "";
  $scope.lblVourcherSuccess = "";
  $scope.applyVoucher = function (voucherCode) {
    $http({
      method: "GET",
      url: `http://localhost:8080/api/v1/voucher/check-vourcher/${voucherCode}`,
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }).then(
      function successCallback(response) {
        // Khi thành công, xử lý dữ liệu
        $scope.vourcherObj = response.data;
        $scope.voucherCode = $scope.vourcherObj.voucherCode;
        console.log("Vourcher Code được check:", $scope.vourcherObj.voucherCode);
        $scope.lblVourcherSuccess =
          "Bạn được giảm: " + $scope.vourcherObj.percentSale + "%";
        $scope.lblVourcherError = "";
        
        // Reset tiền giảm trước khi tính toán
        $scope.tienGiam = 0; 
        for (var i = 0; i < $scope.listCourse.length; i++) {
          $scope.tienGiam +=
            ($scope.listCourse[i].price * $scope.vourcherObj.percentSale) / 100;
        }
        $scope.tongTien = Math.max(0, $scope.tienChuaGiamGia - $scope.tienGiam); // Đảm bảo tổng tiền không âm
      },
      function errorCallback(error) {
        // Xử lý khi có lỗi
        $scope.voucherCode = null;
        $scope.lblVourcherError = error.data.message;
        $scope.lblVourcherSuccess = "";
        console.error("Lỗi khi gọi API:", error);
      }
    );
  };

  // Sử dụng vourcher của tôi
  $scope.useVourcher = function (vourcherCode) {
    console.log(vourcherCode);    
    $scope.voucherCodeSelected = vourcherCode;
    $scope.applyVoucher(vourcherCode);
  };

  // Chuyển sang trang thanh toán
  $scope.thanhToan = function () {
    const url = "http://localhost:8080/api/v1/vnpayajax";

    const formData = new FormData();
    formData.append("tokenString", token);
    formData.append("tienThanhToan", $scope.tongTien);
    formData.append("ListCourseId", JSON.stringify($scope.listCourseId));
    if ($scope.voucherCode != null) {
      formData.append("voucherCode", $scope.voucherCode);
    } else {
      formData.append("voucherCode", "");
    }

    $http
      .post(url, formData, {
        headers: {
          "Content-Type": undefined,
          Authorization: `Bearer ${token}`,
        },
      })
      .then((resp) => {
        $window.location.href = resp.data.data;
      })
      .catch((err) => {
        console.error("Lỗi khi thanh toán:", err);
        alert("Có lỗi xảy ra khi thanh toán");
      });
  };

  // Thêm chức năng xóa khóa học
  $scope.removeCourse = function (index) {
    if (index > -1) {
      $scope.listCourse.splice(index, 1); // Xóa khóa học khỏi danh sách
      $scope.listCourseId.splice(index, 1); // Cập nhật danh sách ID khóa học
      $scope.load_listKhoaHoc(); // Tính toán lại tổng tiền
    }
  };
};

app.controller("donHangController", donHangController);