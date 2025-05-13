<template>
  <MainLayout>
    <div class="feedback-page">
      <!-- 页面标题区域 - 带有淡入动画效果 -->
      <div class="page-header" data-aos="fade-down">
        <h2 class="page-title title-water-effect">
          <el-icon><EditPen /></el-icon> Feedback & Suggestions
        </h2>
        <p class="page-description">Help us improve the platform with your feedback</p>
      </div>
      
      <!-- 反馈表单容器 - 带有淡入动画效果 -->
      <div class="feedback-container" data-aos="fade-up">
        <el-card>
          <!-- 反馈类型选择区域 - 不同反馈类型的选择按钮 -->
          <div class="feedback-types">
            <div 
              v-for="type in feedbackTypes" 
              :key="type.value"
              :class="['feedback-type', { 'active': selectedType === type.value }]"
              @click="selectedType = type.value"
              v-motion="{ hover: { scale: 1.05 } }">
              <el-icon>
                <component :is="type.icon"></component>
              </el-icon>
              <span>{{ type.label }}</span>
            </div>
          </div>

          <!-- 反馈内容表单区域 -->
          <div class="feedback-form">
            <el-form ref="feedbackForm" :model="feedbackData" :rules="rules">
              <!-- 反馈标题输入 -->
              <el-form-item prop="title">
                <el-input 
                  v-model="feedbackData.title" 
                  placeholder="Title (what is this feedback about?)" 
                  maxlength="50" 
                  show-word-limit />
              </el-form-item>

              <!-- 反馈内容详情输入 - 文本域 -->
              <el-form-item prop="description">
                <el-input 
                  v-model="feedbackData.description" 
                  type="textarea" 
                  placeholder="Please describe your feedback in detail..." 
                  rows="6"
                  maxlength="500"
                  show-word-limit />
              </el-form-item>

              <!-- 截图上传区域 - 最多3张图片 -->
              <el-form-item>
                <el-upload
                  ref="uploadRef"
                  :action="action"
                  list-type="picture-card"
                  :multiple="true"
                  :limit="3"
                  @success="handleAvatarSuccess"
                  :auto-upload="true">
                  <template #default>
                    <el-icon style="font-size: 18px;"><Plus /></el-icon>
                    <div style="margin-top: 8px; font-size: 12px;">Add screenshots</div>
                  </template>
                </el-upload>
                <p class="upload-tip">Images help us better understand your feedback (up to 3 images, max 5MB each)</p>
              </el-form-item>

              <!-- 联系方式输入区域 - 可选填写 -->
              <el-form-item>
                <el-input
                  v-model="feedbackData.contactInfo"
                  placeholder="Contact info (optional)" />
                <p class="contact-tip">
                  Leave your email if you want us to follow up with you
                </p>
              </el-form-item>

              <!-- 提交按钮 -->
              <el-form-item>
                <el-button 
                  type="primary" 
                  :loading="submitting"
                  @click="submitFeedback"
                  class="submit-btn">
                  Submit Feedback
                </el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-card>
      </div>

      <!-- 常见问题区域 - 折叠面板形式 -->
      <div class="feedback-faq" data-aos="fade-up">
        <h2>Frequently Asked Questions</h2>
        <el-collapse>
          <!-- 问题1: 响应时间 -->
          <el-collapse-item title="When will I receive a response to my feedback?" name="1">
            <div>We typically respond to feedback within 1-3 business days, depending on the nature of your feedback.</div>
          </el-collapse-item>
          <!-- 问题2: 反馈处理流程 -->
          <el-collapse-item title="What happens after I submit feedback?" name="2">
            <div>Our team will review your feedback and categorize it based on priority. If it's a bug, we'll add it to our fix list. If it's a feature request, we'll consider it for future releases.</div>
          </el-collapse-item>
          <!-- 问题3: 反馈状态查询 -->
          <el-collapse-item title="Can I check the status of my feedback?" name="3">
            <div>Yes! If you provided contact information, we'll keep you updated on the status of your feedback or report.</div>
          </el-collapse-item>
          <!-- 问题4: 严重问题报告方式 -->
          <el-collapse-item title="How do I report critical issues?" name="4">
            <div>For critical issues that need immediate attention, please select "Bug Report" as the feedback type and clearly mark it as urgent in your title.</div>
          </el-collapse-item>
        </el-collapse>
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, reactive } from 'vue'
import MainLayout from '../layouts/MainLayout.vue'
import { QuestionFilled, WarningFilled, StarFilled, BellFilled, Plus, EditPen } from '@element-plus/icons-vue'
import { useMotion } from '@vueuse/motion'
import { ElMessage } from 'element-plus'
import AOS from 'aos'
import 'aos/dist/aos.css'
import { onMounted } from 'vue'
import { createFeedback } from '../api/feedback'
import { baseURL } from '../api/request'


const action=baseURL+'/cpt/file/upload'
// 初始化AOS动画库
onMounted(() => {
  AOS.init({
    duration: 800,  // 动画持续时间（毫秒）
    once: true      // 动画只执行一次
  })
})

// 反馈类型定义 - 包含标签、值和图标
const feedbackTypes = [
  { label: 'Feature Request', value: 'feature', icon: 'StarFilled' },  // 功能请求
  { label: 'Bug Report', value: 'bug', icon: 'WarningFilled' },        // 错误报告
  { label: 'General Question', value: 'question', icon: 'QuestionFilled' }, // 一般问题
  { label: 'Other', value: 'other', icon: 'BellFilled' }               // 其他反馈
]

// 当前选中的反馈类型 - 默认为功能请求
const selectedType = ref('feature')
// 提交状态标志 - 控制按钮加载状态
const submitting = ref(false)

// 反馈表单数据对象
const feedbackData = reactive({
  title: '',       // 反馈标题
  description: '', // 反馈内容
  contactInfo: '', // 联系方式
  type: selectedType, // 反馈类型
  attachments: []  // 上传的文件列表
})

// 表单验证规则
const rules = {
  title: [
    { required: true, message: 'Please enter a title', trigger: 'blur' },
    { min: 5, message: 'Title should be at least 5 characters', trigger: 'blur' }
  ],
  description: [
    { required: true, message: 'Please enter feedback content', trigger: 'blur' },
    { min: 20, message: 'Content should be at least 20 characters', trigger: 'blur' }
  ]
}

const handleAvatarSuccess = (response) => {
  if (response.code === 200) {
    feedbackData.attachments.push(response.msg)
  }
}

// 表单引用 - 用于表单验证
const feedbackForm = ref(null)
const uploadRef = ref(null)

// 提交反馈的方法
const submitFeedback = async () => {
  if (!feedbackForm.value) return
  
  // 表单验证
  await feedbackForm.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true // 设置加载状态
      
      try {
        // 调用createFeedback API
        await createFeedback({
          feedbackType: selectedType.value,
          title: feedbackData.title,
          description: feedbackData.description,
          contactInfo: feedbackData.contactInfo,
          userId: JSON.parse(localStorage.getItem('user')).userId,
          attachments: feedbackData.attachments
        })
        
        // 提交成功提示
        ElMessage({
          message: 'Thank you for your feedback!',
          type: 'success'
        })
        
        // 重置表单
        feedbackData.title = ''
        feedbackData.description = ''
        feedbackData.contactInfo = ''
        feedbackData.attachments = []
        // 清空上传文件列表
        uploadRef.value?.clearFiles()
        
      } catch (error) {
        // 提交失败提示
        ElMessage({
          message: 'Failed to submit feedback. Please try again.',
          type: 'error'
        })
      } finally {
        submitting.value = false // 重置加载状态
      }
    }
  })
}
</script>

<style scoped>
/* 反馈页面容器样式 */
.feedback-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 2rem 1rem;
}

/* 页面标题区域样式 */
.page-header {
  text-align: center;
  margin-bottom: 2rem;
}

/* 页面主标题样式 */
.page-title {
  font-size: 2.2rem;
  color: var(--el-color-primary);
  margin-bottom: 0.5rem;
}

/* 页面描述文字样式 */
.page-description {
  color: var(--el-text-color-secondary);
  font-size: 1.1rem;
}

/* 反馈类型选择区域样式 - 弹性布局 */
.feedback-types {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin-bottom: 2rem;
  justify-content: center;
}

/* 单个反馈类型按钮样式 */
.feedback-type {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 1rem;
  border: 1px solid var(--el-border-color);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  width: 110px;
  text-align: center;
  height: 90px;
}

/* 选中状态的反馈类型按钮样式 */
.feedback-type.active {
  border-color: var(--el-color-primary);
  background-color: var(--el-color-primary-light-9);
}

/* 反馈类型图标样式 */
.feedback-type i {
  font-size: 24px;
  margin-bottom: 8px;
  color: var(--el-color-primary);
}

/* 上传提示和联系方式提示文字样式 */
.upload-tip, .contact-tip {
  font-size: 0.8rem;
  color: var(--el-text-color-secondary);
  margin-top: 0.5rem;
}

/* 提交按钮样式 */
.submit-btn {
  width: 100%;
  margin-top: 1rem;
}

/* 常见问题区域样式 */
.feedback-faq {
  margin-top: 3rem;
}

/* 常见问题标题样式 */
.feedback-faq h2 {
  font-size: 1.5rem;
  margin-bottom: 1rem;
  color: var(--el-color-primary);
}

/* 响应式布局调整 */
@media (max-width: 768px) {
  /* 移动设备下反馈类型区域样式调整 */
  .feedback-types {
    gap: 0.5rem;
    justify-content: space-between;
  }
  
  /* 移动设备下单个反馈类型按钮样式调整 */
  .feedback-type {
    width: calc(50% - 0.5rem);
    padding: 0.8rem;
    font-size: 14px;
    height: 80px;
  }
  
  /* 移动设备下图标样式调整 */
  .feedback-type i {
    font-size: 20px;
    margin-bottom: 6px;
  }
}
</style> 