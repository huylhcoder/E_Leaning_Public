const vnpayController = function ($scope, $http, $location, $window) {
  $scope.soTienCanThanhToan = 100000;

  //Nút thanh toán
  $scope.thanhToan = function () {
    $scope.amount = 100000;
    $http
      .post(`http://localhost:8080/api/v1/vnpayajax`, $scope.amount)
      .then((resp) => {
        // console.log(resp.data.data);
        $window.location.href = resp.data.data;
      })
      .catch((err) => {
        console.log("error: Staus " + err.status);
        alert("error: Staus " + err.status);
      });
  };
};
