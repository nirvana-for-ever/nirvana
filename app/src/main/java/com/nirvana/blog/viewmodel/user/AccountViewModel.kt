package com.nirvana.blog.viewmodel.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nirvana.blog.entity.network.RespResult
import com.nirvana.blog.entity.network.user.LoginUserVo
import com.nirvana.blog.entity.ui.UiResult
import com.nirvana.blog.entity.ui.user.SimpleUserInfo
import com.nirvana.blog.fragment.user.LoginFragment
import com.nirvana.blog.repository.AccountRepository
import com.nirvana.blog.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: AccountRepository
): ViewModel() {

    val loginRespResult by lazy {
        MutableLiveData<UiResult<Any>>()
    }

    val simpleUserInfo by lazy {
        MutableLiveData<SimpleUserInfo>()
    }

    /**
     * 登出完成后，告知 UI 已经完成登出，可以退出界面
     */
    val logoutFinished by lazy {
        MutableLiveData<Boolean>()
    }

    /**
     * 登录：需要判断格式是否正确
     */
    fun login(account: String, password: String, loginType: LoginFragment.LoginType) {
        viewModelScope.launch(Dispatchers.IO) {
            when (loginType) {
                LoginFragment.LoginType.CODE -> {
                    // account 必须是手机号
                    if (account.matches(Regex(Constants.PHONE_REGEX))) {
                        val resp = repository.login(LoginUserVo(account, password, Constants.CODE_LOGIN))
                        loginRespResult.postValue(UiResult(resp.success, resp.message))
                    } else {
                        loginRespResult.postValue(UiResult(false, "手机格式错误，请重试"))
                    }
                }
                LoginFragment.LoginType.PWD -> {
                    // account 可以是手机号和邮箱
                    if (account.matches(Regex(Constants.PHONE_REGEX)) ||
                        account.matches(Regex(Constants.EMAIL_REGEX))) {
                        val resp = repository.login(LoginUserVo(account, password, Constants.PASSWORD_LOGIN))
                        loginRespResult.postValue(UiResult(resp.success, resp.message))
                    } else {
                        loginRespResult.postValue(UiResult(false, "手机/邮箱格式错误，请重试"))
                    }
                }
            }
        }
    }

    /**
     * 获取用户信息
     */
    fun info(type: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val resp: RespResult<SimpleUserInfo> = repository.info(type)
            if (resp.success) {
                when (type) {
                    Constants.GET_USER_INFO_SIMPLE -> simpleUserInfo.postValue(resp.data)
                    Constants.GET_USER_INFO_DETAIL -> simpleUserInfo.postValue(resp.data)
                }
            } else {
                simpleUserInfo.postValue(null)
            }
        }
    }

    /**
     * 登出
     */
    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.logout().success) {
                logoutFinished.postValue(true)
            } else {
                logoutFinished.postValue(false)
            }
        }
    }

}