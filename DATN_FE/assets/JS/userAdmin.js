app.controller('userAdminKhoaController', function ($scope, $http, $window) {
    const base_url = "http://localhost:8080/api/course_progress/userAdmin";
    const base_url_user = "http://localhost:8080/api/course_progress";
    const token =
        "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImxlaG9hbmdodXljb2RlckBnbWFpbC5jb20iLCJzdWIiOiJsZWhvYW5naHV5Y29kZXJAZ21haWwuY29tIiwiZXhwIjoxNzM5NDE2NTMxfQ.HV8XuG-nsjmmq8RtlXP-eNGoKcvr5qpQkS9dstpX0xM"; // Thay thế bằng token thực tế của bạn;
    const tokenLogin = $window.sessionStorage.getItem("jwt_token");
    $scope.listNguoiDung = [];
    $scope.listChiTietNguoiDung = [];

    // Hàm kiểm tra xem người dùng có phải là admin hay không
    function isAdmin(user) {
        return user.role.name === "Admin"; // Thay 'vai_tro' bằng thuộc tính phù hợp trong dữ liệu của bạn
    }

    // Hàm tải danh sách người dùng
    $scope.searchQuery = '';

    $scope.filterUsers = function () {
        if ($scope.searchQuery) {
            $scope.filteredListNguoiDung = $scope.listNguoiDung.filter(user => {
                const userName = user.name ? user.name.toLowerCase() : '';
                const userEmail = user.email ? user.email.toLowerCase() : '';
                return userName.includes($scope.searchQuery.toLowerCase()) ||
                    userEmail.includes($scope.searchQuery.toLowerCase());
            });
        } else {
            $scope.filteredListNguoiDung = $scope.listNguoiDung;
        }

        console.log("Danh sách đã lọc:", $scope.filteredListNguoiDung);
    };


    // Gọi hàm filterUsers khi danh sách người dùng được tải
    $scope.loadDanhSachNguoiDung = function () {
        $http.get(base_url, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            console.log("Dữ liệu từ API:", resp.data);
            $scope.listNguoiDung = resp.data.filter(user => !isAdmin(user));
            $scope.filteredListNguoiDung = $scope.listNguoiDung; // Khởi tạo danh sách đã lọc
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    };

    // Hàm lấy chi tiết người dùng
    $scope.getChiTietKhoaHoc = function (userId) {
        console.log({ userId });
        $http.get(`http://localhost:8080/api/course_progress/${userId}`, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            $scope.listChiTietNguoiDung = resp.data;

            // Kiểm tra nếu không có dữ liệu trả về
            if (!$scope.listChiTietNguoiDung || $scope.listChiTietNguoiDung.length === 0) {
                $scope.listChiTietNguoiDung = []; // Đảm bảo danh sách luôn tồn tại
            }
            $('#userDetailModal').modal('show');
            console.log(resp.data);
        }).catch(err => {
            console.error("Lỗi không thể tải dữ liệu", err);
            alert("Không thể tải dữ liệu người dùng.");
        });
    };




    // $scope.unblockUserStatus = function (userId) {
    //     $http.put(`http://localhost:8080/api/v1/user/unblockUser/${userId}`, {}, {
    //         headers: {
    //             'Authorization': `Bearer ${tokenLogin}`
    //         }
    //     }).then(resp => {
    //         console.log("Thông tin chi tiết", resp);
    //         // Tìm người dùng trong danh sách và cập nhật trạng thái
    //         const user = $scope.listNguoiDung.find(user => user.userId === userId);
    //         if (user) user.isActive = false; // Cập nhật trạng thái chặn
    //         $scope.reloadUserList(); // Tải lại danh sách nếu cần
    //     }).catch(err => {
    //         console.log("Lỗi không thể block người dùng", err);
    //     });
    // };
    $scope.formBlockData = {
        lyDoChan: "", // lý do chặn người dùng
        file: null // Tệp tải lên
    };
    $scope.userIdToBlock = null; // Biến để lưu userId

    $scope.setUserIdToBlock = function (userId) {
        $scope.userIdToBlock = userId; // Lưu userId vào biến
    };
    $scope.unblockUserStatus = function () {
        if (!$scope.userIdToBlock) {
            alert("Không có người dùng nào được chọn để chặn.");
            return;
        }
    
        if (!tokenLogin) {
            Swal.fire({
                title: "Vui lòng đăng nhập để thực hiện hành động này!",
                icon: "warning"
            });
            return; // Nếu không có token, dừng lại
        }
    
        // Kiểm tra dữ liệu form
        const blockReason = $scope.formData.lyDoChan;
        const blockFile = document.getElementById("fileUpload").files[0]; // Lấy tệp từ input
    
        if (!blockReason) {
            alert("Vui lòng nhập lý do chặn!");
            return;
        }
    
        // Hiển thị thông báo xác nhận trước khi gửi
        Swal.fire({
            title: "Bạn có chắc chắn muốn chặn người dùng không?",
            icon: "question",
            showCancelButton: true,
            confirmButtonText: "Có",
            cancelButtonText: "Hủy"
        }).then(result => {
            if (result.isConfirmed) {
                // Hiển thị thông báo "Đang xử lý..."
                Swal.fire({
                    title: "Đang xử lý...",
                    allowOutsideClick: false,
                    didOpen: () => {
                        Swal.showLoading();
                    }
                });
    
                // Tạo FormData để gửi dữ liệu multipart/form-data
                const formData = new FormData();
                formData.append("lyDoChan", blockReason); // Lý do chặn
                if (blockFile) {
                    formData.append("file", blockFile); // Thêm tệp vào FormData nếu có
                }
    
                // Gọi API chặn người dùng
                $http.put(`http://localhost:8080/api/v1/user/unblockUser/${$scope.userIdToBlock}`, formData, {
                    headers: {
                        'Authorization': `Bearer ${tokenLogin}`, // Gửi token trong header
                        "Content-Type": undefined // Để mặc định cho FormData
                    },
                    transformRequest: angular.identity // Đảm bảo gửi đúng định dạng FormData
                }).then(resp => {
                    // Xử lý thành công
                    console.log("Chặn người dùng thành công:", resp.data);
                    // Đóng thông báo "Đang xử lý..."
                    Swal.close();
                    // Hiển thị thông báo thành công
                    Swal.fire({
                        title: "Người dùng đã được chặn thành công!",
                        icon: "success"
                    });
                    // Cập nhật trạng thái người dùng
                    const user = $scope.listNguoiDung.find(user => user.userId === $scope.userIdToBlock);
                    if (user) user.isActive = false; // Cập nhật trạng thái chặn
                    $scope.reloadUserList(); // Tải lại danh sách nếu cần
                }).catch(err => {
                    // Xử lý lỗi
                    console.error("Lỗi khi chặn người dùng:", err);
                    // Đóng thông báo "Đang xử lý..."
                    Swal.close();
                    Swal.fire({
                        title: "Đã xảy ra lỗi khi chặn người dùng. Vui lòng thử lại sau!",
                        icon: "error"
                    });
                });
            }
        });
    };

    $scope.blockUserStatus = function (userId) {
        $http.put(`http://localhost:8080/api/v1/user/blockUser/${userId}`, {}, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            console.log("Thông tin chi tiết", resp);
            // Tìm người dùng trong danh sách và cập nhật trạng thái
            const user = $scope.listNguoiDung.find(user => user.userId === userId);
            if (user) user.isActive = true; // Cập nhật trạng thái mở chặn
            $scope.reloadUserList(); // Tải lại danh sách nếu cần
        }).catch(err => {
            console.log("Lỗi không thể unblock người dùng", err);
        });
    };


    // Hàm để tải lại danh sách người dùng
    $scope.reloadUserList = function () {
        $http.get(base_url, {
            headers: {
                'Authorization': `Bearer ${tokenLogin}`
            }
        }).then(resp => {
            console.log("Cập nhật danh sách người dùng sau khi reload:", resp.data);
            $scope.listNguoiDung = resp.data.filter(user => !isAdmin(user));
            $scope.filteredListNguoiDung = $scope.listNguoiDung;
        }).catch(err => {
            console.log("Lỗi không thể tải lại danh sách", err);
        });
    };
    // Gọi hàm tải danh sách người dùng khi controller được khởi tạo
    $scope.loadDanhSachNguoiDung();
});
// thêm xong token