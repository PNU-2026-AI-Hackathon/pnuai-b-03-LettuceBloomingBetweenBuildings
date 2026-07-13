package ai.pnu.b03.minifarm.dto;

import ai.pnu.b03.minifarm.common.exception.ErrorCode;
import lombok.Getter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final CustomError error;

    // 성공 시 응답 (전달할 데이터가 있는 경우)
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null);
    }

    // 성공 시 응답 (전달할 데이터 없는 경우)
    public static ApiResponse<Void> success() {
        return new ApiResponse<>(true, null, null);
    }

    // 에러 발생 시 응답
    public static ApiResponse<Void> error(ErrorCode errorCode) {
        return new ApiResponse<>(false, null, new CustomError(errorCode));
    }

    @Getter
    public static class CustomError {
        private final String code;
        private final String message;

        public CustomError(ErrorCode errorCode) {
            this.code = errorCode.getCode();
            this.message = errorCode.getMessage();
        }
    }
}