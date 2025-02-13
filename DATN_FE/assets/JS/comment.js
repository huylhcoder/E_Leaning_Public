app.controller('CommentController', function ($scope, $http) {
    // Khởi tạo danh sách comment rỗng
    $scope.DanhSachCMT = [];
    const token = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImxlaG9hbmdodXljb2RlckBnbWFpbC5jb20iLCJzdWIiOiJsZWhvYW5naHV5Y29kZXJAZ21haWwuY29tIiwiZXhwIjoxNzM5NDE2NTMxfQ.HV8XuG-nsjmmq8RtlXP-eNGoKcvr5qpQkS9dstpX0xM"; // Thay thế bằng token thực tế của bạn;

    // Gọi API lấy danh sách comment lần đầu
    $http({
        method: 'GET',
        url: 'http://localhost:8080/api/comment',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    }).then(function (response) {
        $scope.DanhSachCMT = response.data;
    }).catch(function (error) {
        console.error("Lỗi khi lấy danh sách comment:", error);
    });

    // Hàm load lại danh sách comment
    $scope.loadCMT = function () {
        $http({
            method: 'GET',
            url: 'http://localhost:8080/api/comment', // URL API lấy danh sách comment
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(function successCallback(response) {
            // Khi thành công, lưu dữ liệu vào biến DanhSachCMT
            $scope.DanhSachCMT = response.data;
            console.log("Dữ liệu comment:", $scope.DanhSachCMT);
        }, function errorCallback(error) {
            // Xử lý khi có lỗi
            console.error("Lỗi khi gọi API:", error);
            alert('Không thể lấy dữ liệu comment. Vui lòng thử lại!');
        });
    };
    $scope.setRating = function (rating) {
        $scope.comment.starRating = rating; // Gán giá trị sao được chọn
    };
    
    // Hàm thêm comment mới
    $scope.addCMT = function () {
        const comment = {
            userId: 1, // Giả sử bạn có sẵn userId
            content: $scope.comment.content,
            starRating: $scope.comment.starRating
        };

        $http({
            method: 'POST',
            url: 'http://localhost:8080/api/comment',
            data: comment,
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(function (resp) {
            console.log("Thêm comment mới thành công", resp);
            $scope.DanhSachCMT.push(resp.data);
            $scope.comment.content = "";
            $scope.comment.starRating = "";
            window.location.reload(); // Reload lại trang (không khuyến khích cách này, nhưng đúng logic bạn đưa ra)
        }).catch(function (err) {
            console.error("Lỗi không thể thêm comment mới", err);
        });
    };

    // Gọi loadCMT ngay khi khởi tạo controller
    $scope.loadCMT();
});
// thêm xong token