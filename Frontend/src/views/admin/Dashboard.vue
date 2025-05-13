<template>
  <AdminLayout>
    <div class="admin-dashboard">
      <div class="container">
        <h1 class="page-title">Admin Console</h1>
        
        <div class="dashboard-cards">
          <!-- Core Data Cards -->
          <div class="data-card">
            <div class="card-icon users-icon">
              <i class="el-icon-user"></i>
            </div>
            <div class="card-content">
              <h3>Total Users</h3>
              <div class="card-value">{{ stats.userCount }}</div>
            </div>
          </div>
          
          <div class="data-card">
            <div class="card-icon logs-icon">
              <i class="el-icon-document"></i>
            </div>
            <div class="card-content">
              <h3>Total Logs</h3>
              <div class="card-value">{{ stats.logCount }}</div>
            </div>
          </div>
          
          <div class="data-card">
            <div class="card-icon banned-icon">
              <i class="el-icon-remove-outline"></i>
            </div>
            <div class="card-content">
              <h3>Banned Users</h3>
              <div class="card-value">{{ stats.bannedCount }}</div>
            </div>
          </div>
          
          <div class="data-card">
            <div class="card-icon pending-icon">
              <i class="el-icon-time"></i>
            </div>
            <div class="card-content">
              <h3>Pending Logs</h3>
              <div class="card-value">{{ stats.pendingCount }}</div>
            </div>
          </div>
        </div>
        
        <!-- Recent Activity Charts -->
        <div class="chart-section">
          <h2 class="section-title">Recent Activity</h2>
          <div class="charts-container">
            <div class="chart-card">
              <h3>New User Trend</h3>
              <div class="chart-placeholder">
                <!-- In a real project, integrate ECharts or similar library here -->
                <div class="placeholder-stats">
                  <div class="stat-bar" v-for="(value, index) in dates" :key="index" :style="{height: `${value.userCount }px`}"></div>
                </div>
                <div class="chart-axis">
                  <span v-for="(date, index) in dates" :key="index">{{ date.date }}</span>
                </div>
              </div>
            </div>
            
            <div class="chart-card">
              <h3>New Log Trend</h3>
              <div class="chart-placeholder">
                <div class="placeholder-stats">
                  <div class="stat-bar log-bar" v-for="(value, index) in datesLogs" :key="index" :style="{height: `${value.logCount }px`}"></div>
                </div>
                <div class="chart-axis">
                  <span v-for="(date, index) in datesLogs" :key="index">{{ date.date }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </AdminLayout>
</template>

<script>
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import AdminLayout from '../../layouts/AdminLayout.vue'
import { getPlatformData } from '../../api/home'

export default {
  name: 'AdminDashboard',
  components: {
    AdminLayout
  },
  setup() {
    const router = useRouter()
    
    // 统计数据
    const stats = reactive({
      userCount: 0,
      logCount: 0,
      bannedCount: 0,
      pendingCount: 0
    })
    
    // 趋势数据
    const dates = ref([])
    const datesLogs = ref([])
    const userTrend = ref([5, 8, 12, 7, 10, 15, 9])
    const logTrend = ref([8, 12, 15, 10, 14, 18, 11])
    
    // 模拟获取数据
    onMounted(() => {
      // 模拟API请求
      getPlatformData().then(res => {
        stats.userCount = res.data.sumUser
        stats.logCount = res.data.sumReadingLog
        stats.bannedCount = res.data.sumBanUser
        stats.pendingCount = res.data.sumInProgressLog
        
       res.data.recent7days.forEach(item=>{
         dates.value.push({
           date: item.date,
           userCount:item.userCount
        })
       })
        res.data.recent7daysLogs.forEach(item => {
          datesLogs.value.push({
            date: item.date,
            logCount: item.readingCount/10
          })
        })

      });

    })
    
    return {
      stats,
      dates,
      userTrend,
      logTrend,
      datesLogs
    }
  }
}
</script>

<style scoped>
.admin-dashboard {
  padding: 20px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
}

.page-title {
  margin-bottom: 20px;
  font-size: 24px;
  color: #333;
  font-weight: 500;
}

/* 数据卡片 */
.dashboard-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 30px;
}

.data-card {
  flex: 1;
  min-width: 240px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
  display: flex;
  align-items: center;
}

.card-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
}

.card-icon i {
  font-size: 28px;
  color: white;
}

.users-icon {
  background-color: #409eff;
}

.logs-icon {
  background-color: #67c23a;
}

.banned-icon {
  background-color: #f56c6c;
}

.pending-icon {
  background-color: #e6a23c;
}

.card-content h3 {
  margin: 0;
  font-size: 16px;
  color: #606266;
  font-weight: normal;
}

.card-value {
  font-size: 32px;
  color: #303133;
  font-weight: bold;
  margin-top: 5px;
}

/* 图表部分 */
.chart-section {
  margin-top: 30px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.section-title {
  margin-top: 0;
  margin-bottom: 20px;
  font-size: 18px;
  color: #303133;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.charts-container {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
}

.chart-card {
  flex: 1;
  min-width: 300px;
}

.chart-card h3 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 16px;
  color: #606266;
}

.chart-placeholder {
  height: 200px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 20px;
  position: relative;
}

.placeholder-stats {
  height: 150px;
  display: flex;
  align-items: flex-end;
  gap: 20px;
  justify-content: space-between;
}

.stat-bar {
  width: 30px;
  background-color: #409eff;
  border-radius: 3px 3px 0 0;
}

.log-bar {
  background-color: #67c23a;
}

.chart-axis {
  display: flex;
  justify-content: space-between;
  margin-top: 5px;
  color: #909399;
  font-size: 12px;
}
</style> 