// app.controller('commentController', function ($scope, $http, $window) {
//     const token = $window.sessionStorage.getItem("jwt_token"); // Lấy token từ sessionStorage
//     const base_url = "http://localhost:8080/api/v1/user/sendComment"; // URL endpoint API

//     $scope.formData = {
//         phanhoi: "", // Loại phản hồi
//         noidung: "", // Nội dung
//     };

//     // Hàm xử lý khi nhấn nút "Gửi Phản Hồi"
//     $scope.submitFeedback = function () {
//         if (!token) {
//             Swal.fire({
//                 title: "Vui lòng đăng nhập để có thể phản hồi!",
//                 icon: "warning"
//             });
//             return; // Nếu không có token, dừng lại
//         }
        
//         // Lấy dữ liệu từ form
//         const selectedFeedbackType = document.getElementById("phanhoi").value;
//         const feedbackContent = document.getElementById("noidung").value;
//         // Kiểm tra dữ liệu form
//         if (!selectedFeedbackType || selectedFeedbackType === "Loại phản hồi") {
//             alert("Vui lòng chọn loại phản hồi!");
//             return;
//         }
//         if (!feedbackContent) {
//             alert("Vui lòng nhập nội dung phản hồi!");
//             return;
//         }

//         // Tạo FormData để gửi dữ liệu multipart/form-data
//         const formData = new FormData();
//         formData.append("phanhoi", selectedFeedbackType); // Loại phản hồi
//         formData.append("noidung", feedbackContent); // Nội dung phản hồi

//         // Gọi API gửi phản hồi
//         $http.post(base_url, formData, {
//             headers: {
//                 "Authorization": `Bearer ${token}`, // Gửi token trong header
//                 "Content-Type": undefined // Để mặc định cho FormData
//             },
//             transformRequest: angular.identity // Đảm bảo gửi đúng định dạng FormData
//         }).then(function (response) {
//             // Xử lý thành công
//             console.log("Phản hồi thành công:", response.data);
//             Swal.fire({
//                 title: "Phản hồi đã được gửi thành công!",
//                 icon: "success"
//             });
//         }).catch(function (error) {
//             // Xử lý lỗi
//             console.error("Lỗi khi gửi phản hồi:", error);
//             alert("Đã xảy ra lỗi khi gửi phản hồi. Vui lòng thử lại sau!");
//         });
//     };
// });
app.controller('commentController', function ($scope, $http, $window) {
    const token = $window.sessionStorage.getItem("jwt_token"); // Lấy token từ sessionStorage
    const base_url = "http://localhost:8080/api/v1/user/sendComment"; // URL endpoint API

    $scope.formData = {
        phanhoi: "", // Loại phản hồi
        noidung: "", // Nội dung
        file: null // Tệp tải lên
    };

    $scope.clearFormData = function () {
        $scope.formData = {
            phanhoi: "", // Đặt lại loại phản hồi
            noidung: "", // Đặt lại nội dung
            file: null // Đặt lại tệp tải lên
        };
    
        // Nếu bạn muốn xóa giá trị trong input file, bạn có thể làm như sau:
        document.getElementById("fileUpload").value = ""; // Đặt lại input file
    };
    // Hàm xử lý khi nhấn nút "Gửi Phản Hồi"
    $scope.submitFeedback = function () {
        if (!token) {
            Swal.fire({
                title: "Vui lòng đăng nhập để có thể phản hồi!",
                icon: "warning"
            });
            return; // Nếu không có token, dừng lại
        }
        
        // Lấy dữ liệu từ form
        const selectedFeedbackType = $scope.formData.phanhoi;
        const feedbackContent = $scope.formData.noidung;
        const feedbackFile = document.getElementById("fileUpload").files[0]; // Lấy tệp từ input
    
        // Kiểm tra dữ liệu form
        if (!selectedFeedbackType) {
            alert("Vui lòng chọn loại phản hồi!");
            return;
        }
        if (!feedbackContent) {
            alert("Vui lòng nhập nội dung phản hồi!");
            return;
        }
    
        // Hiển thị thông báo xác nhận trước khi gửi
        Swal.fire({
            title: "Bạn có chắc chắn muốn gửi phản hồi không?",
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
                formData.append("phanhoi", selectedFeedbackType); // Loại phản hồi
                formData.append("noidung", feedbackContent); // Nội dung phản hồi
                if (feedbackFile) {
                    formData.append("file", feedbackFile); // Thêm tệp vào FormData nếu có
                }
    
                // Gọi API gửi phản hồi
                $http.post(base_url, formData, {
                    headers: {
                        "Authorization": `Bearer ${token}`, // Gửi token trong header
                        "Content-Type": undefined // Để mặc định cho FormData
                    },
                    transformRequest: angular.identity // Đảm bảo gửi đúng định dạng FormData
                }).then(function (response) {
                    // Xử lý thành công
                    console.log("Phản hồi thành công:", response.data);
                    // Đóng thông báo "Đang xử lý..."
                    Swal.close();
                    // Hiển thị thông báo thành công
                    Swal.fire({
                        title: "Phản hồi đã được gửi thành công!",
                        icon: "success"
                    });
                    $scope.clearFormData();
                }).catch(function (error) {
                    // Xử lý lỗi
                    console.error("Lỗi khi gửi phản hồi:", error);
                    // Đóng thông báo "Đang xử lý..."
                    Swal.close();
                    Swal.fire({
                        title: "Đã xảy ra lỗi khi gửi phản hồi. Vui lòng thử lại sau!",
                        icon: "error"
                    });
                });
            }
        });
    };
});