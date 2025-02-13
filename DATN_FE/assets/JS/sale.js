app.controller('SalesController', function ($scope, $http, $window) {
    // Khởi tạo danh sách khóa học rỗng
    $scope.DanhSachSales = [];
    const token = $window.sessionStorage.getItem("jwt_token");
    // Thay thế bằng token thực tế của bạn;
    $http.get('/api/v1/voucher', {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    }).then(function (response) {
        $scope.DanhSachSales = response.data;
    });
    
    $scope.loadSales = function () {
        $http({
            method: 'GET',
            url: 'http://localhost:8080/api/v1/voucher',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(function successCallback(response) {
            // Khi thành công, xử lý dữ liệu
            $scope.DanhSachSales = response.data.map(voucher => {
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

            console.log("Dữ liệu sales:", $scope.DanhSachSales);
        }, function errorCallback(error) {
            // Xử lý khi có lỗi
            console.error("Lỗi khi gọi API:", error);
            alert('Không thể lấy dữ liệu. Vui lòng thử lại!');
        });
    };


    $scope.viewDetail = function (voucherId) {

        
        $http.get(`http://localhost:8080/api/v1/voucher/${voucherId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            console.log("Thông tin chi tiết", resp);
            // $scope.voucher = resp.data;

            // voucher.startDate = new Date(voucher.startDate);
            // voucher.endDate = new Date(voucher.endDate);
            

            const voucherData = resp.data;

            // Convert date strings to Date objects
            // voucherData.startDate = new Date(voucherData.startDate);
            // voucherData.endDate = new Date(voucherData.endDate);
            voucherData.startDate = voucherData.startDate ? new Date(voucherData.startDate) : null;
            voucherData.endDate = voucherData.endDate ? new Date(voucherData.endDate) : null;
            const endDate = new Date(voucherData.endDate);
            const currentDate = new Date();
            if (endDate < currentDate.setHours(0, 0, 0, 0) || voucherData.quantity == 0 || currentDate.setHours(0, 0, 0, 0) < startDate) {
                voucherData.status = false;
            } 
            
            $scope.voucher = voucherData;
            const voucherModal = new bootstrap.Modal(document.getElementById('voucherModal'));
            voucherModal.show();
            console.log(voucherData)
            // Hiển thị thông tin chi tiết tại đây
        }).catch(err => {
            console.log("Lỗi không thể lấy thông tin chi tiết", err);
        });
    };
    $scope.addVoucher = function () {
        if ($scope.voucher.percentSale > 100) {
            Swal.fire({
                title: "Phần trăm giảm giá không được vượt quá 100%!",
                icon: "error",
                confirmButtonText: "OK",
            });
            return; // Dừng lại nếu phần trăm giảm giá không hợp lệ
        }
        const isDuplicate = $scope.DanhSachSales.some(
            (item) => item.voucherCode === $scope.voucher.voucherCode
        );
    
        if (isDuplicate) {
            Swal.fire({
                title: "Mã khuyến mãi đã tồn tại!",
                icon: "error",
                confirmButtonText: "OK",
            });
            return; // Dừng lại nếu mã khuyến mãi trùng
        }
        const startDate = new Date($scope.voucher.startDate);
        const endDate = new Date($scope.voucher.endDate);
        const currentDate = new Date();
        
        if (startDate > endDate) {
            Swal.fire({
                title: "Ngày bắt đầu không được lớn hơn ngày kết thúc!",
                icon: "error",
                confirmButtonText: "OK",
              });
            return; // Dừng lại nếu điều kiện không hợp lệ
        }
        if (currentDate.setHours(0, 0, 0, 0) > endDate) {
            Swal.fire({
                title: "Ngày kết thúc không được nhỏ hơn ngày hiện tại!",
                icon: "error",
                confirmButtonText: "OK",
              });
            return; // Dừng lại nếu điều kiện không hợp lệ
        }

        if (currentDate.setHours(0, 0, 0, 0) < startDate){
            $scope.voucher.status = false;
        }

        const voucher = {
            voucherCode: $scope.voucher.voucherCode,
            name: $scope.voucher.name,
            percentSale: $scope.voucher.percentSale,
            startDate: $scope.voucher.startDate,
            quantity: $scope.voucher.quantity,
            endDate: $scope.voucher.endDate,
            description: $scope.voucher.description,
            status: $scope.voucher.status
        };
        $http.post(`http://localhost:8080/api/v1/voucher`, voucher, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            console.log("Thêm voucher mới thành công", resp);
            $scope.DanhSachSales.push(resp.data);
            $scope.voucher.voucherCode = ""
            $scope.voucher.name = "";
            $scope.voucher.percentSale = "";
            $scope.voucher.quantity = "";
            $scope.voucher.startDate = "";
            $scope.voucher.endDate = "";
            $scope.voucher.description = "";
            const voucherModal = bootstrap.Modal.getInstance(document.getElementById('voucherModal'));
            voucherModal.hide();
            $scope.loadSales();
        }).catch(err => {
            Swal.fire({
                title: "Thêm thất bại!",
                icon: "error",
                confirmButtonText: "OK",
            });
        });
    };
    $scope.updateVoucher = function () {
       
        const startDate = new Date($scope.voucher.startDate);
        const endDate = new Date($scope.voucher.endDate);
        const currentDate = new Date();
        if (startDate > endDate) {
            Swal.fire({
                title: "Ngày bắt đầu không được lớn hơn ngày kết thúc!",
                icon: "error",
                confirmButtonText: "OK",
              });
            return; // Dừng lại nếu điều kiện không hợp lệ
        }
        if (endDate < currentDate.setHours(0, 0, 0, 0) || $scope.voucher.quantity == 0 || currentDate.setHours(0, 0, 0, 0) < startDate) {
            $scope.voucher.status = false;
        } else {
            $scope.voucher.status = true;
        }
        if ($scope.voucher.percentSale > 100) {
            Swal.fire({
                title: "Phần trăm giảm giá không được vượt quá 100%!",
                icon: "error",
                confirmButtonText: "OK",
            });
            return; // Dừng lại nếu phần trăm giảm giá không hợp lệ
        }
        $http.put(`http://localhost:8080/api/v1/voucher/${$scope.voucher.voucherId}`, $scope.voucher, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            console.log("Thông tin chi tiết", resp);
            $scope.voucher = resp.data;
            $scope.loadSales();
            $scope.resetVoucher();
            const voucherModal = bootstrap.Modal.getInstance(document.getElementById('voucherModal'));
            voucherModal.hide();
            // Load lại danh mục mới
            $http.get(`http://localhost:8080/api/v1/voucher`).then(resp => {
                $scope.DanhSachSales = resp.data;
                //window.location.reload();
                console.log("Voucher trả về", resp);
            }).catch(err => {
                console.log("Lỗi không thể tải dữ liệu", err);
            });
        }).catch(err => {
            console.log("Lỗi không thể lấy thông tin chi tiết", err);
        });
    };
    $scope.deleteVoucher = function (voucherId) {

        $http.delete(`http://localhost:8080/api/v1/voucher/${voucherId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            console.log("Thông tin chi tiết", resp);
            Swal.fire({
                title: "Xóa thành công",
                icon: "success",
                confirmButtonText: "OK",
            }); 
            $scope.loadSales();
            $scope.resetVoucher();
            // Load lại danh mục mới
            $http.get(`http://localhost:8080/api/v1/voucher`).then(resp => {
                $scope.DanhSachSales = resp.data;
                console.log("Voucher trả về", resp);
            }).catch(err => {
                console.log("Lỗi không thể tải dữ liệu", err);
            });
        }).catch(err => {
            Swal.fire({
                title: "Mã khuyến mãi này không thể xóa vì có người đang sử dụng",
                icon: "error",
                confirmButtonText: "OK",
            });        
        });
    };

    $scope.resetVoucher = function () {
        $scope.voucher = {};
    };

    // Gọi hàm loadCourses khi controller được khởi tạo
    $scope.loadSales();
});
// thêm xong token