<template>
  <div class="auth-container">
    <div class="auth-card">
      <!-- 网站Logo区域 -->
      <div class="logo">
        <router-link to="/">Reading Activity Management System</router-link>
      </div>

      <h2 class="forgot-password-title">Reset Password</h2>
      
      <!-- 步骤一：发送验证码 -->
      <el-form
        v-if="!codeSent"
        ref="sendCodeFormRef"
        :model="form"
        :rules="sendCodeRules"
        label-position="top"
        @submit.prevent="handleSendCode"
      >
        <p class="forgot-password-desc">
          Enter your email address to receive a verification code.
        </p>
        <!-- 邮箱输入框 -->
        <el-form-item label="Email" prop="email">
          <el-input 
            v-model="form.email" 
            placeholder="Please enter your registered email" 
            type="email"
            prefix-icon="Message"
            :disabled="loading || countDown > 0" 
          />
        </el-form-item>
        
        <!-- 发送验证码按钮 -->
        <el-form-item>
          <el-button 
            type="primary" 
            class="submit-btn" 
            :loading="loading"
            :disabled="countDown > 0"
            @click="handleSendCode"
          >
            {{ countDown > 0 ? `Resend in ${countDown}s` : 'Send Code' }}
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 步骤二：输入验证码和新密码 -->
      <el-form
        v-if="codeSent"
        ref="resetPasswordFormRef"
        :model="form"
        :rules="resetPasswordRules"
        label-position="top"
        @submit.prevent="handleResetPassword"
      >
         <p class="forgot-password-desc">
          A verification code has been sent to {{ form.email }}. Please check your inbox.
        </p>
        <el-form-item label="Email" prop="email">
          <el-input 
            v-model="form.email" 
            placeholder="Enter your email" 
            prefix-icon="Message"
            :disabled="true"
          />
        </el-form-item>
        <!-- 验证码输入框 -->
        <el-form-item label="Verification Code" prop="code">

        
          <!-- 验证码 -->
          <el-input 
            v-model="form.code" 
            placeholder="Enter the 6-digit code" 
            prefix-icon="ChatDotSquare"
          />
        </el-form-item>

        <!-- 新密码输入框 -->
        <el-form-item label="New Password" prop="newPassword">
          <el-input 
            v-model="form.newPassword" 
            placeholder="Enter your new password" 
            type="password"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>

        <!-- 确认新密码输入框 -->
        <el-form-item label="Confirm New Password" prop="confirmPassword">
          <el-input 
            v-model="form.confirmPassword" 
            placeholder="Confirm your new password" 
            type="password"
            prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        
        <!-- 重置密码按钮 -->
        <el-form-item>
          <el-button 
            type="primary" 
            class="submit-btn" 
            :loading="resetLoading"
            @click="handleResetPassword"
          >
            Reset Password
          </el-button>
        </el-form-item>
      </el-form>

      <!-- 返回登录链接 -->
      <div class="back-to-login">
        <el-button link @click="$router.push('/auth')">Back to Login</el-button>
      </div>
    </div>
    
    <!-- 页脚版权信息 -->
    <div class="auth-footer">
      &copy; {{ new Date().getFullYear() }} Reading Activity Management System - All rights reserved
    </div>
  </div>
</template>

<script setup>
import { getCaptcha,updatePwd} from '../api'

import { ref, reactive, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Message, Lock, ChatDotSquare } from '@element-plus/icons-vue' // Import icons
// import { useUserStore } from '../store/user' // Assuming password reset might involve user store or a separate API call

// 路由实例 - 用于页面导航
const router = useRouter()
// const userStore = useUserStore()

// 组件状态数据
const loading = ref(false) // Loading state for sending code
const resetLoading = ref(false) // Loading state for resetting password
const codeSent = ref(false) // Controls visibility of reset form
const countDown = ref(0) // Countdown timer for resend
let timer = null // Timer instance

const sendCodeFormRef = ref(null)
const resetPasswordFormRef = ref(null)

// 表单数据模型 (包含所有字段)
const form = reactive({
  email: '',
  verificationCode: '',
  newPassword: '',
  confirmPassword: '',
})

// 发送验证码表单验证规则
const sendCodeRules = {
  email: [
    { required: true, message: 'Please enter your email address', trigger: 'blur' },
    { type: 'email', message: 'Please enter a valid email format', trigger: ['blur', 'change'] }
  ]
}

// 重置密码表单验证规则
const resetPasswordRules = {
  code: [
    { required: true, message: 'Please enter the verification code', trigger: 'blur' },
    { len: 6, message: 'Verification code must be 6 digits', trigger: 'blur' } // Assuming 6 digits
  ],
  newPassword: [
  { required: true, message: 'Please enter password', trigger: 'blur' },
    { min: 6, message: 'Password length cannot be less than 6 digits', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        const hasLetter = /[a-zA-Z]/.test(value)
        const hasNumber = /[0-9]/.test(value)
        if (!hasLetter || !hasNumber) {
          callback(new Error('Password must contain letters and numbers'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  confirmPassword: [
  { required: true, message: 'Please enter password again', trigger: 'blur' },
    { 
      validator: (rule, value, callback) => {
        if (value !== form.newPassword) {
          callback(new Error('The two passwords you entered are inconsistent'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
  ]
}

// 启动倒计时
const startCountdown = () => {
  countDown.value = 60 // 60秒倒计时
  if (timer) clearInterval(timer)
  timer = setInterval(() => {
    if (countDown.value > 0) {
      countDown.value--
    } else {
      clearInterval(timer)
      timer = null
    }
  }, 1000)
}

// 处理发送验证码请求
const handleSendCode = async () => {
  if (!sendCodeFormRef.value) return
  
  await sendCodeFormRef.value.validate(async (valid) => {
    if (!valid) return
    loading.value = true

    const res = await getCaptcha(form.email)
    if(res.code===200){
      ElMessage.success('Verification code sent successfully!')
      codeSent.value = true // 显示下一步表单
      startCountdown() // 开始倒计时
      loading.value = false
    }else{
      ElMessage.error('Failed to send verification code. Please try again later.')
      loading.value = false
    }
    

  })
}

// 处理重置密码请求
const handleResetPassword = async () => {
  if (!resetPasswordFormRef.value) return

  await resetPasswordFormRef.value.validate(async (valid) => {
    if (!valid) return
    form.password=form.newPassword

    const res = await updatePwd(form)
    if(res.code===200){
      ElMessage.success('Password reset successfully! You can now log in with your new password.')
      // 可以跳转回登录页
      router.push('/auth') 
    }else{
      ElMessage.error('Failed to reset password. Please check the code or try again later.')
    }
  })
}

// 清理计时器
onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<style scoped>
/* Reuse styles from Auth.vue or define specific ones */
.auth-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: #f5f7fa;
  padding: 20px;
  background-image: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.auth-card {
  width: 100%;
  max-width: 450px;
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
  padding: 40px;
  margin-bottom: 20px;
}

.logo {
  text-align: center;
  margin-bottom: 20px;
  font-size: 26px;
  font-weight: 700;
}

.logo a {
  color: var(--primary-color, #409eff);
  text-decoration: none;
  background: linear-gradient(to right, #409eff, #67c23a);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.forgot-password-title {
  text-align: center;
  font-size: 24px;
  font-weight: 600;
  margin-bottom: 10px;
  color: #303133;
}

.forgot-password-desc {
  text-align: center;
  color: #606266;
  margin-bottom: 30px;
  font-size: 14px;
}

.submit-btn {
  width: 100%;
  padding: 12px 0;
  font-size: 16px;
  height: 50px;
  border-radius: 8px;
  font-weight: 600;
}

.back-to-login {
  text-align: center;
  margin-top: 20px;
}

.auth-footer {
  color: #606266;
  font-size: 14px;
  text-align: center;
  margin-top: 20px;
}
</style> 