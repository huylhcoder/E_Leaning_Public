angular.module('HBaoApp', [])
    .controller('forgotPassController', function ($scope, $http, $window) {
        const base_url = "http://localhost:8080/api/v1/user/ForgotPass";
        const token = "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImxlaG9hbmdodXljb2RlckBnbWFpbC5jb20iLCJzdWIiOiJsZWhvYW5naHV5Y29kZXJAZ21haWwuY29tIiwiZXhwIjoxNzM5NDE2NTMxfQ.HV8XuG-nsjmmq8RtlXP-eNGoKcvr5qpQkS9dstpX0xM";

        $scope.isOtpSent = false;
        $scope.mail = localStorage.getItem('userEmail') || "";
        $scope.otpLS = localStorage.getItem('serverOtp');
        $scope.otp = [];

        // Kiểm tra email và gửi mã OTP
        $scope.checkMailSendOTP = function (email) {
            if (!email) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Vui lòng nhập email!',
                });
                return;
            }
        
            if (!$scope.isOtpSent) {
                // Thông báo xác nhận trước khi gửi OTP
                Swal.fire({
                    title: "Bạn có muốn nhận mã OTP không?",
                    icon: "question",
                    showCancelButton: true,
                    confirmButtonText: "Có",
                    cancelButtonText: "Hủy"
                }).then(result => {
                    if (result.isConfirmed) {
                        // Hiển thị thông báo đang xử lý
                        Swal.fire({
                            title: "Đang xử lý...",
                            allowOutsideClick: false,
                            didOpen: () => {
                                Swal.showLoading();
                            }
                        });
        
                        // Gửi yêu cầu gửi OTP
                        $http.post(base_url + '/send-otp',
                            { email: email },
                            {
                                headers: {
                                    Authorization: `Bearer ${token}`,
                                },
                            }
                        )
                        .then(function (response) {
                            const serverOtp = response.data.otp;
                            localStorage.setItem('serverOtp', serverOtp);
                            localStorage.setItem('userEmail', email);
        
                            Swal.fire({
                                icon: 'success',
                                title: 'Thành công!',
                                text: 'Mã OTP đã được gửi đến email của bạn!',
                            }).then(() => {
                                $scope.isOtpSent = true;
                                window.location.href = 'verificationOTP.html';
                            });
                        })
                        .catch(function (error) {
                            console.error(error);
                            if (error.status === 404) {
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Oops...',
                                    text: 'Email không tồn tại trong hệ thống!',
                                });
                            } else {
                                Swal.fire({
                                    icon: 'error',
                                    title: 'Oops...',
                                    text: 'Có lỗi xảy ra khi gửi mã OTP!',
                                });
                            }
                        });
                    }
                });
            } else {
                Swal.fire({
                    icon: 'warning',
                    title: 'Cảnh báo',
                    text: 'Mã OTP đã được gửi trước đó. Vui lòng kiểm tra email của bạn!',
                });
            }
        };

        // Hàm kiểm tra mã OTP
        $scope.checkOtp = function () {
            const otpString = $scope.otp.join('');
            const serverOtp = localStorage.getItem('serverOtp');

            if (otpString.length < 6) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Vui lòng nhập đầy đủ mã OTP!',
                });
                return;
            }

            if (otpString === serverOtp) {
                Swal.fire({
                    icon: 'success',
                    title: 'Thành công!',
                    text: 'Mã OTP xác thực thành công!',
                }).then(() => {
                    window.location.href = 'changePass.html';
                });
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Mã OTP không chính xác!',
                });
            }
        };

        // Gửi lại mã OTP
        $scope.resendOTPCode = function () {
            const email = localStorage.getItem('userEmail');
            if (!email) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Không tìm thấy email trong hệ thống, vui lòng thử lại!',
                });
                return;
            }
            Swal.fire({
                title: "Bạn có chắc muốn gửi lại mã OTP không?",
                icon: "question",
                showCancelButton: true,
                confirmButtonText: "Có",
                cancelButtonText: "Hủy"
            }).then(result => {
                if (result.isConfirmed) {
                    // Hiển thị thông báo đang xử lý
                    Swal.fire({
                        title: "Đang xử lý...",
                        allowOutsideClick: false,
                        didOpen: () => {
                            Swal.showLoading();
                        }
                    });
                    // Xóa OTP cũ trong localStorage
                    localStorage.removeItem('serverOtp');
                    // Gửi yêu cầu gửi lại OTP
                    $http.post(base_url + '/send-otp',
                        { email: email },
                        {
                            headers: {
                                Authorization: `Bearer ${token}`,
                            },
                        }
                    )
                    .then(function (response) {
                        const serverOtp = response.data.otp;
                        localStorage.setItem('serverOtp', serverOtp);
        
                        Swal.fire({
                            icon: 'success',
                            title: 'Thành công!',
                            text: 'Mã OTP mới đã được gửi đến email của bạn!',
                        });
                    })
                    .catch(function (error) {
                        console.error(error);
                        Swal.fire({
                            icon: 'error',
                            title: 'Oops...',
                            text: error.status === 404
                                ? 'Email không tồn tại trong hệ thống!'
                                : 'Có lỗi xảy ra khi gửi lại mã OTP!',
                        });
                    });
                }
            });
        };        

        // Hàm kiểm tra và đổi mật khẩu
        $scope.changePass = function () {
            const newPassword = $scope.newPassword;
            const email = localStorage.getItem('userEmail');
            const otp = localStorage.getItem('serverOtp');

            if (!newPassword) {
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Mật khẩu không hợp lệ hoặc không khớp!',
                });
                return;
            }

            $http.post(base_url + '/update-password',
                {
                    email: email,
                    newPassword: newPassword,
                    otp: otp
                },
                {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }
            )
                .then(function (response) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Thành công!',
                        text: response.data.message,
                    }).then(() => {
                        localStorage.removeItem('userEmail');
                        localStorage.removeItem('serverOtp');
                        window.location.href = 'http://127.0.0.1:5500/index.html#!/assets/views/dang_nhap';
                    });
                })
                .catch(function (error) {
                    Swal.fire({
                        icon: 'error',
                        title: 'Oops...',
                        text: error.response?.data?.message || 'Có lỗi xảy ra khi đổi mật khẩu!',
                    });
                });
        };

        // Hàm thoát
        $scope.exit = function () {
            localStorage.removeItem('userEmail');
            window.history.back();
        };
    });
// thêm xong token 