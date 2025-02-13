app.controller('feedBackController', function ($scope, $http, $window) {
    $scope.listDanhGia = [];
    const base_url = "http://localhost:8080/api/v1/comment";
    const token = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImxlaG9hbmdodXljb2RlckBnbWFpbC5jb20iLCJzdWIiOiJsZWhvYW5naHV5Y29kZXJAZ21haWwuY29tIiwiZXhwIjoxNzM5NDE2NTMxfQ.HV8XuG-nsjmmq8RtlXP-eNGoKcvr5qpQkS9dstpX0xM"; // Thay thế bằng token thực tế của bạn.
    const tokenLogin = $window.sessionStorage.getItem("jwt_token");
    $scope.disabledReplies = JSON.parse(localStorage.getItem('disabledReplies')) || {};

    // Function to load the feedback list
    $scope.loadDanhSachDanhGia = function () {
        $http.get(base_url, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            console.log("Dữ liệu từ API:", resp.data);

            // Lưu danh sách bình luận
            $scope.listDanhGia = resp.data;

            // Cập nhật trạng thái nút phản hồi từ dữ liệu API
            $scope.listDanhGia.forEach(comment => {
                if (comment.isReplied) { // Giả sử API trả về `isReplied`
                    $scope.disabledReplies[comment.commentId] = true;
                }
            });

            // Lưu trạng thái vào localStorage để duy trì sau khi tải lại trang
            localStorage.setItem('disabledReplies', JSON.stringify($scope.disabledReplies));
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    };

    // Get star ratings as stars
    $scope.getStars = function (rating) {
        let numericRating = Number(rating); // Chuyển đổi thành số
        let stars = '';
        for (let i = 0; i < 5; i++) {
            stars += (i < numericRating) ? '★' : '☆'; // Sử dụng sao đầy (★) cho đã đánh giá, sao rỗng (☆) cho chưa đánh giá
        }
        return stars;
    };

    // Change status of a comment
    $scope.changeStatus = function (commentId) {
        $http.put(`${base_url}/changeStatus/${commentId}`, null, { // Tham số data là `null` vì không có nội dung gửi kèm
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            console.log("Thông tin chi tiết:", resp);

            // Giả sử resp.data chứa thông tin bình luận đã được cập nhật
            const updatedComment = resp.data;

            // Cập nhật trạng thái của bình luận trong danh sách hiện tại
            const index = $scope.listDanhGia.findIndex(item => item.commentId === commentId);
            if (index !== -1) {
                $scope.listDanhGia[index].status = updatedComment.status; // Cập nhật trạng thái mới
            }
            Swal.fire({
                title: "Bình luận đã được duyệt!",
                icon: "success"
            });
            // Gọi lại hàm tải danh sách bình luận để đảm bảo thông tin được cập nhật
            $scope.loadDanhSachDanhGia();
        }).catch(err => {
            console.log("Lỗi không thể cập nhật trạng thái", err);
        });
    };

    // Add reply to a comment
    $scope.addReplyComment = function (commentId) {
        $scope.currentCommentId = commentId;
        $scope.reply = {}; // Reset dữ liệu phản hồi nếu cần
        $scope.selectedComment = $scope.listDanhGia.find(item => item.commentId === commentId);
    };

    // Submit reply
    $scope.submitReply = function () {
        // Kiểm tra nếu nội dung phản hồi trống
        if (!$scope.reply.content || $scope.reply.content.trim() === '') {
            Swal.fire({
                title: "Vui lòng nhập nội dung phản hồi!",
                icon: "error"
            });
            return; // Dừng lại không gửi yêu cầu nếu bỏ trống
        }
    
        const commentId = $scope.currentCommentId; // Lấy commentId đã lưu
        const reply = {
            userId: 1,
            commentId: commentId,
            content: $scope.reply.content,
            replyStatus: $scope.reply.replyStatus
        };
    
        // Gửi yêu cầu POST đến API để thêm reply
        $http.post(`http://localhost:8080/api/reply/${commentId}`, reply, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            console.log("Reply đã được thêm:", resp.data);
    
            // Cập nhật trạng thái của bình luận trong danh sách hiện tại
            $scope.disabledReplies[commentId] = true;
    
            // Lưu trạng thái vào localStorage
            localStorage.setItem('disabledReplies', JSON.stringify($scope.disabledReplies));
    
            Swal.fire({
                title: "Đã phản hồi bình luận!",
                icon: "success"
            });
    
            // Đóng modal sau khi gửi thành công
            $('#exampleModal').modal('hide');
        }).catch(err => {
            console.log("Lỗi khi thêm reply:", err);
        });
    };
    

    // Load feedback list on controller initialization
    $scope.loadDanhSachDanhGia();
});
