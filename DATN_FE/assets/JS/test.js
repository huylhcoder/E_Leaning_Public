const test_Controller = function ($scope, $http, $location, $rootScope) {

    let host = "http://localhost:8080/api";
    $scope.test = {};
   

    let id = 1;

    $scope.load_test = function () {
        let urlKhoaHoc = `${host}/test/take-the-test/${id}`;
        $http.get(urlKhoaHoc).then(resp => {
            $scope.test = resp.data;
            console.table( $scope.test);
            // displayStars($scope.course.totalRate);
        }).catch(err => {
            console.log("Error", err);
        });
    };
   

   

    $scope.load_test();

};
app.controller("test_Controller", test_Controller);
