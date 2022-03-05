package com.nirvana.blog.fragment.user

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.nirvana.blog.R
import com.nirvana.blog.base.BaseFragment
import com.nirvana.blog.databinding.FragmentLoginBinding
import com.nirvana.blog.databinding.UserLoginProblemBinding
import com.nirvana.blog.utils.getMessageTextView
import com.nirvana.blog.utils.getNegBtnColor
import com.nirvana.blog.utils.getNeuBtnColor
import com.nirvana.blog.utils.getPosBtnColor

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    /**
     * 登录方式，默认是手机验证码登录
     */
    private var loginType = LoginType.CODE

    private lateinit var codeSpannableString: SpannableString
    private lateinit var pwdSpannableString: SpannableString

    // 其他方式登录的 AlertDialog
    private val otherLoginAlertDialog by lazy {
        AlertDialog.Builder(requireContext())
            .setTitle("牛蛙呐需要你的支持以开启社交登录")
            .setView(R.layout.user_login_other_login_alert_dialog)
            .setPositiveButton("支持支持!!", null)
            .setNeutralButton("不，我支持", null)
            .setNegativeButton("残忍支持", null)
            .create().apply {
                setOnShowListener {
                    getNegBtnColor().setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryVariant))
                    getNeuBtnColor().setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryVariant))
                    getPosBtnColor().setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryVariant))
                }
            }
    }

    // 未拖动滑块就进行获取验证码或登录
    private val noVerifyAlertDialog by lazy {
        AlertDialog.Builder(requireContext())
            .setMessage("请先拖动滑块验证")
            .setNegativeButton("确定", null)
            .create().apply {
                setOnShowListener {
                    getNegBtnColor().apply {
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.primaryVariant))
                        // 按钮居中
                        val layoutParams = layoutParams as LinearLayout.LayoutParams
                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                        setLayoutParams(layoutParams)
                    }
                    getMessageTextView().apply {
                        setTextColor(ContextCompat.getColor(requireContext(), R.color.black_text_color))
                        textAlignment = View.TEXT_ALIGNMENT_CENTER
                    }
                }
            }
    }

    override fun bind(inflater: LayoutInflater, container: ViewGroup?): FragmentLoginBinding =
        FragmentLoginBinding.inflate(inflater, container, false)

    override fun initView() {
        // 初始化用户须知打勾的 SpannableString
        initMustReadSpannableString()
        // 设置登录方式的 ui
        loginTypeChangeReset()
        // 设置行为验证码
        binding.userLoginVerifyBar.apply {
            draggable = binding.userLoginVerifyDraggable
            actualDraggable = binding.userLoginVerifyActualDraggable
        }
    }

    override fun initObserver() {
        // 回退按钮的回退事件
        binding.userLoginBack.setOnClickListener {
            if (!findNavController().navigateUp()) {
                requireActivity().onBackPressed()
            }
        }

        /*
         * 输入框监听
         */
        binding.userLoginAccount.apply {
            filters = arrayOf(
                EditTextLengthFilter(11)
            )
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (binding.userLoginAccountCancel.visibility == View.GONE && s?.isNotEmpty() == true) {
                        binding.userLoginAccountCancel.visibility = View.VISIBLE
                        // 设置获取验证码的文字变色
                        binding.userLoginGetCode.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary))
                        // 判断是否需要解放登录按钮可使用
                        checkLoginBtnAvailable(binding.userLoginAccountCancel.visibility, binding.userLoginCodeCancel.visibility)
                    }
                }
                override fun afterTextChanged(s: Editable?) {
                    if (binding.userLoginAccountCancel.visibility == View.VISIBLE && s?.toString()?.isEmpty() == true) {
                        binding.userLoginAccountCancel.visibility = View.GONE
                        // 设置获取验证码的文字变色
                        binding.userLoginGetCode.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray_text_color))
                        // 判断是否需要解放登录按钮可使用
                        checkLoginBtnAvailable(binding.userLoginAccountCancel.visibility, binding.userLoginCodeCancel.visibility)
                    }
                }
            })
        }
        binding.userLoginCode.apply {
            filters = arrayOf(
                EditTextLengthFilter(4)
            )
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (binding.userLoginCodeCancel.visibility == View.GONE && s?.isNotEmpty() == true) {
                        binding.userLoginCodeCancel.visibility = View.VISIBLE
                        // 判断是否需要解放登录按钮可使用
                        checkLoginBtnAvailable(binding.userLoginAccountCancel.visibility, binding.userLoginCodeCancel.visibility)
                    }
                }
                override fun afterTextChanged(s: Editable?) {
                    if (binding.userLoginCodeCancel.visibility == View.VISIBLE && s?.toString()?.isEmpty() == true) {
                        binding.userLoginCodeCancel.visibility = View.GONE
                        // 判断是否需要解放登录按钮可使用
                        checkLoginBtnAvailable(binding.userLoginAccountCancel.visibility, binding.userLoginCodeCancel.visibility)
                    }
                }
            })
        }
        binding.userLoginPwd.apply {
            filters = arrayOf(
                EditTextLengthFilter(20)
            )
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (binding.userLoginPwdCancel.visibility == View.GONE && s?.isNotEmpty() == true) {
                        binding.userLoginPwdCancel.visibility = View.VISIBLE
                        // 判断是否需要解放登录按钮可使用
                        checkLoginBtnAvailable(binding.userLoginAccountCancel.visibility, binding.userLoginPwdCancel.visibility)
                    }
                }
                override fun afterTextChanged(s: Editable?) {
                    if (binding.userLoginPwdCancel.visibility == View.VISIBLE && s?.toString()?.isEmpty() == true) {
                        binding.userLoginPwdCancel.visibility = View.GONE
                        // 判断是否需要解放登录按钮可使用
                        checkLoginBtnAvailable(binding.userLoginAccountCancel.visibility, binding.userLoginPwdCancel.visibility)
                    }
                }
            })
        }

        /*
         * 叉叉，清空输入框
         */
        binding.userLoginAccountCancel.setOnClickListener {
            binding.userLoginAccount.setText("")
        }
        binding.userLoginCodeCancel.setOnClickListener {
            binding.userLoginCode.setText("")
        }
        binding.userLoginPwdCancel.setOnClickListener {
            binding.userLoginPwd.setText("")
        }

        // 获取验证码
        binding.userLoginGetCode.setOnClickListener {
            if (checkVerify()) {
                println("发送验证码")
            }
        }

        // 登录
        binding.userLoginBtn.setOnClickListener {
            if (binding.userLoginBtnShadow.alpha == 0f && checkVerify()) {
                println("denglu")
            }
        }

        // 改变登录方式
        binding.userLoginChangeType.setOnClickListener {
            loginType = if (loginType == LoginType.CODE) LoginType.PWD else LoginType.CODE
            loginTypeChangeReset()
        }

        // 登录遇到的问题
        binding.userLoginProblem.setOnClickListener {
            val problemBinding = UserLoginProblemBinding.inflate(layoutInflater, binding.root, false)
            val alertDialog = AlertDialog.Builder(requireContext()).setView(problemBinding.root).show()
            problemBinding.userLoginProblemText.setOnClickListener {
                alertDialog.dismiss()
            }
        }

        // 其他方式登录
        binding.userLoginWx.setOnClickListener {
            otherLoginAlertDialog.show()
        }
        binding.userLoginQq.setOnClickListener {
            otherLoginAlertDialog.show()
        }
        binding.userLoginWb.setOnClickListener {
            otherLoginAlertDialog.show()
        }
    }

    /**
     * 登录方式改变，需要修改 ui
     */
    private fun loginTypeChangeReset() {
        when (loginType) {
            LoginType.CODE -> {
                // 标题改变
                binding.userLoginTitle.text = getString(R.string.me_user_login_title_code_string)
                // 账号输入框 hint 改变
                binding.userLoginAccount.hint = getString(R.string.me_user_login_account_code_hint_string)
                // 设置验证码输入框可见
                binding.userLoginCodeLayout.visibility = View.VISIBLE
                // 设置密码输入框不可见
                binding.userLoginPwdLayout.visibility = View.GONE
                // 切换登录方式按钮的文字改变
                binding.userLoginChangeType.text = getString(R.string.me_user_login_title_pwd_string)
            }
            LoginType.PWD -> {
                // 标题改变
                binding.userLoginTitle.text = getString(R.string.me_user_login_title_pwd_string)
                // 账号输入框 hint 改变
                binding.userLoginAccount.hint = getString(R.string.me_user_login_account_pwd_hint_string)
                // 设置验证码输入框不可见
                binding.userLoginCodeLayout.visibility = View.GONE
                // 设置密码输入框可见
                binding.userLoginPwdLayout.visibility = View.VISIBLE
                // 切换登录方式按钮的文字改变
                binding.userLoginChangeType.text = getString(R.string.me_user_login_title_code_string)
            }
        }
        // 用户须知打勾的文字设置
        setMustReadText(loginType)
        // 重置用户必读的选中情况
        binding.userLoginMustReadCheckbox.isChecked = false
    }

    /**
     * 设置登录按钮是否是可用的，这里仅是设置按钮的样式，真正是否能响应点击事件看具体的点击事件
     */
    private fun checkLoginBtnAvailable(phone: Int, code: Int) {
        binding.userLoginBtnShadow.apply {
            alpha = if (phone == View.VISIBLE && code == View.VISIBLE) 0f else 1f
        }
    }

    /**
     * 初始化用户须知打勾的 SpannableString
     */
    private fun initMustReadSpannableString() {
        pwdSpannableString = SpannableString("我已阅读并同意《用户协议》和《隐私政策》").apply {
            setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.gray_text_color)), 0, 7, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

            setSpan(StyleSpan(Typeface.BOLD), 7, 13, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    findNavController().navigate(R.id.action_loginFragment_to_userProtocolFragment)
                }
                // 去除点击事件文字的下划线
                override fun updateDrawState(ds: TextPaint) {
                    ds.color = ds.linkColor
                    ds.isUnderlineText = false
                }
            }, 7, 13, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.primary)), 7, 13, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

            setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.gray_text_color)), 13, 14, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

            setSpan(StyleSpan(Typeface.BOLD), 14, 20, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    findNavController().navigate(R.id.action_loginFragment_to_privacyPolicyFragment)
                }
                // 去除点击事件文字的下划线
                override fun updateDrawState(ds: TextPaint) {
                    ds.color = ds.linkColor
                    ds.isUnderlineText = false
                }
            }, 14, 20, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.primary)), 14, 20, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }
        codeSpannableString = SpannableString("我已阅读并同意《用户协议》和《隐私政策》，未注册手机号登录时将自动注册").apply {
            setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.gray_text_color)), 0, 7, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

            setSpan(StyleSpan(Typeface.BOLD), 7, 13, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    findNavController().navigate(R.id.action_loginFragment_to_userProtocolFragment)
                }
                // 去除点击事件文字的下划线
                override fun updateDrawState(ds: TextPaint) {
                    ds.color = ds.linkColor
                    ds.isUnderlineText = false
                }
            }, 7, 13, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.primary)), 7, 13, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

            setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.gray_text_color)), 13, 14, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

            setSpan(StyleSpan(Typeface.BOLD), 14, 20, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    findNavController().navigate(R.id.action_loginFragment_to_privacyPolicyFragment)
                }
                // 去除点击事件文字的下划线
                override fun updateDrawState(ds: TextPaint) {
                    ds.color = ds.linkColor
                    ds.isUnderlineText = false
                }
            }, 14, 20, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.primary)), 14, 20, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)

            setSpan(ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.gray_text_color)), 20, length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
        }
    }

    /**
     * 用户须知打勾的文字设置
     */
    private fun setMustReadText(loginType: LoginType) {
        // 设置用户必读的 SpannableString
        binding.userLoginMustRead.apply {
            text = when (loginType) {
                LoginType.CODE -> codeSpannableString
                LoginType.PWD -> pwdSpannableString
            }
            // 必须设置这个属性，否则 ClickableSpan 不可点击
            movementMethod = LinkMovementMethod.getInstance()
            // 点击后的背景阴影不显示
            highlightColor = Color.TRANSPARENT
        }
    }

    private fun checkVerify(): Boolean {
        return if (!binding.userLoginVerifyBar.isVerifyPass()) {
            noVerifyAlertDialog.show()
            false
        } else {
            true
        }
    }

    /**
     * 手机号和验证码输入框的过滤器，过滤掉多余字符
     */
    class EditTextLengthFilter(private val maxLength: Int) : InputFilter {
        /**
         * 方法返回的值将会替换掉 dest 字符串中 dstartd 位置到 dend 位置之间字符
         */
        override fun filter(
            source: CharSequence?,
            start: Int,
            end: Int,
            dest: Spanned?,
            dstart: Int,
            dend: Int
        ): CharSequence {
            // 计算修改后的内容长度
            val afterLength = (dest?.length ?: 0) + (end - start) - (dend - dstart)
            if (source?.isNotEmpty() == true &&
                afterLength > maxLength) {
                return if (dest == null) {
                    source.substring(0, maxLength)
                } else {
                    source.substring(0, maxLength - (dest.length - (dend - dstart)))
                }
            }
            return source ?: ""
        }
    }

    enum class LoginType {
        CODE, PWD
    }
}