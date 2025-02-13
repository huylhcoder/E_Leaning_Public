const CourseManagerController = function ($scope, $http, $location, $window) {
  // const token =
  //   "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3Q2QGdtYWlsLmNvbSIsInN1YiI6InRlc3Q2QGdtYWlsLmNvbSIsImV4cCI6MTczNDM1NjU3NH0.YU7eEm8uyw1Y-uDI5nmgsrJ00S101lYr-oZEZkXZ8eg"; // Thay thế bằng token thực tế của bạn;
  const token = $window.sessionStorage.getItem("jwt_token");
  //
  const base_url = "http://localhost:8080/api/v1/course-manager";
  //
  $scope.chuyenTrangLink = function (value_url) {
    if (value_url == "listCourse_url") {
      $window.location.href = `http://127.0.0.1:5500/indexAdmin.html#!/assets/views/admin/course_manager`; // Thực hiện chuyển trang
    }
    if (value_url == "listSection_url") {
      $window.location.href = `http://127.0.0.1:5500/indexAdmin.html#!/assets/views/admin/course_manager_detail/${$scope.courseId}`; // Thực hiện chuyển trang
    }
    if (value_url == "sectionDetail_url") {
      $window.location.href = `http://127.0.0.1:5500/indexAdmin.html#!/assets/views/admin/course_manager_detail/${$scope.courseId}/section/${$scope.sectionId}`; // Thực hiện chuyển trang
    }
  };

  // làm tròn giá trị
  $scope.formatDuration = function (duration) {
    var hours = Math.floor(duration / 3600);
    var minutes = Math.floor((duration % 3600) / 60);
    var seconds = Math.round(duration % 60);

    if (hours == 0 && minutes == 0) {
      durationRound = Math.round(seconds);
      return durationRound + " giây";
    }

    if (hours > 0) {
      return hours + " giờ " + minutes + " phút " + seconds + " giây";
    } else {
      return minutes + " phút " + seconds + " giây";
    }
  };

  //
  $scope.listKhoaHocDaDang = [];
  $scope.showListKhoaHocDaDang = function () {
    $http
      .get(`${base_url}/posted-course`)
      .then((resp) => {
        $scope.listKhoaHocDaDang = resp.data;
        console.log("Khóa học đã đăng: " + $scope.listKhoaHocDaDang);
      })
      .catch((err) => {
        console.error("Lỗi khóa học đã đăng", err);
      });
  };
  $scope.showListKhoaHocDaDang();
  //
  $scope.listKhoaHocNhap = [];
  $scope.showListKhoaHocNhap = function () {
    $http
      .get(`${base_url}/draft-course`)
      .then((resp) => {
        $scope.listKhoaHocNhap = resp.data;
        console.log("Khóa học nháp: " + $scope.listKhoaHocNhap);
      })
      .catch((err) => {
        console.error("Lỗi khóa học nháp", err);
      });
  };
  $scope.showListKhoaHocNhap();
  //
  $scope.removeDraftCourse = function (courseId) {
    console.log(courseId);
    $http
      .delete(`${base_url}/draft-course/remove-course/${courseId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((resp) => {
        //Hiển thị tại danh sách khóa học nháp sau khi đã xóa
        $scope.showListKhoaHocNhap();
        Swal.fire({
          title: "Xóa thành công",
          icon: "success",
          confirmButtonText: "OK",
        });
      })
      .catch((err) => {
        // Hiển thị thông báo lỗi cho người dùng
        console.error("Lỗi khi xóa khóa học nháp:", err);
        alert("Có lỗi xảy ra khi xóa khóa học nháp. Vui lòng thử lại.");
      });
  };
  //
  $scope.themMoiKhoaHoc = function () {
    $http
      .post(`${base_url}`)
      .then((resp) => {
        $scope.idKhoaHocMoi = resp.data;
        console.log("Id khóa học mới: " + $scope.idKhoaHocMoi);
        // Chuyển hướng đến trang chi tiết khóa học với ID mới tạo
        $location.path(
          `/assets/views/admin/course_manager_detail/${$scope.idKhoaHocMoi}`
        );
      })
      .catch((err) => {
        console.error("Lỗi khi thêm mới khóa học", err);
      });
  };
};

app.controller("CourseManagerController", CourseManagerController);
