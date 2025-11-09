package com.quddaz.stock_simulator.global.initializer

import com.quddaz.stock_simulator.domain.user.entity.Role
import com.quddaz.stock_simulator.domain.user.entity.SocialType
import com.quddaz.stock_simulator.domain.user.entity.User
import com.quddaz.stock_simulator.domain.user.repository.UserRepository
import com.quddaz.stock_simulator.global.util.BaseDataInit
import jakarta.annotation.PostConstruct

@BaseDataInit
class BaseDataInitializer(
    private val userRepository: UserRepository,
) {

    @PostConstruct
    fun init() {
        initCompany()
        initEvents()
        initTestAdminUser()
    }

    private fun initCompany() {
    }

    private fun initEvents() {
    }

    private fun initTestAdminUser() {
        if (!userRepository.existsByEmail("admin@example.com")) {
            userRepository.save(
                User(
                    name = "admin",
                    email = "admin@example.com",
                    socialType = SocialType.GOOGLE,
                    socialId = "admin-social-id",
                    role = Role.ADMIN
                )
            )
        }
    }
}
