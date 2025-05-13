import { createRouter, createWebHistory } from 'vue-router'

// Route configuration - Defines all accessible page paths in the application
const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: { title: 'Home' }
  },
  {
    path: '/auth',
    name: 'Auth',
    component: () => import('../views/Auth.vue'),
    meta: { title: 'Login/Register' }
  },
  {
    path: '/verify',
    name: 'Verify',
    component: () => import('../views/Verify.vue'),
    meta: { title: 'Email Verification' }
  },
  {
    path: '/forgot-password',
    name: 'ForgotPassword',
    component: () => import('../views/ForgotPassword.vue'),
    meta: { title: 'Forgot Password' }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/Dashboard.vue'),
    meta: { title: 'User Dashboard', requiresAuth: true }
  },
  {
    path: '/logs',
    name: 'Logs',
    component: () => import('../views/Logs.vue'),
    meta: { title: 'Reading Log Management', requiresAuth: true }
  },
  {
    path: '/logs/:id',
    name: 'LogDetail',
    component: () => import('../views/LogDetail.vue'),
    meta: { title: 'Log Details', requiresAuth: true }
  },
  

  {
    path: '/logs/create',
    name: 'LogCreate',
    component: () => import('../views/LogEdit.vue'),
    meta: { title: 'Create Log', requiresAuth: true }
  },
  {
    path: '/logs/edit/:id',
    name: 'LogEdit',
    component: () => import('../views/LogEdit.vue'),
    meta: { title: 'Edit Log', requiresAuth: true }
  },
  {
    path: '/logs/edit1/:id',
    name: 'LogEdit1',
    component: () => import('../views/LogEdit1.vue'),
    meta: { title: 'Edit Log', requiresAuth: true }
  },
  {
    path: '/settings',
    name: 'Settings',
    component: () => import('../views/Settings.vue'),
    meta: { title: 'Settings', requiresAuth: true }
  },
  {
    path: '/messages',
    name: 'Messages',
    component: () => import('../views/Messages.vue'),
    meta: { title: 'Message Center', requiresAuth: true }
  },
  {
    path: '/feedback',
    name: 'Feedback',
    component: () => import('../views/Feedback.vue'),
    meta: { title: 'Feedback', requiresAuth: true }
  },
  {
    path: '/appeal',
    name: 'Appeal',
    component: () => import('../views/Appeal.vue'),
    meta: { title: 'Appeal', requiresAuth: true }
  },
  // 管理员路由 - 仅对管理员角色开放
  {
    path: '/admin/dashboard',
    name: 'AdminDashboard',
    component: () => import('../views/admin/Dashboard.vue'),
    meta: { title: 'Admin Dashboard', requiresAuth: true, isAdmin: true }
  },
  {
    path: '/admin/users',
    name: 'AdminUsers',
    component: () => import('../views/admin/Users.vue'),
    meta: { title: 'User Management', requiresAuth: true, isAdmin: true }
  },
  {
    path: '/admin/reviews',
    name: 'AdminReviews',
    component: () => import('../views/admin/Reviews.vue'),
    meta: { title: 'Log Review', requiresAuth: true, isAdmin: true }
  },
  {
    path: '/admin/feedbacks',
    name: 'AdminFeedbacks',
    component: () => import('../views/admin/Feedbacks.vue'),
    meta: { title: 'User Feedback Management', requiresAuth: true, isAdmin: true }
  },
  {
    path: '/admin/appeals',
    name: 'AdminAppeals',
    component: () => import('../views/admin/Appeals.vue'),
    meta: { title: 'User Appeal Management', requiresAuth: true, isAdmin: true }
  }
]

// Create router instance
// createWebHistory creates HTML5 history mode routing (uses normal URLs, not hash mode #)
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// Global navigation guard - Executes permission checks before route transitions
// router.beforeEach((to, from, next) => {
//   // Set page title
//   document.title = to.meta.title ? `${to.meta.title} - Reading Activity Management System` : 'Reading Activity Management System'
  
//   // Get login status and user role (from local storage)
//   const token = localStorage.getItem('token')
//   const userRole = localStorage.getItem('userRole') // 'user' or 'admin'
  
//   console.log('[路由守卫] 检查权限:', { 
//     path: to.fullPath, 
//     requiresAuth: to.meta.requiresAuth, 
//     isAdmin: to.meta.isAdmin,
//     token: !!token,
//     userRole
//   })
  
//   // Check for pages requiring login
//   if (to.meta.requiresAuth && !token) {
//     console.log('[路由守卫] 需要登录，重定向到登录页面')
//     next({ name: 'Auth' })
//   } 
//   // Check for pages requiring admin permissions
//   else if (to.meta.isAdmin && userRole !== 'admin') {
//     console.log('[路由守卫] 需要管理员权限，重定向到用户仪表盘')
//     next({ name: 'Dashboard' })
//   }
//   // Logged-in user accessing login page, redirect to corresponding dashboard
//   else if (token && (to.name === 'Auth' || to.path === '/auth')) {
//     console.log('[路由守卫] 已登录用户访问登录页面，重定向到相应仪表盘')
//     next({ name: userRole === 'admin' ? 'AdminDashboard' : 'Dashboard' })
//   }
//   else {
//     console.log('[路由守卫] 允许访问:', to.fullPath)
//     next()
//   }
// })

export default router 