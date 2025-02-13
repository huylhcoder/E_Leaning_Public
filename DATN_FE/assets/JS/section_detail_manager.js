const SectionManagerController = function (
  $scope,
  $http,
  $location,
  $routeParams,
  $timeout,
  $window
) {
  const section_url = "http://localhost:8080/api/v1/section-manager";
  const token = $window.sessionStorage.getItem("jwt_token");
  $scope.courseId = $routeParams.courseId;
  $scope.sectionId = $routeParams.sectionId;
  $scope.section = {};
  $scope.loading = 0;
  $scope.lessonDetail = {
    lessonId: 0,
    name: "",
    description: "",
  };
  $scope.videoSrc = null;
  $scope.selectedFile = null; // Reset tệp đã chọn
  // $scope.lessonDetail = {};
  $scope.questionIdToDelete = null;
  $scope.testId = 0;
  $scope.listQuestion = [];
  $scope.questionDetail = {};
  $scope.videoDuration = 0; // Đặt thời gian video ban đầu là 0

  $scope.showCourseDetail = function () {
    $http
      .get(
        `http://localhost:8080/api/v1/course-manager-detail/${$scope.courseId}`
      )
      .then((resp) => {
        $scope.course = resp.data;
        console.log("Show khóa học", $scope.course);
      })
      .catch((err) => {
        console.error("Show khóa học", err);
      });
  };
  $scope.showCourseDetail();

  $scope.chuyenTrangLink = function (value_url) {
    if (value_url == "listCourse_url") {
      $window.location.href = `http://127.0.0.1:5500/indexAdmin.html#!/assets/views/admin/course_manager`; // Thực hiện chuyển trang
    }
    if (value_url == "listSection_url") {
      $window.location.href = `http://127.0.0.1:5500/indexAdmin.html#!/assets/views/admin/course_manager_detail/${$scope.courseId}`; // Thực hiện chuyển trang
    }
    if (value_url == "sectionDetail_url") {
      $window.location.href = `http://127.0.0.1:5500/indexAdmin.html#!/assets/views/admin/course_manager_detail/${$scope.courseId}/section/${$scope.sectionId}`; // Thực hiện chuyển trang
    }
  };

  /////////////////////////          SECTION             //////////////
  //Show section

  $scope.showSectionDetail = function () {
    $http
      .get(`${section_url}/${$scope.sectionId} `, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((resp) => {
        $scope.section = resp.data;
        console.log("Show section detail", $scope.section);
      })
      .catch((err) => {
        console.error("Show section detail", err);
      });
  };
  $scope.showSectionDetail();

  //Nút cập nhật phần
  $scope.luuThongTinChiTietKhoaHoc = function () {
    $scope.loading = 1;
    $http
      .put(`${section_url}/${$scope.sectionId}`, $scope.section, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((resp) => {
        $scope.loading = 0;
        Swal.fire({
          title: "Cập nhật thành công",
          icon: "success",
          confirmButtonText: "OK",
        });
        $scope.showListLesson();
      })
      .catch((err) => {
        $scope.loading = 0;
        Swal.fire({
          title: "Cập nhật thất bại",
          text: err.data || "Có lỗi xảy ra!",
          icon: "error",
          confirmButtonText: "OK",
        });
      });
  };
  /////////////////////////         END SECTION             //////////////
  /////////////////////////          lESSON             //////////////
  //Hiển thị chi tiết bài học

  //Hiển thị tất cả bài học
  $scope.listLesson = [];
  $scope.showListLesson = function () {
    $http
      .get(`${section_url}/${$scope.sectionId}/show-list-section `, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((resp) => {
        $scope.listLesson = resp.data;
        console.log("Show lesson", $scope.listLesson);
      })
      .catch((err) => {
        console.error("Show lesson", err);
      });
  };
  $scope.showListLesson();

  // làm tròn giá trị
  $scope.formatDuration = function (duration) {
    var hours = Math.floor(duration / 3600);
    var minutes = Math.floor((duration % 3600) / 60);
    var seconds = Math.round(duration % 60);

    if (hours == 0 && minutes == 0) {
      durationRound = Math.round(seconds);
      return durationRound + " giây";
    }

    if (hours > 0) {
      return hours + " giờ " + minutes + " phút " + seconds + " giây";
    } else {
      return minutes + " phút " + seconds + " giây";
    }
  };

  //Nút thay đổi hình ảnh khi chọn trong form
  $scope.onFileChange = function (input) {
    if (input.files && input.files[0]) {
      const file = input.files[0];
      const fileURL = URL.createObjectURL(file);
      $scope.$apply(function () {
        $scope.videoSrc = fileURL; // Cập nhật videoSrc với URL của video mới
        $scope.selectedFile = file; // Lưu trữ tệp video đã chọn
      });

      // Tạo một thẻ video tạm thời để lấy thời gian
      const videoElement = document.createElement("video");
      videoElement.src = fileURL;

      // Khi video có thể được tải, lấy thời gian
      videoElement.addEventListener("loadedmetadata", function () {
        $scope.$apply(function () {
          $scope.videoDuration = videoElement.duration; // Lưu trữ thời gian video
          console.log($scope.videoDuration); // Log thời gian video sau khi đã cập nhật
        });
      });
    }
  };

  // Các hàm khác không thay đổi

  // Hiển thị modal form thêm mới
  $scope.showFormThemMoi = function () {
    $scope.videoSrc = null;
    $scope.selectedFile = null; // Reset tệp đã chọn
    $scope.lessonDetail = {
      lessonId: 0,
      name: "",
      description: "",
    }; // Reset thông tin bài học
  };

  // Nút thêm hoặc cập nhật
  $scope.saveLesson = function () {
    console.log($scope.lessonDetail);

    $scope.loading = 1; //Hiển thị nút loading

    const formData = new FormData();
    formData.append("videoDuration", $scope.videoDuration);
    formData.append("name", $scope.lessonDetail.name);
    formData.append("description", $scope.lessonDetail.description);
    if ($scope.selectedFile) {
      formData.append("file", $scope.selectedFile); // Thêm tệp video vào FormData
    }
    //Nếu khác null và 0 thì cập nhật
    if ($scope.lessonDetail.lessonId != 0) {
      console.log("Vào phương thức cập nhật");
      $http
        .put(
          `${section_url}/${$scope.sectionId}/lesson/${$scope.lessonDetail.lessonId}/update-lesson`,
          formData,
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": undefined, // Để trình duyệt tự động thiết lập Content-Type cho multipart/form-data
            },
          }
        )
        .then((resp) => {
          $scope.videoSrc = resp.data.pathVideo;
          $scope.loading = 0;
          Swal.fire({
            title: "Cập nhật bài học thành công",
            icon: "success",
            confirmButtonText: "OK",
          });
          $scope.showListLesson();
        })
        .catch((err) => {
          $scope.loading = 0;
          Swal.fire({
            title: "Cập nhật bài học thất bại",
            text: err.data || "Có lỗi xảy ra, vui lòng thử lại.",
            icon: "error",
            confirmButtonText: "OK",
          });
        });
    }

    if ($scope.lessonDetail.lessonId == 0) {
      console.log("Vào phương thức thêm mới");
      $http
        .post(`${section_url}/${$scope.sectionId}/add-lesson`, formData, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": undefined, // Để trình duyệt tự động thiết lập Content-Type cho multipart/form-data
          },
        })
        .then((resp) => {
          $scope.loading = 0;
          Swal.fire({
            title: "Thêm bài học mới thành công",
            icon: "success",
            confirmButtonText: "OK",
          });
          $scope.showListLesson();
        })
        .catch((err) => {
          console.error("Thêm bài học thất bại", err);
          Swal.fire({
            title: "Thêm bài học thất bại",
            text: err.data || "Có lỗi xảy ra, vui lòng thử lại.",
            icon: "error",
            confirmButtonText: "OK",
          });
        });
    }
  };

  $scope.showLessonDetail = function (lessonId) {
    $scope.videoSrc = null;
    $scope.selectedFile = null; // Reset tệp đã chọn
    $http
      .get(`${section_url}/${$scope.sectionId}/lesson/${lessonId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((resp) => {
        $scope.videoSrc = resp.data.pathVideo;
        $scope.lessonDetail = resp.data;
        console.log("Show lesson detail", $scope.lessonDetail);
        // Mở modal khi nhận được dữ liệu
        $("#exampleModalToggle").modal("show");
      })
      .catch((err) => {
        console.error("Show lesson detail", err);
      });
  };

  $scope.deleteLesson = function (lessonId) {
    const url = `${section_url}/${$scope.sectionId}/lesson/${lessonId}/remove-lesson`;

    $http
      .delete(url, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((resp) => {
        // Gọi hàm để hiển thị lại chi tiết câu hỏi sau khi xóa
        $scope.showListLesson();
        Swal.fire({
          title: "Xóa thành công",
          icon: "success",
          confirmButtonText: "OK",
        });
      })
      .catch((err) => {
        Swal.fire({
          title: "Cập nhật thất bại",
          text: err.data || "Có lỗi xảy ra!",
          icon: "error",
          confirmButtonText: "OK",
        });
      });
  };

  // <video width="100%" ng-src="{{videoUrlTest}}" controls>
  //                     Your browser does not support the video tag.
  //                 </video>
  //                 <button
  //                 ng-click="loadVideo()"
  //                 >Load Video</button>
  $scope.videoUrlTest = "";
  $scope.loadVideo = function () {
    console.log("Vào phương thức load video");
    $scope.loading = 1;
    $scope.path =
      "http://res.cloudinary.com/dxj6jmdm8/video/upload/v1735464665/Sun%20Dec%2029%2016:30:52%20ICT%202024.mp4";
    console.log($scope.path);
    $http
      .get(`http://localhost:8080/api/v1/upload-file/download-video`, {
        params: {
          link: $scope.path,
          courseId: $scope.courseId,
          tokenString: token,
        },
        responseType: "arraybuffer",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => {
        $scope.loading = 0;
        var blob = new Blob([response.data], { type: "video/mp4" });
        console.log("Blob size:", blob.size); // Kiểm tra kích thước blob
        if (blob.size > 0) {
          $scope.videoUrlTest = URL.createObjectURL(blob);
        } else {
          console.error("Video blob is empty");
        }
      })
      .catch((err) => {
        $scope.loading = 0;
        console.error("Error", err);
      });
  };
  /////////////////////////         END LESSON             //////////////
  /////////////////////////          QUIZ             //////////////
  //Hiển thị danh sách câu hỏi
  $scope.getTest = function () {
    $http
      .get(`${section_url}/${$scope.sectionId}/test-manager`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((resp) => {
        $scope.testId = resp.data.testID;
        $scope.countdownTimer = resp.data.countdownTimer;
        console.log("Mã bài kiểm tra: " + $scope.testId);
        $scope.listQuestion = resp.data.listQuestion;
        console.log("Danh sách các câu hỏi", $scope.listQuestion);
      })
      .catch((err) => {
        //Nếu không có thì bài kiểm tra bằng null ng-show hiển thị kêu thêm test
        $scope.listQuestion = []; // listQuestion + testId
        console.error("Lỗi không thể tải bài kiểm tra", err);
      });
  };
  $scope.getTest();

  $scope.countdownTimer = 0; // Khởi tạo giá trị mặc định
  $scope.updateCountdownTimer = function (countdownTimerParam) {
    console.log(countdownTimer);

    $http
      .post(
        `${section_url}/${$scope.sectionId}/update-countdown-timer/${$scope.testId}/${countdownTimerParam}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      )
      .then((resp) => {
        $scope.getTest(); //Load lại bài kiểm tra
        Swal.fire({
          title: "Cập nhật thành công",
          icon: "success",
          confirmButtonText: "OK",
        });
      })
      .catch((err) => {
        alert("Thời gian điểm ngược phải lớn hơn hoặc bằng 0 và là số");
        $scope.lbl
        console.error("Lỗi không thể cập nhật thời gian điếm ngược", err);
      });
  };

  //Import excel tạo danh sách câu hỏi
  $scope.uploadFile = function () {
    const url = `${section_url}/${$scope.sectionId}/test/${$scope.testId}/import-quiz`;
    let fileInput = document.getElementById("fileInput");
    let fileImport = fileInput.files[0];
    const formData = new FormData();

    if (fileImport) {
      formData.append("file", fileImport);
      // Gọi API để tải lên file
      $http
        .post(url, formData, {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": undefined, // Để trình duyệt tự động thiết lập Content-Type cho multipart/form-data
          },
        })
        .then(
          function (response) {
            console.log(response);
            $scope.getTest();
            alert("Tải lên thành công!");
            let modal = bootstrap.Modal.getInstance(
              document.getElementById("fileModal")
            );
            modal.hide(); // Đóng modal
          },
          function (error) {
            alert("Lỗi: " + error.data);
          }
        );
    } else {
      alert("Vui lòng chọn file!");
    }
  };

  //HIển thị chi tiết câu hỏi
  $scope.showQuestionDetail = function (questioId) {
    $http
      .get(`${section_url}/${$scope.sectionId}/question-detail/${questioId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((resp) => {
        $scope.questionDetail = resp.data;
        console.log("Chi tiết bài kiểm tra", $scope.questionDetail);
      })
      .catch((err) => {
        //Nếu không có thì bài kiểm tra bằng null ng-show hiển thị kêu thêm test
        $scope.questionDetail = []; // listQuestion + testId
        console.error("Chi tiết bài kiểm tra", err);
      });
  };

  //Nút thêm đáp án
  $scope.addAnswer = function (questionId) {
    $http
      .post(
        `${section_url}/${$scope.sectionId}/question-detail/${questionId}/add-answer`,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      )
      .then((resp) => {
        $scope.showQuestionDetail(questionId);
      })
      .catch((err) => {
        console.error("Lỗi khi thêm đáp án", err);
      });
  };

  //Nút xóa đáp án
  $scope.removeAnswer = function (questionId, answerId) {
    const url = `${section_url}/${$scope.sectionId}/question-detail/${questionId}/remove-answer/${answerId}`;

    $http
      .delete(url, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((resp) => {
        // Gọi hàm để hiển thị lại chi tiết câu hỏi sau khi xóa
        $scope.showQuestionDetail(questionId);
        $scope.getTest();
        Swal.fire({
          title: "Xóa thành công",
          icon: "success",
          confirmButtonText: "OK",
        });
      })
      .catch((err) => {
        // Hiển thị thông báo lỗi cho người dùng
        console.error("Lỗi khi xóa đáp án:", err);
        alert("Có lỗi xảy ra khi xóa đáp án. Vui lòng thử lại.");
      });
  };

  //Hàm cập nhật nội dung câu hỏi
  let timeoutPromise;

  $scope.onQuestionChange = function (questionId) {
    if (timeoutPromise) {
      $timeout.cancel(timeoutPromise);
    }

    timeoutPromise = $timeout(function () {
      // Gọi API cập nhật câu hỏi
      $http
        .put(
          `${section_url}/${$scope.sectionId}/question-detail/${questionId}`,
          {
            contents: $scope.questionDetail.contents, // Gửi nội dung câu hỏi
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json", // Đảm bảo thiết lập Content-Type
            },
          }
        )
        .then(function (response) {
          console.log("Cập nhật câu hỏi thành công", response.data);
        })
        .catch(function (error) {
          console.error("Lỗi cập nhật câu hỏi", error);
        });
    }, 2000); // 2 giây
  };

  //Cập nhật nội dung đáp án
  $scope.onAnswerChange = function (questionId, answer) {
    if (timeoutPromise) {
      $timeout.cancel(timeoutPromise);
    }

    timeoutPromise = $timeout(function () {
      // Gọi API cập nhật đáp án
      $http
        .put(
          `${section_url}/${$scope.sectionId}/question-detail/${questionId}/update-answer/${answer.answerId}`,
          {
            content: answer.text, // Gửi nội dung đáp án như một đối tượng
            isCorrect: answer.correct, // Nếu bạn có trường này trong model
          },
          {
            headers: {
              Authorization: `Bearer ${token}`,
              "Content-Type": "application/json", // Đảm bảo thiết lập Content-Type
            },
          }
        )
        .then(function (response) {
          console.log("Cập nhật đáp án thành công", response.data);
        })
        .catch(function (error) {
          console.error("Lỗi cập nhật đáp án", error);
        });
    }, 2000); // 2 giây
  };

  //Cập nhật trạng thái đáp án khi onclick
  $scope.updateCorrectAnswer = function (questionId, selectedAnswer) {
    console.log("Cập nhật trạng thái đáp án");

    // Đặt tất cả các đáp án thành false
    angular.forEach($scope.questionDetail.listAnswerDTO, function (answer) {
      answer.correct = false; // Đặt tất cả đáp án thành không đúng
    });

    // Đặt đáp án được chọn thành true
    selectedAnswer.correct = true;
    console.log($scope.questionDetail);

    // Gửi yêu cầu cập nhật lên server
    $http
      .put(
        `${section_url}/${$scope.sectionId}/question-detail/${questionId}/update-anwer-correct`,
        $scope.questionDetail.listAnswerDTO, // Gửi trực tiếp danh sách
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      )
      .then(function (response) {
        $scope.getTest();
        console.log("Câu hỏi đã được cập nhật", response.data);
      })
      .catch(function (error) {
        console.error("Có lỗi xảy ra khi cập nhật câu hỏi", error);
      });
  };

  // Lưu questionId vào biến tạm thời
  $scope.setQuestionIdToDelete = function (questionId) {
    $scope.questionIdToDelete = questionId;
  };

  // Xóa câu hỏi khi người dùng xác nhận
  $scope.confirmDeleteQuestion = function () {
    if ($scope.questionIdToDelete) {
      const url = `${section_url}/${$scope.sectionId}/question-detail/${$scope.questionIdToDelete}/remove-question`;

      $http
        .delete(url, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then((resp) => {
          // Gọi hàm để hiển thị lại chi tiết câu hỏi sau khi xóa
          $scope.showQuestionDetail($scope.questionIdToDelete);
          $scope.getTest();
          Swal.fire({
            title: "Xóa thành công",
            icon: "success",
            confirmButtonText: "OK",
          });
        })
        .catch((err) => {
          // Hiển thị thông báo lỗi cho người dùng
          console.error("Lỗi khi xóa đáp án:", err);
          alert("Có lỗi xảy ra khi xóa đáp án. Vui lòng thử lại.");
        });
    }
  };

  //Thêm câu hỏi mói
  $scope.newQuestion = {
    contents: "",
    listAnswer: [],
  };

  // Mở modal để thêm câu hỏi mới
  $scope.openAddQuestionModal = function () {
    $scope.newQuestion = {
      contents: "",
      listAnswerDTO: [{ text: "", correct: false }], // Khởi tạo một đáp án rỗng
    };
    $("#addModal").modal("show");
  };

  // Thêm đáp án mới vào câu hỏi
  $scope.addAnswerToNewQuestion = function () {
    $scope.newQuestion.listAnswerDTO.push({ text: "" });
  };
  $scope.addAnswerToNewQuestion = function () {
    $scope.newQuestion.listAnswerDTO.push({ text: "" });
  };
  $scope.updateCorrectNewAnswer = function (selectedAnswer) {
    // Duyệt qua tất cả các đáp án và thiết lập correct thành false
    $scope.newQuestion.listAnswerDTO.forEach(function (answer) {
      answer.correct = false; // Đặt tất cả đáp án thành false
    });

    // Đặt đáp án được chọn thành true
    selectedAnswer.correct = true;
  };
  $scope.createQuestion = function () {
    if (!$scope.newQuestion.contents) {
      alert("Vui lòng nhập nội dung câu hỏi.");
      return;
    }

    const url = `${section_url}/${$scope.sectionId}/test-manager/${$scope.testId}/add-question`;
    console.log($scope.newQuestion);

    $http
      .post(url, $scope.newQuestion, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((resp) => {
        console.log(resp);
        Swal.fire({
          title: "Tạo câu hỏi thành công",
          icon: "success",
          confirmButtonText: "OK",
        });
        $("#editModal").modal("hide"); // Đóng modal sau khi tạo thành công
        $scope.getTest(); // Cập nhật danh sách câu hỏi
      })
      .catch((err) => {
        console.error("Lỗi khi tạo câu hỏi:", err);
        alert("Có lỗi xảy ra khi tạo câu hỏi. Vui lòng thử lại.");
      });
  };
  //Kết thúc phương thức thêm câu hỏi mới
  // Tạo bài kiểm tra mới
  $scope.addTest = function () {
    const url = `${section_url}/${$scope.sectionId}/add-test`;
    $http
      .post(
        url,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      )
      .then((resp) => {
        Swal.fire({
          title: "Tạo bài kiểm tra thành công tiếp tục nhập câu hỏi và đáp án",
          icon: "success",
          confirmButtonText: "OK",
        });
        $scope.getTest(); // Cập nhật danh sách câu hỏi
      })
      .catch((err) => {
        console.error("Lỗi khi tạo câu hỏi:", err);
        alert("Có lỗi xảy ra khi tạo câu hỏi. Vui lòng thử lại.");
      });
  };
  // Kết thúc tạo bài kiểm tra mới

  /////////////////////////          END QUIZ             //////////////
};

app.controller("SectionManagerController", SectionManagerController);
