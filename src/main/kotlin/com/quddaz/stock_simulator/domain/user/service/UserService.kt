package com.quddaz.stock_simulator.domain.user.service

import com.quddaz.stock_simulator.domain.user.entity.Role
import com.quddaz.stock_simulator.domain.user.entity.SocialType
import com.quddaz.stock_simulator.domain.user.entity.User
import com.quddaz.stock_simulator.domain.user.exception.UserDomainException
import com.quddaz.stock_simulator.domain.user.exception.errorcode.UserErrorCode
import com.quddaz.stock_simulator.domain.user.repository.UserRepository
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val em : EntityManager
) {
    fun findBySocialId(socialId: String) =
        userRepository.findBySocialId(socialId) ?: throw UserDomainException(UserErrorCode.USER_NOT_FOUND)


    fun findById(id: Long) =
        userRepository.findById(id).orElseThrow { UserDomainException(UserErrorCode.USER_NOT_FOUND) }


    /** 관리자 유저 초기화 */
    @Transactional
    fun initAdminUser(email: String, money: Long) {
        if (!userRepository.existsByEmail(email)) {
            userRepository.save(
                User(
                    name = "admin",
                    email = email,
                    socialType = SocialType.GOOGLE,
                    socialId = "admin-social-id",
                    money = money,
                    role = Role.ADMIN
                )
            )
        }
    }

    //* 모든 유저의 자금을 초기화 *//
    @Transactional
    fun resetAllUserMoney(defaultMoney: Long) {
        userRepository.setAllUserDefaultMoney(defaultMoney)
        em.flush()   // 변경사항 DB 반영
        em.clear() // 영속성 컨텍스트 초기화
    }

}