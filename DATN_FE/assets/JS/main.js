const app = angular.module("myapp", ["ngRoute"]);

app.config(function ($routeProvider) {
  $routeProvider
    // Trang thường//////////////////////////////////////////
    .when("/", {
      templateUrl: "assets/views/home.html",
      controller: "HomeController",
    })
    //Test//////////////////////////////////////////////////////
    .when("/assets/views/test_chuyen_trang", {
      templateUrl: "assets/views/test_chuyen_trang.html",
    })
    //TestH/////////////////////////////////////////////////////
    .when("/assets/views/header_khoa", {
      templateUrl: "assets/views/header_khoa.html",
      controller: "headerKhoaController",
    })
    //Admin/////////////////////////////////////////////////
    .when("/assets/views/admin/thong_ke", {
      templateUrl: "assets/views/admin/thong_ke.html",
      controller: "StatisticalController",
    })
    .when("/assets/views/admin/course_manager", {
      templateUrl: "assets/views/admin/course_manager.html",
      controller: "CourseManagerController",
    })
    .when("/assets/views/admin/course_manager_detail/:courseId", {
      templateUrl: "assets/views/admin/course_manager_detail.html",
      controller: "CourseManagerDetailController",
    })
    .when(
      "/assets/views/admin/course_manager_detail/:courseId/section/:sectionId",
      {
        templateUrl: "assets/views/admin/section_detail_manager.html",
        controller: "SectionManagerController",
      }
    )
    .when("/assets/views/admin/quan_ly_khuyen_mai", {
      templateUrl: "assets/views/admin/quan_ly_khuyen_mai.html",
      controller: "SalesController",
    })
    .when("/assets/views/admin/quan_ly_danh_muc", {
      templateUrl: "assets/views/admin/quan_ly_danh_muc.html",
      controller: "CategoryManagerController",
    })
    .when("/assets/views/payment/don_hang", {
      templateUrl: "assets/views/payment/don_hang.html",
      controller: "donHangController",
    })
    //User////////////////////////////////////////////////////////
    .when("/assets/views/user/hoc_bai", {
      templateUrl: "assets/views/user/hoc_bai.html",
      controller : "learnController",
    })
    .when("/assets/views/user/khoahocyeuthich", {
      templateUrl: "assets/views/user/khoahocyeuthich.html",
      controller : "favorite_courseController",
    })
    .when("/assets/views/user/testmyvoucher", {
      templateUrl: "assets/views/user/testmyvoucher.html",
      controller: "MyVoucherController",
    })
    .when("/assets/views/user/gio_hang", {
      templateUrl: "assets/views/user/gio_hang.html",
      controller: "HBaoCartCourseController",
    })
    .when("/assets/views/user/chi_tiet_khoa_hoc", {
      templateUrl: "assets/views/user/chi_tiet_khoa_hoc.html",
      controller: "detail_courseController",
    })
    .when("/assets/views/user/quiz", {
      templateUrl: "assets/views/user/quiz.html",
    })
    .when("/assets/views/user/demo_thong_tin_user", {
      templateUrl: "assets/views/user/demo_thong_tin_user.html",
      controller: "demothongtinuserController",
    })
    .when("/assets/views/user/checkout", {
      templateUrl: "assets/views/user/checkout.html",
      controller: "CheckoutController",
    })
    .when("/assets/views/user/course", {
      templateUrl: "assets/views/user/course.html",
      controller: "CourseController",
    })
    .when("/assets/views/user/danh_gia_khoa_hoc", {
      templateUrl: "assets/views/user/danh_gia_khoa_hoc.html",
    })
    .when("/assets/views/user/danh_muc_khoa_hoc", {
      templateUrl: "assets/views/user/danh_muc_khoa_hoc.html",
      controller: "HBaoCourseController",
    })
    .when("/assets/views/payment/thanh_toan_thanh_cong", {
      templateUrl: "assets/views/payment/thanh_toan_thanh_cong.html",
      controller: "HBaoCourseController",
    })
    .when("/assets/views/user/take_the_test", {
      templateUrl: "assets/views/user/take_the_test.html",
      controller: "takeTheTestController",
    })
    .when("/assets/views/payment/thanh_toan_thanh_cong", {
      templateUrl: "assets/views/payment/thanh_toan_thanh_cong.html",
      controller: "thanhToanThanhCongController",
    })
    .when("/assets/views/payment/thanh_toan_that_bai", {
      templateUrl: "assets/views/payment/thanh_toan_that_bai.html",
      controller: "thanhToanThatBaiController",
    })
    .when("/assets/views/dang_ky", {
      templateUrl: "assets/views/dang_ky.html",
      controller: "accountsController",
    })
    .when("/assets/views/dang_nhap", {
      templateUrl: "assets/views/dang_nhap.html",
      controller: "accountsController",
    })
    .when("/assets/views/admin/feedback", {
      templateUrl: "assets/views/admin/feedback.html",
      controller: "feedBackController",
    })
    .when("/assets/views/about", {
      templateUrl: "assets/views/about.html",
      controller: "feedBackController",
    })
    .when("/assets/views/FAQ", {
      templateUrl: "assets/views/FAQ.html",
    })
    .when("/assets/views/lien_he", {
      templateUrl: "assets/views/lien_he.html",
    })
    .when("/assets/views/user/dong_gop_y_kien", {
      templateUrl: "assets/views/user/dong_gop_y_kien.html",
      controller: "commentController",
    })
    .when("/assets/views/admin/userAdmin", {
      templateUrl: "assets/views/admin/userAdmin.html",
      controller: "userAdminKhoaController",
    })
    // Nếu như không phải các link trên thì hiển thị trang home -->otherwise////////////////////
    .otherwise({
      templateUrl: "assets/views/home.html",
      controller: "HomeController",
    });
});

//Cấu hình tiền tệ Việt Nam cho Input 100.000đ
/* <input
  ng-model="course.price"
  type="text"
  class="form-control"
  id="coursePrice"
  placeholder="Nhập giá"
  currency-input // Thêm dòng này là được
/>; */
app.directive("currencyInput", function () {
  return {
    restrict: "A",
    require: "ngModel",
    link: function (scope, element, attrs, ngModel) {
      // Hàm chuyển đổi giá trị từ model sang view
      ngModel.$formatters.push(function (value) {
        return formatCurrency(value);
      });

      // Hàm chuyển đổi giá trị từ view về model
      ngModel.$parsers.push(function (value) {
        // Xóa các ký tự không phải số
        var numberValue = value.replace(/[^0-9]/g, "");
        ngModel.$setViewValue(numberValue);
        ngModel.$render();
        return numberValue;
      });

      // Định dạng giá trị thành tiền tệ
      function formatCurrency(value) {
        if (!value) return "";
        var numberValue = parseFloat(value);
        if (isNaN(numberValue)) return "";
        return numberValue.toLocaleString("vi-VN", {
          style: "currency",
          currency: "VND",
        });
      }

      // Cập nhật giá trị khi người dùng nhập
      element.on("blur", function () {
        var value = ngModel.$modelValue;
        element.val(formatCurrency(value));
      });

      element.on("focus", function () {
        var value = ngModel.$modelValue;
        element.val(value);
      });
    },
  };
});

app.run(function ($rootScope) {
  $rootScope.$on("$routeChangeStart", function () {
    $rootScope.loading = true;
  });
  $rootScope.$on("$routeChangeSuccess", function () {
    $rootScope.loading = false;
  });
  $rootScope.$on("$routeChangeError", function () {
    $rootScope.loading = false;
    alert("Lỗi");
  });
});
