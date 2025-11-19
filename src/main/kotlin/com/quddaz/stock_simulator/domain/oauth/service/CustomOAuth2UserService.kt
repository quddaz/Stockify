package com.quddaz.stock_simulator.domain.oauth.service

import com.quddaz.stock_simulator.domain.oauth.entity.CustomOAuth2User
import com.quddaz.stock_simulator.domain.oauth.exception.LoginTypeNotSupportException
import com.quddaz.stock_simulator.domain.oauth.exception.errorcode.AuthErrorCode
import com.quddaz.stock_simulator.domain.oauth.format.GoogleResponse
import com.quddaz.stock_simulator.domain.oauth.format.Oauth2Response
import com.quddaz.stock_simulator.domain.user.service.UserService
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService(
    private val userService: UserService
) : DefaultOAuth2UserService() {


    override fun loadUser(userRequest: OAuth2UserRequest): CustomOAuth2User {
        val oAuth2User = super.loadUser(userRequest)

        val oauthResponse = findOAuthResponse(userRequest, oAuth2User)

        return saveOrUpdate(oauthResponse)
    }

    private val providerMap: Map<String, (Map<String, Any>) -> Oauth2Response> = mapOf(
        "google" to { attrs -> GoogleResponse(attrs) },
    )

    private fun findOAuthResponse(userRequest: OAuth2UserRequest, oAuth2User: OAuth2User): Oauth2Response {
        val creator = providerMap[userRequest.clientRegistration.registrationId]
            ?: throw LoginTypeNotSupportException(AuthErrorCode.UNSUPPORTED_OAUTH_PROVIDER)
        return creator(oAuth2User.attributes)
    }

    private fun saveOrUpdate(oauthResponse: Oauth2Response): CustomOAuth2User {
        val user = userService.findBySocialId(oauthResponse.getProviderId())
            ?: userService.save(oauthResponse.toUser())

        return CustomOAuth2User.from(user)
    }

}