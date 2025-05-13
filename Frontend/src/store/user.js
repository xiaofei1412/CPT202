import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo')) || null,
    role: localStorage.getItem('userRole') || '', // 'user' 或 'admin'
  }),
  
  getters: {
    isLoggedIn: (state) => !!state.token,
    isAdmin: (state) => state.role === 'admin',
  },
  
  actions: {
    // 登录
    async login(userData) {
      try {
        // 模拟网络延迟
        await new Promise(resolve => setTimeout(resolve, 500))
        
        // 这里应该是调用API的登录接口，这里简化处理
        this.token = 'mock-token-' + Date.now()
        this.role = userData.email.includes('admin') ? 'admin' : 'user' // 简单判断，实际应从后端获取
        this.userInfo = {
          email: userData.email,
          avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png', // 默认头像
        }
        
        // 存储到本地
        localStorage.setItem('token', this.token)
        localStorage.setItem('userRole', this.role)
        localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
        
        console.log('登录成功，角色:', this.role)
        
        return { role: this.role, success: true }
      } catch (error) {
        console.error('登录失败:', error)
        throw error
      }
    },
    
    // 注册
    register(userData) {
      // 模拟注册成功
      return Promise.resolve({ email: userData.email })
    },
    
    // 更新用户信息
    updateUserInfo(data) {
      this.userInfo = { ...this.userInfo, ...data }
      localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
      return Promise.resolve(this.userInfo)
    },
    
    // 退出登录
    logout() {
      this.token = ''
      this.userInfo = null
      this.role = ''
      
      localStorage.removeItem('token')
      localStorage.removeItem('userRole')
      localStorage.removeItem('userInfo')
      
      return Promise.resolve()
    }
  }
}) 