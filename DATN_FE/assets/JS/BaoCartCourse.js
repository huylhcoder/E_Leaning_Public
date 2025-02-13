app.controller(
  "HBaoCartCourseController",
  function ($scope, $http, $rootScope, $window, $location) {
    const token = $window.sessionStorage.getItem("jwt_token"); // Lấy token từ sessionStorage
    const base_url = `http://localhost:8080/api/v1/cart/user/${token}`;
    $scope.listGioHang = [];
    $rootScope.don_hang = []; //Chứa course
    $scope.tongTien = 0;

    if (!token) {
      console.log("Token hoặc userId không tồn tại. Vui lòng đăng nhập lại.");
      return;
    }

    $scope.loadGioHang = function () {
      $http
        .get(base_url, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then((resp) => {
          $scope.listGioHang = resp.data; // Set lại list giỏ hàng
          $scope.listGioHang.forEach((item) => {
            item.checked = true; // Đặt tất cả checkbox thành true
          });
          $scope.calculateTotalPrice(); // Tính tổng tiền
          $scope.loadDonHang();
        })
        .catch((err) => {
          console.log("Lỗi không thể tải dữ liệu", err);
        });
    };
    $scope.loadGioHang();

    $scope.loadDonHang = function () {
      $rootScope.don_hang = [];
      $scope.listGioHang.forEach((item) => {
        $rootScope.don_hang.push(item.course);
      });
    };

    $scope.calculateTotalPrice = function (Cart) {
      //Nếu có thay đổi
      if (Cart != null) {
        if (Cart.checked == true) {
          // Nếu item được chọn, thêm vào don_hang
          $rootScope.don_hang.push(Cart.course);
        } else {
          // Nếu item không được chọn, loại bỏ khỏi don_hang
          $rootScope.don_hang = $rootScope.don_hang.filter(
            (course) => course.courseId !== Cart.course.courseId
          );
        }
      }
      $scope.tongTien = $scope.listGioHang.reduce(function (sum, item) {
        return sum + (item.checked ? item.course.price || 0 : 0); // Chỉ tính giá nếu checkbox được tích
      }, 0);
    };

    $scope.formatCurrency = function (value) {
      return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".") + " VND";
    };

    $scope.deleteItem = function (cartId) {
      if (window.confirm("Bạn có chắc chắn muốn xóa mục này khỏi giỏ hàng?")) {
        $http
          .delete(`http://localhost:8080/api/v1/cart/delete/${cartId}`, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          })
          .then((resp) => {
            $scope.loadGioHang();
            // setInterval(() => {
            //     $scope.loadGioHang();
            // }, 1000);
            $http
              .get(base_url, {
                headers: {
                  Authorization: `Bearer ${token}`,
                },
              })
              .then((resp) => {
                $rootScope.listGioHang = resp.data; // Cập nhật giỏ hàng
                $rootScope.$broadcast("cartUpdated", resp.data); // Phát ra sự kiện cartUpdated
              })
              .catch((err) => {
                console.log("Lỗi không thể tải giỏ hàng", err);
              });
          })
          .catch((err) => {
            console.log("Lỗi không thể lấy thông tin chi tiết", err);
          });
      } else {
        console.log("Người dùng đã hủy thao tác xóa.");
      }
    };

    $scope.thanhToan = function () {
      //console.log($rootScope.don_hang);
      $location.path("/assets/views/payment/don_hang");
    };
  }
);
// Thêm token xong
