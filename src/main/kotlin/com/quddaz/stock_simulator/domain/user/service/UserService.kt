package com.quddaz.stock_simulator.domain.user.service

import com.quddaz.stock_simulator.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
){

}