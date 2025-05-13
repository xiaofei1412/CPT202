<template>
  <div class="settings-page">
    <div class="container">
      <!-- 页面标题和返回按钮 -->
      <div class="page-header">
        <el-button 
          class="back-button"
          type="primary" 
          text 
          @click="goBack"
        >
          <el-icon><component :is="ArrowLeft" /></el-icon>
          <span>Back</span>
        </el-button>
        <h1 class="page-title">Settings</h1>
      </div>
      
      <div class="settings-wrapper">
        <!-- 左侧菜单栏 - Removed Change Phone item -->
        <div class="settings-menu">
          <div 
            v-for="(item, index) in menuItems" 
            :key="index"
            :class="['menu-item', activeTab === item.key ? 'active' : '']"
            @click="activeTab = item.key"
          >
            <span>{{ item.label }}</span> 
          </div>
        </div>
        
        <!-- 右侧内容区域 - 根据选中的标签显示不同设置项 -->
        <div class="settings-content">
          <!-- 更改头像设置面板 - Adapted for reading-activity's script -->
          <div v-if="activeTab === 'avatar'" class="tab-content">
            <h2>Change Avatar</h2>
            
            <!-- Removed avatarStatus alerts -->

            <div class="avatar-section">
              <div class="avatar-preview-container">
                <el-avatar 
                  :size="100" 
                  :src="currentAvatar || '/default-avatar.png'"
                  shape="circle" 
                  fit="cover"
                  class="large-avatar-preview" 
                />
                <p class="preview-label">Avatar Preview</p>
              </div>

              <div class="upload-controls">
                <el-upload
                  :action="action" 
                  :auto-upload="true"
                  :show-file-list="false"
                  
                  :on-success="handleAvatarSuccess"
                  accept="image/jpeg, image/png" 
                  ref="uploadRef"
                >
                  <el-button type="primary" plain>
                    <!-- Kept icon from frontend, ensure Upload is available or remove -->
                    <el-icon><Upload /></el-icon> Select Image 
                  </el-button>
                </el-upload>
                <p class="el-upload__tip">
                  Supports JPG, PNG formats. File size less than 2MB.
                </p>
                
                <el-button 
                  type="primary" 
                  :disabled="!newAvatar"
                  @click="saveAvatar"
                  class="save-avatar-button"
                >
                  <!-- Simplified button - removed loading state/icon -->
                  Save Avatar
                </el-button>
              </div>
            </div>
          </div>
          
          <!-- Phone Number Panel Removed -->
          <!-- <div v-if="activeTab === 'phone'" class="tab-content"> -->
          <!--   ... phone content removed ... -->
          <!-- </div> -->
          
          <!-- Change Password Panel -->
          <div v-if="activeTab === 'password'" class="tab-content">
            <h2>Change Password</h2>
            <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="100px">
              <el-form-item label="Current Password" prop="oldPassword">
                <el-input v-model="passwordForm.oldPassword" type="password" show-password></el-input>
              </el-form-item>
              <el-form-item label="New Password" prop="newPassword">
                <el-input v-model="passwordForm.newPassword" type="password" show-password></el-input>
              </el-form-item>
              <el-form-item label="Confirm Password" prop="confirmPassword">
                <el-input v-model="passwordForm.confirmPassword" type="password" show-password></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="savePassword">Update Password</el-button>
              </el-form-item>
            </el-form>
          </div>
          
          <!-- Change Email Panel -->
          <div v-if="activeTab === 'email'" class="tab-content">
            <h2>Change Email</h2>
            <p class="current-email">Current Email: {{ userEmail }}</p>
            <el-form :model="emailForm" :rules="emailRules" ref="emailFormRef" label-width="100px">
              <el-form-item label="New Email" prop="newEmail">
                <el-input v-model="emailForm.newEmail"></el-input>
              </el-form-item>
              <el-form-item label="Verification Code" prop="verifyCode">
                <div class="verify-code-wrapper">
                  <el-input v-model="emailForm.verifyCode"></el-input>
                  <el-button 
                    :disabled="countDown > 0" 
                    @click="sendVerifyCode"
                  >
                    {{ countDown > 0 ? `Resend in ${countDown}s` : 'Get Code' }}
                  </el-button>
                </div>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="saveEmail">Update Email</el-button>
              </el-form-item>
            </el-form>
          </div>
          
          <!-- Account Security Panel -->
          <div v-if="activeTab === 'security'" class="tab-content">
            <h2>Account Security</h2>
            <div class="security-wrapper">
              <p class="warning">
                 <!-- Consider using el-icon component if el-icon-warning class doesn't work -->
                <i class="el-icon-warning"></i>
                After deleting your account, all data will be permanently deleted and cannot be recovered. Please proceed with caution.
              </p>
              <el-button type="danger" @click="showDeleteConfirm">Delete Account</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- Delete Account Dialog -->
    <el-dialog
      title="Delete Account Confirmation"
      v-model="deleteDialogVisible"
      width="400px"
    >
      <div class="delete-confirm">
        <p class="warning-text">
          After deleting your account, all your data will be permanently deleted and cannot be recovered.
          Please confirm this action:
        </p>
        <el-input
          type="password"
          v-model="deleteConfirmPassword"
          placeholder="Enter your password to confirm"
        ></el-input>
      </div>
      <template #footer>
        <el-button @click="deleteDialogVisible = false">Cancel</el-button>
        <el-button type="danger" @click="confirmDeleteAccount">Confirm Delete</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ArrowLeft, Upload } from '@element-plus/icons-vue'
import { baseURL } from '../api/request'
import { updateUser, getUser, getCaptcha, deleteByAccountAndPassword } from '../api/index'

export default {
  name: 'Settings',
  components: { ArrowLeft, Upload },
  setup() {
    // 菜单标签及当前激活标签
    const action= baseURL+'/cpt/file/upload'
    const activeTab = ref('avatar')
    const menuItems = [
      { key: 'avatar', label: 'Change Avatar' },
      // Removed phone item
      { key: 'password', label: 'Change Password' },
      { key: 'email', label: 'Change Email' },
      { key: 'security', label: 'Account Security' },
    ]
    
    // 用户基础数据
    const userEmail = ref('user@example.com')
    const currentAvatar = ref('')
    const newAvatar = ref('')
    // Removed currentPhone
    
    // 密码表单相关
    const passwordFormRef = ref(null)
    const passwordForm = reactive({
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    })
    const passwordRules = {
      oldPassword: [
        { required: true, message: 'Please enter your current password', trigger: 'blur' }
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
        if (value !== passwordForm.newPassword) {
          callback(new Error('The two passwords you entered are inconsistent'))
        } else {
          callback()
        }
      }, 
      trigger: 'blur' 
    }
      ]
    }
    
    // Removed Phone Number related refs and rules
    
    // 邮箱表单相关
    const emailFormRef = ref(null)
    const emailForm = reactive({
      newEmail: '',
      verifyCode: ''
    })
    const emailRules = {
      newEmail: [
        { required: true, message: 'Please enter a new email', trigger: 'blur' },
        { type: 'email', message: 'Please enter a valid email address', trigger: 'blur' }
      ],
      verifyCode: [
        { required: true, message: 'Please enter the verification code', trigger: 'blur' },
        { len: 6, message: 'Verification code must be 6 digits', trigger: 'blur' }
      ]
    }
    const countDown = ref(0)
    
    // 账户删除相关
    const deleteDialogVisible = ref(false)
    const deleteConfirmPassword = ref('')
    
    // 初始化用户数据
    onMounted(() => {
      currentAvatar.value = JSON.parse(localStorage.getItem('user')).avatarPath
      // 模拟API数据获取
      // setTimeout(() => {
      //   userEmail.value = 'user@example.com'
      //   currentAvatar.value = '/default-avatar.png'
        
      // }, 500)
    })

    const handleAvatarSuccess = (response) => {
      
      if (response.code === 200) {
        console.log(response.msg);
        currentAvatar.value = response.msg
        newAvatar.value = response.msg
      }
    }
    // 头像处理相关方法
    const handleAvatarChange = (file) => {
      newAvatar.value = file.raw
      // 预览图片
      const reader = new FileReader()
      reader.readAsDataURL(file.raw)
      reader.onload = () => {
        currentAvatar.value = reader.result
      }
    }
    
    // 保存头像 - 将选择的头像上传到服务器
    const saveAvatar = async () => {
      if (!newAvatar.value) {
        ElMessage.warning('Please select an image first')
        return
      }
      let params = {
        avatarPath: newAvatar.value,
        userId: JSON.parse(localStorage.getItem('user')).userId
      }
      
      try {
        const res = await updateUser(params)
        if (res.code === 200) {
          ElMessage.success('Avatar updated successfully')
          newAvatar.value = ''
          let res1 = await getUser(JSON.parse(localStorage.getItem('user')).userId)
          if (res1.code === 200) {
            // 更新缓存
            localStorage.setItem('user', JSON.stringify(res1.data))
          }
        } else {
          ElMessage.error(res.msg || 'Update failed')
        }
      } catch (error) {
        console.error('Update avatar error:', error)
        ElMessage.error('Failed to update avatar')
      }
    }
    
    // 密码处理相关方法
    const savePassword = async () => {
      if (!passwordFormRef.value) return
      
      await passwordFormRef.value.validate((valid) => {
        if (valid) {
          // 模拟API请求
          updateUser({
            userId:JSON.parse(localStorage.getItem('user')).userId,
            password:passwordForm.newPassword
          }).then(res => {
            if(res.code===200){
              ElMessage.success('Password updated successfully')
              passwordForm.oldPassword = ''
              passwordForm.newPassword = ''
              passwordForm.confirmPassword = ''
            }else{
              ElMessage.error(res.msg || 'Update failed')
            }
          })
        }
      })
    }
    
    // Removed Phone Number related functions (sendPhoneVerifyCode, savePhone)
    
    // 邮箱处理相关方法
    const sendVerifyCode = async () => {
      if (!emailForm.newEmail) {
        ElMessage.warning('Please enter your new email first')
        return
      }
      // 设置60秒倒计时
      countDown.value = 60
          const timer = setInterval(() => {
            countDown.value--
            if (countDown.value <= 0) {
              clearInterval(timer)
            }
          }, 1000)
      try {
        const res = await getCaptcha(emailForm.newEmail)
        if (res.code === 200) {
          ElMessage.success(`Verification code sent to ${emailForm.newEmail}`)
          
        } else {
          ElMessage.error(res.msg || 'Failed to send verification code')
        }
      } catch (error) {
        console.error('Send verification code error:', error)
        ElMessage.error('Failed to send verification code')
      }
    }
    const saveEmail = async () => {
      if (!emailFormRef.value) return


      
       emailFormRef.value.validate((valid) => {
        if (valid) {
          let params = {
            email: emailForm.newEmail,
            verificationCode: emailForm.verifyCode,
            userId: JSON.parse(localStorage.getItem('user')).userId
          }
          let res =  updateUser(params).then(res => {
            console.log(res);
          
          if (res.code === 200) {
            ElMessage.success('Email updated successfully')
            emailForm.newEmail = ''
            emailForm.verifyCode = ''
          } else {
            ElMessage.error("verifyCode error")
          }
          });
          
          
        }
      })
    }
    
    // 账户删除相关方法
    const showDeleteConfirm = () => {
      deleteDialogVisible.value = true
    }
    
    const confirmDeleteAccount = () => {
      if (!deleteConfirmPassword.value) {
        ElMessage.warning('Please enter your password to confirm')
        return
      }

      let params = {
        account: JSON.parse(localStorage.getItem('user')).userId,
        password: deleteConfirmPassword.value
      }
      deleteByAccountAndPassword(params.account,params.password).then(res => {

      if (res.code === 200) {
        ElMessage.success('Account deleted successfully')
        // 清除本地存储并重定向
        localStorage.removeItem('token')
            localStorage.removeItem('userRole')
            localStorage.removeItem('user')
            window.location.href = '/'

      } else {
        ElMessage.error(res.msg || 'Delete failed')
      }
      });
      
    
    }
    
    // 返回上一页
    const goBack = () => {
      window.history.back()
    }
    
    return {
      // 菜单相关
      activeTab,
      menuItems,
      goBack,
      ArrowLeft,
      
      // 用户数据
      userEmail,
      currentAvatar,
      newAvatar,
      // Removed currentPhone from return
      
      // 头像相关
      handleAvatarChange,
      saveAvatar,
      
      // Removed Phone related vars/functions from return
      
      // 密码相关
      passwordFormRef,
      passwordForm,
      passwordRules,
      savePassword,
      
      // 邮箱相关
      emailFormRef,
      emailForm,
      emailRules,
      countDown,
      sendVerifyCode,
      saveEmail,
      
      // 账户删除相关
      deleteDialogVisible,
      deleteConfirmPassword,
      showDeleteConfirm,
      confirmDeleteAccount,
      action,
      handleAvatarSuccess
    }
  }
}
</script>

<style scoped>
/* Replace entire style section with frontend/Settings.vue styles */
/* 设置页面容器 */
.settings-page {
  padding: 20px;
  background-color: #f7f8fc;
  min-height: calc(100vh - 60px); /* Assuming header height is 60px */
}

/* 内容限宽容器 */
.container {
  max-width: 1200px;
  margin: 0 auto;
  background-color: #fff;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
}

/* 页面头部样式 */
.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 25px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

/* 返回按钮样式 */
.back-button {
  margin-right: 15px;
}
.back-button span {
  margin-left: 5px;
}

/* 页面标题样式 */
.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0;
}

/* 设置区域包装容器 - 左右分栏 */
.settings-wrapper {
  display: flex;
  gap: 30px;
}

/* 左侧菜单栏样式 */
.settings-menu {
  width: 200px;
  flex-shrink: 0;
  border-right: 1px solid #eee;
  padding-right: 20px;
}

/* 菜单项样式 */
.menu-item {
  display: flex;
  align-items: center;
  padding: 12px 15px;
  cursor: pointer;
  border-radius: 6px;
  margin-bottom: 8px;
  font-size: 15px;
  color: #606266;
  transition: background-color 0.2s ease, color 0.2s ease;
}
.menu-item .el-icon {
  margin-right: 10px;
  font-size: 18px;
}

.menu-item:hover {
  background-color: #ecf5ff;
  color: #409eff;
}

/* 当前激活的菜单项样式 */
.menu-item.active {
  background-color: #409eff;
  color: #fff;
  font-weight: 500;
}
.menu-item.active:hover {
   background-color: #3a8ee6;
}

/* 右侧内容区样式 */
.settings-content {
  flex-grow: 1;
}

/* 标签内容区域样式 */
.tab-content {
  /* Add some padding if needed */
}

/* 标签内容标题样式 */
.tab-content h2 {
  font-size: 20px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 25px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

/* Avatar Specific Styles */
.avatar-section {
  display: flex;
  gap: 30px;
  align-items: flex-start;
  margin-bottom: 20px;
}

.avatar-preview-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.large-avatar-preview {
  border: 1px solid #eee;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}

.preview-label {
  font-size: 13px;
  color: #909399;
}

.upload-controls {
  display: flex;
  flex-direction: column;
  gap: 15px;
  align-items: flex-start;
}

.el-upload__tip {
  color: #909399;
  font-size: 13px;
  margin-top: -5px; /* Adjust based on button size */
  line-height: 1.4;
}

.save-avatar-button {
  margin-top: 10px;
}

/* Phone/Email Verification Input */
.verify-code-wrapper {
  display: flex;
  gap: 10px;
}
.verify-code-wrapper .el-input {
  flex-grow: 1;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  line-height: 1.4;
  margin-top: 5px;
}

/* Current Info Display */
.current-info,
.current-email {
  margin-bottom: 20px;
  color: #606266;
}
.not-set {
  color: #909399;
  font-style: italic;
}

/* Security Section */
.security-wrapper {
  padding: 15px;
  border: 1px solid #fde2e2; /* Light red border */
  background-color: #fef0f0; /* Light red background */
  border-radius: 4px;
}
.warning {
  color: #f56c6c; /* Danger red */
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  gap: 8px;
}
.warning i {
  font-size: 18px;
}

/* Delete Confirmation Dialog */
.delete-confirm .warning-text {
  margin-bottom: 15px;
  color: #e6a23c; /* Warning yellow */
}
.delete-confirm .el-input {
  margin-bottom: 10px;
}

/* General Info Box */
.info-box {
  padding: 15px;
  border-radius: 4px;
  margin-top: 20px;
  display: flex;
  align-items: center; /* Align icon and text */
  gap: 10px;
}
.info-box i {
  font-size: 20px; /* Ensure icons are visible */
}
.info-box p {
   margin: 0; /* Remove default paragraph margin */
}

.success-box {
  background-color: #f0f9eb; /* Light green background */
  border: 1px solid #e1f3d8; /* Light green border */
  color: #67c23a; /* Success green */
}
.success-box i {
  color: #67c23a; /* Success green icon */
}

/* Alert messages styling */
.el-alert {
  margin-bottom: 20px;
}

</style> 