<template>
  <div class="auth-container">
    <div class="auth-card">
      <!-- 网站Logo区域 -->
      <div class="logo">
        <router-link to="/">Reading Activity Management System</router-link>
      </div>
      
      <!-- 登录/注册切换选项卡 -->
      <el-tabs v-model="activeTab" class="auth-tabs">
        <!-- 登录表单面板 -->
        <el-tab-pane label="Login" name="login">
          <el-form
            ref="loginFormRef"
            :model="loginForm"
            :rules="loginRules"
            label-position="top"
            @submit.prevent="handleLogin"
          >
            <!-- 邮箱输入框 -->
            <el-form-item label="Email" prop="email">
              <el-input 
                v-model="loginForm.email" 
                placeholder="Please enter email" 
                type="email"
                prefix-icon="Message"
              />
            </el-form-item>
            
            <!-- 密码输入框 -->
            <el-form-item label="Password" prop="password">
              <el-input 
                v-model="loginForm.password" 
                placeholder="Password must be at least 6 characters" 
                type="password"
                prefix-icon="Lock"
                show-password
              />
            </el-form-item>
            
            <!-- 登录选项 - 记住我和忘记密码 -->
            <div class="form-options">
              <el-checkbox v-model="loginForm.remember">Remember me</el-checkbox>
              <el-button link size="small" @click="$router.push('/forgot-password')">Forgot password?</el-button>
            </div>
            
            <!-- 登录按钮 -->
            <el-form-item>
              <el-button 
                type="primary" 
                class="submit-btn" 
                :loading="loading"
                @click="handleLogin"
              >
                Login
              </el-button>
            </el-form-item>
          </el-form>
          <span class="appeal-link" @click="$router.push('/appeal')">Appeal</span>
        </el-tab-pane>
        
        <!-- 注册表单面板 -->
        <el-tab-pane label="Register" name="register">
          <el-form
            ref="registerFormRef"
            :model="registerForm"
            :rules="registerRules"
            label-position="top"
            @submit.prevent="handleRegister"
          >
            <!-- 邮箱输入框 -->
            <el-form-item label="Email" prop="email">
              <el-input 
                v-model="registerForm.email" 
                placeholder="Please enter email" 
                type="email"
                prefix-icon="Message"
              />
              <div class="captcha-container">
                <el-button type="primary" size="small" :disabled="captchaLoading" @click="sendVerificationCode">
                  {{ captchaButtonText }}
                </el-button>
              </div>
            </el-form-item>
            
            <!-- 验证码输入框 -->
            <el-form-item label="verificationCode" prop="verificationCode">
              <el-input 
                v-model="registerForm.verificationCode" 
                placeholder="verificationCode" 
                prefix-icon="Key"
              />
            </el-form-item>
            
            <!-- 密码输入框 -->
            <el-form-item label="Password" prop="password">
              <el-input 
                v-model="registerForm.password" 
                placeholder="Password must contain letters and numbers (at least 6 characters)" 
                type="password"
                prefix-icon="Lock"
                show-password
              />
            </el-form-item>
            
            <!-- 确认密码输入框 -->
            <el-form-item label="Confirm Password" prop="confirmPassword">
              <el-input 
                v-model="registerForm.confirmPassword" 
                placeholder="Please enter the same password as above" 
                type="password"
                prefix-icon="Lock"
                show-password
              />
            </el-form-item>
            
            <!-- 头像上传 -->
            <el-form-item label="Avatar (Optional)" prop="avatar">
              <el-upload
                class="avatar-uploader"
                :action="action" 
                :show-file-list="false"
                :before-upload="beforeAvatarUpload"
                @success="handleAvatarSuccess"
                :size="1024*1024*2"
                :limit="1"
                accept="image/jpeg, image/png"
              >
                <img v-if="registerForm.avatarPath" :src="registerForm.avatarPath" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                <template #tip>
                  <div class="el-upload__tip">
                    JPG/PNG files with a size less than 2MB
                  </div>
                </template>
              </el-upload>
            </el-form-item>
            
            <!-- 注册按钮 -->
            <el-form-item>
              <el-button 
                type="primary" 
                class="submit-btn" 
                :loading="loading"
                @click="handleRegister"
              >
                Register
              </el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </div>
    
    <!-- 页脚版权信息 -->
    <div class="auth-footer">
      &copy; {{ new Date().getFullYear() }} Reading Activity Management System - All rights reserved
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../store/user'
import { Plus } from '@element-plus/icons-vue'
import { getCaptcha, register, login } from '../api'
import { baseURL } from '../api/request'

// 路由实例 - 用于页面导航
const router = useRouter()
// 用户状态管理 - 从pinia获取
const userStore = useUserStore()

const action=baseURL+'/cpt/file/upload'

// 组件状态数据
const activeTab = ref('login')   // 当前激活的选项卡 - 默认为登录选项卡
const loading = ref(false)       // 加载状态 - 控制按钮的loading效果
const loginFormRef = ref(null)   // 登录表单引用 - 用于表单验证
const registerFormRef = ref(null) // 注册表单引用 - 用于表单验证

// 头像相关状态
const avatarFile = ref(null)
const avatarPreviewUrl = ref('')

// 验证码相关状态
const captchaLoading = ref(false)
const captchaButtonText = ref('send verification code')
const captchaTimer = ref(null)
const captchaCountdown = ref(60)

// 头像上传成功处理函数
const handleAvatarSuccess = (response) => {
  console.log(response)
  registerForm.avatarPath = response.msg
}


// 登录表单数据模型
const loginForm = reactive({
  email: '',         // 用户邮箱
  password: '',      // 用户密码
  remember: false    // 记住登录状态选项
})

// 注册表单数据模型
const registerForm = reactive({
  email: '',           // 注册邮箱
  password: '',        // 设置密码
  confirmPassword: '', // 确认密码
  verificationCode: '', // 验证码
  avatarPath: '' // 头像
})

// 登录表单验证规则
const loginRules = {
  email: [
    { required: true, message: 'Please enter email', trigger: 'blur' },           // 必填验证
    { type: 'email', message: 'Please enter correct email format', trigger: 'blur' } // 邮箱格式验证
  ],
  password: [
    { required: true, message: 'Please enter password', trigger: 'blur' },          // 必填验证
    { min: 6, message: 'Password length cannot be less than 6 digits', trigger: 'blur' } // 密码长度验证
  ]
}

// 注册表单验证规则
const registerRules = {
  email: [
    { required: true, message: 'Please enter email', trigger: 'blur' },           
    { type: 'email', message: 'Please enter correct email format', trigger: 'blur' }
  ],
  password: [
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
        if (value !== registerForm.password) {
          callback(new Error('The two passwords you entered are inconsistent'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
  ],
  verificationCode: [
    { required: true, message: 'Please enter verification code', trigger: 'blur' },
    { len: 6, message: 'Verification code should be 6 digits', trigger: 'blur' }
  ]
}


// 登录处理函数
const handleLogin = async () => {
  if (!loginFormRef.value) return
  // 表单验证
  await loginFormRef.value.validate(async (valid) => {
    if (!valid) return
    login(loginForm).then(res => {
      console.log(res)
      if(res.code==200){
        ElMessage.success('login success')
        const targetRoute = res.data.roleId == '2' ? '/admin/dashboard' : '/dashboard'
        localStorage.setItem('user', JSON.stringify(res.data))
        localStorage.setItem('userid', res.data.id)
        window.location.href = targetRoute
      }else{
        ElMessage.error(res.msg)
      }
    })
  })
}

// 发送验证码的方法
const sendVerificationCode = async () => {
  // 验证邮箱格式
  const emailValidator = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
  if (!emailValidator.test(registerForm.email)) {
    ElMessage.error('请输入有效的邮箱地址')
    return
  }
  
  try {
    captchaLoading.value = true
    // 调用发送验证码的API
    await getCaptcha(registerForm.email)
    
    ElMessage.success('verification code sent successfully')
    
    // 开始倒计时
    captchaCountdown.value = 60
    captchaButtonText.value = `${captchaCountdown.value}seconds to resend`
    
    // 清除之前的计时器（如果有）
    if (captchaTimer.value) clearInterval(captchaTimer.value)
    
    // 设置新的计时器
    captchaTimer.value = setInterval(() => {
      captchaCountdown.value--
      captchaButtonText.value = `${captchaCountdown.value}seconds to resend`
      
      if (captchaCountdown.value <= 0) {
        clearInterval(captchaTimer.value)
        captchaButtonText.value = 'send verification code'
        captchaLoading.value = false
      }
    }, 1000)
  } catch (error) {
    ElMessage.error('验证码发送失败：' + (error.message || '请稍后重试'))
    console.error('发送验证码错误:', error)
    captchaLoading.value = false
  }
}

// 注册处理函数
const handleRegister = async () => {
  if (!registerFormRef.value) return

  // 表单验证
  await registerFormRef.value.validate(async (valid) => {
    if (!valid) return
    console.log(registerForm);
    register(registerForm).then(res => {
      console.log(res)
      if(res.code==200){
        ElMessage.success('register success')
        activeTab.value='login'
        // router.push('/login')
      }else{
        ElMessage.error(res.msg)
      }
    }).catch(err => {
      console.log(err)
    })
  })
}
</script>

<style scoped>
/* 登录/注册页面容器 - 居中显示 */
.auth-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: var(--background-color, #f5f7fa);
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
  transition: all 0.3s ease;
}

.logo {
  text-align: center;
  margin-bottom: 30px;
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

.auth-tabs {
  width: 100%;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.submit-btn {
  width: 100%;
  padding: 12px 0;
  font-size: 16px;
  height: 50px;
  border-radius: 8px;
  font-weight: 600;
  transition: all 0.3s ease;
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0,0,0,0.1);
}

.auth-footer {
  color: var(--text-light, #606266);
  font-size: 14px;
  text-align: center;
  margin-top: 20px;
}

/* Responsive adjustments */
@media (max-width: 480px) {
  .auth-card {
    padding: 25px;
  }
}

/* 头像上传器样式 */
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px; /* 根据需要调整大小 */
  height: 100px;
  text-align: center;
}

.avatar-uploader .avatar {
  width: 100px; /* 根据需要调整大小 */
  height: 100px;
  display: block;
}

.el-upload__tip {
  font-size: 12px;
  color: #999;
  margin-top: 7px;
}

.captcha-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 8px;
}
.appeal-link{
  color: #409eff;
  cursor: pointer;
  margin-left: 10px;
  font-size: 14px;
  float: right;
}
</style> 