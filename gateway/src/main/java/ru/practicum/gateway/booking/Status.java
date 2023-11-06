package ru.practicum.gateway.booking;

public enum Status {
    WAITING,  // новое бронирование, ожидает одобрения
    APPROVED, // бронирование подтверждено владельцем
    REJECTED, // бронирование отклонено владельцем
    CANCELED, // бронирование отменено создателем
    ALL,      // по умолчанию
    CURRENT,  // текущие
    PAST,     // завершенные
    FUTURE,   // будущие
    UNSUPPORTED_STATUS,
    WRONG
}
