app.controller(
  "takeTheTestController",
  function ($scope, $http, $window, $location) {
    const token = $window.sessionStorage.getItem("jwt_token");
    if (token == null) {
      $window.location.href =
        "http://127.0.0.1:5500/index.html#!/assets/views/dang_nhap";
    }

    // Lấy giá trị của id từ search parameters trong URL
    $scope.id = $location.search().id;
    $scope.testId = $scope.id;
    $scope.quizs = {};
    $scope.selectedAnswers = []; // Một list chứa đáp án đã chọn
    $scope.pageSize = 1;
    $scope.start = 0;
    $scope.submissionTime = null;
    $scope.isSubmitted = false;
    $scope.userId = 2;
    $scope.timeIsUp = false;

    $scope.goBack = function () {
      window.history.back();
    };

    $scope.handleSubmit = function () {
      const totalQuestions = $scope.quizs.listQuestion.length;
      const answeredQuestions = $scope.selectedAnswers.filter(
        (answer) => answer !== null
      ).length;

      console.log($scope.timeIsUp);
      

      if ($scope.timeIsUp == true) {
        $http
          .post(
            `http://localhost:8080/api/v1/test/submit/${$scope.id}`,
            {},
            {
              headers: {
                Authorization: `Bearer ${token}`,
              },
            }
          )
          .then(
            function (res) {
              $scope.findTestResult();
            },
            function (error) {
              alert("Lỗi khi nộp bài. Vui lòng thử lại!");
            }
          );
        return;
      }

      if (answeredQuestions < totalQuestions) {
        alert(
          `Bạn đã trả lời ${answeredQuestions}/${totalQuestions} câu hỏi. Vui lòng hoàn thành tất cả trước khi nộp bài.`
        );
        return;
      }

      if (!confirm("Bạn có chắc chắn muốn nộp bài?")) return;

      $http
        .post(
          `http://localhost:8080/api/v1/test/submit/${$scope.id}`,
          {},
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
        .then(
          function (res) {
            console.log(res);
            $scope.findTestResult();
            alert("Nộp bài thành công!");
          },
          function (error) {
            alert("Lỗi khi nộp bài. Vui lòng thử lại!");
          }
        );
    };

    $scope.findTestResult = function () {
      $http
        .get(
          "http://localhost:8080/api/v1/test/check-if-the-user-has-taken-the-quiz/" +
            $scope.testId,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
        .then(
          function (res) {
            $scope.userTestResult = res.data; //UserTestResult - Entity
            $scope.isSubmitted = true; //Hiển thị kết quả
            $scope.userId = $scope.userTestResult.user.userId;
            console.log("Kết quả kiểm tra mới nhất:");
            console.log($scope.userTestResult);
          },
          function (err) {
            $scope.isSubmitted = false; //Hiển thị bài kiểm tra
            console.log(err.message);
          }
        );
    };

    $scope.startCountdown = function (seconds) {
      // Chuyển đổi giây thành định dạng hh:mm:ss
      function formatTime(seconds) {
        const hours = Math.floor(seconds / 3600);
        const minutes = Math.floor((seconds % 3600) / 60);
        const secs = seconds % 60;
        return `${String(hours).padStart(2, "0")}:${String(minutes).padStart(
          2,
          "0"
        )}:${String(secs).padStart(2, "0")}`;
      }

      // Cập nhật thời gian ban đầu
      $scope.countdown = formatTime(seconds);

      // Bắt đầu đếm ngược
      countdownTimer = setInterval(function () {
        seconds--;

        // Cập nhật thời gian còn lại
        $scope.countdown = formatTime(seconds);
        $scope.$apply(); // Cập nhật giao diện

        // Nếu hết thời gian, tự động nộp bài
        if (seconds <= 0) {
          clearInterval(countdownTimer);
          alert("Thời gian đã hết! Bài kiểm tra sẽ được nộp tự động.");
          $scope.timeIsUp = true;
          $scope.handleSubmit(); // Gọi hàm nộp bài
        }
      }, 1000);
    };

    // Khởi tạo khi load trang
    $scope.init = function () {
     
      if ($scope.isSubmitted) {
        console.log($scope.isSubmitted);
        $scope.showTest();
      } else {
        $scope.isSubmitted = false;
        // Lấy danh sách câu hỏi từ API
        $http
          .get("http://localhost:8080/api/v1/test/take-the-test/" + $scope.id, {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          })
          .then(
            function (res) {
              if (
                res.data &&
                res.data.listQuestion &&
                res.data.listQuestion.length > 0
              ) {
                $scope.quizs = res.data;
                console.log("Bài kiểm tra: ");
                console.log($scope.quizs);
                $scope.loadSelectedAnswers();
              } else {
                $scope.quizs = { listQuestion: [] };
              }
            },
            function (err) {
              console.error("Lỗi khi request môn học:", err);
              alert(
                "Lỗi khi request môn học: " +
                  err.status +
                  " - " +
                  err.statusText
              );
              $scope.quizs = { listQuestion: [] };
            }
          );
        // Bắt đầu đếm ngược nếu có countdownTimer
        if ($scope.quizs.countdownTimer > 0) {
          $scope.timeIsUp = true;
          $scope.startCountdown($scope.quizs.countdownTimer);
        }
      }
    };

    $scope.showTest = function () {
      $http
        .get("http://localhost:8080/api/v1/test/take-the-test/" + $scope.id, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        })
        .then(
          function (res) {
            if (
              res.data &&
              res.data.listQuestion &&
              res.data.listQuestion.length > 0
            ) {
              $scope.quizs = res.data;
              console.log($scope.quizs);
              $scope.loadSelectedAnswers();
            } else {
              $scope.quizs = { listQuestion: [] };
            }
          },
          function (err) {
            console.error("Lỗi khi request môn học:", err);
            alert(
              "Lỗi khi request môn học: " + err.status + " - " + err.statusText
            );
            $scope.quizs = { listQuestion: [] };
          }
        );
    };
    $scope.first = function () {
      $scope.start = 0;
    };
    $scope.prev = function () {
      if ($scope.start > 0) $scope.start -= $scope.pageSize;
    };
    $scope.next = function () {
      if ($scope.start < $scope.quizs.listQuestion.length - $scope.pageSize)
        $scope.start += $scope.pageSize;
    };
    $scope.last = function () {
      $scope.start = $scope.quizs.listQuestion.length - $scope.pageSize;
    };

    // Hàm để tải danh sách đáp án đã chọn
    $scope.loadSelectedAnswers = function () {
      $http
        .get(
          `http://localhost:8080/api/v1/user_answer_history/get-answers/${$scope.id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
        .then(
          function (answerRes) {
            if (answerRes.data) {
              console.log("Danh sách đáp án đã chọn");
              console.log(answerRes.data);
              answerRes.data.forEach((answer) => {
                const questionId = answer.questionId; // Lấy questionId
                const answerId = answer.answerId; // Lấy answerId

                // Đảm bảo selectedAnswers đã được khởi tạo
                if (!$scope.selectedAnswers[questionId - 1]) {
                  $scope.selectedAnswers[questionId - 1] = {};
                }
                // Gán answerId vào selectedAnswers
                $scope.selectedAnswers[questionId - 1].answerId = answerId;
              });
            }
          },
          function (error) {
            console.error("Lỗi khi lấy đáp án đã làm:", error);
          }
        );
    };
    // Gọi hàm loadSelectedAnswers khi controller được khởi tạo

    $scope.selectAnswer = function (questionId, answerId) {
      questionId = Number(questionId);
      answerId = Number(answerId);
      if (!isNaN(questionId) && !isNaN(answerId)) {
        // Gửi yêu cầu POST với FormData để cập nhật đáp án
        var formData = new FormData();
        formData.append("maBaiKiemTra", $scope.id); // mã bài kiểm tra
        formData.append("maCauHoi", questionId);
        formData.append("maDapAn", answerId);

        $http
          .post(
            "http://localhost:8080/api/v1/user_answer_history/save-answer",
            formData,
            {
              headers: {
                Authorization: `Bearer ${token}`,
                "Content-Type": undefined,
              },
            }
          )
          .then(
            function (res) {
              console.log("Cập nhật đáp án thành công:", res.data);
              // Gọi lại hàm để tải danh sách đáp án đã chọn
              $scope.loadSelectedAnswers();
            },
            function (error) {
              console.error("Lỗi khi lưu đáp án:", error);
            }
          );
      }
    };

    $scope.isUserAnswer = function (questionId, answerId) {
      const userAnswer = $scope.selectedAnswers[questionId - 1];
      return userAnswer && userAnswer.answerId === answerId;
    };

    // Kiểm tra nếu tất cả câu hỏi đã được trả lời
    $scope.validateAllAnswered = function () {
      return $scope.selectedAnswers.every((answer) => answer !== null);
    };

    // Hàm xóa lịch sử đáp án
    $scope.clearAnswerHistory = function () {
      return $http
        .delete(
          `http://localhost:8080/api/v1/test/delete-answers/${$scope.id}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        )
        .then(
          function (response) {
            console.log("Lịch sử đáp án đã được xóa thành công.");
            $scope.selectedAnswers = []; // Xóa tất cả các đáp án đã chọn trong scope
          },
          function (error) {
            console.error("Lỗi khi xóa lịch sử đáp án:", error);
          }
        );
    };

    $scope.resetQuiz = function () {
      $scope.clearAnswerHistory().then(function () {
        $scope.selectedAnswers = "";
        // Kiểm tra xem quizs.listQuestion có hợp lệ không trước khi sử dụng length
        if ($scope.quizs && $scope.quizs.listQuestion) {
          $scope.selectedAnswers = new Array(
            $scope.quizs.listQuestion.length
          ).fill(null);
        } else {
          $scope.selectedAnswers = []; // Nếu không có listQuestion thì reset mảng câu trả lời
        }
        $scope.isSubmitted = false;

        $scope.init();
      });
    };

    // Gọi init khi controller được load
    $scope.findTestResult();
    $scope.init();
    $scope.loadSelectedAnswers();
  }
);
