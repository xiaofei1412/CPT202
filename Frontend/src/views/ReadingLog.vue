<!-- Reading Journal Page -->
<template>
  <MainLayout>
    <div class="reading-log-page">
      <!-- 页面标题区域 - 带有向下淡入动画 -->
      <div class="page-header" data-aos="fade-down">
        <h2 class="page-title title-water-effect">
          <el-icon><Collection /></el-icon> Reading Journal
        </h2>
        <p class="page-description">Track your reading progress and reflections</p>
      </div>

      <!-- 日志操作区域 - 添加按钮和筛选功能 -->
      <div class="log-actions" data-aos="fade-up">
        <!-- 新增日志按钮 -->
        <el-button type="primary" @click="navigateToAddLog" class="add-log-btn">
          <el-icon><Plus /></el-icon>
          <span>New Entry</span>
        </el-button>
        
        <!-- 筛选和搜索区域 -->
        <div class="filter-section">
          <!-- 搜索输入框 - 可按标题、作者或标签搜索 -->
          <el-input 
            v-model="searchQuery" 
            placeholder="Search by title, author or tags" 
            class="search-input"
            clearable>
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
          
          <!-- 过滤按钮和下拉面板 -->
          <el-dropdown trigger="click" @visible-change="handleFilterVisibleChange">
            <el-button type="primary" plain class="filter-btn">
              <el-icon><Filter /></el-icon>
              Filter
              <el-badge :value="activeFilterCount" :hidden="activeFilterCount === 0" class="filter-badge" />
            </el-button>
            
            <template #dropdown>
              <el-dropdown-menu class="filter-dropdown-menu">
                <div class="filter-dropdown-title">Filter Options</div>
                
                <!-- 标题筛选 -->
                <div class="filter-dropdown-section">
                  <div class="filter-dropdown-label">Book Title</div>
                  <el-select 
                    v-model="filterTitle" 
                    placeholder="Select Book Title" 
                    class="filter-dropdown-select"
                    filterable
                    clearable>
                    <el-option
                      v-for="title in uniqueTitles"
                      :key="title"
                      :label="title"
                      :value="title"
                    />
                  </el-select>
                </div>
                
                <!-- 作者筛选 -->
                <div class="filter-dropdown-section">
                  <div class="filter-dropdown-label">Author</div>
                  <el-select 
                    v-model="filterAuthor" 
                    placeholder="Select Author" 
                    class="filter-dropdown-select"
                    filterable
                    clearable>
                    <el-option
                      v-for="author in uniqueAuthors"
                      :key="author"
                      :label="author"
                      :value="author"
                    />
                  </el-select>
                </div>
                
                <!-- 状态筛选 -->
                <div class="filter-dropdown-section">
                  <div class="filter-dropdown-label">Reading Status</div>
                  <el-select 
                    v-model="filterStatus" 
                    placeholder="Select Status" 
                    class="filter-dropdown-select"
                    clearable>
                    <el-option label="Completed" value="completed" />
                    <el-option label="In Progress" value="in-progress" />
                    <el-option label="To Read" value="to-read" />
                  </el-select>
                </div>
                
                <!-- 阅读时长筛选 -->
                <div class="filter-dropdown-section">
                  <div class="filter-dropdown-label">Reading Time</div>
                  <el-select
                    v-model="filterDuration"
                    placeholder="Select Duration"
                    class="filter-dropdown-select"
                    clearable>
                    <el-option label="< 30 min" value="short" />
                    <el-option label="30-60 min" value="medium" />
                    <el-option label="1-2 hours" value="long" />
                    <el-option label="> 2 hours" value="very-long" />
                  </el-select>
                </div>
                
                <!-- 日期范围筛选 -->
                <div class="filter-dropdown-section">
                  <div class="filter-dropdown-label">Date Range</div>
                  <el-date-picker
                    v-model="dateRange"
                    type="daterange"
                    range-separator="to"
                    start-placeholder="Start Date"
                    end-placeholder="End Date"
                    class="filter-dropdown-date"
                  />
                </div>
                
                <!-- 按钮区域 -->
                <div class="filter-dropdown-actions">
                  <el-button type="default" @click="clearAllFilters">Clear All</el-button>
                  <el-button type="primary" @click="closeFilterDropdown">Apply</el-button>
                </div>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>

      <!-- 加载中状态显示 - 使用骨架屏 -->
      <div v-if="isLoading" class="loading-container" data-aos="fade">
        <el-skeleton :rows="3" animated />
        <el-skeleton :rows="3" animated />
      </div>

      <!-- 空状态显示 - 当没有日志时展示的界面 -->
      <div v-else-if="filteredLogs.length === 0" class="empty-state" data-aos="fade-up">
        <img src="../assets/empty-logs.svg" alt="No entries" class="empty-image" />
        <h3>No Reading Entries</h3>
        <p>Start tracking your reading journey by adding your first entry</p>
        <el-button type="primary" @click="navigateToAddLog">
          Add Your First Reading Entry
        </el-button>
      </div>

      <!-- 日志列表区域 - 显示所有筛选后的日志卡片 -->
      <div v-else class="log-list" data-aos="fade-up">
        <!-- 日志卡片 - 循环渲染每个日志项 -->
        <div 
          v-for="log in filteredLogs" 
          :key="log.id" 
          class="log-card"
          v-motion="{
            initial: { opacity: 0, y: 50 },
            enter: { opacity: 1, y: 0, 
              transition: { delay: log.index * 100 }
            }
          }"
        >
          <!-- 阅读状态标签 - 根据status显示不同颜色 -->
          <div class="log-status" :class="log.status">
            {{ statusDisplay(log.status) }}
          </div>
          
          <!-- 日志内容区域 -->
          <div class="log-content">
            <!-- 书籍标题和作者 -->
            <h3 class="log-title">{{ log.title }}</h3>
            <p class="log-author">by {{ log.author }}</p>
            
            <!-- 日志元数据 - 日期、阅读时长、心情评分 -->
            <div class="log-meta">
              <!-- 阅读日期 -->
              <div class="meta-item">
                <el-icon><Calendar /></el-icon>
                <span>{{ formatDate(log.date) }}</span>
              </div>
              <!-- 阅读时长 -->
              <div class="meta-item">
                <el-icon><Timer /></el-icon>
                <span>{{ log.duration }} minutes</span>
              </div>
              <!-- 心情评分 -->
              <div class="meta-item" v-if="log.mood">
                <el-icon><Star /></el-icon>
                <span>{{ log.mood }}/5</span>
              </div>
            </div>
            
            <!-- 标签展示区 - 显示所有相关标签 -->
            <div class="log-tags" v-if="log.tags && log.tags.length">
              <el-tag 
                v-for="tag in log.tags" 
                :key="tag" 
                size="small" 
                class="log-tag">
                {{ tag }}
              </el-tag>
            </div>
            
            <!-- 阅读笔记预览 - 显示截断的笔记内容 -->
            <div class="log-notes" v-if="log.notes">
              <p>{{ truncateNotes(log.notes) }}</p>
            </div>
          </div>
          
          <!-- 日志操作按钮区 - 编辑和删除 -->
          <div class="log-actions-panel">
            <!-- 编辑按钮 - 带有工具提示 -->
            <el-tooltip content="Edit" placement="top">
              <el-button 
                type="primary" 
                circle 
                @click="editLog(log.id)"
                class="action-btn">
                <el-icon><Edit /></el-icon>
              </el-button>
            </el-tooltip>
            
            <!-- 删除按钮 - 带有工具提示 -->
            <el-tooltip content="Delete" placement="top">
              <el-button 
                type="danger" 
                circle 
                @click="confirmDelete(log.id)"
                class="action-btn">
                <el-icon><Delete /></el-icon>
              </el-button>
            </el-tooltip>
          </div>
        </div>
      </div>
      
      <!-- 删除确认对话框 - 确认删除日志操作 -->
      <el-dialog
        v-model="deleteDialogVisible"
        title="Confirm Deletion"
        width="30%"
        center>
        <span>Are you sure you want to delete this reading entry? This action cannot be undone.</span>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="deleteDialogVisible = false">Cancel</el-button>
            <el-button type="danger" @click="deleteLog">
              Delete
            </el-button>
          </span>
        </template>
      </el-dialog>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import { Plus, Search, Calendar, Timer, Star, Edit, Delete, Filter, Collection } from '@element-plus/icons-vue'
import { useMotion } from '@vueuse/motion'
import { format } from 'date-fns'
import AOS from 'aos'
import 'aos/dist/aos.css'

// 初始化动画库
onMounted(() => {
  // AOS动画库初始化
  AOS.init({
    duration: 800,  // 动画持续时间（毫秒）
    once: true      // 动画只播放一次
  })
  loadLogs()        // 加载日志数据
})

// 路由和状态管理
const router = useRouter()         // 路由实例，用于页面导航
const isLoading = ref(true)        // 加载状态标志
const logs = ref([])               // 日志数据数组
const searchQuery = ref('')        // 搜索关键词
const filterStatus = ref('')       // 状态筛选值
const filterTitle = ref('')        // 标题筛选值
const filterAuthor = ref('')       // 作者筛选值
const filterDuration = ref('')     // 阅读时长筛选值
const dateRange = ref([])          // 日期范围筛选
const deleteDialogVisible = ref(false)  // 删除确认对话框可见性
const logToDeleteId = ref(null)    // 待删除的日志ID

// 过滤器UI相关状态
const filterDropdownVisible = ref(false)

// 计算活跃的过滤器数量
const activeFilterCount = computed(() => {
  let count = 0
  if (filterTitle.value) count++
  if (filterAuthor.value) count++
  if (filterStatus.value) count++
  if (filterDuration.value) count++
  if (dateRange.value && dateRange.value.length === 2) count++
  return count
})

// 处理过滤器下拉面板可见性变化
const handleFilterVisibleChange = (visible) => {
  filterDropdownVisible.value = visible
}

// 关闭过滤器下拉面板
const closeFilterDropdown = () => {
  filterDropdownVisible.value = false
}

// 清除所有过滤条件
const clearAllFilters = () => {
  filterTitle.value = ''
  filterAuthor.value = ''
  filterStatus.value = ''
  filterDuration.value = ''
  dateRange.value = []
}

// 模拟加载日志数据
const loadLogs = () => {
  // 模拟API请求延迟
  setTimeout(() => {
    // 模拟数据 - 实际应用中应从API获取
    logs.value = [
      {
        id: 1,
        title: 'To Kill a Mockingbird',
        author: 'Harper Lee',
        date: new Date('2023-06-15'),
        duration: 120,
        status: 'completed',
        mood: 5,
        tags: ['Fiction', 'Classic', 'American Literature'],
        notes: 'A powerful exploration of racial injustice and moral growth in the American South. The characters were deeply compelling, especially Atticus Finch, whose moral compass and integrity made a strong impression on me.'
      },
      {
        id: 2,
        title: '1984',
        author: 'George Orwell',
        date: new Date('2023-05-28'),
        duration: 90,
        status: 'completed',
        mood: 4,
        tags: ['Dystopian', 'Classic', 'Political'],
        notes: 'A chilling portrayal of totalitarianism and surveillance. The concept of "Big Brother" and "thoughtcrime" remain eerily relevant today.'
      },
      {
        id: 3,
        title: 'The Great Gatsby',
        author: 'F. Scott Fitzgerald',
        date: new Date('2023-07-03'),
        duration: 60,
        status: 'in-progress',
        mood: 4,
        tags: ['Fiction', 'Classic', 'American Dream'],
        notes: 'Currently reading this classic tale of wealth, ambition, and the American Dream. The prose is beautiful and the characters are fascinating in their complexity.'
      },
      {
        id: 4,
        title: 'Pride and Prejudice',
        author: 'Jane Austen',
        date: new Date('2023-07-10'),
        duration: 75,
        status: 'completed',
        mood: 5,
        tags: ['Classic', 'Romance', 'British Literature'],
        notes: 'A timeless exploration of social status, manners, and marriage in early 19th century England. Elizabeth Bennet is such a compelling character with her wit and intelligence.'
      }
    ]
    isLoading.value = false // 设置加载完成
  }, 1000) // 模拟1秒延迟
}

// 获取唯一的书籍标题列表用于筛选
const uniqueTitles = computed(() => {
  const titles = logs.value.map(log => log.title)
  return [...new Set(titles)].sort()
})

// 获取唯一的作者列表用于筛选
const uniqueAuthors = computed(() => {
  const authors = logs.value.map(log => log.author)
  return [...new Set(authors)].sort()
})

// 过滤和搜索功能计算属性
const filteredLogs = computed(() => {
  let result = [...logs.value] // 复制日志数组
  
  // 为动画添加索引
  result = result.map((log, index) => ({...log, index}))
  
  // 应用搜索关键词筛选
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase() // 转为小写进行不区分大小写的搜索
    result = result.filter(log => 
      log.title.toLowerCase().includes(query) || 
      log.author.toLowerCase().includes(query) ||
      (log.tags && log.tags.some(tag => tag.toLowerCase().includes(query)))
    )
  }
  
  // 应用标题筛选
  if (filterTitle.value) {
    result = result.filter(log => log.title === filterTitle.value)
  }
  
  // 应用作者筛选
  if (filterAuthor.value) {
    result = result.filter(log => log.author === filterAuthor.value)
  }
  
  // 应用状态筛选
  if (filterStatus.value) {
    result = result.filter(log => log.status === filterStatus.value)
  }
  
  // 应用阅读时长筛选
  if (filterDuration.value) {
    switch(filterDuration.value) {
      case 'short':
        result = result.filter(log => log.duration < 30)
        break
      case 'medium':
        result = result.filter(log => log.duration >= 30 && log.duration <= 60)
        break
      case 'long':
        result = result.filter(log => log.duration > 60 && log.duration <= 120)
        break
      case 'very-long':
        result = result.filter(log => log.duration > 120)
        break
    }
  }
  
  // 应用日期范围筛选
  if (dateRange.value && dateRange.value[0] && dateRange.value[1]) {
    const startDate = dateRange.value[0]
    const endDate = dateRange.value[1]
    result = result.filter(log => 
      log.date >= startDate && log.date <= endDate
    )
  }
  
  return result
})

// 状态显示文本映射函数
const statusDisplay = (status) => {
  const map = {
    'completed': 'Completed',
    'in-progress': 'In Progress',
    'to-read': 'To Read'
  }
  return map[status] || status
}

// 格式化日期显示
const formatDate = (date) => {
  if (!date) return ''
  return format(date, 'MMM dd, yyyy') // 格式化为"月 日, 年"
}

// 截断笔记内容预览
const truncateNotes = (notes, length = 100) => {
  if (!notes) return ''
  if (notes.length <= length) return notes
  return notes.substring(0, length) + '...' // 超过长度则截断并添加省略号
}

// 导航函数 - 跳转到添加日志页面
const navigateToAddLog = () => {
  router.push('/log/add')
}

// 导航函数 - 跳转到编辑日志页面
const editLog = (id) => {
  router.push(`/log/edit/${id}`)
}

// 删除相关功能
// 确认删除弹窗
const confirmDelete = (id) => {
  logToDeleteId.value = id
  deleteDialogVisible.value = true
}

// 执行删除操作
const deleteLog = () => {
  // 从日志数组中移除指定ID的日志
  logs.value = logs.value.filter(log => log.id !== logToDeleteId.value)
  // 关闭确认对话框
  deleteDialogVisible.value = false
  // 重置待删除ID
  logToDeleteId.value = null
}
</script>

<style scoped>
/* 阅读日志页面容器 */
.reading-log-page {
  max-width: 960px;
  margin: 0 auto;
  padding: 2rem 1rem;
}

/* 页面标题区域样式 */
.page-header {
  text-align: center;
  margin-bottom: 2rem;
}

/* 页面主标题 */
.page-title {
  font-size: 2.2rem;
  color: var(--el-color-primary);
  margin-bottom: 0.5rem;
}

/* 页面描述文字 */
.page-description {
  color: var(--el-text-color-secondary);
  font-size: 1.1rem;
}

/* 日志操作区域样式 - 包含添加按钮和筛选区 */
.log-actions {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
  gap: 1rem;
}

/* 筛选区域样式 */
.filter-section {
  display: flex;
  gap: 1rem;
  align-items: center;
}

/* 搜索输入框样式 */
.search-input {
  width: 300px;
}

/* 过滤按钮样式 */
.filter-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 16px;
  position: relative;
}

.filter-badge {
  margin-left: 4px;
}

/* 过滤下拉菜单样式 */
:deep(.filter-dropdown-menu) {
  width: 340px;
  padding: 16px;
}

.filter-dropdown-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-color-primary);
  margin-bottom: 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid var(--el-border-color-light);
}

.filter-dropdown-section {
  margin-bottom: 16px;
}

.filter-dropdown-label {
  font-size: 14px;
  margin-bottom: 8px;
  color: var(--el-text-color-primary);
}

.filter-dropdown-select {
  width: 100%;
}

.filter-dropdown-date {
  width: 100%;
}

.filter-dropdown-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 24px;
  padding-top: 16px;
  border-top: 1px solid var(--el-border-color-light);
  gap: 12px;
}

/* 原有状态筛选下拉框和日期选择器样式更新 */
.filter-select, .date-picker {
  display: none; /* 隐藏原有的独立过滤器 */
}

/* 添加日志按钮样式 */
.add-log-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

/* 空状态区域样式 */
.empty-state {
  text-align: center;
  padding: 3rem 0;
}

/* 空状态图片样式 */
.empty-image {
  width: 200px;
  max-width: 100%;
  margin-bottom: 1.5rem;
}

/* 日志列表区域样式 - 网格布局 */
.log-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 1.5rem;
}

/* 日志卡片样式 */
.log-card {
  position: relative;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  background-color: white;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

/* 日志卡片悬停效果 */
.log-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.1);
}

/* 阅读状态标签样式 */
.log-status {
  position: absolute;
  top: 0;
  right: 0;
  padding: 0.4rem 0.8rem;
  font-size: 0.8rem;
  color: white;
  z-index: 1;
}

/* 已完成状态标签样式 - 绿色 */
.log-status.completed {
  background-color: var(--el-color-success);
}

/* 进行中状态标签样式 - 黄色 */
.log-status.in-progress {
  background-color: var(--el-color-warning);
}

/* 待读状态标签样式 - 灰蓝色 */
.log-status.to-read {
  background-color: var(--el-color-info);
}

/* 日志内容区域样式 */
.log-content {
  padding: 1.5rem;
}

/* 日志标题样式 */
.log-title {
  font-size: 1.2rem;
  margin: 0 0 0.2rem 0;
  color: var(--el-text-color-primary);
}

/* 作者信息样式 */
.log-author {
  color: var(--el-text-color-secondary);
  margin: 0 0 1rem 0;
  font-style: italic;
}

/* 元数据信息区域样式 */
.log-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin-bottom: 1rem;
}

/* 单个元数据项样式 */
.meta-item {
  display: flex;
  align-items: center;
  gap: 0.4rem;
  color: var(--el-text-color-secondary);
  font-size: 0.9rem;
}

/* 标签区域样式 */
.log-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

/* 单个标签样式 */
.log-tag {
  border-radius: 12px;
}

/* 笔记内容预览区域样式 */
.log-notes {
  color: var(--el-text-color-secondary);
  font-size: 0.9rem;
  line-height: 1.5;
  border-top: 1px solid var(--el-border-color-light);
  padding-top: 1rem;
  margin-top: 1rem;
}

/* 操作按钮区域样式 */
.log-actions-panel {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
  padding: 0.75rem;
  background-color: var(--el-color-primary-light-9);
  border-top: 1px solid var(--el-border-color-light);
}

/* 操作按钮样式及动画效果 */
.action-btn {
  opacity: 0.8;
  transition: opacity 0.3s ease, transform 0.3s ease;
}

/* 按钮悬停效果 */
.action-btn:hover {
  opacity: 1;
  transform: scale(1.1);
}

/* 加载状态容器样式 */
.loading-container {
  padding: 2rem 0;
}

/* 响应式布局调整 */
@media (max-width: 768px) {
  /* 移动设备下操作区域垂直排列 */
  .log-actions {
    flex-direction: column;
    align-items: stretch;
  }
  
  /* 移动设备下筛选区域占满宽度 */
  .filter-section {
    width: 100%;
  }
  
  /* 移动设备下输入框和选择器占满宽度 */
  .search-input, .filter-select, .date-picker {
    width: 100%;
  }
  
  /* 移动设备下列表改为单列布局 */
  .log-list {
    grid-template-columns: 1fr;
  }
}
</style> 