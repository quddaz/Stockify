package com.quddaz.stock_simulator.domain.user

import com.quddaz.stock_simulator.domain.user.entity.Role
import com.quddaz.stock_simulator.domain.user.entity.SocialType
import com.quddaz.stock_simulator.domain.user.entity.User
import com.quddaz.stock_simulator.domain.user.exception.UserDomainException
import com.quddaz.stock_simulator.domain.user.exception.errorcode.UserErrorCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest
class UserTest {
    fun createUser(): User = User(
        socialId = "socialId",
        email = "email",
        name = "name",
        socialType = SocialType.GOOGLE,
        role = Role.USER
    )

    @Test
    fun `프로필 업데이트`() {
        // given
        val user = createUser()

        // when
        user.updateProfile("김철수", "new@email.com")

        // then
        assertThat(user.name).isEqualTo("김철수")
        assertThat(user.email).isEqualTo("new@email.com")
    }

    @Test
    fun `돈 사용 정상 차감`() {
        // given
        val user = createUser()

        // when
        user.spend(1_000_000L)

        // then
        assertThat(user.money).isEqualTo(9_000_000L)
    }

    @Test
    fun `돈 사용 금액 부족`() {
        // given
        val user = createUser()

        // when
        val exception = assertThrows<UserDomainException> {
            user.spend(20_000_000L)
        }

        // when & then
        assertEquals(UserErrorCode.USER_MONEY_INSUFFICIENT, exception.errorCode)
    }

    @Test
    fun `돈 벌기`() {
        // given
        val user = createUser()

        // when
        user.earn(500_000L)

        // then
        assertThat(user.money).isEqualTo(10_500_000L)
    }

}