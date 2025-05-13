<template>
  <div class="appeal-page">
    <div class="container">
      <div class="appeal-card">
        <!-- 账号限制通知区域 -->
        <div class="ban-notice">
          <i class="el-icon-warning-outline warning-icon"></i>
          <h2>Your account has been restricted</h2>
          
          <!-- 限制原因显示区域 - 条件渲染 -->
          <div class="ban-reason" v-if="banReason">
            <h3>Reason for restriction:</h3>
            <p>{{ banReason }}</p>
          </div>
          
          <!-- 限制日期信息 -->
          <div class="ban-date">
            <p>Restriction date: {{ banDate }}</p>
          </div>
        </div>
        
        <!-- 申诉表单区域 -->
        <div class="appeal-form">
          <h3>Appeal Form</h3>
          <el-form 
            :model="appealForm" 
            :rules="rules" 
            ref="appealFormRef"
            label-width="100px"
          >
              <!-- 邮箱 -->
              <el-form-item label="Email" prop="email">
              <el-input v-model="appealForm.email" placeholder="Please enter your email"></el-input>
            </el-form-item>
            <!-- 申诉理由输入框 -->
            <el-form-item label="Reason" prop="reason">
              <el-input 
                type="textarea" 
                v-model="appealForm.reason"
                :rows="6"
                placeholder="Please explain your appeal reason in detail..."
              ></el-input>
            </el-form-item>
        
            

            <!-- 提交按钮 -->
            <el-form-item>
              <el-button type="primary" @click="submitAppeal" :loading="submitting">Submit Appeal</el-button>
            </el-form-item>
          </el-form>
        </div>
        
        <!-- 申诉成功通知对话框 -->
        <el-dialog
          v-model="successDialogVisible"
          title="Appeal Submitted"
          width="400px"
        >
          <div class="success-dialog">
            <i class="el-icon-success success-icon"></i>
            <p>Your appeal has been successfully submitted. We will review it within 3 business days.</p>
            <p>Please check your email or system notifications for updates.</p>
          </div>
          <template #footer>
            <el-button type="primary" @click="successDialogVisible = false">OK</el-button>
          </template>
        </el-dialog>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { appeal } from '../api'
export default {
  name: 'Appeal',
  setup() {
    // 表单引用 - 用于表单验证
    const appealFormRef = ref(null)
    // 提交状态标志 - 控制按钮loading状态
    const submitting = ref(false)
    // 成功对话框显示状态
    const successDialogVisible = ref(false)
    // 账号限制原因
    const banReason = ref('')
    // 账号限制日期
    const banDate = ref('')
    
    // 申诉表单数据对象
    const appealForm = reactive({
      reason: '',
      email: ''
    })
    
    // 表单验证规则
    const rules = {
      reason: [
        { required: true, message: 'Please fill in the appeal reason', trigger: 'blur' },
        { min: 20, message: 'Appeal reason must be at least 20 characters', trigger: 'blur' }
      ],
      email: [
        { required: true, message: 'Please enter your email', trigger: 'blur' },
        { type: 'email', message: 'Please enter a valid email address', trigger: ['blur', 'change'] }
      ]
    }
    
    // 组件挂载时获取限制信息
    onMounted(() => {
      // 模拟从服务器获取账号限制原因和时间
      // 实际项目中应该通过API获取这些数据
      setTimeout(() => {
        banReason.value = 'Violation of community rules: posted inappropriate content'
        banDate.value = '2023-04-01 10:30:45'
      }, 500)
    })
    
    // 提交申诉处理函数
    const submitAppeal = async () => {
      if (!appealFormRef.value) return
      
      // 表单验证
      await appealFormRef.value.validate((valid) => {
        if (valid) {
          // 开始提交 - 显示loading状态
          submitting.value = true
          appeal({
            appealMessage:appealForm.reason,
            email:appealForm.email
          }).then(res => {
            if(res.code===200){
              ElMessage.success('Appeal submitted successfully')
              successDialogVisible.value = true
              submitting.value = false
            }else{
              ElMessage.error(res.msg || 'Appeal failed')
              submitting.value = false
            }
          })
         
        } else {
          // 验证失败提示
          ElMessage.warning('Please fill in the appeal reason correctly')
        }
      })
    }
    
    // 返回模板需要的响应式数据和方法
    return {
      appealFormRef,
      appealForm,
      rules,
      submitting,
      submitAppeal,
      successDialogVisible,
      banReason,
      banDate
    }
  }
}
</script>

<style scoped>
/* 申诉页面容器样式 - 居中显示 */
.appeal-page {
  padding: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 200px);
}

/* 内容宽度限制 */
.container {
  width: 100%;
  max-width: 600px;
}

/* 申诉卡片样式 - 添加阴影和圆角 */
.appeal-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 30px;
}

/* 限制通知区域样式 */
.ban-notice {
  text-align: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #ebeef5;
}

/* 警告图标样式 */
.warning-icon {
  font-size: 48px;
  color: #e6a23c;
  margin-bottom: 15px;
}

/* 限制标题样式 */
.ban-notice h2 {
  color: #f56c6c;
  margin-bottom: 15px;
  font-size: 22px;
}

/* 限制原因容器样式 */
.ban-reason {
  margin: 20px 0;
  text-align: left;
  background: #fdf6ec;
  padding: 15px;
  border-radius: 4px;
}

/* 限制原因标题样式 */
.ban-reason h3 {
  font-size: 16px;
  margin-bottom: 10px;
  color: #606266;
}

/* 限制日期样式 */
.ban-date {
  margin-top: 15px;
  color: #909399;
  font-size: 14px;
}

/* 申诉表单样式 */
.appeal-form {
  margin-top: 30px;
}

/* 申诉表单标题样式 */
.appeal-form h3 {
  margin-bottom: 20px;
  font-size: 18px;
  color: #303133;
}

/* 成功对话框内容样式 */
.success-dialog {
  text-align: center;
  padding: 20px 0;
}

/* 成功图标样式 */
.success-icon {
  font-size: 48px;
  color: #67c23a;
  margin-bottom: 20px;
}
</style> 