<template>
  <div class="admin-layout">
    <!-- 顶部导航栏 -->
    <div class="admin-navbar">
      <div class="navbar-container">
        <div class="logo">
          <router-link to="/admin/dashboard">Admin Console</router-link>
        </div>
        
        <div class="nav-links">
          <router-link to="/admin/dashboard" class="nav-item" :class="{ active: isActive('/admin/dashboard') }">
            <i class="el-icon-monitor"></i>
            Dashboard
          </router-link>
          
          <router-link to="/admin/users" class="nav-item" :class="{ active: isActive('/admin/users') }">
            <i class="el-icon-user"></i>
            User Management
          </router-link>
          
          <router-link to="/admin/reviews" class="nav-item" :class="{ active: isActive('/admin/reviews') }">
            <i class="el-icon-document-checked"></i>
            Log Review
          </router-link>
          
          <router-link to="/admin/appeals" class="nav-item" :class="{ active: isActive('/admin/appeals') }">
            <i class="el-icon-warning-outline"></i>
            User Appeals
          </router-link>
          
          <router-link to="/admin/feedbacks" class="nav-item" :class="{ active: isActive('/admin/feedbacks') }">
            <i class="el-icon-chat-line-square"></i>
            Feedback
          </router-link>
        </div>
        
        <div class="user-actions">
          <el-dropdown 
            trigger="click" 
            class="user-profile-dropdown" 
            popper-class="narrow-user-dropdown"
          >
            <div class="admin-user">
              <el-avatar size="small" src="https://randomuser.me/api/portraits/men/1.jpg"></el-avatar>
              <span>Admin</span>
              <i class="el-icon-arrow-down"></i>
            </div>
            <template #dropdown>
              <el-dropdown-menu class="admin-dropdown-menu">
                <el-dropdown-item>
                  <router-link to="/settings" class="dropdown-link">Settings</router-link>
                </el-dropdown-item>
                <el-dropdown-item divided>
                  <a href="#" @click.prevent="logout" class="dropdown-link">Log Out</a>
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    
    <!-- 主内容区域 -->
    <div class="admin-content">
      <slot></slot>
    </div>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
// Import user store
import { useUserStore } from '../store/user' 

export default {
  name: 'AdminLayout',
  setup() {
    const router = useRouter()
    // Get user store instance
    const userStore = useUserStore()
    
    // 判断当前路由是否激活
    const isActive = (route) => {
      return router.currentRoute.value.path === route
    }
    
    // 退出登录
    const logout = () => {
      ElMessageBox.confirm(
        'Are you sure you want to log out?',
        'Logout',
        {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }
      ).then(() => {
        // Call store logout action before redirecting
        userStore.logout() 
        
        // 在实际应用中，这里应该调用登出API
        // 然后清除本地存储的token等身份验证信息 (userStore.logout应该处理这个)
        // 此处只是简单演示跳转逻辑
        router.push('/auth')
      }).catch(() => {})
    }
    
    return {
      isActive,
      logout
    }
  }
}
</script>

<style scoped>
.admin-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* 导航栏样式 */
.admin-navbar {
  background-color: #6a1b9a;
  color: #fff;
  height: 64px;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.navbar-container {
  display: flex;
  align-items: center;
  height: 100%;
  padding: 0 24px;
  max-width: 1400px;
  margin: 0 auto;
}

.logo {
  min-width: 200px;
  margin-right: 30px;
}

.logo a {
  color: #fff;
  font-size: 20px;
  font-weight: 600;
  text-decoration: none;
  display: flex;
  align-items: center;
}

.nav-links {
  display: flex;
  height: 100%;
  flex: 1;
}

.nav-item {
  color: rgba(255, 255, 255, 0.65);
  display: flex;
  align-items: center;
  padding: 0 20px;
  height: 100%;
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s;
}

.nav-item i {
  margin-right: 8px;
}

.nav-item:hover {
  color: #fff;
  background-color: rgba(255, 255, 255, 0.08);
}

.nav-item.active {
  color: #fff;
  background-color: #9c27b0;
}

.user-actions {
  margin-left: auto;
}

.admin-user {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 6px 12px;
  border-radius: 4px;
  transition: all 0.3s;
}

.admin-user:hover {
  background-color: rgba(255, 255, 255, 0.08);
}

.admin-user span {
  margin: 0 8px;
  color: rgba(255, 255, 255, 0.85);
}

.dropdown-link {
  text-decoration: none;
  color: inherit;
  display: block;
  width: 100%;
}

/* 添加全局样式来覆盖element-plus下拉菜单宽度 */
:deep(.el-dropdown-menu) {
  min-width: 120px !important;
  max-width: 160px !important;
}

/* Add styles for narrow dropdown from MainLayout */
:global(.narrow-user-dropdown .el-dropdown-menu) {
   max-width: 160px !important; 
   min-width: 150px !important; 
}

:global(.narrow-user-dropdown .el-dropdown-menu__item) {
  /* Optional: padding: 0 15px; */ 
}

/* 内容区域样式 */
.admin-content {
  padding-top: 64px;
  flex: 1;
  background-color: #f0f2f5;
}
</style> 