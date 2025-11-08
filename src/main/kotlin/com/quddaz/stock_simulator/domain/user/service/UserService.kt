package com.quddaz.stock_simulator.domain.user.service

import com.quddaz.stock_simulator.domain.user.exception.UserDomainException
import com.quddaz.stock_simulator.domain.user.exception.errorcode.UserErrorCode
import com.quddaz.stock_simulator.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun findBySocialId(socialId: String) =
        userRepository.findBySocialId(socialId) ?: throw UserDomainException(UserErrorCode.USER_NOT_FOUND)

    fun findById(id: Long) =
        userRepository.findById(id).orElseThrow { UserDomainException(UserErrorCode.USER_NOT_FOUND) }
}