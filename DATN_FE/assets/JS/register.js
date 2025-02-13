const registerController = function ($scope, $location) {
  
    $scope.dangKy = function () {
        // Thêm thông tin học sinh vào danh sách học sinh
        $scope.Student.id = $scope.Student.username;
        console.log($scope.Student);
        // console.log("Log 1 sinh viên =", $scope.Student);
        $scope.Students.push($scope.Student);
        // Lưu danh sách học sinh vào localStorage
        localStorage.setItem('students', JSON.stringify($scope.Students));
        // Thông báo đăng ký thành công và chuyển hướng về trang chủ
        alert("Đã đăng ký thành công tài khoàn: " + $scope.Student.username);
        $location.path('/');
    }
};
app.controller("registerController", registerController);