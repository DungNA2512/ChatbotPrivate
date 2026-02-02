package com.eurostark.chatbot.chatbotbackend.entity;

public enum StepType {
    MENU, // chọn 1,2,3
    INPUT, // nhập dữ liệu (ngày, text, số)
    CONFIRM, // xác nhận (đồng ý / hủy)
    ACTION, // thực hiện hành động
}
