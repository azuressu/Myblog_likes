package com.example.post3.exception;

public class NotFoundException extends RuntimeException{

    /* SerialVersionUID: Java의 직렬화와 관련된 필드, 사용자 정의 예외 클래스 정의 시 중요한 역할
    *                   Java에서 객체를 직렬화 하면 해당 객체의 상태를 byte stream으로 변환해 저장하거나 네트워크를 통해 전송할 수 있게 됨
    *                   이 직렬화된 byte stream을 다시 역직렬화해 객체로 복원 가능함
    *                   하지만 직렬화된 객체를 저장하거나 네트워크로 전송할 때, 클래스 구조가 변경되거나 업데이트 되는 경우 문제 발생 가능
    *                   이 때, SerialVersionUID를 사용해 클래스 버전을 명시해 지정해 문제 방지 가능 */

    /* SerialVersionUID는 직렬화 버전을 나타내는 식별자로, long 타입의 값이어야 함
       이 값은 직렬화 프로세스에서 사용되며, 역직렬화 시에도 사용됨
       SerialVersionUID를 명시적으로 설정함으로써 클래스의 구조가 변경되더라도
       이전에 직렬화된 데이터와 호환성을 유지할 수 있게 됨
        주의할 점은 serialVersionUID를 변경하면 이전 직렬화된 객체와의 호환성이 깨질 수 있으므로,
        클래스를 변경하는 경우 신중하게 관리해야 함
        클래스를 변경하더라도 serialVersionUID를 변경하지 않거나, 변경해야 하는 경우 호환되는 버전으로 변경해주는 것이 중요 */

    private static final long serialVersionUID = 1L;

    // 빈 생성자
    public NotFoundException() {}

    // Error message를 넣을 생성자
    /* public Throwable(String message) {
            fillInStackTrace();
            detailMessage = message;
       } */
    public NotFoundException(String message) {
        super(message);
    }

    // Throwable
    /* public Throwable(Throwable cause) {
            fillInStackTrace();
            detailMessage = (cause==null ? null : cause.toString());
            this.cause = cause;
        }  */
    public NotFoundException(Throwable throwable) {
        super(throwable);
    }

    // Error message와 Throwable
    /* public Throwable(String message, Throwable cause) {
            fillInStackTrace();
            detailMessage = message;
            this.cause = cause;
       } */
    public NotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /* protected Throwable(String message, Throwable cause,
                        boolean enableSuppression,
                        boolean writableStackTrace) {
            if (writableStackTrace) {
                fillInStackTrace();
            } else {
                stackTrace = null;
            }
            detailMessage = message;
            this.cause = cause;
            if (!enableSuppression)
                suppressedExceptions = null;
        }  */

    public NotFoundException(String message, Throwable throwable, boolean enableSuppression, boolean writableStackTrace) {
        super(message, throwable, enableSuppression, writableStackTrace);
    }

}