<template>
  <div class="verify-container">
    <div class="verify-card">
      <!-- 网站logo区域 -->
      <div class="logo">
        <router-link to="/">Reading Activity Management System</router-link>
      </div>
      
      <!-- 电子邮件图标 -->
      <div class="verify-icon">
        <el-icon :size="60" color="#4A90E2"><Message /></el-icon>
      </div>
      
      <!-- 页面标题 -->
      <h2 class="verify-title">Email Verification</h2>
      
      <!-- 说明文本 - 显示邮箱地址和验证码说明 -->
      <p class="verify-desc">
        Verification code sent to <span class="email">{{ email }}</span>
        <br>Please enter the 6-digit code you received
      </p>
      
      <!-- 6位验证码输入框组 -->
      <div class="code-inputs">
        <el-input
          v-for="(digit, index) in 6"
          :key="index"
          v-model="codeDigits[index]"
          maxlength="1"
          :ref="el => { if (el) inputRefs[index] = el }"
          @input="handleInput(index)"
          @keydown.delete="handleDelete(index)"
          @focus="handleFocus(index)"
          class="code-input"
        />
      </div>
      
      <!-- 重新发送验证码按钮 - 带倒计时 -->
      <div class="resend">
        <el-button 
          type="text" 
          :disabled="countdown > 0"
          @click="handleResend"
        >
          {{ countdown > 0 ? `Resend (${countdown}s)` : 'Resend' }}
        </el-button>
      </div>
      
      <!-- 验证提交按钮 -->
      <el-button 
        type="primary"
        class="verify-btn"
        :loading="loading"
        @click="handleVerify"
      >
        Verify
      </el-button>
      
      <!-- 返回登录链接 -->
      <div class="back-link">
        <router-link to="/auth">Back to Login</router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Message } from '@element-plus/icons-vue' // Make sure Message icon is imported

// 路由相关
const route = useRoute()
const router = useRouter()

// 组件状态数据
const loading = ref(false)       // 验证按钮加载状态
const countdown = ref(0)         // 重发倒计时
const countdownTimer = ref(null) // 倒计时定时器引用
const codeDigits = ref(['', '', '', '', '', '']) // 6位验证码数组
const inputRefs = ref([])        // 输入框DOM引用数组

// 计算属性 - 从路由参数获取邮箱地址
const email = computed(() => route.query.email || '未知邮箱') // Reverted

// 计算属性 - 将6个输入框的值合并为完整验证码
const verificationCode = computed(() => codeDigits.value.join(''))

// 开始60秒倒计时
const startCountdown = () => {
  countdown.value = 60 // 设置初始倒计时秒数
  if (countdownTimer.value) clearInterval(countdownTimer.value) // Clear existing timer if any
  countdownTimer.value = setInterval(() => {
    if (countdown.value > 0) {
      countdown.value-- // 每秒减1
    } else {
      clearInterval(countdownTimer.value) // 倒计时结束时清除定时器
      countdownTimer.value = null
    }
  }, 1000)
}

// 处理重新发送验证码
const handleResend = () => {
  // 在实际应用中，这里应该调用API重新发送验证码
  ElMessage({
    type: 'success',
    message: '验证码已重新发送' // Reverted
  })
  
  startCountdown() // 重新开始倒计时
}

// 处理验证码输入 - 自动跳转到下一个输入框
const handleInput = (index) => {
  // 自动移动到下一个输入框
  if (codeDigits.value[index] && index < 5) {
    inputRefs.value[index + 1].focus()
  }
  // Optional: Handle pasting code
  if (codeDigits.value[index].length > 1 && index === 0) {
     const pastedCode = codeDigits.value[index].slice(0, 6).split('');
     for(let i = 0; i < pastedCode.length; i++) {
       codeDigits.value[i] = pastedCode[i];
     }
     if (pastedCode.length === 6) {
       inputRefs.value[5].focus(); // Focus last input if full code pasted
     } else {
       inputRefs.value[pastedCode.length]?.focus(); // Focus next empty
     }
  }
}

// 处理删除键 - 自动跳转到上一个输入框
const handleDelete = (index) => {
  // If current input is empty and index > 0, clear previous and focus it
  if (!codeDigits.value[index] && index > 0) {
    codeDigits.value[index - 1] = '' // 清空上一个输入框的值
    inputRefs.value[index - 1].focus() // 聚焦到上一个输入框
  }
}

// 处理输入框聚焦 - 自动全选内容
const handleFocus = (index) => {
  // 全选当前内容
  setTimeout(() => {
    inputRefs.value[index]?.select() // Add optional chaining
  }, 0)
}

// 验证码提交处理
const handleVerify = () => {
  // 验证码长度检查
  if (verificationCode.value.length !== 6) {
    ElMessage({
      type: 'warning',
      message: '请输入完整的验证码' // Reverted
    })
    return
  }
  
  loading.value = true // 设置加载状态
  
  // 模拟验证过程
  setTimeout(() => {
    // 在实际应用中，这里应该调用API验证
    if (verificationCode.value === '123456') { // 假设正确验证码是123456
      // 验证成功
      ElMessage({
        type: 'success',
        message: '验证成功！' // Reverted
      })
      router.push('/auth') // 导航到登录页
    } else {
      // 验证失败
      ElMessage({
        type: 'error',
        message: '验证码错误，请重试' // Reverted
      })
      codeDigits.value = ['', '', '', '', '', ''] // 清空所有输入
      inputRefs.value[0]?.focus() // 聚焦第一个输入框
    }
    
    loading.value = false // 重置加载状态
  }, 1000)
}

// 生命周期钩子 - 组件挂载时
onMounted(() => {
  startCountdown() // 开始倒计时
  
  // 自动聚焦第一个输入框
  setTimeout(() => {
    inputRefs.value[0]?.focus() // Add optional chaining
  }, 200)
})

// 生命周期钩子 - 组件卸载时
onUnmounted(() => {
  // 清除倒计时定时器，防止内存泄漏
  if (countdownTimer.value) {
    clearInterval(countdownTimer.value)
  }
})
</script>

<style scoped>
/* 验证页面容器 - 铺满整个视口并居中内容 */
.verify-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background-color: var(--background-color);
  padding: 20px;
}

/* 验证卡片样式 - 白色背景带阴影 */
.verify-card {
  width: 100%;
  max-width: 400px;
  background-color: white;
  border-radius: 8px;
  box-shadow: var(--card-shadow);
  padding: 30px;
  text-align: center;
}

/* Logo样式 */
.logo {
  margin-bottom: 20px;
  font-size: 24px;
  font-weight: 600;
}

/* Logo链接样式 */
.logo a {
  color: var(--primary-color);
  text-decoration: none;
}

/* 邮箱图标样式 */
.verify-icon {
  margin: 20px 0;
}

/* 页面标题样式 */
.verify-title {
  font-size: 24px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 16px;
}

/* 说明文本样式 */
.verify-desc {
  color: var(--text-secondary);
  margin-bottom: 30px;
  line-height: 1.6;
}

/* 邮箱地址高亮样式 */
.email {
  color: var(--primary-color);
  font-weight: 500;
}

/* 验证码输入框组样式 - 水平排列 */
.code-inputs {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-bottom: 24px;
}

/* 单个验证码输入框样式 */
.code-input {
  width: 40px;
  max-width: 40px;
  text-align: center;
}

/* 设置输入框内的文本居中和大小 */
:deep(.el-input__inner) {
  text-align: center;
  font-size: 20px;
  padding: 0;
}

/* 重新发送按钮容器 */
.resend {
  margin-bottom: 24px;
}

/* 验证按钮样式 */
.verify-btn {
  width: 100%;
  padding: 12px 0;
  font-size: 16px;
  margin-bottom: 16px;
}

/* 返回登录链接容器 */
.back-link {
  margin-top: 16px;
}

/* 返回登录链接样式 */
.back-link a {
  color: var(--text-secondary);
  text-decoration: none;
}

/* 返回登录链接悬停效果 */
.back-link a:hover {
  color: var(--primary-color);
}

/* 响应式布局调整 */
@media (max-width: 480px) {
  /* 移动设备下卡片内边距减小 */
  .verify-card {
    padding: 20px;
  }
  
  /* 移动设备下输入框尺寸稍小 */
  .code-input {
    width: 36px;
    max-width: 36px;
  }
}
</style> 