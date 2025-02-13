const app = angular.module("myAppAdmin", ["ngRoute"]);

app.config(function ($routeProvider) {
  $routeProvider
    //Admin
    .when("/assets/views/admin/course_manager", {
      templateUrl: "assets/views/admin/course_manager.html",
      controller: "CourseManagerController",
    })
    .when("/assets/views/admin/course_manager_detail", {
      templateUrl: "assets/views/admin/course_manager_detail.html",
      controller: "CourseManagerDetailController",
    })
    .when("/assets/views/admin/quan_ly_khuyen_mai", {
      templateUrl: "assets/views/admin/quan_ly_khuyen_mai.html",
      controller: "SalesController",
    })
    .when("/assets/views/admin/quan_ly_danh_muc", {
      templateUrl: "assets/views/admin/quan_ly_danh_muc.html",
      controller: "CategoryManagerController",
    })
    .when("/assets/views/admin/thong_ke", {
      templateUrl: "assets/views/admin/thong_ke.html",
      controller: "StatisticalController",
    })
    .when("/assets/views/admin/userAdmin", {
      templateUrl: "assets/views/admin/userAdmin.html",
      controller: "userAdminKhoaController",
    })
    .when("/assets/views/admin/feedback", {
      templateUrl: "assets/views/admin/feedback.html",
      controller: "feedBackController",
    })
    .when("/assets/views/about", {
      templateUrl: "assets/views/about.html",
      controller: "feedBackController",
    })
    .when("/assets/views/user/demo_thong_tin_user", {
      templateUrl: "assets/views/user/demo_thong_tin_user.html",
      controller: "demothongtinuserController",
    })
    //Admin Huy
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
    .otherwise({
      templateUrl: "assets/views/admin/course_manager.html",
      controller: "CourseManagerController",
    });
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
