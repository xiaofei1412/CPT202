<template>
  <MainLayout>
    <div class="log-edit-page">
      <!-- 页面标题区域 - 带有淡入动画效果 -->
      <div class="page-header" data-aos="fade-down">
        <h1 class="page-title">Edit Reading Entry</h1>
        <p class="page-description">Update your reading journal</p>
      </div>

      <!-- 编辑表单容器 - 淡入动画效果 -->
      <div class="edit-container" data-aos="fade-up">
        <el-form
          ref="formRef"
          :model="logForm"
          :rules="rules"
          label-position="top"
          class="log-form"
        >
          <!-- 书籍标题输入区域 - 带有字数限制和悬停动画 -->
          <el-form-item label="Book Title" prop="title">
            <el-input 
              v-model="logForm.title"
              placeholder="Enter the title of the book"
              maxlength="100"
              show-word-limit
              v-motion="{ hover: { scale: 1.02 } }"
            >
              <template #prefix>
                <el-icon><Reading /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <!-- 作者名称输入区域 - 带有字数限制和悬停动画 -->
          <el-form-item label="Author" prop="author">
            <el-input
              v-model="logForm.author"
              placeholder="Enter the author's name"
              maxlength="50"
              show-word-limit
              v-motion="{ hover: { scale: 1.02 } }"
            >
              <template #prefix>
                <el-icon><User /></el-icon>
              </template>
            </el-input>
          </el-form-item>

          <!-- 阅读状态选择区域 - 完成、进行中、待读 -->
          <el-form-item label="Reading Status" prop="status">
            <el-select
              v-model="logForm.readStatus"
              placeholder="Select reading status"
              class="w-full"
              v-motion="{ hover: { scale: 1.02 } }"
            >
              <el-option label="Completed" value="completed" />
              <el-option label="In Progress" value="in-progress" />
              <el-option label="To Read" value="to-read" />
            </el-select>
          </el-form-item>

          <!-- 阅读日期和时长输入区域 - 水平布局的两个输入项 -->
          <div class="form-row">
            <!-- 阅读日期选择器 -->
            <el-form-item label="Reading Date" prop="readingDate" class="date-item">
              <el-date-picker
                v-model="logForm.readingDate"
                type="date"
                placeholder="Select reading date"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
                class="w-full"
                v-motion="{ hover: { scale: 1.02 } }"
              />
            </el-form-item>

            <!-- 阅读时长输入框 - 限制在1-1440分钟之间 -->
            <el-form-item label="Reading Duration (minutes)" prop="readTime" class="time-item">
              <div class="duration-wrapper">
                <el-input-number
                  v-model="logForm.readingTime"
                  :min="1"
                  :max="1440"
                  controls-position="right"
                  class="w-full"
                  v-motion="{ hover: { scale: 1.02 } }"
                />
              </div>
            </el-form-item>
          </div>
          
          <!-- 阅读笔记输入区域 - 大型文本框用于详细笔记 -->
          <el-form-item label="Reading Notes & Thoughts" prop="content">
            <div class="editor-wrapper" v-motion="{ hover: { scale: 1.01 } }">
              <el-input
                v-model="logForm.personalNotes"
                type="textarea"
                :rows="8"
                placeholder="Enter your reading notes and reflections..."
                maxlength="5000"
                show-word-limit
              />
            </div>
          </el-form-item>
          
          <!-- 阅读标签区域 - 可添加多个自定义标签 -->
          <el-form-item label="Reading Tags">
            <div class="tags-container" v-motion="{ hover: { scale: 1.02 } }">
            
              <el-select v-model="logForm.tags" multiple placeholder="Select tags" class="w-full">
                <el-option v-for="tag in tagList" :key="tag" :label="tag" :value="tag"></el-option>
              </el-select>
            </div>
          </el-form-item>

          <!-- 阅读心情评分 - 五星评分组件 -->
          <el-form-item label="Reading Mood">
            <div class="mood-container" v-motion="{ hover: { scale: 1.05 } }">
              <el-rate
                v-model="logForm.mood"
                :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                :texts="['Average', 'Good', 'Very Good', 'Great', 'Excellent']"
                show-text
              />
            </div>
          </el-form-item>
          
          <!-- 表单按钮区域 - 取消和保存按钮 -->
          <div class="action-buttons">
            <el-button @click="cancel" class="cancel-button">
              Cancel
            </el-button>
            <el-button 
              type="primary" 
              :loading="loading"
              @click="updateLog"
              class="submit-button"
              v-motion="{ hover: { scale: 1.05 } }"
            >
              Save Changes
            </el-button>
          </div>
        </el-form>
      </div>

      <!-- 确认对话框 - 保存前的二次确认 -->
      <el-dialog
        v-model="confirmDialogVisible"
        title="Confirm Changes"
        width="30%">
        <span>Are you sure you want to save these changes?</span>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="confirmDialogVisible = false">Cancel</el-button>
            <el-button type="primary" @click="confirmUpdate">
              Confirm
            </el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import { Reading, User, Plus } from '@element-plus/icons-vue'
import { useMotion } from '@vueuse/motion'
import { ElMessage } from 'element-plus'
import gsap from 'gsap'
import AOS from 'aos'
import 'aos/dist/aos.css'
import { createLog } from '../api/home'
const tagList = ref([
"Fiction",
"Non-Fiction",
"Science Fiction",
"Fantasy",
"Mystery",
"Thriller",
"Romance",
"Historical Fiction",
"Horror",
"Biography",
"Autobiography",
"Young Adult (YA)",
"Children's",
"Classics",
"Contemporary",
"Dystopian",
"Adventure",
"Poetry",
"Self-Help",
"Graphic Novel",
"Memoir",
"Humor" 
])

// 初始化动画效果
onMounted(() => {
  AOS.init({
    duration: 800,
    once: true
  })
  
  // If route has an id parameter, load the log for editing
  if (route.params.id) {
    loadLog()
  } else {
    // Initialize with empty form for creating a new log
    loading.value = false
  }

  // Mock data for English titles
  const mockTitles = [
    "To Kill a Mockingbird",
    "1984",
    "Pride and Prejudice",
    "The Great Gatsby",
    "The Catcher in the Rye"
  ]
  
  // Replace any Chinese title in the log form with an English one
  if (logForm.title && logForm.title.includes('阅读日志')) {
    const randomIndex = Math.floor(Math.random() * mockTitles.length)
    logForm.title = mockTitles[randomIndex]
    logForm.author = getAuthorForTitle(mockTitles[randomIndex])
  }
})

const router = useRouter()
const route = useRoute()
const loading = ref(true)
const formRef = ref(null)
const confirmDialogVisible = ref(false)

// Form data
const logForm = ref({
  title: '',
  author: '',
  status: 'in-progress',
  readDate: '',
  readTime: 60,
  content: '',
  mood: 3,
  tags: []
})

// Form validation rules
const rules = {
  title: [{ required: true, message: 'Please enter the book title', trigger: 'blur' }],
  author: [{ required: true, message: 'Please enter the author\'s name', trigger: 'blur' }],
  status: [{ required: true, message: 'Please select a reading status', trigger: 'change' }],
  readingDate: [{ required: true, message: 'Please select a reading date', trigger: 'change' }],
  readTime: [{ required: true, message: 'Please enter reading duration', trigger: 'change' }],
  content: [
    { required: true, message: 'Please enter your reading notes', trigger: 'blur' },
    { min: 50, message: 'Notes should be at least 50 characters', trigger: 'blur' }
  ]
}

// Tags related
const inputTagVisible = ref(false)
const inputTagValue = ref('')
const tagInputRef = ref(null)
const selectedTags = ref([])

// Simulate loading a log from API for editing
const loadLogDetails = () => {
  // Get random English book title and author
  const bookTitles = [
    "To Kill a Mockingbird",
    "1984",
    "Pride and Prejudice",
    "The Great Gatsby",
    "The Catcher in the Rye"
  ];
  const bookAuthors = [
    "Harper Lee",
    "George Orwell",
    "Jane Austen",
    "F. Scott Fitzgerald",
    "J.D. Salinger"
  ];
  const randomIndex = Math.floor(Math.random() * bookTitles.length);
  
  // Mocked sample log data
  const mockLog = {
    id: route.params.id,
    title: bookTitles[randomIndex],
    author: bookAuthors[randomIndex],
    readDate: '2023-06-15',
    duration: 90,
    notes: "This book offers profound insights into human nature and society. The characters are brilliantly developed, and the narrative explores themes that remain relevant today. I particularly enjoyed the author's distinctive writing style and the way the plot unfolds.",
    tags: ['Classic Literature', 'Fiction', 'Social Commentary'],
    mood: 4
  }
  
  // Populate the form with mock data
  logForm.value = { ...mockLog }
  
  // Set tags properly
  selectedTags.value = [...mockLog.tags]
  
  // Apply loading animation
  gsap.from('.log-form', {
    opacity: 0,
    y: 20,
    duration: 0.5,
    ease: 'power2.out'
  })
  
  loading.value = false
}

const showTagInput = () => {
  inputTagVisible.value = true
  nextTick(() => {
    tagInputRef.value?.input?.focus()
  })
}

const handleTagClose = (tag) => {
  selectedTags.value = selectedTags.value.filter(t => t !== tag)
  
  // Tag removal animation
  gsap.to(`.tag-${selectedTags.value.indexOf(tag)}`, {
    scale: 0.5,
    opacity: 0,
    duration: 0.2
  })
}

const handleTagInputConfirm = () => {
  if (inputTagValue.value && !selectedTags.value.includes(inputTagValue.value)) {
    selectedTags.value.push(inputTagValue.value)
    
    // New tag animation
    gsap.from(`.tag-${selectedTags.value.length - 1}`, {
      scale: 0.5,
      opacity: 0,
      duration: 0.3
    })
  }
  inputTagVisible.value = false
  inputTagValue.value = ''
}

// Load a log from the store
const loadLog = async () => {
  try {
    loading.value = true
    
    // Get the log from the store
    const logData = await loadLogDetails()
    
    // English books and authors to use when replacing Chinese content
    const englishBooks = [
      { title: "To Kill a Mockingbird", author: "Harper Lee" },
      { title: "1984", author: "George Orwell" },
      { title: "Pride and Prejudice", author: "Jane Austen" },
      { title: "The Great Gatsby", author: "F. Scott Fitzgerald" },
      { title: "The Catcher in the Rye", author: "J.D. Salinger" }
    ]
    
    // If the log has a Chinese title, replace it with an English one
    if (logForm.value.title && logForm.value.title.includes('阅读日志')) {
      const randomIndex = Math.floor(Math.random() * englishBooks.length)
      const englishBook = englishBooks[randomIndex]
      
      logForm.value.title = englishBook.title
      logForm.value.author = englishBook.author
    }
    
    loading.value = false
  } catch (error) {
    console.error('Error loading log:', error)
    ElMessage.error('Failed to load log data')
    loading.value = false
  }
}

// Update log - show confirmation dialog
const updateLog = async () => {
  if (!formRef.value) return
  const userId = JSON.parse(localStorage.getItem('user')).userId
  createLog({ ...logForm.value, userId }).then(res => {
    if (res.code === 200) {
      ElMessage.success('Log created successfully')
      router.push('/logs')
    } else {
      ElMessage.error(res.message)
    }
  })
  
  // try {
  //   await formRef.value.validate()
  //   confirmDialogVisible.value = true
  // } catch (error) {
  //   console.error('Form validation failed:', error)
  // }
}

// Confirm update and submit
const confirmUpdate = async () => {
  loading.value = true
  
  // Form submission animation
  gsap.to('.log-form', {
    scale: 0.98,
    opacity: 0.8,
    duration: 0.2
  })
  
  // Simulate API call
  await new Promise(resolve => setTimeout(resolve, 1000))
  
  // Success animation
  gsap.to('.log-form', {
    scale: 1,
    opacity: 1,
    duration: 0.3
  })
  
  ElMessage({
    type: 'success',
    message: 'Reading log updated successfully!'
  })
  
  confirmDialogVisible.value = false
  loading.value = false
  
  // Navigate back to log list
  setTimeout(() => {
    router.push('/logs')
  }, 1000)
}

// Cancel editing
const cancel = () => {
  router.push('/logs')
}

// Helper function to get an appropriate author for a title
const getAuthorForTitle = (title) => {
  const authorMap = {
    "To Kill a Mockingbird": "Harper Lee",
    "1984": "George Orwell",
    "Pride and Prejudice": "Jane Austen",
    "The Great Gatsby": "F. Scott Fitzgerald",
    "The Catcher in the Rye": "J.D. Salinger"
  }
  return authorMap[title] || "Unknown Author"
}
</script>

<style scoped>
.log-edit-page {
  max-width: 100%;
  margin: 0 auto;
  padding: 2rem 1rem;
}

.page-header {
  text-align: center;
  margin-bottom: 2rem;
}

.page-title {
  font-size: 2.2rem;
  color: var(--el-color-primary);
  margin-bottom: 0.5rem;
}

.page-description {
  color: var(--el-text-color-secondary);
  font-size: 1.1rem;
}

.edit-container {
  max-width: 100%;
  margin: 0 auto;
  background: white;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.log-form {
  width: 100%;
}

.form-row {
  display: flex;
  gap: 1rem;
  width: 100%;
}

.date-item {
  flex: 1;
}

.time-item {
  flex: 1;
}

.duration-wrapper {
  width: 100%;
}

.editor-wrapper {
  width: 100%;
  background: var(--el-color-primary-light-9);
  border-radius: 8px;
  padding: 1rem;
  transition: all 0.3s ease;
  min-height: 300px;
}

.editor-wrapper:hover {
  background: var(--el-color-primary-light-8);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  padding: 1rem;
  background: var(--el-color-primary-light-9);
  border-radius: 8px;
  min-height: 3rem;
  width: 100%;
  transition: all 0.3s ease;
}

.tags-container:hover {
  background: var(--el-color-primary-light-8);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.tag-item {
  transition: all 0.3s ease;
  border-radius: 12px;
  padding: 0 12px;
}

.tag-item:hover {
  transform: scale(1.05);
}

.tag-input {
  width: 120px;
  margin-right: 0.5rem;
}

.add-tag-button {
  height: 32px;
  padding: 0 10px;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 0.3rem;
}

.mood-container {
  padding: 1rem;
  background: var(--el-color-primary-light-9);
  border-radius: 8px;
  transition: all 0.3s ease;
  width: 100%;
}

.mood-container:hover {
  background: var(--el-color-primary-light-8);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.action-buttons {
  display: flex;
  gap: 1rem;
  margin-top: 1rem;
  justify-content: flex-end;
}

.cancel-button, .submit-button {
  padding: 0.75rem 1.5rem;
  font-size: 1rem;
}

.submit-button {
  min-width: 120px;
}

.w-full {
  width: 100%;
}

@media (max-width: 768px) {
  .edit-container {
    padding: 1.5rem;
  }
  
  .form-row {
    flex-direction: column;
    gap: 0;
  }
  
  .action-buttons {
    flex-direction: column;
  }
  
  .cancel-button, .submit-button {
    width: 100%;
  }
}
</style> 