const detail_courseController = function (
  $scope,
  $http,
  $location,
  $rootScope, $window
) {
  // const userId = 2;
  // const base_url = `http://localhost:8080/api/v1/cart/user/${userId}`;
  const token = $window.sessionStorage.getItem("jwt_token");
  const tokenUnLogin =
    "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImxlaG9hbmdodXljb2RlckBnbWFpbC5jb20iLCJzdWIiOiJsZWhvYW5naHV5Y29kZXJAZ21haWwuY29tIiwiZXhwIjoxNzM5NDE2NTMxfQ.HV8XuG-nsjmmq8RtlXP-eNGoKcvr5qpQkS9dstpX0xM";
  console.log("Token tại trang chi tiết của tôi: token" + token);   // Thay thế bằng token thực tế của bạn;
  let host = "http://localhost:8080/api";
  $scope.course = {};
  $scope.relatedCourses = [];
  $scope.relatedCourses = []; // Mảng để chứa các khóa học liên quan - KHOA
  $scope.listYeuThich = [];
  $scope.listPhanHoi = {};
  $scope.listComment = [];
  $scope.checkRegisteredCourses = null;
  $scope.hashtags = []; // Khởi tạo mảng hashtags - KHOA
  $scope.showHashtags = false; // Kiểm soát khả năng hiển thị của hashtags - KHOA
  $scope.showRelatedCourses = false; // Kiểm soát khả năng hiển thị của hashtags - KHOA
  // Lấy id từ URL query string
  let id = $location.search().id; // Đây là cách lấy id từ URL query string
  // Biến để kiểm tra khóa học có trong danh sách yêu thích hay không
  //
  $scope.gotoLearn = function (courseId) {
    $('#exampleModal').modal('hide'); // Đóng modal nếu nó đang mở
    if (courseId) {
        $location.path('/assets/views/user/hoc_bai').search({
            id: courseId
        });
        window.scrollTo(0, 0); // Cuộn lên đầu trang
    }
};
  if (!id) {
    console.error("ID khóa học không được truyền vào!");
    return;
  }
  // định dạng tiền tệ
  $scope.formatCurrency = function (value) {
    return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ".") + " VND";
  };
  // load khóa học
  $scope.load_course = function () {
    if (token == null) {
      let urlKhoaHoc = `${host}/course/course-detail/${id}`;
      urlCheckRegistered = `${host}/course/checkRegisteredCourses?courseId=${id}`; // Chỉ gửi courseId, userId lấy từ token phía backend
      $http
        .get(urlKhoaHoc, {
          headers: {
            Authorization: `Bearer ${tokenUnLogin}`,
          },
        })
        .then((resp) => {
          $scope.course = resp.data;
          $scope.checkIfCourseIsFavorite($scope.course.courseId); // Kiểm tra khóa học có trong danh sách yêu thích
        })
        .catch((err) => {
          console.log("Error", err);
        });
    } else {
      let urlKhoaHoc = `${host}/course/course-detail/${id}`;
      $http
        .get(urlKhoaHoc, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then((resp) => {
          $scope.course = resp.data;
          $scope.checkIfCourseIsFavorite($scope.course.courseId); // Kiểm tra khóa học có trong danh sách yêu thích
          $http
                .get(`${host}/course/checkRegisteredCourses?courseId=${id}`, {
                  headers: {
                    Authorization: `Bearer ${token}`,
                  },
                })
                .then((resp) => {
                    $scope.checkRegisteredCourses = resp.data; // API trả về true/false
                })
                .catch((err) => {
                    console.error("Error checking registered courses", err);
                    $scope.checkRegisteredCourses = false; // Xử lý lỗi, mặc định chưa đăng ký
                });
        })
        .catch((err) => {
          console.log("Error", err);
        });
    }

  };
  $scope.registeredCourses = [];
  $scope.loadCuaToi = function () {
    $http.get(`http://localhost:8080/api/course/regiteredCourse/${token}`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    }).then(resp => {
        const courses = resp.data;
        console.log("Courses loaded:", courses); // Kiểm tra dữ liệu trả về
        if (courses.length === 0) {
            $scope.noResults = true;
            $scope.totalCourses = 0;
            $scope.registeredCourses = [];
        } else {
            $scope.noResults = false;
            $scope.registeredCourses = courses;
            $scope.totalCourses = courses.length;
            $scope.totalPages = Math.ceil($scope.totalCourses / $scope.itemsPerPage);
        } 
        console.log("regisr",$scope.registeredCourses);
        
    }).catch(err => {
        console.error("Lỗi không thể tải dữ liệu", err);
    });
};

$scope.loadCuaToi();
$scope.isCourseRegistered = function(courseId) {
  return $scope.registeredCourses.some(course => course.courseId === courseId);
  
};

  // Hàm kiểm tra khóa học có trong danh sách yêu thích
  // Hàm kiểm tra khóa học có trong danh sách yêu thích
  $scope.isFavorite = false; // Biến kiểm tra khóa học có yêu thích hay không

  $scope.checkIfCourseIsFavorite = function (courseId) {
    $scope.isCourseFavorite = $scope.listYeuThich.some(
      (course) => course.courseId === courseId
    );
  };

  $scope.checkIfCourseIsFavorite();
  // load danh sách comment
  $scope.loadListComment = function () {
    // Gọi API với $http
    if (token == null) {
      $http({
        method: "GET",
        //url: `http://localhost:8080/api/comment/course/${id}` // URL API lấy danh sách khóa học
        url: `http://localhost:8080/api/reply/courseId/${id}`,
        headers: {
          Authorization: `Bearer ${tokenUnLogin}`,
        },
      }).then(
        function successCallback(response) {
          // Khi thành công, lưu dữ liệu vào biến DanhSachKhoaHoc
          $scope.listComment = response.data;
          console.log("List comment", $scope.listComment);
        },
        function errorCallback(error) {
          // Xử lý khi có lỗi
          console.error("Lỗi khi gọi API:", error);
          alert("Không thể lấy dữ liệu khóa học. Vui lòng thử lại!");
        }
      );
    } else {
      $http({
        method: "GET",
        //url: `http://localhost:8080/api/comment/course/${id}` // URL API lấy danh sách khóa học
        url: `http://localhost:8080/api/reply/courseId/${id}`,
        headers: {
          Authorization: `Bearer ${token}`,
        },
      }).then(
        function successCallback(response) {
          // Khi thành công, lưu dữ liệu vào biến DanhSachKhoaHoc
          $scope.listComment = response.data;
          console.log("List comment", $scope.listComment);
        },
        function errorCallback(error) {
          // Xử lý khi có lỗi
          console.error("Lỗi khi gọi API:", error);
          alert("Không thể lấy dữ liệu khóa học. Vui lòng thử lại!");
        }
      );
    }

  };
  // thêm comment

  // Hàm để tải hashtags theo courseId
  $scope.loadHashtag = function () {
    if (token == null) {
      console.log("courseId:", id); // Kiểm tra courseId
      $http
        .get(`http://localhost:8080/api/hashtag/${id}`, {
          headers: {
            Authorization: `Bearer ${tokenUnLogin}`,
          },
        })
        .then(function (resp) {
          console.log("Dữ liệu từ API:", resp.data); // Kiểm tra dữ liệu API trả về
          if (Array.isArray(resp.data)) {
            // Kiểm tra dữ liệu trả về là một mảng
            $scope.hashtags = resp.data; // Thiết lập dữ liệu phản hồi vào mảng hashtags
            $scope.showHashtags = true; // Đảm bảo rằng hashtags được hiển thị
          } else {
            console.log("Dữ liệu trả về không phải là mảng", resp.data);
          }
        })
        .catch(function (err) {
          console.log("Lỗi không thể tải dữ liệu", err); // Ghi log bất kỳ lỗi nào
        });
    } else {
      console.log("courseId:", id); // Kiểm tra courseId
      $http
        .get(`http://localhost:8080/api/hashtag/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then(function (resp) {
          console.log("Dữ liệu từ API:", resp.data); // Kiểm tra dữ liệu API trả về
          if (Array.isArray(resp.data)) {
            // Kiểm tra dữ liệu trả về là một mảng
            $scope.hashtags = resp.data; // Thiết lập dữ liệu phản hồi vào mảng hashtags
            $scope.showHashtags = true; // Đảm bảo rằng hashtags được hiển thị
          } else {
            console.log("Dữ liệu trả về không phải là mảng", resp.data);
          }
        })
        .catch(function (err) {
          console.log("Lỗi không thể tải dữ liệu", err); // Ghi log bất kỳ lỗi nào
        });
    }

  };
  //
  $scope.loadRelatedCoursesKhoa = function () {
    if (token == null) {
      $http
        .get(`http://localhost:8080/api/hashtag/hashtagKhoa/${id}`, {
          headers: {
            Authorization: `Bearer ${tokenUnLogin}`,
          },
        })
        .then(function (resp) {
          // Giả sử bạn muốn lọc lại các khóa học chỉ có hashtagId nhất định
          $scope.listCourseOfHashtag = resp.data;
          console.log("Hashtag:", $scope.listCourseOfHashtag);
        })
        .catch(function (err) {
          console.log("Lỗi không thể tải khóa học liên quan", err);
        });
    } else {
      $http
        .get(`http://localhost:8080/api/hashtag/hashtagKhoa/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then(function (resp) {
          // Giả sử bạn muốn lọc lại các khóa học chỉ có hashtagId nhất định
          $scope.listCourseOfHashtag = resp.data;
          console.log("Hashtag:", $scope.listCourseOfHashtag);
        })
        .catch(function (err) {
          console.log("Lỗi không thể tải khóa học liên quan", err);
        });
    }

  };
  // Phản hồi comment người dùng
  $scope.loadPhanHoi = function () {
    // Lấy commentId từ scope
    $http
      .get(`http://localhost:8080/api/reply/${id}`)
      .then((resp) => {
        console.log("Phản hồi cho commentId", resp.data);
        $scope.listPhanHoi = resp.data; // Lưu phản hồi vào biến listPhanHoi
      })
      .catch((err) => {
        console.log("Lỗi không thể tải dữ liệu", err);
      });
  };
  // nút mua ngay
  $scope.BuyNow = function (courseId) {
    // Kiểm tra token trước khi gửi yêu cầu
    if (!token) {
      Swal.fire({
        title: "Vui lòng đăng nhập để mua khóa học",
        icon: "warning",
      });
      return; // Nếu không có token, dừng lại
    }
    // Khởi tạo don_hang nếu chưa có
    if (!$rootScope.don_hang) {
      $rootScope.don_hang = [];
    }
    console.log($scope.course);
    $rootScope.don_hang.push($scope.course);
    $location.path("/assets/views/payment/don_hang");
  };
  // thêm khóa học vào giỏ hàng
  $scope.addCourseToCart = function (courseParam) {
    // Kiểm tra token trước khi gửi yêu cầu
    if (!token) {
        Swal.fire({
            title: "Vui lòng đăng nhập để thêm khóa học vào giỏ hàng!",
            icon: "warning"
        });
        return; // Nếu không có token, dừng lại
    }
    $http.post(`http://localhost:8080/api/v1/cart/addCourse/${courseParam}`, {}, {
        headers: {
            Authorization: `Bearer ${token}`,
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
  $scope.toggleFavoriteCourse = function (courseId) {
    // Giải mã token để lấy thông tin userId
    const decodedToken = JSON.parse(atob(token.split('.')[1]));  // Giải mã payload từ token
    const userId = decodedToken.userId;  // Lấy userId từ payload của token

    const isAlreadyFavorite = $scope.listYeuThich.some(
      (course) => course.courseId === courseId
    );

    if (isAlreadyFavorite) {
      // Nếu khóa học đã có trong yêu thích, gọi API xóa khóa học
      $http
        .delete(
          `http://localhost:8080/api/v1/favorite_course/delete/${courseId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
        .then((resp) => {
          console.log("Khóa học đã bị xóa khỏi yêu thích", resp.data);
          // Cập nhật lại danh sách yêu thích sau khi xóa
          $scope.loadYeuThich();
          // Kiểm tra lại xem khóa học có trong danh sách yêu thích không
          $scope.checkIfCourseIsFavorite(courseId);
          Swal.fire({
            title: "Khóa học đã bị xóa khỏi yêu thích!",
            icon: "success",
          });
        })
        .catch((err) => {
          console.log("Lỗi khi xóa khóa học khỏi yêu thích", err);
          Swal.fire({
            title: "Lỗi khi xóa khóa học khỏi yêu thích!",
            icon: "error",
          });
        });
    } else {
      // Nếu khóa học chưa có trong yêu thích, gọi API thêm khóa học vào yêu thích
      $http
        .post(
          `http://localhost:8080/api/v1/favorite_course/add/${courseId}`,
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
        .then((resp) => {
          console.log("Khóa học đã được thêm vào yêu thích", resp.data);
          // Cập nhật lại danh sách yêu thích sau khi thêm
          $scope.loadYeuThich();
          // Kiểm tra lại xem khóa học có trong danh sách yêu thích không
          $scope.checkIfCourseIsFavorite(courseId);
          Swal.fire({
            title: "Khóa học đã được thêm vào yêu thích!",
            icon: "success",
          });
        })
        .catch((err) => {
          console.log("Lỗi khi thêm khóa học vào yêu thích", err);
          Swal.fire({
            title: "Lỗi khi thêm khóa học vào yêu thích!",
            icon: "error",
          });
        });
    }
  };
  $scope.isFavorite = function (courseId) {
    return $scope.listYeuThich.some((course) => course.id === courseId);
  };

  // thêm khóa học vào danh sách yêu thích

  // thêm khóa họ vào danh sách khóa học của tôi
  $scope.addCourseToMyListCourse = function (courseId) {

    console.log("Token:", token);
    // Kiểm tra token trước khi gửi yêu cầu
    if (!token) {
      Swal.fire({
        title: "Vui lòng đăng nhập để tham gia khóa học!",
        icon: "warning",
      });
      return; // Nếu không có token, dừng lại
    }

    // Gửi yêu cầu thêm khóa học vào giỏ hàng
    $http
      .post(
        `http://localhost:8080/api/v1/cart/addCourseInMyList/${courseId}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`, // Đảm bảo rằng token được gửi đúng cách
          },
        }
      )
      .then((resp) => {
        console.log("Đăng ký khóa học thành công", resp.data); // Sử dụng resp.data
        Swal.fire({
          title: "Đăng ký khóa học thành công!",
          icon: "success",
        });
        $location.path("/assets/views/user/khoahocyeuthich");
        window.scrollTo(0, 0);
      })
      .catch((err) => {
        console.log("Lỗi không thể đăng ký khóa học", err);

        // Kiểm tra nếu lỗi từ backend cho biết khóa học đã có trong giỏ
        if (err.response && err.response.status === 400) {
          Swal.fire({
            title:
              "Đăng ký khóa học thất bại, khóa học đã có trong danh sách khóa học của bạn!",
            icon: "error",
          });
        } else {
          Swal.fire({
            title: "Đã có lỗi xảy ra khi đăng ký khóa học!",
            icon: "error",
          });
        }
      });
  };
  // load danh sách khóa học yêu thích
  $scope.loadYeuThich = function () {
    $http
      .get(`http://localhost:8080/api/v1/favorite_course/user/${token}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((resp) => {
        // Gán dữ liệu vào listYeuThich
        $scope.listYeuThich = resp.data;

        // Log dữ liệu của listYeuThich ra console
        console.log("Dữ liệu yêu thích:", $scope.listYeuThich);
      })
      .catch((err) => {
        console.log("Lỗi không thể tải dữ liệu", err);
      });
  };
  // chuyển đến trang chi tiết khóa học
  $scope.gotoCourseDetail = function (courseId) {
    $location
      .path("/assets/views/user/chi_tiet_khoa_hoc")
      .search({ id: courseId });
    window.scrollTo(0, 0);
  };
  // chuyển đến trang danh mục
  $scope.gotoDanhMucKhoaHoc = function () {
    $location.path("/assets/views/user/danh_muc_khoa_hoc");
    window.scrollTo(0, 0); // Cuộn lên đầu trang
  };
  // chuyển đến trang danh mục theo hashtag
  $scope.load_course_hashtag = function (hashTagId) {
    if (token == null) {
      console.log(hashTagId);
      let urlKhoaHoc = `http://localhost:8080/api/course/findCourseHashTag/${hashTagId}`;
      $http
        .get(urlKhoaHoc, {
          headers: {
            Authorization: `Bearer ${tokenUnLogin}`,
          },
        })
        .then((resp) => {
          $rootScope.listKhoaHoc = resp.data; // Lưu khóa học vào $rootScope
          $location
            .path("/assets/views/user/danh_muc_khoa_hoc")
            .search({ hashTagId: hashTagId });
          window.scrollTo(0, 0);
        })
        .catch((err) => {
          console.log("Error", err);
        });
    } else {
      console.log(hashTagId);
      let urlKhoaHoc = `http://localhost:8080/api/course/findCourseHashTag/${hashTagId}`;
      $http
        .get(urlKhoaHoc, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then((resp) => {
          $rootScope.listKhoaHoc = resp.data; // Lưu khóa học vào $rootScope
          $location
            .path("/assets/views/user/danh_muc_khoa_hoc")
            .search({ hashTagId: hashTagId });
          window.scrollTo(0, 0);
        })
        .catch((err) => {
          console.log("Error", err);
        });
    }
  };
  // --- TEST - HBao ---
  // Lấy phần tử video
  // const progressBar = document.getElementById('progressBar');
  // const video = document.getElementById('myVideo');

  // let lastAllowedTime = 0; // Lưu thời gian đã xem được cho phép tua tới
  // let isSeekingForward = false; // Để kiểm tra xem có đang tua về sau hay không

  // // Lắng nghe sự kiện 'seeking' để kiểm tra hướng tua
  // video.addEventListener('seeking', () => {
  //     if (video.currentTime > lastAllowedTime) {
  //         // Nếu người dùng cố tua về sau
  //         isSeekingForward = true;
  //         video.currentTime = 0; // Đặt lại thời gian về 0
  //     } else {
  //         // Nếu người dùng tua về trước, cho phép
  //         isSeekingForward = false;
  //     }
  // });

  // // Lắng nghe sự kiện 'timeupdate' để cập nhật tiến độ
  // video.addEventListener('timeupdate', () => {
  //     // Nếu không phải tua về sau, cập nhật thời gian đã xem
  //     if (!isSeekingForward) {
  //         lastAllowedTime = Math.max(lastAllowedTime, video.currentTime);
  //     }

  //     // Tính toán tiến độ dựa trên thời gian đã xem (không tính tua về sau)
  //     const progress = (lastAllowedTime / video.duration) * 100;

  //     // Gán giá trị tiến độ vào thuộc tính --progress
  //     progressBar.style.setProperty('--progress', progress.toFixed(0));

  //     // Kiểm tra nếu tiến độ đạt 100% và thay đổi màu nền
  //     if (progress === 100) {
  //         progressBar.style.backgroundColor = 'MediumAquamarine'; // Đổi màu khi tiến độ đạt 100%
  //     } else {
  //         progressBar.style.backgroundColor = 'IndianRed'; // Màu mặc định khi tiến độ chưa đạt 100%
  //     }
  // });

  // // Đặt sự kiện 'seeked' để ngăn người dùng tua về sau (bổ sung an toàn)
  // video.addEventListener('seeked', () => {
  //     if (video.currentTime > lastAllowedTime) {
  //         video.currentTime = 0; // Đặt lại thời gian về 0 nếu tua về sau
  //     }
  // });

  // --- hết phần test ---

  // Khởi tạo tải khóa học và mục lục
  $scope.loadHashtag();
  $scope.load_course();
  $scope.loadYeuThich();
  // $scope.loadPhanHoi();
  $scope.loadListComment();
  $scope.loadRelatedCoursesKhoa();
  // $scope.load_related_courses(); // Gọi hàm tải khóa học liên quan
};
app.controller("detail_courseController", detail_courseController);
