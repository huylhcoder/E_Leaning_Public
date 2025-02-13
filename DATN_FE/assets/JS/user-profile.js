const demothongtinuserController = function (
  $scope,
  $http,
  $location,
  $rootScope,
  $window
) {
  const token = $window.sessionStorage.getItem("jwt_token");
  //Nữa lấy token bằng session đăng nhập là oke
  $scope.user = {};
  //Hiển thị thông tin user
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

  // Tự động gọi hàm để lấy dữ liệu ngay khi trang được tải
  $scope.getUserDataByEmail();

  $scope.selectedImage = null;
  $scope.onFileChange = function (element) {
    const file = element.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = function (e) {
        $scope.$apply(function () {
          $scope.selectedImage = e.target.result; // Set the image source
        });
      };
      reader.readAsDataURL(file);
    }
  };

  function dataURLtoBlob(dataURL) {
    const byteString = atob(dataURL.split(",")[1]);
    const mimeString = dataURL.split(",")[0].split(":")[1].split(";")[0];
    const ab = new ArrayBuffer(byteString.length);
    const ia = new Uint8Array(ab);
    for (let i = 0; i < byteString.length; i++) {
      ia[i] = byteString.charCodeAt(i);
    }
    return new Blob([ab], { type: mimeString });
  }
  $scope.updateUserData = function () {
    // Kiểm tra dữ liệu cần thiết trước khi gửi yêu cầu PUT
    if (!$scope.user.email || !$scope.user.name) {
      alert("Vui lòng điền đầy đủ thông tin.");
      return;
    }

    // Regex kiểm tra số điện thoại (dành cho Việt Nam)
    const phoneRegex = /^0\d{9,10}$/;
    if (!phoneRegex.test($scope.user.phone)) {
      Swal.fire({
        title: "Số điện thoại không hợp lệ!",
        text: "Vui lòng nhập đúng định dạng số điện thoại (10-11 chữ số, bắt đầu bằng 0).",
        icon: "warning",
        confirmButtonText: "OK",
      });
      return;
    }

    // Regex kiểm tra họ tên đầy đủ (chỉ chứa chữ cái và khoảng trắng)
    const nameRegex = /^[a-zA-ZÀ-ỹ\s]+$/;
    if (!nameRegex.test($scope.user.name)) {
      Swal.fire({
        title: "Họ tên không hợp lệ!",
        text: "Họ tên chỉ được chứa chữ cái và khoảng trắng, không bao gồm số hoặc ký tự đặc biệt.",
        icon: "warning",
        confirmButtonText: "OK",
      });
      return;
    }

    const formData = new FormData();
    // Kiểm tra và thêm các tham số vào FormData
    if ($scope.user) {
      formData.append("name", $scope.user.name || "");
      formData.append("phone", $scope.user.phone || "");
      formData.append("urlProfileImage", $scope.user.urlProfileImage || "");
      // Thêm tệp tin vào FormData
      if ($scope.selectedImage) {
        const fileBlob = dataURLtoBlob($scope.selectedImage); // Chuyển đổi từ data URL sang Blob
        formData.append("file", fileBlob, "avatar.png"); // Gửi tệp tin với tên
      }
    }

    $scope.loading = 1;
    $http
      .put(`http://localhost:8080/api/v1/user/update/${token}`, formData, {
        transformRequest: angular.identity,
        headers: {
          "Content-Type": undefined, // Để Angular tự động xử lý Content-Type
          Authorization: `Bearer ${token}`, // Thêm token vào header
        },
      })
      .then((resp) => {
        $scope.user = resp.data;
        $scope.loading = 0;
        Swal.fire({
          title: "Cập nhật thông tin người dùng thành công!",
          icon: "success",
          confirmButtonText: "OK",
        });
      })
      .catch((err) => {
        $scope.loading = 0;
        console.error("Cập nhật thất bại", err);
        Swal.fire({
          title: "Cập nhật thất bại",
          text: err.data || "Có lỗi xảy ra!",
          icon: "error",
          confirmButtonText: "OK",
        });
      });
  };


  // $scope.isLoggedIn = !!token; // Kiểm tra xem người dùng đã đăng nhập hay chưa
  // $scope.logout = function () {
  //   $window.sessionStorage.removeItem("jwt_token"); // Xóa JWT token khỏi sessionStorage
  //   $scope.isLoggedIn = false; // Cập nhật trạng thái đăng nhập

  //   window.location.href = "http://127.0.0.1:5500/index.html#!/assets/views/dang_nhap";
  // };



};
app.controller("demothongtinuserController", demothongtinuserController);
// thêm xong token
