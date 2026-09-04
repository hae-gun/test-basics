package practice;

/**
 * 고정길이 전문에서 필드를 꺼내는 파서.
 *
 * 전문은 구분자 없이 붙어 있고, 각 필드는 시작 위치(offset)와 길이(length)로 지정한다.
 * 문자 필드는 우측을 공백으로 채우고, 숫자 필드는 좌측을 0으로 채우는 것이 관례다.
 *
 * 예) "홍길동    0000012345"
 *      offset 0, length 10 -> "홍길동"
 *      offset 10, length 10 -> 12345
 *
 * 이 클래스의 테스트를 직접 작성한다. 구현은 건드리지 않는다.
 */
public class FixedLengthMessage {

    private final String raw;

    public FixedLengthMessage(String raw) {
        if (raw == null) {
            throw new IllegalArgumentException("전문이 null 입니다");
        }
        this.raw = raw;
    }

    /**
     * 문자 필드를 꺼낸다. 우측 공백은 제거한다.
     */
    public String getString(int offset, int length) {
        return slice(offset, length).stripTrailing();
    }

    /**
     * 숫자 필드를 꺼낸다. 좌측 0 패딩은 제거되어 숫자로 반환된다.
     * 필드가 공백뿐이면 0 으로 본다.
     */
    public long getNumber(int offset, int length) {
        String value = slice(offset, length).strip();
        if (value.isEmpty()) {
            return 0L;
        }
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "숫자 필드가 아닙니다. offset=%d, length=%d, value=%s".formatted(offset, length, value));
        }
    }

    public int length() {
        return raw.length();
    }

    private String slice(int offset, int length) {
        if (offset < 0 || length <= 0) {
            throw new IllegalArgumentException(
                    "offset 은 0 이상, length 는 1 이상이어야 합니다. offset=%d, length=%d".formatted(offset, length));
        }
        if (offset + length > raw.length()) {
            throw new IllegalArgumentException(
                    "전문 길이를 넘어섰습니다. 요청=%d, 전문길이=%d".formatted(offset + length, raw.length()));
        }
        return raw.substring(offset, offset + length);
    }
}
