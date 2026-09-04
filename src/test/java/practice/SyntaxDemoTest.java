package practice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

// AssertJ 의 assertThat 계열을 static import 로 가져온다.
// JUnit 의 Assertions.assertEquals 도 있지만 AssertJ 쪽이 읽기 쉽고 라인이 요구한 도구다.
import static org.assertj.core.api.Assertions.*;

/**
 * 문법만 보는 파일이다. 여기 있는 걸 외울 필요는 없고,
 * 연습하다가 "이거 어떻게 쓰더라" 할 때 돌아와서 보면 된다.
 */
class SyntaxDemoTest {

    // @Test 가 붙은 메서드 하나가 테스트 하나다. public 일 필요 없다.
    @Test
    void 가장_단순한_테스트() {
        assertThat(1 + 1).isEqualTo(2);
    }

    // @DisplayName 은 실행 결과에 찍히는 이름이다.
    // 메서드 이름 대신 "무엇이 참이어야 하는가"를 문장으로 쓴다.
    @Test
    @DisplayName("문자열의 앞뒤 공백은 strip 으로 제거된다")
    void 이름은_문장으로() {
        String value = "  hello  ";

        assertThat(value.strip()).isEqualTo("hello");
    }

    @Test
    @DisplayName("given-when-then 으로 나누면 읽기 쉬워진다")
    void 구조() {
        // given - 준비
        List<String> names = List.of("kim", "lee", "park");

        // when - 실행
        int size = names.size();

        // then - 검증
        assertThat(size).isEqualTo(3);
    }

    @Nested
    @DisplayName("자주 쓰는 단언")
    class 자주_쓰는_단언 {

        @Test
        void 값_비교() {
            assertThat("결제완료").isEqualTo("결제완료");
            assertThat(10_000L).isGreaterThan(0L);
            assertThat(0).isZero();
            assertThat(true).isTrue();
            assertThat((String) null).isNull();
        }

        @Test
        void 문자열() {
            String message = "취소 가능 금액을 초과했습니다.";

            assertThat(message).contains("초과");
            assertThat(message).startsWith("취소");
            assertThat(message).hasSize(17);
        }

        @Test
        void 컬렉션() {
            List<Integer> amounts = List.of(3_000, 7_000);

            assertThat(amounts).hasSize(2);
            assertThat(amounts).containsExactly(3_000, 7_000);   // 순서까지 일치
            assertThat(amounts).contains(7_000);                  // 포함만 확인
            assertThat(amounts).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("예외 검증")
    class 예외_검증 {

        @Test
        @DisplayName("어떤 예외가 나는지 확인한다")
        void 예외_타입() {
            assertThatThrownBy(() -> Integer.parseInt("abc"))
                    .isInstanceOf(NumberFormatException.class);
        }

        @Test
        @DisplayName("메시지까지 확인한다")
        void 예외_메시지() {
            assertThatThrownBy(() -> { throw new IllegalArgumentException("금액은 0보다 커야 합니다"); })
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("0보다");
        }

        @Test
        @DisplayName("자주 쓰는 예외는 전용 메서드가 있다")
        void 전용_메서드() {
            assertThatIllegalArgumentException()
                    .isThrownBy(() -> { throw new IllegalArgumentException("잘못된 값"); })
                    .withMessageContaining("잘못된");
        }

        @Test
        @DisplayName("예외가 나지 않아야 하는 경우도 검증할 수 있다")
        void 예외_없음() {
            assertThatNoException()
                    .isThrownBy(() -> Integer.parseInt("123"));
        }
    }
}
