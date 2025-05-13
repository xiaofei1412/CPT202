<template>
  <MainLayout>
    <div class="logs-page">
      <!-- 页面标题区域 - 淡入动画效果 -->
      <div class="page-header" data-aos="fade-down">
        <h1 class="page-title">Reading Log</h1>
        <p class="page-description">Record your reading journey</p>
      </div>
      
      <!-- 操作栏 - 包含新建按钮和筛选器 -->
      <div class="action-bar" data-aos="fade-right">
        <!-- 新建日志按钮 - 悬停放大效果 -->
        <el-button type="primary" @click="$router.push('/logs/create')" v-motion="{ hover: { scale: 1.05 } }">
          <font-awesome-icon icon="plus" class="button-icon" /> New Entry
        </el-button>
        
        <!-- 搜索和筛选区域 -->
        <div class="filters">
          <!-- 搜索输入框 -->
          <el-input
            v-model="searchQuery"
            placeholder="Search log..."
            class="search-input"
          >
            <template #prefix>
              <font-awesome-icon icon="search" />
            </template>
          </el-input>
          <!-- 搜索按钮 -->
          <el-button type="primary" plain class="search-btn" @click="searchLogs">
            <font-awesome-icon icon="search" />
            Search
          </el-button>
          
          <!-- 过滤按钮 -->
          <el-button type="primary" plain class="filter-btn" @click="filterDrawerVisible = true">
            <font-awesome-icon icon="filter" class="button-icon" />
            Filter
            <el-badge :value="activeFilterCount" :hidden="activeFilterCount === 0" class="filter-badge" />
          </el-button>
        </div>
      </div>
      
      <!-- 过滤器抽屉 -->
      <el-drawer
        v-model="filterDrawerVisible"
        title="Filter Options"
        direction="rtl"
        size="400px"
        :destroy-on-close="false"
      >
        <div class="filter-drawer-content">
          <!-- 书名筛选 -->
          <div class="filter-section">
            <div class="filter-label">Book Title</div>
            <el-input v-model="queryParams.title" placeholder="Please enter the book title"></el-input>
          </div>
          
          <!-- 作者筛选 -->
          <div class="filter-section">
            <div class="filter-label">Author</div>
            <el-input v-model="queryParams.author" placeholder="Please enter the author"></el-input>
          </div>
          
          <!-- 状态筛选 -->
          <div class="filter-section">
            <div class="filter-label">Status</div>
            <el-select 
              v-model="queryParams.status" 
              placeholder="Select Status" 
              class="filter-select"
              clearable>
              <el-option label="Approved" value="approved" />
              <el-option label="Pending" value="pending" />
              <el-option label="Rejected" value="rejected" />
            </el-select>
          </div>
          
          <!-- 阅读时长筛选 -->
          <div class="filter-section">
            <div class="filter-label">Reading Time</div>
            <el-select
              v-model="queryParams.readingTimeType"
              placeholder="Select Duration"
              class="filter-select"
              clearable>
              <el-option label="< 30 min" value="1" />
              <el-option label="30-60 min" value="2" />
              <el-option label="1-2 hours" value="3" />
              <el-option label="> 2 hours" value="4" />
            </el-select>
          </div>
          
          <!-- 日期范围筛选 -->
          <div class="filter-section">
            <div class="filter-label">Date Range</div>
            <el-date-picker
              v-model="queryParams.dateRange"
              type="daterange"
              range-separator="to"
              start-placeholder="Start Date"
              end-placeholder="End Date"
              class="filter-date"
              :editable="false"
            />
          </div>
          
          <!-- 排序选项 -->
          <div class="filter-section">
            <div class="filter-label">Sort By</div>
            <el-select 
              v-model="queryParams.sortType" 
              placeholder="Sort" 
              class="filter-select">
              <el-option label="Latest First" value="1" />
              <el-option label="Oldest First" value="2" />
              <el-option label="Longest Duration" value="3" />
            </el-select>
          </div>
          
          <!-- 按钮区域 -->
          <div class="filter-actions">
            <el-button type="default" @click="clearAllFilters">Clear All</el-button>
            <el-button type="primary" @click="applyFilters">Apply</el-button>
          </div>
        </div>
      </el-drawer>
      
      <!-- 日志列表区域 - 非加载状态时显示 -->
      <div class="logs-list" v-if="!loading">
        <el-card v-for="(log, index) in displayedLogs" 
          :key="log.id" 
          class="log-item"
          shadow="hover"
          data-aos="fade-up"
          :data-aos-delay="index * 50"
          v-motion="{ hover: { y: -5 } }">
          <!-- 日志内容区域 -->
          <div class="log-content">
            <!-- 日志主要信息 -->
            <div class="log-main">
              <h3 class="log-title">{{ log.title }}</h3>
              <div class="log-info">
                <!-- 作者信息 -->
                <span class="info-item">
                  <font-awesome-icon :icon="['far', 'user']" class="info-icon" />
                  {{ log.author }}
                </span>
                <!-- 日期信息 -->
                <span class="info-item">
                  <font-awesome-icon :icon="['far', 'calendar']" class="info-icon" />
                  {{ log.readingDate }}
                </span>
                <!-- 时长信息 -->
                <span class="info-item">
                  <font-awesome-icon :icon="['far', 'clock']" class="info-icon" />
                  {{ log.readingTime }} minutes
                </span>
              </div>
            </div>
            
            <!-- 日志状态标签 -->
            <div class="log-status">
              <el-tag :type="getStatusType(log.status)" size="small" effect="light">
                <!-- {{ getStatusText(log.status) }} -->
                  {{ log.status }}
              </el-tag>
            </div>
          </div>
          
          <!-- 日志操作按钮区域 -->
          <div class="log-actions">
            <!-- 查看按钮 - 悬停滑动效果 -->
            <el-button 
              size="small" 
              type="primary" 
              text
              @click="viewLog(log.readingLogId)"
              v-motion="{ hover: { x: 5 } }"
            >
              <font-awesome-icon icon="eye" class="action-icon" /> View
            </el-button>
            <!-- 编辑按钮 - 悬停滑动效果 -->
            <el-button 
              size="small" 
              type="primary" 
              text
              @click="editLog(log.readingLogId)"
              v-if="log.userId === userId"
              v-motion="{ hover: { x: 5 } }"
            >
              <font-awesome-icon icon="edit" class="action-icon" /> Edit
            </el-button>
            <!-- 删除按钮 - 悬停滑动效果 -->
            <el-button 
              size="small" 
              type="danger" 
              text
              @click="confirmDelete(log.readingLogId)"
              v-if="log.userId === userId"
              v-motion="{ hover: { x: 5 } }"
            >
              <font-awesome-icon icon="trash" class="action-icon" /> Delete
            </el-button>
          </div>
        </el-card>
        
        <!-- 空状态提示 - 当 filteredLogs 为空时显示 -->
        <el-empty 
          v-if="filteredLogs.length === 0"
          description="No reading log matching your filters"
          :image-size="200"
        >
          <el-button type="primary" @click="$router.push('/logs/create')">
            Record the first reading log
          </el-button>
        </el-empty>
      </div>
      
      <!-- 加载状态 - 骨架屏 -->
      <div v-else class="loading-container">
        <el-skeleton :rows="5" animated />
      </div>
      
      <!-- 分页控件 - 当 totalItems > 0 时显示 -->
      <div class="pagination" v-if="!loading && totalItems > 0" data-aos="fade-up">
        <el-pagination
          v-model:current-page="currentPage"
          :page-size="pageSize"
          :total="totalItems"
          layout="prev, pager, next, jumper, ->, total"
          background
          @current-change="handlePageChange"
        />
      </div>
    </div>
    
    <!-- 删除确认对话框 -->
    <el-dialog
      v-model="deleteDialog.visible"
      title="Confirm Delete"
      width="30%"
      :close-on-click-modal="false"
    >
      <span>Are you sure you want to delete this reading log? This operation cannot be undone.</span>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="deleteDialog.visible = false">Cancel</el-button>
          <el-button type="danger" @click="deleteLogs" :loading="deleteDialog.loading">
            Confirm Delete
          </el-button>
        </span>
      </template>
    </el-dialog>
  </MainLayout>
</template>

<script setup>
import { ref, computed, reactive, onMounted, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useLogStore } from '../store/log'
import MainLayout from '../layouts/MainLayout.vue'
import { Plus, Search, Filter, User, Calendar, Timer } from '@element-plus/icons-vue'
import gsap from 'gsap'
import AOS from 'aos'
import 'aos/dist/aos.css'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { getLogList, deleteLog } from '../api/home'
const searchLogs = () => {
  console.log(searchQuery.value);
  init()
}

// 路由实例 - 用于页面导航
const router = useRouter()
// 日志状态管理 - 从pinia获取
const logStore = useLogStore()

// 组件状态数据
const loading = ref(true)           // 页面加载状态
const tableLoading = ref(false)     // 表格加载状态
const searchQuery = ref('')         // 搜索关键词
const filterStatus = ref('')        // 状态筛选值
const filterTitle = ref('')         // 标题筛选值
const filterAuthor = ref('')        // 作者筛选值
const filterDuration = ref('')      // 阅读时长筛选值
const sortBy = ref('latest')        // 排序方式
const dateRange = ref([])           // 日期范围筛选
const currentPage = ref(1)          // 当前页码
const pageSize = ref(10)            // 每页显示数量
const filterDrawerVisible = ref(false) // 过滤器抽屉可见性

// 删除对话框状态
const deleteDialog = reactive({
  visible: false,                   // 对话框可见性
  loading: false,                   // 删除操作加载状态
  logId: null                       // 待删除的日志ID
})

const deleteLogs = () => {
  deleteLog(deleteDialog.logId).then(res=>{
    ElMessage({
      type: 'success',
      message: 'Log deleted successfully'
    })
    deleteDialog.visible = false
    deleteDialog.logId = null
    init()
  })
}
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

// 处理过滤器抽屉可见性变化
const handleFilterVisible = (visible) => {
  filterDrawerVisible.value = visible
}

// 关闭过滤器抽屉
const closeFilterDrawer = () => {
  // filterDrawerVisible.value = false
  console.log(queryParams.value);
}

// 清除所有过滤条件
const clearAllFilters = () => {
  // 清空所有筛选条件
  queryParams.value = {
    pageNum: currentPage.value,
    pageSize: pageSize.value,
    userId: userId
  }
  // 重新调用init函数
  init()
  // 关闭筛选抽屉
  filterDrawerVisible.value = false
}

// 获取唯一的书籍标题列表用于筛选
const uniqueTitles = computed(() => {
  const titles = logStore.logs.map(log => log.title)
  return [...new Set(titles)].sort()
})

// 获取唯一的作者列表用于筛选
const uniqueAuthors = computed(() => {
  const authors = logStore.logs.map(log => log.author)
  return [...new Set(authors)].sort()
})

// 计算属性 - 经过筛选的日志列表
const filteredLogs = computed(() => {
  let result = [...logStore.logs]
  
  // 根据标题或作者搜索
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    result = result.filter(log => 
      log.title.toLowerCase().includes(query) || 
      log.author.toLowerCase().includes(query)
    )
  }
  
  // 标题筛选
  if (filterTitle.value) {
    result = result.filter(log => log.title === filterTitle.value)
  }
  
  // 作者筛选
  if (filterAuthor.value) {
    result = result.filter(log => log.author === filterAuthor.value)
  }
  
  // 状态筛选
  if (filterStatus.value) {
    result = result.filter(log => log.status === filterStatus.value)
  }
  
  // 阅读时长筛选
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
  
  // 日期范围筛选
  if (dateRange.value && dateRange.value.length === 2) {
    const [start, end] = dateRange.value
    result = result.filter(log => {
      const logDate = new Date(log.date)
      return logDate >= start && logDate <= end
    })
  }
  
  // 应用排序
  switch(sortBy.value) {
    case 'latest':
      result.sort((a, b) => new Date(b.date) - new Date(a.date))
      break
    case 'oldest':
      result.sort((a, b) => new Date(a.date) - new Date(b.date))
      break
    case 'duration':
      result.sort((a, b) => b.duration - a.duration)
      break
  }
  
  return result
})

// 计算属性 - 当前页要显示的日志
// const displayedLogs = computed(() => {
//   const start = (currentPage.value - 1) * pageSize.value
//   const end = start + pageSize.value
//   return filteredLogs.value.slice(start, end)
// })
const displayedLogs = ref([])
const userId = JSON.parse(localStorage.getItem('user'))?.userId||''
const queryParams = ref({
  pageNum: currentPage.value,
  pageSize: pageSize.value,
  userId: userId
})
const init = () => {
  queryParams.value.title = searchQuery.value
  getLogList(queryParams.value).then(res=>{
    displayedLogs.value = res.data.records
    totalItems.value = res.data.total
  })
}
init()
const applyFilters = () => {
  if (queryParams.value.dateRange) {
    queryParams.value.startTime = formatDate(queryParams.value.dateRange[0])
    queryParams.value.endTime = formatDate(queryParams.value.dateRange[1], true)
  }
  init()
  console.log(queryParams.value);
  // 关闭
  filterDrawerVisible.value=false

}
const formatDate = (date, isEnd = false) => {
  const month = (date.getMonth() + 1).toString().padStart(2, '0')
  const day = date.getDate().toString().padStart(2, '0')
  const time = isEnd ? '23:59:59' : '00:00:00'
  return `${date.getFullYear()}-${month}-${day} ${time}`
}

// 计算属性 - 总日志数 (基于筛选结果)
const totalItems = ref(0)

// Watch for filter changes, reset to first page
watch([searchQuery, filterTitle, filterAuthor, filterStatus, filterDuration, dateRange, sortBy], () => {
  currentPage.value = 1
})

// Get status display type
const getStatusType = (status) => {
  const map = {
    approved: 'success',
    pending: 'warning',
    rejected: 'danger'
  }
  return map[status] || 'info'
}

// Get status display text
// 获取状态显示文本，将状态代码转换为用户友好的文本
const getStatusText = (status) => {
  const map = {
    approved: 'Approved', // 已批准
    pending: 'Pending',   // 待审核
    rejected: 'Rejected'  // 已拒绝
  }
  return map[status] || 'Unknown' // 如果状态不在映射中，返回"Unknown"
}

// Row click handler
// 行点击处理函数，用户点击整行时触发
const handleRowClick = (row) => {
  viewLog(row.id) // 调用查看日志函数并传递日志ID
}

// View log
// 查看日志详情，通过路由导航到日志详情页面
const viewLog = (id) => {
  router.push(`/logs/${id}`) // 使用动态路由参数导航到特定日志
}

// Edit log
// 编辑日志，通过路由导航到日志编辑页面
const editLog = (id) => {
  router.push(`/logs/edit1/${id}`) // 使用动态路由参数导航到特定日志的编辑页面
}

// Confirm delete
// 确认删除操作，显示确认对话框
const confirmDelete = (id) => {
  deleteDialog.logId = id // 设置要删除的日志ID
  deleteDialog.visible = true // 显示确认对话框
}

// Delete log
// 删除日志的异步函数，与后端API交互
// const deleteLog = async () => {
//   if (!deleteDialog.logId) return // 如果没有日志ID，直接返回
  
//   try {
//     deleteDialog.loading = true // 设置加载状态
//     await logStore.deleteLog(deleteDialog.logId) // 调用store中的删除方法
    
//     ElMessage({
//       type: 'success',
//       message: 'Log deleted successfully' // 显示成功消息提示
//     })
//   } catch (error) {
//     ElMessage.error('Delete failed, please try again later') // 显示错误消息提示
//     console.error(error) // 记录错误到控制台以便调试
//   } finally {
//     deleteDialog.loading = false // 无论成功或失败，重置加载状态
//     deleteDialog.visible = false // 隐藏对话框
//     deleteDialog.logId = null // 清除日志ID
//   }
// }

// Pagination handlers
// 页面改变处理
const handlePageChange = (page) => {
  currentPage.value = page
  queryParams.value.pageNum = page
  init()

  // Optional: Scroll to top of the list when page changes
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 分页处理函数 - 改变每页显示数量
const handleSizeChange = (size) => {
  pageSize.value = size // 更新每页显示数量
}

// Initialize
// 组件挂载时初始化数据
onMounted(async () => {
  try {
    const result = await logStore.fetchLogs() // 从store获取日志数据
    // totalLogs.value = result.total // 设置总日志数量 (改为 computed)
    
    // Override with English titles and authors if Chinese ones are detected
    // 如果检测到中文标题和作者，使用英文替代（用于国际化展示）
    const englishTitles = [
      "To Kill a Mockingbird",
      "1984",
      "Pride and Prejudice",
      "The Great Gatsby",
      "Lord of the Flies",
      "Brave New World",
      "The Catcher in the Rye",
      "Fahrenheit 451"
    ];
    
    const englishAuthors = [
      "Harper Lee",
      "George Orwell",
      "Jane Austen",
      "F. Scott Fitzgerald",
      "William Golding",
      "Aldous Huxley",
      "J.D. Salinger",
      "Ray Bradbury"
    ];
    
    // Replace Chinese titles and authors with English ones
    // 将中文标题和作者替换为英文（循环使用预定义的英文标题和作者）
    logStore.logs.forEach((log, index) => {
      if (log.title.includes('阅读日志')) {
        log.title = englishTitles[index % englishTitles.length]; // 使用模运算确保不越界
        log.author = englishAuthors[index % englishAuthors.length];
      }
    });
  } finally {
    loading.value = false // 无论成功或失败，完成加载
  }
})

// Initialize AOS
// 初始化AOS动画库，设置滚动动画效果
onMounted(() => {
  AOS.init({
    duration: 800, // 动画持续时间（毫秒）
    once: true     // 动画只执行一次，不重复
  })
})

// GSAP animations
// 使用GSAP库初始化元素动画效果
onMounted(() => {
  // List item animations
  // 列表项动画：从下往上淡入，错开时间以创建连续动画效果
  gsap.from('.log-item', {
    opacity: 0,     // 从透明开始
    y: 20,          // 从下方20px的位置开始
    duration: 0.5,  // 动画持续0.5秒
    stagger: 0.1,   // 每个项目之间延迟0.1秒
    ease: 'power2.out' // 使用power2.out缓动函数，创建平滑效果
  })
})
</script>

<style scoped>
/* 直接修改Element Plus的下拉菜单样式 */
/* 使用 :deep 和 !important 确保样式优先级 */
:deep(.el-dropdown-menu) {
  width: 400px !important;
  padding: 20px !important;
  border: 1px solid #DCDFE6 !important;
  border-radius: 8px !important;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1) !important;
}

:deep(.el-dropdown-menu__item) {
  padding: 0 !important;
  height: auto !important;
  line-height: normal !important;
}

:deep(.el-dropdown-menu__item:hover) {
  background-color: transparent !important;
}

:deep(.el-select) {
  width: 100% !important;
}

:deep(.el-date-editor--daterange) {
  width: 100% !important;
}

/* 自定义下拉框标题和分区样式 */
.filter-dropdown-title {
  font-size: 18px !important;
  font-weight: 600 !important;
  color: var(--el-color-primary) !important;
  margin-bottom: 20px !important;
  padding-bottom: 10px !important;
  border-bottom: 1px solid #EBEEF5 !important;
}

.filter-dropdown-section {
  margin-bottom: 20px !important;
  width: 100% !important;
}

.filter-dropdown-label {
  font-size: 14px !important;
  margin-bottom: 8px !important;
  color: #303133 !important;
  font-weight: 500 !important;
}

.filter-dropdown-actions {
  display: flex !important;
  justify-content: flex-end !important;
  margin-top: 24px !important;
  padding-top: 16px !important;
  border-top: 1px solid #EBEEF5 !important;
  gap: 12px !important;
}

.filter-dropdown-actions .el-button {
  padding: 8px 20px !important;
}

/* 按钮样式 */
.filter-btn {
  display: flex !important;
  align-items: center !important;
  gap: 6px !important;
  padding: 8px 16px !important;
  position: relative !important;
}

.filter-btn:hover {
  background-color: var(--el-color-primary) !important;
  color: white !important;
}

/* Responsive adjustments for filter dropdown */
@media (max-width: 576px) {
  :deep(.el-dropdown-menu) {
    width: 320px !important;
    padding: 16px !important;
  }
  
  .filter-dropdown-title {
    font-size: 16px !important;
  }
  
  .filter-dropdown-label {
    font-size: 13px !important;
  }
}

/* Button icon styles */
.button-icon {
  margin-right: 5px;
}

/* Info icon styles */
.info-icon {
  margin-right: 5px;
  width: 14px;
}

/* Action icon styles */
.action-icon {
  margin-right: 3px;
  font-size: 14px;
}

.logs-page {
  padding: 24px;
  max-width: 1200px;
  margin: 0 auto;
}

.page-header {
  text-align: center;
  margin-bottom: 32px;
}

.page-title {
  font-size: 28px;
  font-weight: 600;
  background: linear-gradient(120deg, var(--el-color-primary), var(--el-color-primary-light-3));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin-bottom: 8px;
}

.page-description {
  color: var(--el-text-color-secondary);
  font-size: 16px;
}

.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 15px;
}

.left-tools, .right-tools {
  display: flex;
  align-items: center;
  gap: 10px;
}

.search-input {
  width: 200px;
}

.filter-select {
  width: 120px;
}

.date-picker {
  width: 320px;
}

.logs-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin: 24px 0;
}

.loading-container {
  padding: 20px 0;
}

.empty-logs {
  padding: 40px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.empty-logs .el-button {
  margin-top: 20px;
}

.log-item {
  border: none;
  background: linear-gradient(to right, #ffffff, #f8f9fa);
  transition: all 0.3s ease;
}

.log-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.1);
  background: linear-gradient(to right, #ffffff, #f0f2f5);
}

.log-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.log-main {
  flex: 1;
}

.log-title {
  font-size: 18px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin: 0 0 12px 0;
}

.log-info {
  display: flex;
  gap: 16px;
  color: var(--el-text-color-secondary);
  font-size: 14px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 4px;
}

.info-item .el-icon {
  font-size: 16px;
}

.log-status {
  padding-left: 20px;
}

.log-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  border-top: 1px solid var(--el-border-color-lighter);
  margin-top: 16px;
  padding-top: 16px;
}

.action-bar {
  background-color: var(--el-bg-color);
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}

.filters {
  display: flex;
  gap: 12px;
  margin-top: 16px;
}

.search-input {
  flex: 1;
  max-width: 300px;
}

.pagination {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .log-content {
    flex-direction: column;
  }
  
  .log-status {
    padding-left: 0;
    margin-top: 12px;
  }
  
  .log-info {
    flex-direction: column;
    gap: 8px;
  }
  
  .filters {
    flex-direction: column;
  }
  
  .search-input,
  .filter-select {
    width: 100%;
    max-width: none;
  }
}

/* 过滤器抽屉样式 */
.filter-drawer-content {
  padding: 20px;
}

.filter-section {
  margin-bottom: 24px;
}

.filter-label {
  font-size: 14px;
  color: var(--el-text-color-regular);
  margin-bottom: 8px;
  font-weight: 500;
}

.filter-select {
  width: 100%;
}

.filter-date {
  width: 100%;
}

.filter-actions {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 16px;
  background-color: var(--el-bg-color);
  border-top: 1px solid var(--el-border-color-lighter);
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}
</style> 