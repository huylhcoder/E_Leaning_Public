const CourseManagerDetailController = function (
  $scope,
  $http,
  $location,
  $routeParams,
  $window
) {
  const base_url = "http://localhost:8080/api/v1/course-manager-detail";
  const category_url = "http://localhost:8080/api/category";
  const level_url = "http://localhost:8080/api/v1/course-level";
  const section_url = "http://localhost:8080/api/v1/section-manager/course";
  // const token =
  //   "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3Q2QGdtYWlsLmNvbSIsInN1YiI6InRlc3Q2QGdtYWlsLmNvbSIsImV4cCI6MTczNDM1NjU3NH0.YU7eEm8uyw1Y-uDI5nmgsrJ00S101lYr-oZEZkXZ8eg"; // Thay thế bằng token thực tế của bạn
  const token = $window.sessionStorage.getItem("jwt_token");
  $scope.courseId = $routeParams.courseId;
  console.log("Log ra nè: " + $scope.courseId);

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

  // Hiển thị ảnh mới được chọn
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

  // Lấy danh sách danh mục
  $scope.listCategory = [];
  $scope.showListCategory = function () {
    $http
      .get(`${category_url}`)
      .then((resp) => {
        $scope.listCategory = resp.data;
        console.log("Show danh mục", $scope.listCategory);
      })
      .catch((err) => {
        console.error("Show danh mục", err);
      });
  };
  $scope.showListCategory();

  // Lấy danh sách độ khó
  $scope.listLevel = [];
  $scope.showListLevel = function () {
    $http
      .get(`${level_url}`)
      .then((resp) => {
        $scope.listLevel = resp.data;
        console.log("Show level", $scope.listLevel);
      })
      .catch((err) => {
        console.error("Show level", err);
      });
  };
  $scope.showListLevel();

  // Hiển thị thông tin khóa học
  $scope.course = {};

  $scope.showCourseDetail = function () {
    $http
      .get(`${base_url}/${$scope.courseId}`)
      .then((resp) => {
        $scope.course = resp.data;
        console.log("Show khóa học", $scope.course);
      })
      .catch((err) => {
        console.error("Show khóa học", err);
      });
  };
  $scope.showCourseDetail();

  // Hiển thị danh sách phần
  $scope.listSection = [];
  $scope.showListSection = function () {
    // Giả sử bạn đã có Bearer Token trong biến token
    $http
      .get(`${section_url}/${$scope.courseId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((resp) => {
        $scope.listSection = resp.data;
        console.log("Show section", $scope.listSection);
      })
      .catch((err) => {
        console.error("Show section", err);
      });
  };
  $scope.showListSection();

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

  // Cập nhật thông tin chi tiết khóa học
  $scope.luuThongTinChiTietKhoaHoc = function () {
    const formData = new FormData();

    // Kiểm tra và thêm các tham số vào FormData
    if ($scope.course) {
      formData.append("courseId", $scope.course.courseId || "");
      formData.append("name", $scope.course.name || "");
      formData.append("status", $scope.course.status || "");
      formData.append("description", $scope.course.description || "");
      formData.append("avatar", $scope.course.avatar || "");
      formData.append("price", $scope.course.price || "");
      formData.append("topic", $scope.course.topic || "");
      formData.append("categoryId", $scope.course.categoryId || "");
      formData.append("levelId", $scope.course.levelId || "");

      // Thêm tệp tin vào FormData
      if ($scope.selectedImage) {
        const fileBlob = dataURLtoBlob($scope.selectedImage); // Chuyển đổi từ data URL sang Blob
        formData.append("file", fileBlob, "avatar.png"); // Gửi tệp tin với tên
      }
    }

    $http
      .put(`${base_url}/${$scope.courseId}`, formData, {
        transformRequest: angular.identity,
        headers: { "Content-Type": undefined }, // Để Angular tự động xử lý Content-Type
      })
      .then((resp) => {
        $scope.course = resp.data;
        Swal.fire({
          title: "Cập nhật thông tin khóa học",
          icon: "success",
          confirmButtonText: "OK",
        });
      })
      .catch((err) => {
        console.error("Cập nhật thất bại", err);
        Swal.fire({
          title: "Cập nhật thất bại",
          text: err.data || "Có lỗi xảy ra!",
          icon: "error",
          confirmButtonText: "OK",
        });
      });
  };

  // Hàm chuyển đổi data URL sang Blob
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

  //Thêm section mới và mở form section
  $scope.themMoiSection = function (sectionId) {
    console.log(token);
    $http
      .post(
        `${section_url}/${$scope.courseId}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      )
      .then((resp) => {
        $scope.idSectionMoi = resp.data;
        console.log("Id phần mới: " + resp.data);
        console.log("Id phần mới: " + $scope.idSectionMoi);
        // Chuyển hướng đến trang chi tiết khóa học với ID mới tạo
        $location.path(
          `/assets/views/admin/course_manager_detail/${$scope.courseId}/section/${$scope.idSectionMoi}`
        );
      })
      .catch((err) => {
        console.error("Lỗi khi thêm mới khóa học", err);
      });
  };

  $scope.removeSection = function (sectionId) {
    console.log(sectionId);
    $http
      .delete(`${base_url}/remove-section/${sectionId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((resp) => {
        //Hiển thị lại danh sách phần
        $scope.showListSection();
        Swal.fire({
          title: "Xóa thành công",
          icon: "success",
          confirmButtonText: "OK",
        });
      })
      .catch((err) => {
        // Hiển thị thông báo lỗi cho người dùng
        console.error("Lỗi khi xóa phần:", err);
        alert("Có lỗi xảy ra khi xóa phần. Vui lòng thử lại.");
      });
  };
};

app.controller("CourseManagerDetailController", CourseManagerDetailController);
