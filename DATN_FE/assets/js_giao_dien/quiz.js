const questions = [
    { question: "Có mấy loại Service?", answers: ["3", "4", "1", "2"], correct: 1 },
    { question: "JavaScript là ngôn ngữ?", answers: ["Lập trình", "Đánh dấu", "Kiểu dữ liệu", "Trình duyệt"], correct: 0 },
    { question: "HTML là viết tắt của?", answers: ["Hypertext Markup Language", "Home Tool Markup Language", "Hyperlink Markup Language", "Hypertext Media Language"], correct: 0 },
    { question: "CSS viết tắt của?", answers: ["Cascading Style Sheets", "Central Style System", "Cascading Simple Sheets", "Central Style Sheets"], correct: 0 },
    { question: "PHP là viết tắt của?", answers: ["Personal Hypertext Preprocessor", "Hypertext Preprocessor", "PHP: Hypertext Preprocessor", "Private Home Page"], correct: 2 },
    { question: "React là framework của?", answers: ["JavaScript", "PHP", "Python", "Ruby"], correct: 0 },
    { question: "SQL dùng để?", answers: ["Query cơ sở dữ liệu", "Tạo website", "Viết API", "Lập trình frontend"], correct: 0 },
    { question: "Node.js là gì?", answers: ["Runtime environment", "Web browser", "Ngôn ngữ lập trình", "Cơ sở dữ liệu"], correct: 0 },
    { question: "Python là ngôn ngữ?", answers: ["Biên dịch", "Thông dịch", "Kiểu lập trình hàm", "Chạy trên trình duyệt"], correct: 1 },
    { question: "JavaScript được dùng để?", answers: ["Lập trình server", "Lập trình web", "Cả hai", "Không phải câu nào đúng"], correct: 2 }
];

let currentQuestionIndex = 0;
let userAnswers = Array(questions.length).fill(null);

const questionText = document.getElementById("question-text");
const answerInputs = document.querySelectorAll('input[name="answer"]');
const answerLabels = document.querySelectorAll(".form-check-label");
const prevBtn = document.getElementById("prev-btn");
const nextBtn = document.getElementById("next-btn");
const submitBtn = document.getElementById("submit-btn");
const currentQuestionSpan = document.getElementById("current-question");


function loadQuestion(index) {
    const currentQuestion = questions[index];
    questionText.textContent = `Câu hỏi ${index + 1}: ${currentQuestion.question}`;
    answerLabels.forEach((label, i) => {
        label.textContent = currentQuestion.answers[i];
        answerInputs[i].checked = userAnswers[index] === i;
    });
    currentQuestionSpan.textContent = index + 1;

    prevBtn.disabled = index === 0;
    
    // Cập nhật trạng thái nút Next
    if (index === questions.length - 1) {
        nextBtn.disabled = true;  // Vô hiệu hóa nút Next ở câu cuối
    } else {
        nextBtn.disabled = false; // Cho phép nhấn nếu chưa phải câu cuối
    }

    submitBtn.style.display = index === questions.length - 1 ? "inline-block" : "none"; // Hiển thị nút Nộp bài ở câu cuối
}

nextBtn.addEventListener("click", () => {
    const selectedAnswer = Array.from(answerInputs).findIndex(input => input.checked);
    if (selectedAnswer !== -1) {
        userAnswers[currentQuestionIndex] = selectedAnswer;
    }

    if (currentQuestionIndex < questions.length - 1) {
        currentQuestionIndex++;
        loadQuestion(currentQuestionIndex);
    }
});

prevBtn.addEventListener("click", () => {
    if (currentQuestionIndex > 0) {
        currentQuestionIndex--;
        loadQuestion(currentQuestionIndex);
    }
});



loadQuestion(currentQuestionIndex);

submitBtn.addEventListener("click", () => {
    const confirmed = confirm("Bạn có chắc muốn nộp bài?");
    if (confirmed) {
        // Lưu kết quả vào sessionStorage
        sessionStorage.setItem('quizResults', JSON.stringify(userAnswers));
        sessionStorage.setItem('quizQuestions', JSON.stringify(questions));

        // Chuyển hướng sang trang kết quả
        window.location.href = 'result.html';
    }
});





