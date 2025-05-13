<template>
  <MainLayout>
    <div class="dashboard">
      <!-- 页面标题和欢迎信息 -->
      <div class="dashboard-header">
        <h2 class="page-title title-water-effect" data-aos="fade-right">
          <el-icon><DataLine /></el-icon> Reading Dashboard
        </h2>
        <p class="page-desc" data-aos="fade-right" data-aos-delay="100">
          Track your reading statistics and progress
        </p>
      </div>
      
      <!-- 用户信息卡片 - 显示头像和基本信息 -->
      <el-card class="user-info-card" shadow="hover" ref="userInfoCard" v-motion>
        <div class="user-info">
          <el-avatar :size="80" :src="userInfo?.avatarPath"></el-avatar>
          <div class="user-details">
            <h3 class="user-name">{{ userInfo?.email }}</h3>
            <p class="user-since">Join Time: {{ userInfo?.createdAt }}</p>
          </div>
        </div>
      </el-card>

      <!-- 阅读概览区域 - 显示成就、趋势和分类 -->
      <div class="reading-overview" data-aos="fade-up">
        <div class="overview-header">
          <h2 class="section-title">Reading Overview</h2>
          <div class="time-selector">
            <el-radio-group v-model="timeRange" size="small">
              <el-radio-button value="week">This Week</el-radio-button>
              <el-radio-button value="month">This Month</el-radio-button>
              <el-radio-button value="year">This Year</el-radio-button>
            </el-radio-group>
          </div>
        </div>

        <!-- 概览卡片容器 (将包含所有4个卡片) -->
        <div class="overview-cards">
          <!-- 阅读成就卡片 - Removed -->
          <!-- 
          <div class="overview-card" data-aos="zoom-in" data-aos-delay="100">
            ...
          </div>
          -->

          <!-- 阅读趋势卡片 - 显示每周阅读时间柱状图 -->
          <div class="overview-card" data-aos="zoom-in" data-aos-delay="100">
            <el-icon class="card-icon"><TrendCharts /></el-icon>
            <div class="card-content">
              <h3 class="card-title">Reading Trend</h3>
              <!-- 趋势图 - 根据时间范围显示不同格式 -->
              <div class="trend-chart">
                <div v-for="(value, index) in trendValues" 
                     :key="index" 
                     class="chart-bar"
                     :style="{ height: value + '%' }"
                     data-aos="height"
                     :data-aos-delay="index * 100">
                </div>
              </div>
              <!-- 标签 - 根据时间范围显示不同格式 -->
              <div class="chart-labels">
                <span v-for="label in trendLabels" 
                      :key="label">{{ label }}</span>
              </div>
            </div>
          </div>

          <!-- 阅读分类卡片 - 显示不同类型书籍的阅读比例 -->
          <div class="overview-card" data-aos="zoom-in" data-aos-delay="200">
            <el-icon class="card-icon"><Reading /></el-icon>
            <div class="card-content">
              <h3 class="card-title">Reading Category</h3>
              <div class="category-list">
                <div class="category-item" v-motion="{ hover: { x: 5 } }" v-for="(item,index) in categoryData" :key="index">
                  <span class="category-name">{{item.name}}</span>
                  <el-progress :percentage="item.value" :show-text="false" />
                </div>
              </div>
            </div>
          </div>
          
          <!-- 阅读时间卡片 - (Moved from stats-row, use zoom-in) -->
          <el-card class="stat-card reading-time unified-card" shadow="hover" data-aos="zoom-in" data-aos-delay="200">
            <template #header>
              <div class="card-header">
                <h3 class="card-title">
                  <el-icon><Clock /></el-icon> Reading Time
                </h3>
              </div>
            </template>
            <div class="stat-content">
              <div class="stat-value">{{formatTime(readingTimeDisplay) }} </div>
              <el-progress
                :percentage="Math.min(100, (readingTimeDisplay / readingTimeGoal) * 100)"
                :format="() => ''"
                :stroke-width="12"
                :show-text="false"
              ></el-progress>
              <div class="stat-desc">
                <!-- {{ getTimeRangeText('time') }} Goal: {{ formatTime(readingTimeGoal) }} -->
                You can do it! 
              </div>
            </div>
          </el-card>
          
          <!-- 连续阅读卡片 - (Moved from stats-row, use zoom-in) -->
          <el-card class="stat-card streak unified-card" shadow="hover" data-aos="zoom-in" data-aos-delay="300">
            <template #header>
              <div class="card-header">
                <h3 class="card-title">
                  <el-icon><Calendar /></el-icon> Continuous Reading
                </h3>
              </div>
            </template>
            <div class="stat-content">
              <!-- streak-value -->
              <div class="stat-value ">{{ streak }} Days</div>
              <!-- 本周阅读打卡可视化 -->
              <div class="streak-days">
                <!-- <div v-for="(day, index) in 7" :key="index" 
                     :class="['streak-day', { active: index < streak % 7 }]"
                     data-aos="zoom-in"
                     :data-aos-delay="index * 50">
                  {{ ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'][index] }}
                </div> -->
                <!--只显示今天是星期几 -->
                <div class="streak-day active">{{ ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'][new Date().getDay()-1] }}</div>
              </div>
              <div class="stat-desc">
                Keep it up!
              </div>
            </div>
          </el-card>
        </div>
      </div>
      
      <!-- 阅读统计卡片区域 - (Container now empty or can be removed) -->
      <div class="stats-container">
        <div class="stats-row">
          <!-- 阅读日志数量卡片 - Removed -->
          <!-- 
          <el-card class="stat-card total-logs" shadow="hover" data-aos="fade-up">
            ...
          </el-card>
          -->
        </div>
      </div>
      
      <!-- 最近阅读日志卡片 - 显示用户最近的阅读记录 -->
      <el-card class="recent-logs-card" shadow="hover" data-aos="fade-up">
        <template #header>
          <div class="card-header">
            <h3 class="card-title">Recent Reading Log</h3>
            <!-- 查看全部链接 -->
            <router-link to="/logs" class="view-all">
              View All
              <el-icon><ArrowRight /></el-icon>
            </router-link>
          </div>
        </template>
        
        <!-- 加载状态 -->
        <div v-if="loading" class="loading-container">
          <el-skeleton :rows="3" animated />
        </div>
        
        <!-- 空状态 - 没有阅读日志时显示 -->
        <div v-else-if="tableData.length === 0" class="empty-logs">
          <el-empty description="No Reading Log" />
          <el-button type="primary" @click="$router.push('/logs/create')">
            Create Your First Reading Log
          </el-button>
        </div>
        
        <!-- 日志列表 - 表格形式展示最近的阅读记录 -->
        <div v-else class="logs-list">
          <el-table :data="tableData" style="width: 100%" :border="false">
            <el-table-column prop="title" label="Title">
              <template #default="{ row }">
                <router-link :to="`/logs/${row.id}`" class="log-link">
                  {{ row.title }}
                </router-link>
              </template>
            </el-table-column>
            <el-table-column prop="author" label="Author" width="150" />
            <el-table-column prop="readingDate" label="Reading Date" width="120" />
            <el-table-column prop="readingTime" label="Duration" width="100">
              <template #default="{ row }">
                {{ row.readingTime }} Minutes
              </template>
            </el-table-column>
            <el-table-column label="Status" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)" size="small">
                  {{ getStatusText(row.status) }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
          
          <div class="add-log">
            <el-button type="primary" @click="$router.push('/logs/create')">
              <el-icon><Plus /></el-icon> Add New Reading Log
            </el-button>
          </div>
        </div>
      </el-card>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, computed, onMounted, nextTick, reactive, watch } from 'vue'
import { useUserStore } from '../store/user'
import { useLogStore } from '../store/log'
import MainLayout from '../layouts/MainLayout.vue'
import { Calendar, Clock, Notebook, ArrowRight, Plus, Trophy, Medal, Star, TrendCharts, Reading, DataLine } from '@element-plus/icons-vue'
import { useMotion } from '@vueuse/motion'
import gsap from 'gsap'
import AOS from 'aos'
import 'aos/dist/aos.css'
import { getDashboardData } from '../api/home'

const userStore = useUserStore()
const logStore = useLogStore()
const userInfo = ref()
userInfo.value=JSON.parse(localStorage.getItem('user'))

// Status
const loading = ref(true)
const streak = ref(5)
const readingTimeGoal = ref(15)
const timeRange = ref('week')

// Add computed property for dynamic achievement goal text
const achievementGoalText = computed(() => {
  switch(timeRange.value) {
    case 'week':
      return 'Complete Weekly Goal';
    case 'month':
      return 'Complete Monthly Goal';
    case 'year':
      return 'Complete Yearly Goal';
    default:
      return 'Complete Goal'; // Fallback
  }
});

// 根据时间范围计算的值
const trendValues = ref([])

const trendLabels = computed(() => {
  switch(timeRange.value) {
    case 'week':
      return ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
    case 'month':
      return ['Week 1', 'Week 2', 'Week 3', 'Week 4'];
    case 'year':
      return Array.from({ length: 12 }, (_, i) => i + 1);
    default:
      return ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
  }
})

// 获取时间范围文本
const getTimeRangeText = (type) => {
  switch(timeRange.value) {
    case 'week':
      return 'Weekly';
    case 'month':
      return 'Monthly';
    case 'year':
      return 'Yearly';
    default:
      return 'Monthly';
  }
}

// 动画引用
const userInfoCard = ref(null)
const { motion: userInfoMotion } = useMotion(userInfoCard, {
  initial: { opacity: 0, y: 20 },
  visible: { opacity: 1, y: 0, transition: { duration: 600 } }
})

// 初始化函数
const initializeAnimations = () => {
  console.log('Initializing animations...')
  
  // 确保AOS刷新，识别新元素
  setTimeout(() => {
    AOS.refresh()
  }, 100)

  // GSAP 动画 for progress bars
  gsap.from('.el-progress-bar__inner', {
    scaleX: 0,
    duration: 1,
    ease: 'power2.out',
    delay: 0.6
  })

  // 数值增长动画 - 使用nextTick确保DOM已更新
  nextTick(() => {
    const elements = {
      readingTime: document.querySelector('.reading-time .stat-value'),
      streak: document.querySelector('.streak-value')
    }

    if (elements.readingTime) {
      const finalTime = logStore.readingTime
      gsap.from(elements.readingTime, {
        innerText: 0,
        duration: 1.5,
        snap: { innerText: 1 },
        ease: 'power1.out',
        delay: 0.8,
        onUpdate: function() {
          elements.readingTime.textContent = formatTime(Math.floor(this.targets()[0].innerText))
        }
      })
    }

    if (elements.streak) {
      gsap.from(elements.streak, {
        innerText: 0,
        duration: 1.5,
        snap: { innerText: 1 },
        ease: 'power1.out',
        delay: 0.8
      })
    }
  })
}

// 初始化
onMounted(async () => {
  console.log('Dashboard component mounted')
  
  // 初始化AOS
  AOS.init({
    duration: 600,
    once: false,
    offset: 50,
    disable: 'mobile'
  })
  
  try {
    await logStore.fetchLogs()
    
    // Override mock data with English titles and authors if "阅读日志" titles are found
    if (logStore.logs.some(log => log.title.includes('阅读日志'))) {
      const englishTitles = [
        "To Kill a Mockingbird", 
        "1984", 
        "Pride and Prejudice",
        "The Great Gatsby"
      ];
      
      const englishAuthors = [
        "Harper Lee",
        "George Orwell",
        "Jane Austen",
        "F. Scott Fitzgerald"
      ];
      
      // Replace Chinese titles and authors with English ones
      logStore.logs = logStore.logs.map((log, index) => ({
        ...log,
        title: englishTitles[index % englishTitles.length],
        author: englishAuthors[index % englishAuthors.length]
      }));
    }
  } finally {
    loading.value = false
    
    // 使用多层nextTick和setTimeout确保DOM完全渲染后再初始化动画
    nextTick(() => {
      setTimeout(() => {
        // initializeAnimations()
        
        // 额外的AOS刷新，确保所有元素都被识别
        setTimeout(() => {
          // AOS.refresh()
        }, 200)
      }, 100)
    })
  }
})

// Computed properties
const joinDate = computed(() => {
  return new Date().toLocaleDateString('en-US', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
})

// Format time (minutes to hours and minutes)
const formatTime = (minutes) => {
  const hours = Math.floor(minutes / 60)
  const mins = minutes % 60
  
  if (hours > 0) {
    return `${hours} Hours${mins > 0 ? mins + ' Minutes' : ''}`
  }
  
  return `${mins} Minutes`
}

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
const getStatusText = (status) => {
  const map = {
    approved: 'Approved',
    pending: 'Reviewing',
    rejected: 'Rejected'
  }
  return map[status] || 'Unknown'
}

// 监听时间范围变化，更新数据
watch(timeRange, (newRange) => {
  console.log(newRange)
  setTimeout(() => {
    let type=1
    if(newRange=='week') type=1
    if(newRange=='month') type=2
    if(newRange=='year') type=3
    getDashboardData({
      userId: userInfo.value.userId,
      type:type
    }).then(res => {
      console.log(res.data)
    updateDashboardData(newRange,res.data)
        // initializeAnimations()

    })
  }, 100);
}, { immediate: true })
const categoryData = ref([])
const tableData = ref([])
const readingTimeDisplay = ref(0)

// 根据选定的时间范围更新仪表盘数据
const updateDashboardData = (range,data) => {
  // 在实际应用中，这里应该调用API获取不同时间范围的数据
  // 这里仅用模拟数据演示

  // 更新阅读时间
  categoryData.value = []
  for (const [key,value] of Object.entries(data.typeMap)) {
      categoryData.value.push({
        name:key,
        value:value
      })
    }
  if (range === 'week') {
    
    tableData.value = data.list
    streak.value = data.days
    readingTimeGoal.value = 15
    readingTimeDisplay.value = data.sumTime
    trendValues.value = [data.readDays.Mon, data.readDays.Tue, data.readDays.Wed, data.readDays.Thu, data.readDays.Fri, data.readDays.Sat, data.readDays.Sun]
  } else if (range === 'month') {
    streak.value = data.days
    readingTimeDisplay.value = data.sumTime
    readingTimeGoal.value = 60
    tableData.value = data.list
    trendValues.value = [data.readDays.Week1, data.readDays.Week2, data.readDays.Week3, data.readDays.Week4]
  } else if (range === 'year') {
    streak.value = data.days
    readingTimeDisplay.value = data.sumTime
    readingTimeGoal.value = 600
    trendValues.value = [data.readDays[1], data.readDays[2], data.readDays[3], data.readDays[4], data.readDays[5], data.readDays[6], data.readDays[7], data.readDays[8], data.readDays[9], data.readDays[10], data.readDays[11], data.readDays[12]]
  }
  
  // 计算目标完成百分比
  const readingGoalPercentage = Math.round((readingTimeDisplay.value / readingTimeGoal.value) * 100)
}

// 获取时间范围对应的标签
const getTrendLabels = computed(() => {
  if (timeRange.value === 'week') {
    return ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
  } else if (timeRange.value === 'month') {
    return ['Week 1', 'Week 2', 'Week 3', 'Week 4']
  } else {
    return ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
  }
})

// 获取时间范围显示文本
const timeRangeText = computed(() => {
  if (timeRange.value === 'week') return 'This Week'
  if (timeRange.value === 'month') return 'This Month'
  return 'This Year'
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.dashboard-header {
  margin-bottom: 30px;
}

/* 标题水波纹效果 (保留) */
/* ... title-water-effect styles ... */

.page-title {
  font-size: 28px;
  font-weight: 600;
  color: var(--text-primary);
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.page-desc {
  color: var(--text-secondary);
}

.user-info-card {
  margin-bottom: 30px;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-details {
  margin-left: 20px;
}

.user-name {
  font-size: 20px;
  font-weight: 500;
  color: var(--text-primary);
  margin-bottom: 4px;
}

.user-since {
  font-size: 14px;
  color: var(--text-secondary);
}

.reading-overview {
  margin-bottom: 30px; /* 调整间距 */
}

.overview-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-title {
  font-size: 22px;
  font-weight: 600;
  color: var(--text-primary);
}

/* 调整时间选择器样式 (可选) */
.time-selector .el-radio-button__inner {
  padding: 8px 15px;
}

/* 概览卡片容器 - 改为 2x2 网格布局 */
.overview-cards {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px; /* 设置统一间距 */
}

/* 阅读成就/趋势/分类卡片样式 */
.overview-card {
  background-color: var(--card-bg);
  padding: 25px;
  border-radius: 8px;
  box-shadow: var(--card-shadow);
  display: flex;
  align-items: flex-start; /* 顶部对齐 */
  transition: all 0.3s ease;
}

.overview-card:hover, 
.unified-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--card-shadow-hover);
}

.card-icon {
  font-size: 28px;
  color: var(--primary-color);
  margin-right: 15px;
  background-color: var(--primary-light);
  padding: 10px;
  border-radius: 50%;
  flex-shrink: 0; /* 防止图标被压缩 */
}

.card-content {
  flex-grow: 1;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 15px;
  display: flex; /* 让 icon 和 title 在一行 */
  align-items: center;
  gap: 6px;
}

/* 成就列表 */
.achievement-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.achievement {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: var(--text-secondary);
  transition: color 0.3s ease;
}

.achievement .el-icon {
  color: #f7ba2a; /* 星星/奖牌颜色 */
}

/* 阅读趋势图 */
.trend-chart {
  display: flex;
  justify-content: space-around;
  align-items: flex-end;
  height: 80px; /* 调整图表高度 */
  margin-bottom: 10px;
}

.chart-bar {
  width: 15px; /* 柱状图宽度 */
  background-color: var(--primary-color);
  border-radius: 3px 3px 0 0;
  transition: height 0.5s ease-out; /* 高度变化动画 */
}

.chart-labels {
  display: flex;
  justify-content: space-around;
  font-size: 12px;
  color: var(--text-secondary);
}

/* 阅读分类 */
.category-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.category-item {
  display: flex;
  align-items: center;
  gap: 10px;
}

.category-name {
  font-size: 14px;
  color: var(--text-primary);
  width: 120px; /* 固定宽度方便对齐 */
  flex-shrink: 0;
}

/* 确保进度条充满剩余空间 */
.category-item .el-progress {
  flex-grow: 1;
}

/* 阅读统计卡片区域 - (容器现在不需要特定样式，行容器也不需要) */
.stats-container {
   /* margin-bottom: 30px; (如果不需要间距可以移除) */
}

.stats-row {
   /* display: grid; (不再需要) */
   /* grid-template-columns: repeat(3, 1fr); (不再需要) */
   /* gap: 20px; (不再需要) */
}

/* 为移动过来的 el-card 添加统一外观 (或调整现有样式) */
.stat-card.unified-card {
  /* 保持和 overview-card 类似的外观 */
  background-color: var(--card-bg);
  padding: 25px; /* 统一内边距 */
  border-radius: 8px;
  box-shadow: var(--card-shadow);
  display: flex; /* 改为 flex 布局 */
  flex-direction: column; /* 内容垂直排列 */
  transition: all 0.3s ease;
}

/* 移除 el-card 默认的 header padding */
.stat-card.unified-card .el-card__header {
  padding: 0;
  border-bottom: none;
  margin-bottom: 15px; /* 调整 header 和 content 间距 */
}

/* 统一 card-title 样式 (复用或调整) */
.stat-card.unified-card .card-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 0; /* header 内不再需要下边距 */
  display: flex;
  align-items: center;
  gap: 6px;
}

.stat-card.unified-card .stat-content {
  text-align: center; /* 内容居中 */
  flex-grow: 1; /* 占据剩余空间 */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center; /* 垂直居中 */
}

.stat-card.unified-card .stat-value {
  font-size: 32px; /* 调整字体大小 */
  font-weight: 600;
  color: var(--primary-color);
  margin-bottom: 10px;
}

.stat-card.unified-card .stat-desc {
  font-size: 13px;
  color: var(--text-secondary);
  margin-top: 15px;
}

.stat-card.unified-card .el-progress {
  width: 80%; /* 控制进度条宽度 */
  margin: 10px 0;
}

/* 连续阅读打卡样式 */
.streak-days {
  display: flex;
  justify-content: center;
  gap: 5px;
  margin: 15px 0;
}

.streak-day {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #f0f0f0;
  color: var(--text-secondary);
  font-size: 12px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.streak-day.active {
  background-color: var(--primary-color);
  color: white;
}

/* 最近阅读日志卡片 */
.recent-logs-card {
  margin-bottom: 24px;
  border: none;
}

.view-all {
  display: flex;
  align-items: center;
  color: var(--primary-color);
  font-size: 14px;
  text-decoration: none;
}

.view-all .el-icon {
  margin-left: 5px;
}

.loading-container {
  padding: 20px 0;
}

.empty-logs {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 30px 0;
}

.empty-logs .el-button {
  margin-top: 20px;
}

.log-link {
  color: var(--text-primary);
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.log-link:hover {
  color: var(--el-color-primary);
}

.add-log {
  margin-top: 20px;
  text-align: center;
}

/* 响应式调整 (可能需要调整 .overview-cards 的列数) */
@media (max-width: 992px) {
  .overview-cards {
    grid-template-columns: repeat(2, 1fr); /* 平板保持两列 */
  }
}

@media (max-width: 768px) {
  .overview-cards {
    grid-template-columns: 1fr; /* 移动设备改为单列 */
  }
}
</style> 