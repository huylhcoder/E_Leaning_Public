const thanhToanThanhCongController = function (
  $scope,
  $http,
  $location,
  $routeParams,
  $window
) {
  Swal.fire({
    position: "center",
    icon: "success",
    title: "Thanh toán thành công",
    showConfirmButton: false,
    timer: 2000,
  });
};

app.controller("thanhToanThanhCongController", thanhToanThanhCongController);
