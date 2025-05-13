<template>
  <div class="layout">
    <!-- Top navigation bar -->
    <header class="header">
      <div class="container header-container">
        <!-- Site logo and name -->
        <div class="logo">
          <router-link to="/">
            <font-awesome-icon icon="book" class="logo-icon" />
            Reading Activity Management
          </router-link>
        </div>
        
        <!-- Main navigation menu - Using Element Plus menu component -->
        <el-menu
          class="menu"
          mode="horizontal"
          :ellipsis="false"
          :default-active="activeIndex"
          background-color="#FFFFFF"
          text-color="#333333"
          active-text-color="#4A90E2"
          router
        >
          <!-- Home menu item - Visible to all users -->
          <el-menu-item index="/">
            <font-awesome-icon icon="home" class="menu-icon" />
            Home
          </el-menu-item>
          
          <!-- Menu items displayed after login - Rendered via v-if condition -->
          <template v-if="userStore">
            <!-- User dashboard entry -->
            <el-menu-item index="/dashboard">
              <font-awesome-icon icon="tasks" class="menu-icon" />
              My Dashboard
            </el-menu-item>
            <!-- Reading logs menu item -->
            <el-menu-item index="/logs">
              <font-awesome-icon :icon="['far', 'bookmark']" class="menu-icon" />
              Reading Logs
            </el-menu-item>
            <!-- Message center menu item -->
            <el-menu-item index="/messages">
              <font-awesome-icon :icon="['far', 'comment']" class="menu-icon" />
              Messages
            </el-menu-item>
            <!-- Feedback menu item -->
            <el-menu-item index="/feedback">
              <font-awesome-icon :icon="['far', 'thumbs-up']" class="menu-icon" />
              Feedback
            </el-menu-item>
            
            <!-- Admin menu - Visible only to administrators -->
            <template v-if="userStore.roleId == '2'">
              <!-- Admin submenu - Using dropdown menu format -->
              <el-sub-menu index="/admin">
                <template #title>
                  <font-awesome-icon icon="cog" class="menu-icon" />
                  Administration
                </template>
                <!-- Admin dashboard submenu item -->
                <el-menu-item index="/admin/dashboard">
                  <font-awesome-icon icon="chart-line" class="menu-icon" />
                  Admin Dashboard
                </el-menu-item>
                <!-- User management submenu item -->
                <el-menu-item index="/admin/users">
                  <font-awesome-icon :icon="['far', 'user']" class="menu-icon" />
                  User Management
                </el-menu-item>
                <!-- Log review submenu item -->
                <el-menu-item index="/admin/reviews">
                  <font-awesome-icon icon="eye" class="menu-icon" />
                  Log Reviews
                </el-menu-item>
                <!-- User feedback view submenu item -->
                <el-menu-item index="/admin/feedbacks">
                  <font-awesome-icon :icon="['far', 'comment']" class="menu-icon" />
                  User Feedback
                </el-menu-item>
              </el-sub-menu>
            </template>
          </template>
        </el-menu>
        
        <!-- User actions area - Display different content based on login status -->
        <div class="user-actions">
          <!-- Guest status - Display login/register button -->
          <template v-if="!userStore">
            <el-button link @click="goAuth">
              <font-awesome-icon :icon="['far', 'user']" class="button-icon" />
              Login / Register
            </el-button>
          </template>
          
          <!-- Logged-in status - Display user avatar and dropdown menu -->
          <template v-else>
            <el-dropdown 
              trigger="click" 
              @command="handleCommand"
              class="user-profile-dropdown" 
              popper-class="narrow-user-dropdown"
            >
              <!-- User avatar and email display -->
              <div class="user-avatar">
                <el-avatar :size="32" :src="userStore.avatarPath || ''"></el-avatar>
                <span class="username">{{ userStore.email }}</span>
              </div>
              <!-- Dropdown menu options -->
              <template #dropdown>
                <el-dropdown-menu>
                  <!-- Personal center option -->
                  <el-dropdown-item command="dashboard">
                    <font-awesome-icon icon="tasks" class="dropdown-icon" />
                    My Dashboard
                  </el-dropdown-item>
                  <!-- Settings option -->
                  <el-dropdown-item command="settings">
                    <font-awesome-icon icon="cog" class="dropdown-icon" />
                    Settings
                  </el-dropdown-item>
                  <!-- Logout option - Separated from above options with a divider -->
                  <el-dropdown-item divided command="logout">
                    <font-awesome-icon icon="sign-out-alt" class="dropdown-icon" />
                    Logout
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </div>
      </div>
    </header>
    
    <!-- Main content area - Uses slot to receive child component content -->
    <main class="main-content">
      <div class="container">
        <slot></slot>
      </div>
    </main>
    
    <!-- Footer area -->
    <footer class="footer">
      <div class="container">
        <p>
          © {{ new Date().getFullYear() }} Reading Activity Management System - Developed with Vue 3
          <!-- Social media icons -->
          <span class="social-icons">
            <font-awesome-icon :icon="['fab', 'github']" class="social-icon" />
            <font-awesome-icon :icon="['fab', 'weixin']" class="social-icon" />
          </span>
        </p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../store/user'
import { library } from '@fortawesome/fontawesome-svg-core'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faHome, faTasks, faBookmark as farBookmark, faComment as farComment, faThumbsUp as farThumbsUp, faCog, faUser as farUser, faSignOutAlt, faEye, faChartLine } from '@fortawesome/free-solid-svg-icons'
import { faGithub, faWeixin } from '@fortawesome/free-brands-svg-icons'

// Add imported icons to the library
library.add(faHome, faTasks, farBookmark, farComment, farThumbsUp, faCog, farUser, faSignOutAlt, faEye, faChartLine, faGithub, faWeixin)

// Router instance, used for page navigation
const router = useRouter()
// Current route information, used to determine the currently active menu item
const route = useRoute()
// User state management, using Pinia for storage
const userStore = ref({
  avatarPath: '',
  email: '',
  id: '',
  roleId: '',
  token: '',
  username: '',
})
try {
  const storedUser = localStorage.getItem('user')
  userStore.value = storedUser && storedUser !== 'undefined' ? JSON.parse(storedUser) : null
} catch (error) {
  console.error('Error parsing user data:', error)
  userStore.value = null
}

// Compute the currently active menu item - Automatically matches based on the current path
const activeIndex = computed(() => route.path)

// Handle user dropdown menu commands
const handleCommand = (command) => {
  // If logout is selected, call the logout method and return to the homepage
  if (command === 'logout') {
    // userStore.logout()
    localStorage.removeItem('user')
    localStorage.removeItem('userid')
    router.push('/')
    window.location.reload()
  } else {
    // Otherwise navigate to the corresponding page
    router.push(`/${command}`)
  }
}

const goAuth = () => {
  router.push('/auth')
}
</script>

<style scoped>
/* Layout container - Use flex layout to ensure the footer is at the bottom */
.layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

/* Top navigation bar style */
.header {
  background-color: #FFFFFF;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05); /* Add shadow effect to enhance hierarchy */
  position: sticky; /* Keep the navigation bar at the top of the view when scrolling */
  top: 0;
  z-index: 100; /* Ensure the navigation bar is displayed above other elements */
}

/* Navigation bar content layout */
.header-container {
  display: flex;
  align-items: center;
  height: 60px;
}

/* Logo style */
.logo {
  margin-right: 40px;
  font-size: 20px;
  font-weight: 600;
}

.logo a {
  color: var(--primary-color);
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 8px;
}

.logo-icon {
  color: var(--el-color-primary);
  font-size: 24px;
}

/* Navigation menu style */
.menu {
  flex: 1; /* Occupy remaining space */
  border-bottom: none; /* Remove bottom border */
}

/* Menu icon style */
.menu-icon {
  margin-right: 8px;
  font-size: 16px;
}

/* Button icon style */
.button-icon {
  margin-right: 5px;
}

/* Dropdown menu icon style */
.dropdown-icon {
  margin-right: 5px;
  width: 16px;
}

/* User actions area style */
.user-actions {
  margin-left: auto; /* Push user actions to the right */
}

/* 用户头像区域样式 */
.user-avatar {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 0 10px; /* Add some padding for better click area */
}

/* 用户名样式 - 过长时显示省略号 */
.username {
  margin-left: 8px;
  font-size: 14px;
  color: var(--el-text-color-regular); /* Use Element Plus variable */
}

/* 主内容区域样式 */
.main-content {
  flex: 1; /* 占据剩余空间，使页脚保持在底部 */
  padding: 24px 0;
  /* Ensure container within main-content is styled if needed */
  /* e.g., .main-content .container { max-width: 1200px; margin: 0 auto; } */
}

/* 页脚样式 */
.footer {
  background-color: #FFFFFF;
  border-top: 1px solid var(--el-border-color-light); /* Use Element Plus variable */
  padding: 24px 0;
  color: var(--el-text-color-secondary); /* Use Element Plus variable */
  text-align: center;
}

/* 社交图标容器 */
.social-icons {
  display: inline-flex;
  gap: 12px;
  margin-left: 15px;
}

/* 社交图标样式 */
.social-icon {
  font-size: 18px;
  cursor: pointer;
  transition: color 0.3s; /* 添加过渡效果 */
}

.social-icon:hover {
  color: var(--el-color-primary); /* 鼠标悬停时改变颜色 */
}

/* 响应式设计 - 在移动设备上调整布局 */
@media (max-width: 768px) {
  /* 在小屏幕上改为垂直布局 */
  .header-container {
    flex-direction: column;
    height: auto;
    padding: 10px 0;
  }
  
  /* 调整Logo在移动设备上的边距 */
  .logo {
    margin-right: 0;
    margin-bottom: 10px;
  }
  
  /* 确保菜单占满宽度 */
  .menu {
    width: 100%;
  }
  
  /* 调整用户操作区域的位置 */
  .user-actions {
    margin: 10px 0 0;
  }
}

/* Target the popper element directly using the custom popper-class */
/* We target the menu *inside* the popper */
:global(.narrow-user-dropdown .el-dropdown-menu) {
   max-width: 160px !important; /* Set a max-width and use !important if needed */
   min-width: 150px !important; /* Ensure it doesn't get too small */
   /* Or set a fixed width: width: 160px !important; */
}

/* Optional: Adjust padding or other styles within the narrow dropdown */
:global(.narrow-user-dropdown .el-dropdown-menu__item) {
  /* Example: Adjust padding if needed */
  /* padding: 0 15px; */ 
}

</style> 