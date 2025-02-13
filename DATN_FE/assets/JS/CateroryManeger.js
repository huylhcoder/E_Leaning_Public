const CategoryManagerController = function ($scope, $http, $location, $rootScope) {
    console.log(123);

    const base_url = "http://localhost:8080/api/category";
    const base_url_v1 = "http://localhost:8080/api/v1/tamCategory";
    const token =
    "eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImxlaG9hbmdodXljb2RlckBnbWFpbC5jb20iLCJzdWIiOiJsZWhvYW5naHV5Y29kZXJAZ21haWwuY29tIiwiZXhwIjoxNzM5NDE2NTMxfQ.HV8XuG-nsjmmq8RtlXP-eNGoKcvr5qpQkS9dstpX0xM"; // Thay thế bằng token thực tế của bạn;
    
    $scope.listDanhMuc = [];
    $http.get(base_url, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    }).then(resp => {
        $scope.listDanhMuc = resp.data;
        console.log("Khóa học trả về", resp);
    }).catch(err => {
        console.log("Lỗi không thể tải dữ liệu", err);
    });


    $scope.errorMessage = "";

$scope.showErrorModal = function (message) {
    $scope.errorMessage = message;
    const modal = new bootstrap.Modal(document.getElementById('errorModal'));
    modal.show();
};

$scope.addCategory = function () {
    if (!$scope.category || !$scope.category.name || $scope.category.name.trim() === "") {
        $scope.showErrorModal("Vui lòng nhập tên danh mục!");
        return;
    }

    if ($scope.isDuplicate($scope.category.name)) {
        $scope.showErrorModal("Danh mục đã tồn tại!");
        return;
    }

    const category = { name: $scope.category.name };

    $http.post(base_url_v1, category, {
        headers: { 'Authorization': `Bearer ${token}` }
    }).then(resp => {
        console.log("Thêm danh mục mới thành công", resp);
        $scope.listDanhMuc.push(resp.data);
        $scope.category.name = ""; // Reset input
    }).catch(err => {
        console.log("Lỗi không thể thêm danh mục mới", err);
    });
};

    $scope.viewDetail = function (categoryId) {
        $http.get(`${base_url_v1}/${categoryId}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        }).then(resp => {
            console.log("Thông tin chi tiết", resp);
            $scope.category = resp.data;
            // Hiển thị thông tin chi tiết tại đây
        }).catch(err => {
            console.log("Lỗi không thể lấy thông tin chi tiết", err);
        });
    };

    // Cập nhật trong hàm `updateCategory`
$scope.updateCategory = function () {
    if (!$scope.category || !$scope.category.name || $scope.category.name.trim() === "") {
        $scope.showErrorModal("Vui lòng nhập tên danh mục!");
        return;
    }

    if ($scope.isDuplicate($scope.category.name)) {
        $scope.showErrorModal("Danh mục đã tồn tại!");
        return;
    }

    $http.put(`${base_url_v1}/${$scope.category.categoryId}`, $scope.category, {
        headers: { 'Authorization': `Bearer ${token}` }
    }).then(resp => {
        console.log("Cập nhật danh mục thành công", resp);
        $scope.category = resp.data;

        // Reload danh sách
        $http.get(base_url).then(resp => {
            $scope.listDanhMuc = resp.data;
        }).catch(err => {
            console.log("Lỗi không thể tải dữ liệu", err);
        });
    }).catch(err => {
        console.log("Lỗi không thể cập nhật danh mục", err);
    });
};

$scope.confirmDelete = function () {
    if (!$scope.categoryToDelete) return;

    $http.delete(`${base_url_v1}/${$scope.categoryToDelete}`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    }).then(resp => {
        console.log("Xóa danh mục thành công", resp);

        // Cập nhật lại danh sách
        $scope.listDanhMuc = $scope.listDanhMuc.filter(item => item.categoryId !== $scope.categoryToDelete);
        $scope.categoryToDelete = null;

        // Ẩn modal
        const modal = bootstrap.Modal.getInstance(document.getElementById('deleteConfirmModal'));
        modal.hide();
    }).catch(err => {
        console.log("Lỗi khi xóa danh mục", err);
    });
};

    $scope.resetCategory = function () {
        $scope.category = null;
    };

    $scope.isDuplicate = function (name) {
        return $scope.listDanhMuc.some(category => category.name.toLowerCase() === name.toLowerCase());
    };

    $scope.categoryToDelete = null;

$scope.showDeleteModal = function (categoryId) {
    $scope.categoryToDelete = categoryId;
    const modal = new bootstrap.Modal(document.getElementById('deleteConfirmModal'));
    modal.show();
};

};
app.controller("CategoryManagerController", CategoryManagerController);
// thêm xong token