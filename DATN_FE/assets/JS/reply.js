// const replyController = function ($scope, $http, $location, $rootScope) {
//     const token =
//         "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6InRlc3Q2QGdtYWlsLmNvbSIsInN1YiI6InRlc3Q2QGdtYWlsLmNvbSIsImV4cCI6MTczNDM1NjU3NH0.YU7eEm8uyw1Y-uDI5nmgsrJ00S101lYr-oZEZkXZ8eg"; // Thay thế bằng token thực tế của bạn;
//     $scope.listPhanHoi = [];
//     $scope.loadPhanHoi = function () {
//         var commentId = 3;
//         $http.get(`http://localhost:8080/api/reply/${commentId}`, {
//             headers: {
//                 'Authorization': `Bearer ${token}`
//             }
//         }).then(resp => {
//             console.log("Phản hồi cho commentId " + commentId + ":", resp.data);
//             $scope.listPhanHoi = resp.data; // Lưu phản hồi vào biến listPhanHoi
//         }).catch(err => {
//             console.log("Lỗi không thể tải dữ liệu", err);
//         });
//     };
// };
// app.controller("replyController", replyController);
// // thêm xong token
