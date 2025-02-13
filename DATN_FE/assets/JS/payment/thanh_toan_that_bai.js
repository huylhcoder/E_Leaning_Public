const thanhToanThatBaiController = function (
  $scope,
  $http,
  $location,
  $routeParams,
  $window
) {
  $scope.thanhToan = function () {
    $location.path("/assets/views/payment/don_hang");
  };
};

app.controller("thanhToanThatBaiController", thanhToanThatBaiController);
