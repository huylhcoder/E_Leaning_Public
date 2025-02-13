const accountsController = function ($scope, $http, $location, $window) {
  const base_url = "http://localhost:8080/api/v1/users";
  const token = $window.sessionStorage.getItem("jwt_token");
  console.log("Token đang được lưu " + token);

  $scope.loading = false;
  $scope.userRegisterDTO = {
    fullname: "",
    email: "",
    password: "",
    retype_password: "",
  };

  $scope.passwordFieldType = "password"; // Mặc định là password
  $scope.togglePasswordVisibility = function () {
    $scope.passwordFieldType =
      $scope.passwordFieldType === "password" ? "text" : "password";
  };

  $scope.checkUser = function () {
    $scope.loading = 1;
    const userRegisterDTO = {
      fullname: $scope.userRegisterDTO.fullname,
      email: $scope.userRegisterDTO.email,
      password: $scope.userRegisterDTO.password,
      retype_password: $scope.userRegisterDTO.retypePassword,
    };
    $http
      .post(`${base_url}/check-user`, userRegisterDTO)
      .then(function (response) {
        console.log("Check user: " + response);
        $scope.dangKy(); //Thực hiện đăng ký nếu như pass
        $scope.loading = 0;
      })
      .catch(function (err) {
        console.log(err);
        Swal.fire("Lỗi khi đăng ký", err.data.message, "error");
        $scope.loading = 0;
      });
  };

  $scope.dangKy = function () {
    $scope.loading = 1;
    // Gửi mã xác nhận đến email
    $http
      .post(`${base_url}/send-verification-code`, $scope.userRegisterDTO.email)
      .then(function (response) {
        let code = response.data;
        console.log(code);
        // Hiển thị SweetAlert2 để yêu cầu người dùng nhập mã xác nhận
        Swal.fire({
          title: "Nhập mã xác nhận",
          input: "text",
          confirmButtonText: "Xác nhận",
          preConfirm: (verificationCode) => {
            // Kiểm tra người dùng chưa nhập code
            if (!verificationCode) {
              Swal.showValidationMessage("Vui lòng nhập mã xác nhận");
              return;
            }

            // Kiểm tra nhập code đúng không
            if (verificationCode == code) {
              $scope.loading = 1;
              const userRegisterDTO = {
                fullname: $scope.userRegisterDTO.fullname,
                email: $scope.userRegisterDTO.email,
                password: $scope.userRegisterDTO.password,
                retype_password: $scope.userRegisterDTO.retypePassword,
              };
              console.log(userRegisterDTO);

              // Gọi API đăng ký
              return $http
                .post(`${base_url}/register`, userRegisterDTO)
                .then(function (response) {
                  $scope.loading = 0;
                  console.log("Đăng ký " + response);
                  $location.path(`/assets/views/dang_nhap`);
                  // Hiển thị thông báo thành công
                  return Swal.fire({
                    title: "Tạo tài khoản thành công!",
                    icon: "success",
                    confirmButtonText: "OK",
                  });
                })
                .catch(function (err) {
                  console.error(err);
                  Swal.fire("Lỗi khi đăng ký", "Không thể đăng ký", "error");
                  $scope.loading = 0;
                });
            } else {
              Swal.showValidationMessage("Mã xác nhận không đúng.");
              $scope.loading = 0;
            }
          },
        }).then((result) => {
          $location.path(`/assets/views/dang_nhap`);
        });
      })
      .catch(function (error) {
        console.log(error);
        Swal.fire("Có lỗi xảy ra!", "Vui lòng thử lại.", "error");
        $scope.loading = 0;
        return;
      });
  };

  $scope.validationLogin = function () {
    $scope.error = false;
    $scope.emailError = "";
    $scope.passwordError = "";

    // Kiểm tra email không được bỏ trống
    if ($scope.email == null || $scope.email.trim() === "") {
      $scope.emailError = "Email không được bỏ trống";
      $scope.error = true;
    } else {
      // Kiểm tra định dạng email
      const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailPattern.test($scope.email)) {
        $scope.emailError = "Email không đúng định dạng";
        $scope.error = true;
      }
    }

    // Kiểm tra password không được bỏ trống
    if ($scope.password == null || $scope.password.trim() === "") {
      $scope.passwordError = "Password không được bỏ trống";
      $scope.error = true;
    }

    // Nếu có lỗi xảy ra, không thực hiện tiếp
    if ($scope.error) {
      return true; // Trả về true để chỉ ra rằng có lỗi
    }
    return false; // Trả về false nếu không có lỗi
  };

  $scope.login = function () {
    // Bắt lỗi đăng nhập
    if ($scope.validationLogin()) {
      return; // Nếu có lỗi, không thực hiện lệnh post
    }
    //Thêm user mới
    const userLoginDTO = {
      email: $scope.email,
      password: $scope.password,
    };

    $http
      .post(`${base_url}/login`, userLoginDTO)
      .then(function (response) {
        // Lưu JWT vào localStorage
        $window.sessionStorage.setItem("jwt_token", response.data.token); // Lấy token từ đối tượng
        // Lưu role vào sessionStorage hoặc biến toàn cục
        $window.sessionStorage.setItem("user_role", response.data.role);
        let token = $window.sessionStorage.getItem("jwt_token");
        console.log(token);

        // Chuyển trang sau khi đăng nhập thành công dựa trên vai trò
        if (response.data.role === "ADMIN") {
          window.location.href = "http://127.0.0.1:5500/indexAdmin.html";
          return Swal.fire({
            title: "Đăng nhập thành công tài khoản ADMIN",
            icon: "success",
            confirmButtonText: "OK",
          });
        } else if (response.data.role === "USER") {
          $location.path(`/assets/views/user/home`);
          return Swal.fire({
            title: "Đăng nhập thành công tài khoản USER",
            icon: "success",
            confirmButtonText: "OK",
          }).then(() => {
            window.location.reload(); // Reload lại trang
          });
        } else {
          $location.path(`/assets/views/default`);
        }

        Swal.fire({
          title: "Đăng nhập thành công",
          icon: "success",
          confirmButtonText: "OK",
        });
      })
      .catch(function (error) {
        Swal.fire(
          "Sai tài khoản hoặc mật khẩu",
          "Đăng nhập thất bại",
          "error"
        );
      });
  };
};

app.controller("accountsController", accountsController);
