<template>
  <AdminLayout>
    <div class="admin-reviews">
      <div class="container">
        <h1 class="page-title">Log Review</h1>
        
        <!-- Filter section -->
        <div class="filter-section">
          <el-select 
            v-model="statusFilter" 
            placeholder="Review Status" 
            @change="handleStatusChange"
            class="status-filter"
          >
            <el-option label="Pending" value="pending"></el-option>
            <el-option label="Approved" value="approved"></el-option>
            <el-option label="Rejected" value="rejected"></el-option>
            <el-option label="All" value="all"></el-option>
          </el-select>
        </div>
        
        <!-- Log list -->
        <div class="logs-container" v-loading="loading">
          <template v-if="logsList.length > 0">
            <div 
              v-for="log in logsList" 
              :key="log.id"
              class="log-card"
              :class="{'log-pending': log.status === 'pending', 'log-rejected': log.status === 'rejected'}"
            >
              <div class="log-header">
                <div class="log-title-section">
                  <h3 class="log-title">{{ log.title }}</h3>
                  <el-tag 
                    :type="getStatusType(log.status)" 
                    size="small"
                  >
                    {{ getStatusText(log.status) }}
                  </el-tag>
                </div>
                <div class="log-meta">
                  <span>Author: {{ log.author }}</span>
                  <span>Submitted Date: {{ log.createdAt }}</span>
                </div>
              </div>
              
              <div class="log-content">
                <p>{{ truncateContent(log.personalNotes) }}</p>
              </div>
              
              <div class="log-actions">
                <el-button 
                  type="primary" 
                  size="small" 
                  plain
                  @click="viewLogDetail(log.readingLogId)"
                >
                  View Details
                </el-button>
                
                <template v-if="log.status === 'pending'">
                  <el-button 
                    type="success" 
                    size="small"
                    @click="approveLog(log)"
                  >
                    Approve
                  </el-button>
                  
                  <el-button 
                    type="danger" 
                    size="small"
                    @click="openRejectDialog(log)"
                  >
                    Reject
                  </el-button>
                </template>
                
                <el-dropdown 
                  trigger="click" 
                  @command="(command) => handleCommand(command, log)"
                  v-if="log.status === 'pending'"
                >
                  <el-button 
                    type="info" 
                    size="small"
                    plain
                  >
                    More Actions<i class="el-icon-arrow-down el-icon--right"></i>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item command="delete">Delete Log</el-dropdown-item>
                      <el-dropdown-item command="ban" divided>Ban Author</el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </div>
            </div>
            
            <!-- Pagination -->
            <div class="pagination-container">
              <el-pagination
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="currentPage"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="pageSize"
                layout="total, sizes, prev, pager, next, jumper"
                :total="totalItems"
              >
              </el-pagination>
            </div>
          </template>
          
          <div v-else class="empty-data">
            <i class="el-icon-document"></i>
            <p>No {{ statusFilter === 'pending' ? 'Pending' : '' }} logs</p>
          </div>
        </div>
        
        <!-- Log detail dialog -->
        <el-dialog 
          v-model="logDetailVisible" 
          title="Log Details" 
          width="700px"
        >
          <div v-if="selectedLog" class="log-detail">
            <div class="detail-header">
              <h2>{{ selectedLog.title }}</h2>
              <div class="detail-meta">
                <div class="meta-item">
                  <span class="meta-label">Author:</span>
                  <span>{{ selectedLog.author }}</span>
                </div>
                <div class="meta-item">
                  <span class="meta-label">Reading Date:</span>
                  <span>{{ selectedLog.readingDate }}</span>
                </div>
                <div class="meta-item">
                  <span class="meta-label">Reading Duration:</span>
                  <span>{{ selectedLog.readingTime }}</span>
                </div>
                <div class="meta-item">
                  <span class="meta-label">Submitted Date:</span>
                  <span>{{ selectedLog.createdAt }}</span>
                </div>
                <div class="meta-item">
                  <span class="meta-label">Status:</span>
                  <el-tag :type="getStatusType(selectedLog.status)">
                    {{ getStatusText(selectedLog.status) }}
                  </el-tag>
                </div>
              </div>
            </div>
            
            <div class="detail-content">
              <h3>Content</h3>
              <div class="content-box">
                {{ selectedLog.personalNotes }}
              </div>
            </div>
            
            <div class="review-actions" v-if="selectedLog.status === 'pending'">
              <el-button 
                type="success" 
                @click="approveLog(selectedLog); logDetailVisible = false"
              >
                Approve
              </el-button>
              
              <el-button 
                type="danger" 
                @click="openRejectDialog(selectedLog); logDetailVisible = false"
              >
                Reject
              </el-button>
              
              <el-button 
                @click="handleCommand('delete', selectedLog); logDetailVisible = false"
              >
                Delete Log
              </el-button>
              
              <el-button 
                type="warning" 
                @click="handleCommand('ban', selectedLog); logDetailVisible = false"
              >
                Ban Author
              </el-button>
            </div>
            
            <div class="reject-info" v-if="selectedLog.status === 'rejected'">
              <h3>Rejection Reason</h3>
              <p>{{ selectedLog.rejectReason }}</p>
            </div>
          </div>
        </el-dialog>
        
        <!-- Rejection reason dialog -->
        <el-dialog 
          v-model="rejectDialogVisible" 
          title="Reject Log" 
          width="500px"
        >
          <div class="reject-dialog">
            <p>Are you sure you want to reject the log <strong>{{ rejectLog?.title }}</strong>?</p>
            
            <el-form :model="rejectForm" :rules="rejectRules" ref="rejectFormRef" label-width="80px">
              <el-form-item label="Rejection Reason" prop="reason">
                <el-input 
                  type="textarea" 
                  v-model="rejectForm.reason"
                  :rows="3"
                  placeholder="Please enter the rejection reason"
                ></el-input>
              </el-form-item>
            </el-form>
          </div>
          
          <template #footer>
            <el-button @click="rejectDialogVisible = false">Cancel</el-button>
            <el-button type="danger" @click="confirmRejectLog">Confirm Rejection</el-button>
          </template>
        </el-dialog>
      </div>
    </div>
  </AdminLayout>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import AdminLayout from '../../layouts/AdminLayout.vue'
import { getLogList, updateLog, deleteLogs } from '../../api/log'
import { updateUser } from '../../api/index'

export default {
  name: 'AdminReviews',
  components: {
    AdminLayout
  },
  setup() {
    const router = useRouter()
    const loading = ref(false)
    
    // Filter
    const statusFilter = ref('pending')
    
    // Pagination
    const currentPage = ref(1)
    const pageSize = ref(10)
    const totalItems = ref(0)
    
    // Log list
    const logsList = ref([])
    
    // Log detail
    const logDetailVisible = ref(false)
    const selectedLog = ref(null)
    
    // Reject log
    const rejectDialogVisible = ref(false)
    const rejectLog = ref(null)
    const rejectFormRef = ref(null)
    const rejectForm = reactive({
      reason: ''
    })
    const rejectRules = {
      reason: [
        { required: true, message: 'Please enter the rejection reason', trigger: 'blur' },
        { min: 5, message: 'Rejection reason must be at least 5 characters', trigger: 'blur' }
      ]
    }
    
    // Load the logs list
    const loadLogsList = () => {
      loading.value = true
      getLogList({
        pageNum: currentPage.value,
        pageSize: pageSize.value,
        status: statusFilter.value
      }).then(res => {

        logsList.value = res.data.records
        totalItems.value = res.data.total
        loading.value = false
      })
      
      // Mock API request
      // setTimeout(() => {
      //   // Mock data
      //   const allLogs = [
      //     {
      //       id: 1,
      //       title: 'To Kill a Mockingbird - Analysis',
      //       author: 'John Smith',
      //       submitDate: '2023-04-05 14:30',
      //       readDate: '2023-04-01',
      //       readDuration: '3 hours',
      //       content: 'In this American classic, Harper Lee explores themes of racial injustice and moral growth through the eyes of young Scout Finch. The character of Atticus Finch represents moral integrity and wisdom, showing courage in the face of prejudice. I was particularly moved by the way Lee portrays childhood innocence confronting the harsh realities of adult society. The novel reminds us that empathy is essential for understanding others, captured in Atticus\'s famous quote about walking in another person\'s shoes.',
      //       status: 'pending'
      //     },
      //     {
      //       id: 2,
      //       title: 'One Hundred Years of Solitude - Notes',
      //       author: 'Emma Johnson',
      //       submitDate: '2023-04-04 10:15',
      //       readDate: '2023-03-28',
      //       readDuration: '5 hours',
      //       content: 'One Hundred Years of Solitude is Gabriel García Márquez\'s masterpiece, chronicling seven generations of the Buendía family. Through magical realism, the novel interweaves Latin American history with mythology. Reading this work, I was amazed by the rich imagination and profound symbolism. The theme of solitude permeates throughout, prompting deep reflection. Márquez\'s Macondo is like a microcosm of the world, reflecting various contradictions and struggles in human society.',
      //       status: 'approved'
      //     },
      //     {
      //       id: 3,
      //       title: 'Sapiens: A Brief History of Humankind - Reading Notes',
      //       author: 'Michael Wong',
      //       submitDate: '2023-04-03 16:45',
      //       readDate: '2023-03-25',
      //       readDuration: '4 hours',
      //       content: 'Sapiens offers a biological perspective on how Homo sapiens rose from the African savannah to global dominance. Author Yuval Noah Harari presents unique and incisive viewpoints, arguing that the Agricultural Revolution was humanity\'s greatest trap, while information and imagination were key to our success. This book shattered my traditional understanding of history, making me reconsider the development trajectory and future direction of human society. Particularly, the concept of "the power of fictional stories" profoundly reveals the core mechanisms of human social operation.',
      //       status: 'rejected',
      //       rejectReason: 'Content is too general. Please add more personal reading experiences and reflections.'
      //     }
      //   ]
        
      //   // Filter based on status
      //   if (statusFilter.value !== 'all') {
      //     logsList.value = allLogs.filter(log => log.status === statusFilter.value)
      //   } else {
      //     logsList.value = allLogs
      //   }
        
      //   totalItems.value = logsList.value.length
      //   loading.value = false
      // }, 500)
    }
    
    // Initialize
    onMounted(() => {
      loadLogsList()
    })
    
    // Filter processing
    const handleStatusChange = () => {
      currentPage.value = 1
      loadLogsList()
    }
    
    // Pagination processing
    const handleSizeChange = (size) => {
      pageSize.value = size
      loadLogsList()
    }
    
    const handleCurrentChange = (page) => {
      currentPage.value = page
      loadLogsList()
    }
    
    // View log details
    const viewLogDetail = (logId) => {
      const log = logsList.value.find(l => l.readingLogId === logId)
      if (log) {
        selectedLog.value = log
        logDetailVisible.value = true
      }
    }
    
    // Approve log
    const approveLog = (log) => {
      ElMessageBox.confirm(
        `Are you sure you want to approve the log "${log.title}"?`,
        'Confirmation',
        {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'info'
        }
      ).then(() => {
        updateLog({
          readingLogId: log.readingLogId,
          status: 'approved',
          aduit: 1
        }).then(res => {
          ElMessage.success('Log has been approved')
          loadLogsList()
        })
      
      }).catch(() => {})
    }
    
    // Reject log
    const openRejectDialog = (log) => {
      rejectLog.value = log
      rejectDialogVisible.value = true
    }
    
    const confirmRejectLog = async () => {
      if (!rejectFormRef.value) return
      
      await rejectFormRef.value.validate((valid) => {
        if (valid) {
          updateLog({
            readingLogId: rejectLog.value.readingLogId,
            status: 'rejected',
            rejectReason: rejectForm.reason,
            aduit: 1
          }).then(res => {
            ElMessage.success('Log has been rejected')
            rejectDialogVisible.value = false
            loadLogsList()
          })
        
        }
      })
    }
    
    // More actions
    const handleCommand = (command, log) => {
      switch (command) {
        case 'delete':
          deleteLog(log)
          break
        case 'ban':
          banAuthor(log)
          break
      }
    }
    
    // Delete log
    const deleteLog = (log) => {
      ElMessageBox.confirm(
        `Are you sure you want to delete the log "${log.title}"? This action cannot be undone.`,
        'Warning',
        {
          confirmButtonText: 'Delete',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }
      ).then(() => {
        deleteLogs(log.readingLogId).then(res => {
          ElMessage.success('Log has been deleted')
          loadLogsList()
        })
        
      }).catch(() => {})
    }
    
    // Ban author
    const banAuthor = (log) => {
      ElMessageBox.confirm(
        `Are you sure you want to ban user ${log.author}?`,
        'Warning',
        {
          confirmButtonText: 'Ban User',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }
      ).then(() => {
        router.push('/admin/users')
        // Integration with user management ban process needed here
        ElMessage.info('Redirected to User Management page. Please complete the ban operation there.')
      }).catch(() => {})
    }
    
    // Helper function
    const truncateContent = (content) => {
      return content.length > 150 ? content.substring(0, 150) + '...' : content
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
        case 'pending': return 'Pending Review'
        case 'approved': return 'Approved'
        case 'rejected': return 'Rejected'
        default: return 'Unknown'
      }
    }
    
    return {
      loading,
      statusFilter,
      currentPage,
      pageSize,
      totalItems,
      logsList,
      logDetailVisible,
      selectedLog,
      rejectDialogVisible,
      rejectLog,
      rejectFormRef,
      rejectForm,
      rejectRules,
      handleStatusChange,
      handleSizeChange,
      handleCurrentChange,
      viewLogDetail,
      approveLog,
      openRejectDialog,
      confirmRejectLog,
      handleCommand,
      truncateContent,
      getStatusType,
      getStatusText
    }
  }
}
</script>

<style scoped>
.admin-reviews {
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

/* Filter section */
.filter-section {
  display: flex;
  margin-bottom: 20px;
  align-items: center;
}

.status-filter {
  width: 120px;
}

/* Log list */
.logs-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
  min-height: 300px;
}

.log-card {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 15px;
  margin-bottom: 15px;
  transition: all 0.3s;
}

.log-card:hover {
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.log-pending {
  border-left: 4px solid #e6a23c;
}

.log-rejected {
  border-left: 4px solid #f56c6c;
}

.log-header {
  margin-bottom: 10px;
}

.log-title-section {
  display: flex;
  align-items: center;
  margin-bottom: 5px;
}

.log-title {
  margin: 0;
  font-size: 18px;
  margin-right: 10px;
}

.log-meta {
  color: #909399;
  font-size: 13px;
  display: flex;
  gap: 15px;
}

.log-content {
  color: #606266;
  margin-bottom: 15px;
  line-height: 1.5;
}

.log-actions {
  display: flex;
  gap: 10px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.empty-data {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 200px;
  color: #909399;
}

.empty-data i {
  font-size: 48px;
  margin-bottom: 10px;
}

/* Log details */
.log-detail {
  padding: 0 10px;
}

.detail-header {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.detail-header h2 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 20px;
  color: #303133;
}

.detail-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.meta-item {
  display: flex;
  align-items: center;
}

.meta-label {
  color: #606266;
  margin-right: 5px;
  font-weight: bold;
}

.detail-content h3 {
  font-size: 16px;
  margin-bottom: 10px;
  color: #303133;
}

.content-box {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  line-height: 1.6;
  color: #606266;
  white-space: pre-line;
}

.review-actions {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}

.reject-info {
  margin-top: 20px;
  background: #fef0f0;
  padding: 15px;
  border-radius: 4px;
}

.reject-info h3 {
  font-size: 16px;
  margin-top: 0;
  margin-bottom: 10px;
  color: #f56c6c;
}

.reject-info p {
  margin: 0;
  color: #606266;
}

/* Rejection dialog */
.reject-dialog p {
  margin-bottom: 15px;
}
</style> 