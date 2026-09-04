package practice;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.*;

class FixedLengthMessageTest {

    @Test
    @DisplayName("문자 필드를 offset 과 length 로 꺼낸다")
    void 문자_필드_추출() {
        // given
        FixedLengthMessage message = new FixedLengthMessage("홍길동    0000012345");

        // when
        String name = message.getString(0, 7);

        // then
        assertThat(name).isEqualTo("홍길동");
    }

    @Test
    @DisplayName("우측 공백만 제거하고 좌측 공백은 남긴다.")
    void 우측_공백만_제거() {
        //given
        FixedLengthMessage message = new FixedLengthMessage(" 김철수    ");

        // when
        String name = message.getString(0, 7);

        //then
        assertThat(name).isEqualTo(" 김철수").hasSize(4);
    }

    @Test
    @DisplayName("숫자 필드의 좌측 0 패딩이 제거되고 숫자로 나온다.")
    void 숫자_좌측_0공백_제거(){
        //given
        FixedLengthMessage message = new FixedLengthMessage("A00012345");

        // when
        long number = message.getNumber(1, 8);

        //then
        assertThat(number).isEqualTo(12345L);
    }

    @Test
    @DisplayName("길이는 바이트가 아니라 문자 수로 센다")
    void 전문_길이_반환(){
        //given
        FixedLengthMessage message = new FixedLengthMessage("가나다라    12345");

        //when
        int length = message.length();

        //then
        assertThat(length).isEqualTo(13);
    }

    // 경계값 예외 테스트
    @Test
    @DisplayName("offset 0 은 유효한 숫자이다.")
    void offset_하한_통과(){
        //given
        FixedLengthMessage message = new FixedLengthMessage("ABCDEFG");

        //when
        String result = message.getString(0, 1);

        //then
        assertThat(result).isEqualTo("A");
    }

    @Test
    @DisplayName("offset 이 음수면 실패한다.")
    void offset_하한_초과(){

        //given
        FixedLengthMessage message = new FixedLengthMessage("ABCDEFG");

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> message.getString(-1, 1))
                .withMessageContaining("offset 은 0 이상");
    }

    @Test
    @DisplayName("전문 끝까지 정확히 채우는 필드는 읽을 수 있다.")
    void 상한_통과(){
        //given
        FixedLengthMessage message = new FixedLengthMessage("ABCDEFG");

        //when
        String result = message.getString(4,3);

        // then
        assertThat(result).isEqualTo("EFG");
    }

    @Test
    @DisplayName("offset + length 가 전문 길이를 넘으면 실패한다.")
    void 상한_초과(){
        //given
        FixedLengthMessage message = new FixedLengthMessage("ABCDEFG");

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> message.getString(5, 3))
                .withMessageContaining("전문 길이를 넘어섰습니다");
    }

    @Test
    @DisplayName("길이가 1인 문자 필드도 사용 가능하다.")
    void 문자_최소길이(){
        //given
        FixedLengthMessage message = new FixedLengthMessage("A");

        //when
        String result = message.getString(0,1);

        //then
        assertThat(result).isEqualTo("A");
    }

    @Test
    @DisplayName("숫자 필드가 공백뿐이면 0 으로 한다.")
    void 공백_숫자_필드(){
        //given
        FixedLengthMessage message = new FixedLengthMessage("     ");

        //when
        long result = message.getNumber(0, 5);

        //then
        assertThat(result).isEqualTo(0L);
    }

    @Test
    @DisplayName("생성자에 null을 넣으면 실패한다.")
    void null_필드_실패(){
        //given & when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new FixedLengthMessage(null))
                .withMessageContaining("전문이 null");
    }

    @Test
    @DisplayName("숫자 필드에 숫자가 아닌 값이 들어 있으면 실패한다.")
    void 숫자_필드_숫자_아님(){
        //given
        FixedLengthMessage message = new FixedLengthMessage("A1234B678");

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> message.getNumber(1, 8))
                .withMessageContaining("숫자 필드가 아닙니다");
    }

    @Test
    @DisplayName("길이가 0인 필드는 실패한다.")
    void 길이_0_실패(){
        //given
        FixedLengthMessage message = new FixedLengthMessage("ABCDEFG");

        //when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> message.getString(0,0))
                .withMessageContaining("length 는 1 이상");
    }

}
