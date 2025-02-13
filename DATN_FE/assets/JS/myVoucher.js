app.controller('MyVoucherController', function ($scope, $http, $window) {
    $scope.DanhSachVoucher = [];
    const token = $window.sessionStorage.getItem("jwt_token");
    $http.get('/api/v1/user/vouchers', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    }).then(function (response) {
        $scope.DanhSachVoucher = response.data;
    });
    $scope.loadSalesUser = function () {
        $http({
            method: 'GET',
            url: 'http://localhost:8080/api/v1/voucher/user/vouchers',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(function successCallback(response) {
            // Khi thành công, xử lý dữ liệu
            $scope.DanhSachVoucher = response.data.map(voucher => {
                const endDate = new Date(voucher.endDate);
                const startDate = new Date(voucher.startDate);
                const currentDate = new Date();

                // Hàm định dạng ngày thành dd-mm-yyyy
                const formatDate = (date) => {
                    if (!date) return null; // Trả null nếu không có giá trị
                    const dd = String(date.getDate()).padStart(2, '0');
                    const mm = String(date.getMonth() + 1).padStart(2, '0');
                    const yyyy = date.getFullYear();
                    return `${dd}-${mm}-${yyyy}`;
                };

                voucher.startDate = formatDate(startDate);
                voucher.endDate = formatDate(endDate);

                // Kiểm tra trạng thái hiệu lực
                if (endDate < currentDate.setHours(0, 0, 0, 0) || voucher.quantity == 0 || currentDate.setHours(0, 0, 0, 0) < startDate) {
                    voucher.status = false;
                } else {
                    voucher.status = true;
                }
                return voucher;
            });

            console.log("Dữ liệu sales:", $scope.DanhSachVoucher);
        }, function errorCallback(error) {
            // Xử lý khi có lỗi
            console.error("Lỗi khi gọi API:", error);
 
        });
    }
    $scope.loadSalesUser();
});