<template>
  <AdminLayout>
    <div class="admin-appeals">
      <div class="container">
        <h1 class="page-title">User Appeal Management</h1>
        
        <!-- 筛选条件 -->
        <div class="filter-section">
          <el-select 
            v-model="statusFilter" 
            placeholder="Appeal Status" 
            @change="loadAppeals"
            class="status-filter"
          >
            <el-option label="Pending" value="pending"></el-option>
            <el-option label="Approved" value="approved"></el-option>
            <el-option label="Rejected" value="rejected"></el-option>
            <el-option label="All" value="all"></el-option>
          </el-select>
          
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="To"
            start-placeholder="Start date"
            end-placeholder="End date"
            @change="loadAppeals"
            class="date-filter"
          />
        </div>
        
        <!-- 申诉列表 -->
        <div class="appeals-container" v-loading="loading">
          <el-empty v-if="appealsList.length === 0" description="No appeals found"></el-empty>
          
          <div v-else>
            <div 
              v-for="appeal in appealsList" 
              :key="appeal.id"
              class="appeal-card"
              :class="{
                'appeal-pending': appeal.status === 'pending',
                'appeal-approved': appeal.status === 'approved',
                'appeal-rejected': appeal.status === 'rejected'
              }"
            >
              <div class="appeal-header">
                <div class="appeal-info">
                  <h3 class="appeal-title">
                    Appeal #{{ appeal.appealId }} from {{ appeal.username }}
                  </h3>
                  <div class="appeal-meta">
                    <span class="appeal-date">
                      <i class="el-icon-time"></i>
                      Submitted: {{ appeal.submittedAt }}
                    </span>
                    <el-tag :type="getStatusType(appeal.status)" size="small">
                      {{ getStatusText(appeal.status) }}
                    </el-tag>
                  </div>
                </div>
                
                <div class="appeal-actions">
                  <el-dropdown @command="(cmd) => handleCommand(cmd, appeal)">
                    <el-button type="primary" size="small">
                      Actions<el-icon class="el-icon--right"><arrow-down /></el-icon>
                    </el-button>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item command="view">View Details</el-dropdown-item>
                        <el-dropdown-item command="approve" v-if="appeal.status === 'pending'">Approve</el-dropdown-item>
                        <el-dropdown-item command="reject" v-if="appeal.status === 'pending'">Reject</el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
              </div>
              
              <div class="appeal-content">
                <div class="ban-info">
                  <h4>Ban Reason:</h4>
                  <p>{{ appeal.banReason }}</p>
                  <div class="ban-date">Ban Date: {{ appeal.banDate }}</div>
                </div>
                
                <div class="appeal-reason">
                  <h4>Appeal Reason:</h4>
                  <p>{{ truncateContent(appeal.appealMessage) }}</p>
                </div>
              </div>
            </div>
            
            <!-- 分页 -->
            <div class="pagination-container">
              <el-pagination
                background
                layout="prev, pager, next"
                :total="totalItems"
                :current-page="currentPage"
                :page-size="pageSize"
                @current-change="handlePageChange"
              ></el-pagination>
            </div>
          </div>
        </div>
        
        <!-- 申诉详情对话框 -->
        <el-dialog
          v-model="appealDetailVisible"
          title="Appeal Details"
          width="600px"
        >
          <div v-if="selectedAppeal" class="appeal-detail">
            <h3 class="user-info">
              <el-avatar :size="32" class="user-avatar" :src="selectedAppeal.avator"></el-avatar>
              {{ selectedAppeal.username }}
            </h3>
            
            <div class="detail-section">
              <h4>Ban Information</h4>
              <div class="ban-detail">
                <p><strong>Ban Reason:</strong> {{ selectedAppeal.banReason }}</p>
                <p><strong>Ban Date:</strong> {{ selectedAppeal.banDate }}</p>
                <!-- <p><strong>Ban Expires:</strong> {{ selectedAppeal.banExpires || 'Permanent' }}</p> -->
              </div>
            </div>
            
            <div class="detail-section">
              <h4>Appeal Information</h4>
              <div class="appeal-detail">
                <p><strong>Submitted:</strong> {{ selectedAppeal.submittedAt }}</p>
                <p><strong>Status:</strong> 
                  <el-tag :type="getStatusType(selectedAppeal.status)" size="small">
                    {{ getStatusText(selectedAppeal.status) }}
                  </el-tag>
                </p>
                <p><strong>Appeal Reason:</strong></p>
                <div class="reason-content">{{ selectedAppeal.appealMessage }}</div>
              </div>
            </div>
            
            <div class="detail-section" v-if="selectedAppeal.status === 'rejected'">
              <h4>Rejection Information</h4>
              <div class="reject-detail">
                <p><strong>Rejected By:</strong> {{ selectedAppeal.rejectedBy }}</p>
                <p><strong>Rejection Date:</strong> {{ selectedAppeal.rejectionDate }}</p>
                <p><strong>Rejection Reason:</strong></p>
                <div class="reason-content">{{ selectedAppeal.rejectionReason }}</div>
              </div>
            </div>
            
            <div class="detail-actions" v-if="selectedAppeal.status === 'pending'">
              <el-button type="success" @click="approveAppeal(selectedAppeal)">Approve Appeal</el-button>
              <el-button type="danger" @click="openRejectDialog(selectedAppeal)">Reject Appeal</el-button>
            </div>
          </div>
        </el-dialog>
        
        <!-- 拒绝申诉对话框 -->
        <el-dialog
          v-model="rejectDialogVisible"
          title="Reject Appeal"
          width="500px"
        >
          <el-form
            v-if="rejectAppeal"
            :model="rejectForm"
            :rules="rejectRules"
            ref="rejectFormRef"
            label-width="0"
          >
            <p class="reject-note">Please provide a reason for rejecting this appeal.</p>
            <el-form-item prop="reason">
              <el-input
                type="textarea"
                v-model="rejectForm.reason"
                :rows="5"
                placeholder="Enter rejection reason..."
              ></el-input>
            </el-form-item>
          </el-form>
          <template #footer>
            <span>
              <el-button @click="rejectDialogVisible = false">Cancel</el-button>
              <el-button type="primary" @click="confirmRejectAppeal" :loading="submitting">Confirm</el-button>
            </span>
          </template>
        </el-dialog>
      </div>
    </div>
  </AdminLayout>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import AdminLayout from '../../layouts/AdminLayout.vue'
import { ArrowDown } from '@element-plus/icons-vue'
import { getAppealsList, updateAppeal } from '../../api/applea'

export default {
  name: 'AdminAppeals',
  components: {
    AdminLayout,
    ArrowDown
  },
  setup() {
    // 状态变量
    const loading = ref(false)
    const submitting = ref(false)
    
    // 筛选条件
    const statusFilter = ref('pending')
    const dateRange = ref([])
    
    // 分页信息
    const currentPage = ref(1)
    const pageSize = ref(10)
    const totalItems = ref(0)
    
    // 申诉列表数据
    const appealsList = ref([])
    
    // 申诉详情
    const appealDetailVisible = ref(false)
    const selectedAppeal = ref(null)
    
    // 拒绝申诉
    const rejectDialogVisible = ref(false)
    const rejectAppeal = ref(null)
    const rejectFormRef = ref(null)
    const rejectForm = reactive({
      reason: ''
    })
    
    // 表单验证规则
    const rejectRules = {
      reason: [
        { required: true, message: 'Please enter a rejection reason', trigger: 'blur' },
        { min: 10, message: 'Reason must be at least 10 characters', trigger: 'blur' }
      ]
    }
    
    // 加载申诉列表
    const loadAppeals = () => {
      loading.value = true
      getAppealsList({
        status: statusFilter.value,
        startDate: formatDate(dateRange.value[0]),
        endDate: formatDate(dateRange.value[1]),
        pageNum: currentPage.value,
        pageSize: pageSize.value
      }).then(res => {
        console.log(res)
        appealsList.value = res.data.records
        totalItems.value = res.data.total
        loading.value = false
      })
      // 模拟API请求获取申诉列表
      // setTimeout(() => {
      //   appealsList.value = [
      //     {
      //       id: 101,
      //       username: 'john_doe',
      //       date: '2023-05-15 14:30:22',
      //       banReason: 'Violation of community guidelines - Inappropriate content',
      //       banDate: '2023-05-10 09:15:33',
      //       banExpires: '2023-06-10 09:15:33',
      //       reason: 'I believe my account was restricted by mistake. The content I posted was educational and didn\'t violate any guidelines. I have been a member for over 3 years with no previous violations. Please review my case and restore my account access.',
      //       status: 'pending'
      //     },
      //     {
      //       id: 102,
      //       username: 'alice_smith',
      //       date: '2023-05-14 10:22:15',
      //       banReason: 'Multiple violations of reading log submission rules',
      //       banDate: '2023-05-09 16:45:12',
      //       banExpires: null,
      //       reason: 'I acknowledge my previous mistakes and have learned from them. I promise to follow all community guidelines moving forward. I would greatly appreciate another chance as I value this platform for my reading journey.',
      //       status: 'approved'
      //     },
      //     {
      //       id: 103,
      //       username: 'robert_jackson',
      //       date: '2023-05-13 08:11:05',
      //       banReason: 'Suspicious account activity',
      //       banDate: '2023-05-08 23:10:45',
      //       banExpires: '2023-05-22 23:10:45',
      //       reason: 'My account was accessed from a new device while I was traveling. I can provide verification of my identity if needed. This was not suspicious activity but merely me using a different device.',
      //       status: 'rejected',
      //       rejectedBy: 'admin_sarah',
      //       rejectionDate: '2023-05-14 11:30:22',
      //       rejectionReason: 'Your appeal has been rejected because our systems detected multiple login attempts with incorrect passwords from various locations. For security reasons, we cannot restore access at this time.'
      //     }
      //   ].filter(appeal => {
      //     // 根据状态筛选
      //     if (statusFilter.value !== 'all' && appeal.status !== statusFilter.value) {
      //       return false
      //     }
          
      //     // 根据日期范围筛选
      //     if (dateRange.value && dateRange.value.length === 2) {
      //       const appealDate = new Date(appeal.date)
      //       const startDate = dateRange.value[0]
      //       const endDate = dateRange.value[1]
            
      //       if (appealDate < startDate || appealDate > endDate) {
      //         return false
      //       }
      //     }
          
      //     return true
      //   })
        
      //   totalItems.value = appealsList.value.length
      //   loading.value = false
      // }, 800)
    }
    
    // 初始化
    onMounted(() => {
      loadAppeals()
    })
    
    // 分页处理
    const handlePageChange = (page) => {
      currentPage.value = page
      loadAppeals()
    }
    
    // 查看申诉详情
    const viewAppealDetail = (appeal) => {
      selectedAppeal.value = appeal
      appealDetailVisible.value = true
    }
    
    // 操作处理
    const handleCommand = (command, appeal) => {
      switch (command) {
        case 'view':
          viewAppealDetail(appeal)
          break
        case 'approve':
          approveAppeal(appeal)
          break
        case 'reject':
          openRejectDialog(appeal)
          break
      }
    }
    
    // 批准申诉
    const approveAppeal = (appeal) => {
      ElMessageBox.confirm(
        `Are you sure you want to approve this appeal and restore access for user ${appeal.username}?`,
        'Confirm Approval',
        {
          confirmButtonText: 'Approve',
          cancelButtonText: 'Cancel',
          type: 'info'
        }
      ).then(() => {
        updateAppeal({
          appealId: appeal.appealId,
          status: 'approved'
        }).then(res => {
          console.log(res)
          loadAppeals()
          ElMessage.success(`Appeal for ${appeal.username} has been approved`)
          appealDetailVisible.value = false

        })
        // 模拟API请求
        // setTimeout(() => {
        //   const index = appealsList.value.findIndex(item => item.id === appeal.id)
        //   if (index !== -1) {
        //     appealsList.value[index].status = 'approved'
        //   }
          
        //   ElMessage.success(`Appeal for ${appeal.username} has been approved`)
        //   appealDetailVisible.value = false
        // }, 600)
      }).catch(() => {})
    }
    
    // 打开拒绝对话框
    const openRejectDialog = (appeal) => {
      rejectAppeal.value = appeal
      rejectForm.reason = ''
      rejectDialogVisible.value = true
    }
    
    // 确认拒绝申诉
    const confirmRejectAppeal = async () => {
      if (!rejectFormRef.value) return
      
      await rejectFormRef.value.validate((valid) => {
        if (valid) {
          submitting.value = true
          updateAppeal({
            appealId: rejectAppeal.value.appealId,
            status: 'rejected',
            adminResponse: rejectForm.reason
            
          }).then(res => {
            console.log(res)
            loadAppeals()
            submitting.value = false
            rejectDialogVisible.value = false
            ElMessage.success(`Appeal for ${rejectAppeal.value.username} has been rejected`)
          })
          
          // 模拟API请求
          // setTimeout(() => {
          //   const index = appealsList.value.findIndex(item => item.id === rejectAppeal.value.id)
          //   if (index !== -1) {
          //     appealsList.value[index].status = 'rejected'
          //     appealsList.value[index].rejectionReason = rejectForm.reason
          //     appealsList.value[index].rejectedBy = 'current_admin'
          //     appealsList.value[index].rejectionDate = new Date().toISOString().slice(0, 19).replace('T', ' ')
          //   }
            
          //   submitting.value = false
          //   rejectDialogVisible.value = false
          //   appealDetailVisible.value = false
            
          //   ElMessage.success(`Appeal for ${rejectAppeal.value.username} has been rejected`)
          // }, 800)
        }
      })
    }
    
    // 辅助函数
    const formatDate = (date) => {
      if (!date) return null
      const d = new Date(date)
      const year = d.getFullYear()
      const month = String(d.getMonth() + 1).padStart(2, '0')
      const day = String(d.getDate()).padStart(2, '0')
      return `${year}-${month}-${day}`
    }
    
    const truncateContent = (content) => {
      return content.length > 100 ? content.substring(0, 100) + '...' : content
    }
    
    const getStatusType = (status) => {
      switch (status) {
        case 'pending': return 'warning'
        case 'approved': return 'success'
        case 'rejected': return 'danger'
        default: return 'info'
      }
    }
    
    const getStatusText = (status) => {
      switch (status) {
        case 'pending': return 'Pending'
        case 'approved': return 'Approved'
        case 'rejected': return 'Rejected'
        default: return 'Unknown'
      }
    }
    
    return {
      loading,
      submitting,
      statusFilter,
      dateRange,
      currentPage,
      pageSize,
      totalItems,
      appealsList,
      appealDetailVisible,
      selectedAppeal,
      rejectDialogVisible,
      rejectAppeal,
      rejectFormRef,
      rejectForm,
      rejectRules,
      loadAppeals,
      handlePageChange,
      viewAppealDetail,
      handleCommand,
      approveAppeal,
      openRejectDialog,
      confirmRejectAppeal,
      truncateContent,
      getStatusType,
      getStatusText
    }
  }
}
</script>

<style scoped>
.admin-appeals {
  padding: 20px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
}

.page-title {
  margin-bottom: 24px;
  font-size: 24px;
  color: #333;
  font-weight: 500;
}

/* 筛选区域 */
.filter-section {
  margin-bottom: 24px;
  display: flex;
  gap: 20px;
  align-items: center;
  flex-wrap: wrap;
}

.status-filter {
  width: 180px;
}

.date-filter {
  width: 380px;
}

/* 申诉卡片 */
.appeals-container {
  min-height: 300px;
}

.appeal-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
  margin-bottom: 16px;
  transition: all 0.3s ease;
}

.appeal-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

/* 申诉状态样式 */
.appeal-pending {
  border-left: 4px solid #e6a23c;
}

.appeal-approved {
  border-left: 4px solid #67c23a;
}

.appeal-rejected {
  border-left: 4px solid #f56c6c;
}

.appeal-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 16px;
}

.appeal-title {
  margin: 0 0 8px 0;
  font-size: 18px;
  color: #333;
}

.appeal-meta {
  display: flex;
  align-items: center;
  gap: 12px;
  color: #666;
  font-size: 14px;
}

.appeal-date {
  display: flex;
  align-items: center;
  gap: 5px;
}

.appeal-content {
  padding-top: 12px;
  border-top: 1px solid #eaeaea;
}

.ban-info, .appeal-reason {
  margin-bottom: 16px;
}

.ban-info h4, .appeal-reason h4 {
  margin: 0 0 8px 0;
  font-size: 16px;
  color: #333;
}

.ban-info p, .appeal-reason p {
  margin: 0;
  color: #666;
  line-height: 1.5;
}

.ban-date {
  margin-top: 8px;
  font-size: 14px;
  color: #999;
}

/* 分页容器 */
.pagination-container {
  margin-top: 24px;
  display: flex;
  justify-content: center;
}

/* 详情对话框 */
.appeal-detail {
  padding: 0 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
  font-size: 18px;
  font-weight: 500;
}

.detail-section {
  margin-bottom: 24px;
}

.detail-section h4 {
  margin: 0 0 12px 0;
  font-size: 16px;
  color: #333;
  border-bottom: 1px solid #eaeaea;
  padding-bottom: 8px;
}

.reason-content {
  background: #f9f9f9;
  padding: 12px;
  border-radius: 4px;
  color: #666;
  white-space: pre-line;
  margin-top: 8px;
}

.detail-actions {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 拒绝对话框 */
.reject-note {
  margin-bottom: 16px;
  color: #666;
}
</style> 