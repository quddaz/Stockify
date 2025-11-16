package com.quddaz.stock_simulator.domain.user

import com.quddaz.stock_simulator.domain.user.entity.Role
import com.quddaz.stock_simulator.domain.user.entity.SocialType
import com.quddaz.stock_simulator.domain.user.entity.User
import com.quddaz.stock_simulator.domain.user.repository.UserRepository
import com.quddaz.stock_simulator.domain.user.service.UserService
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import kotlin.test.Test
import kotlin.test.assertEquals

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class UserServiceIntegrationTest(
    @Autowired private val userService: UserService,
    @Autowired private val userRepository: UserRepository
) {

    private val testEmail = "admin@test.com"
    private val initialMoney = 10_000_000L

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
    }

    @Test
    fun `관리자 유저 초기화`() {
        // given & when
        userService.initAdminUser(testEmail, initialMoney)

        // then
        val admin = userRepository.findByEmail(testEmail)
        assert(admin != null)
        assert(admin!!.role == Role.ADMIN)
        assert(admin.money == initialMoney)
    }

    @Test
    fun `모든 유저 자금 초기화`() {
        // given
        userRepository.save(
            User("user1", "user1@test.com", SocialType.GOOGLE, "id1", Role.USER, money = 1_000L)
        )
        userRepository.save(
            User("user2", "user2@test.com", SocialType.GOOGLE, "id2", Role.USER, money = 2_000L)
        )

        // when
        userService.resetAllUserMoney(initialMoney)

        // then
        val users = userRepository.findAll()
        assertEquals(users.get(0).money, initialMoney)
    }

    @Test
    fun `유저 조회`() {
        // given
        val saved = userRepository.save(
            User("userX", "userX@test.com", SocialType.GOOGLE, "idX", Role.USER)
        )

        // when
        val foundById = userService.findById(saved.id!!)
        val foundBySocialId = userService.findBySocialId(saved.socialId)

        // then
        assertTrue(foundById.id == saved.id)
        if (foundBySocialId != null) {
            assertTrue(foundBySocialId.socialId == saved.socialId)
        }
    }
}
