<template>
  <AdminLayout>
    <div class="admin-feedbacks">
      <div class="container">
        <h1 class="page-title">User Feedback Management</h1>
        
        <!-- Filter section -->
        <div class="filter-section">
          <el-select 
            v-model="statusFilter" 
            placeholder="Status" 
            @change="handleFilterChange"
            class="status-filter"
          >
            <el-option label="All" value="all"></el-option>
            <el-option label="Pending" value="pending"></el-option>
            <el-option label="Replied" value="replied"></el-option>
          </el-select>
          
          <el-select 
            v-model="typeFilter" 
            placeholder="Feedback Type" 
            @change="handleFilterChange"
            class="type-filter"
          >
            <el-option label="All" value="all"></el-option>
            <el-option label="Feature Request" value="feature"></el-option>
            <el-option label="Bug Report" value="bug"></el-option>
            <el-option label="General Question" value="question"></el-option>
            <el-option label="Other" value="other"></el-option>

          </el-select>
        </div>
        
        <!-- Feedback content area -->
        <div class="feedbacks-container" v-loading="loading">
          <div class="feedbacks-wrapper">
            <!-- Feedback list on the left -->
            <div class="feedbacks-list">
              <div 
                v-for="item in feedbacksList" 
                :key="item.id"
                class="feedback-item"
                :class="{
                  'feedback-item-active': selectedFeedback && selectedFeedback.feedbackId === item.feedbackId,
                  'feedback-item-replied': item.status === 'replied'
                }"
                @click="selectFeedback(item)"
              >
                <div class="feedback-item-header">
                  <el-tag 
                    size="small" 
                    :type="getTypeTag(item.feedbackType)"
                  >
                    {{ getTypeText(item.feedbackType) }}
                  </el-tag>
                  <el-tag 
                    size="small" 
                    :type="item.status === 'replied' ? 'success' : 'warning'"
                  >
                    {{ item.status === 'replied' ? 'Replied' : 'Pending' }}
                  </el-tag>
                </div>
                
                <div class="feedback-item-content">
                  <div class="feedback-user-info">
                    <span class="username">{{ item.username }}</span>
                    <span class="date">{{ item.createdAt }}</span>
                  </div>
                  <div class="feedback-preview">
                    {{ truncateText(item.description, 60) }}
                  </div>
                </div>
              </div>
              
              <div v-if="feedbacksList.length === 0" class="empty-data">
                <i class="el-icon-chat-line-square"></i>
                <p>No feedback data</p>
              </div>
              
              <!-- Pagination -->
              <div class="pagination-container" v-if="feedbacksList.length > 0">
                <div class="pagination-info">
                  <span>Total {{ totalItems }} items</span>
                  <el-select v-model="pageSize" @change="handleSizeChange" class="page-size-select">
                    <el-option 
                      v-for="size in [10, 20, 50, 100]" 
                      :key="size" 
                      :label="`${size}/page`" 
                      :value="size">
                    </el-option>
                  </el-select>
                </div>
                <div class="pagination-controls">
                  <el-pagination
                    @current-change="handleCurrentChange"
                    :current-page="currentPage"
                    layout="prev, pager, next, jumper"
                    :total="totalItems"
                    :page-size="pageSize"
                  >
                  </el-pagination>
                </div>
              </div>
            </div>
            
            <!-- Feedback details and reply area on the right -->
            <div class="feedback-detail" v-if="selectedFeedback">
              <div class="detail-header">
                <h2>Feedback Details</h2>
                <div class="detail-tags">
                  <el-tag :type="getTypeTag(selectedFeedback.feedbackType)">
                    {{ getTypeText(selectedFeedback.feedbackType) }}
                  </el-tag>
                  <el-tag 
                    :type="selectedFeedback.status === 'replied' ? 'success' : 'warning'"
                  >
                    {{ selectedFeedback.status === 'replied' ? 'Replied' : 'Pending' }}
                  </el-tag>
                </div>
              </div>
              
              <div class="detail-user-info">
                <div class="user-avatar">
                  <el-avatar :size="40" :src="selectedFeedback.avatar"></el-avatar>
                </div>
                <div class="user-detail">
                  <div class="username">{{ selectedFeedback.username }}</div>
                  <div class="date">{{ selectedFeedback.date }}</div>
                </div>
              </div>
              
              <div class="detail-content">
                <h3>Feedback Content</h3>
                <div class="content-box">
                  {{ selectedFeedback.description }}
                </div>
                
                <div class="contact-info" v-if="selectedFeedback.contactInfo">
                  <h3>Contact Information</h3>
                  <div class="content-box contact-box">
                    {{ selectedFeedback.contactInfo }}
                  </div>
                </div>
              </div>
              
              <div class="reply-section">
                <div class="reply-history" v-if="selectedFeedback.reply">
                  <h3>Reply History</h3>
                  <div class="reply-box">
                    <div class="reply-header">
                      <span class="reply-admin">Admin Reply</span>
                      <span class="reply-date">{{ selectedFeedback.replyDate }}</span>
                    </div>
                    <div class="reply-content">
                      {{ selectedFeedback.adminResponse }}
                    </div>
                  </div>
                </div>
                
                <div class="reply-form" v-if="selectedFeedback.status !== 'replied'">
                  <h3>Reply to Feedback</h3>
                  <el-form :model="replyForm" :rules="replyRules" ref="replyFormRef">
                    <el-form-item prop="content">
                      <el-input 
                        type="textarea" 
                        v-model="replyForm.content"
                        :rows="4"
                        placeholder="Please enter your reply..."
                      ></el-input>
                    </el-form-item>
                    
                    <el-form-item>
                      <el-button type="primary" @click="submitReply">Send Reply</el-button>
                      <el-button 
                        type="info" 
                        plain 
                        @click="markAsResolved"
                      >
                        Mark as Resolved (No Reply)
                      </el-button>
                    </el-form-item>
                  </el-form>
                </div>
              </div>
            </div>
            
            <div class="feedback-detail empty-detail" v-else>
              <i class="el-icon-chat-dot-square"></i>
              <p>Select a feedback from the left</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </AdminLayout>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import AdminLayout from '../../layouts/AdminLayout.vue'
import { getFeedbackList, updateFeedback } from '../../api/feedback'

export default {
  name: 'AdminFeedbacks',
  components: {
    AdminLayout
  },
  setup() {
    const loading = ref(false)
    
    // Filters
    const statusFilter = ref('all')
    const typeFilter = ref('all')
    
    // Pagination
    const currentPage = ref(1)
    const pageSize = ref(10)
    const totalItems = ref(0)
    
    // Feedback list and selected feedback
    const feedbacksList = ref([])
    const selectedFeedback = ref(null)
    
    // Reply form
    const replyFormRef = ref(null)
    const replyForm = reactive({
      content: ''
    })
    const replyRules = {
      content: [
        { required: true, message: 'Please enter a reply', trigger: 'blur' },
        { min: 5, message: 'Reply must be at least 5 characters', trigger: 'blur' }
      ]
    }
    
    // Load feedback list
    const loadFeedbacksList = () => {
      loading.value = true
      getFeedbackList({
        pageNum: currentPage.value,
        pageSize: pageSize.value,
        status: statusFilter.value,
        type: typeFilter.value
      }).then(res => {
        feedbacksList.value = res.data.records
        totalItems.value = res.data.total,
        loading.value = false
      })
      
      // Mock API request
      // setTimeout(() => {
      //   // Mock data
      //   const allFeedbacks = [
      //     {
      //       id: 1,
      //       username: 'John Smith',
      //       avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      //       date: '2023-04-05 14:30',
      //       type: 'suggestion',
      //       content: 'I suggest adding reading goals and progress tracking features to help users set monthly or annual reading plans, and display completion status through charts. This would motivate users to maintain reading habits and improve platform stickiness.',
      //       contact: 'john.smith@example.com',
      //       status: 'pending',
      //       reply: '',
      //       replyDate: ''
      //     },
      //     {
      //       id: 2,
      //       username: 'Emma Johnson',
      //       avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      //       date: '2023-04-04 10:15',
      //       type: 'bug',
      //       content: 'When accessing the reading log details page on mobile, images display abnormally and sometimes extend beyond screen boundaries, requiring horizontal scrolling. Also, on smaller devices like iPhone SE, there are text layout issues with some content being obscured.',
      //       contact: '13800138000',
      //       status: 'replied',
      //       reply: 'Thank you for your feedback! We have recorded this issue, and our development team will fix the mobile adaptation problem in the next version. This issue is expected to be resolved in the update released next week. If you have any other concerns, please feel free to provide feedback anytime.',
      //       replyDate: '2023-04-04 15:20'
      //     },
      //     {
      //       id: 3,
      //       username: 'Michael Wu',
      //       avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      //       date: '2023-04-03 16:45',
      //       type: 'other',
      //       content: 'I really like the design and functionality of this reading platform, and the user experience is great. However, I was wondering if there are plans to launch a mobile app? Using the web version on a mobile phone is not very convenient, and a native app would be more user-friendly.',
      //       contact: '',
      //       status: 'pending',
      //       reply: '',
      //       replyDate: ''
      //     }
      //   ]
        
      //   // Filter based on conditions
      //   let filteredList = [...allFeedbacks]
        
      //   if (statusFilter.value !== 'all') {
      //     filteredList = filteredList.filter(item => item.status === statusFilter.value)
      //   }
        
      //   if (typeFilter.value !== 'all') {
      //     filteredList = filteredList.filter(item => item.type === typeFilter.value)
      //   }
        
      //   feedbacksList.value = filteredList
      //   totalItems.value = filteredList.length
        
      //   // If there's a selected feedback, update its data
      //   if (selectedFeedback.value) {
      //     const updated = filteredList.find(item => item.id === selectedFeedback.value.id)
      //     if (updated) {
      //       selectedFeedback.value = updated
      //     } else {
      //       selectedFeedback.value = null
      //     }
      //   }
        
      //   loading.value = false
      // }, 500)
    }
    
    // Initialize
    onMounted(() => {
      loadFeedbacksList()
    })
    
    // Filter handling
    const handleFilterChange = () => {
      currentPage.value = 1
      loadFeedbacksList()
    }
    
    // Pagination handling
    const handleSizeChange = (size) => {
      pageSize.value = size
      loadFeedbacksList()
    }
    
    const handleCurrentChange = (page) => {
      currentPage.value = page
      loadFeedbacksList()
    }
    
    // Select feedback
    const selectFeedback = (feedback) => {
      selectedFeedback.value = feedback
      replyForm.content = '' // Clear reply content
    }
    
    // Submit reply
    const submitReply = async () => {
      if (!replyFormRef.value) return
      
      await replyFormRef.value.validate((valid) => {
        if (valid && selectedFeedback.value) {
          const index = feedbacksList.value.findIndex(item => item.feedbackId === selectedFeedback.value.feedbackId)
          console.log(index);
          
          updateFeedback({
            feedbackId: selectedFeedback.value.feedbackId,
            adminResponse: replyForm.content,
            status: 'replied',
            replyDate: new Date().toLocaleString('zh-CN', { 
              year: 'numeric',
              month: '2-digit',
              day: '2-digit',
              hour: '2-digit',
              minute: '2-digit',
              second: '2-digit',
              hour12: false
            }).replace(/\//g, '-')
          }).then(res => {
            console.log(res)
            ElMessage.success('Reply sent successfully')

              selectedFeedback.value.status = 'replied'
              loadFeedbacksList()
            
      
            
          })

          // Mock API request
          // setTimeout(() => {
          //   // Update feedback status
          //   const index = feedbacksList.value.findIndex(item => item.id === selectedFeedback.value.id)
          //   if (index !== -1) {
          //     feedbacksList.value[index].status = 'replied'
          //     feedbacksList.value[index].reply = replyForm.content
          //     feedbacksList.value[index].replyDate = new Date().toLocaleString()
              
          //     // Update selected feedback
          //     selectedFeedback.value = feedbacksList.value[index]
          //   }
            
          //   replyForm.content = ''
          //   ElMessage.success('Reply sent successfully')
          // }, 500)
        }
      })
    }
    
    // Mark as resolved (no reply)
    const markAsResolved = () => {
      if (!selectedFeedback.value) return
      
      ElMessageBox.confirm(
        'Are you sure you want to mark this feedback as resolved?',
        'Confirmation',
        {
          confirmButtonText: 'Confirm',
          cancelButtonText: 'Cancel',
          type: 'info'
        }
      ).then(() => {
        // Mock API request
        setTimeout(() => {
          updateFeedback({
            feedbackId: selectedFeedback.value.feedbackId,
            status: 'replied',
            adminResponse: '',
            replyDate: new Date().toLocaleString('zh-CN', { 
              year: 'numeric',
              month: '2-digit',
              day: '2-digit',
              hour: '2-digit',
              minute: '2-digit',
              second: '2-digit',
              hour12: false
            }).replace(/\//g, '-')
          }).then(res => {
            console.log(res)
            ElMessage.success('Marked as resolved')
            selectedFeedback.value.status = 'replied'
            loadFeedbacksList()

          })
          // Update feedback status
          const index = feedbacksList.value.findIndex(item => item.id === selectedFeedback.value.id)
          if (index !== -1) {
            feedbacksList.value[index].status = 'replied'
            feedbacksList.value[index].reply = '[System Auto-Reply] This feedback has been processed by the administrator.'
            feedbacksList.value[index].replyDate = new Date().toLocaleString()
            
            // Update selected feedback
            selectedFeedback.value = feedbacksList.value[index]
          }
          
          ElMessage.success('Marked as resolved')
        }, 500)
      }).catch(() => {})
    }
    
    // Helper functions
    const truncateText = (text, length) => {
      return text.length > length ? text.substring(0, length) + '...' : text
    }
    
    const getTypeTag = (type) => {
      switch (type) {
        case 'suggestion': return 'success'
        case 'bug': return 'danger'
        case 'other': return 'info'
        default: return 'info'
      }
    }
    
    const getTypeText = (type) => {
      switch (type) {
        case 'suggestion': return 'Feature Suggestion'
        case 'bug': return 'Bug Report'
        case 'other': return 'Other'
        case 'feature': return 'Feature Request'
        case 'question': return 'General Question'
        default: return 'Unknown'
      }
    }
    
    return {
      loading,
      statusFilter,
      typeFilter,
      currentPage,
      pageSize,
      totalItems,
      feedbacksList,
      selectedFeedback,
      replyFormRef,
      replyForm,
      replyRules,
      handleFilterChange,
      handleSizeChange,
      handleCurrentChange,
      selectFeedback,
      submitReply,
      markAsResolved,
      truncateText,
      getTypeTag,
      getTypeText
    }
  }
}
</script>

<style scoped>
.admin-feedbacks {
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
  gap: 15px;
}

.status-filter,
.type-filter {
  width: 120px;
}

/* Feedback container */
.feedbacks-container {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.feedbacks-wrapper {
  display: flex;
  gap: 20px;
  min-height: 500px;
}

/* Feedback list on the left */
.feedbacks-list {
  width: 300px;
  border-right: 1px solid #ebeef5;
  padding-right: 20px;
  overflow-y: auto;
}

.feedback-item {
  border: 1px solid #ebeef5;
  border-radius: 4px;
  padding: 12px;
  margin-bottom: 10px;
  cursor: pointer;
  transition: all 0.3s;
}

.feedback-item:hover {
  background-color: #f5f7fa;
}

.feedback-item-active {
  background-color: #ecf5ff;
  border-color: #c6e2ff;
}

.feedback-item-replied {
  opacity: 0.8;
}

.feedback-item-header {
  display: flex;
  gap: 8px;
  margin-bottom: 8px;
}

.feedback-user-info {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
  font-size: 13px;
}

.username {
  font-weight: bold;
  color: #606266;
}

.date {
  color: #909399;
}

.feedback-preview {
  font-size: 13px;
  color: #606266;
  line-height: 1.4;
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

.pagination-container {
  margin-top: 20px;
  padding-top: 15px;
  border-top: 1px solid #ebeef5;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.pagination-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-size-select {
  width: 100px;
}

.pagination-controls {
  display: flex;
  justify-content: center;
}

/* Feedback details */
.feedback-detail {
  flex: 1;
  padding-left: 20px;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.detail-header h2 {
  margin: 0;
  font-size: 18px;
  color: #303133;
}

.detail-tags {
  display: flex;
  gap: 10px;
}

.detail-user-info {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.user-avatar {
  margin-right: 15px;
}

.user-detail .username {
  font-size: 15px;
  margin-bottom: 3px;
}

.user-detail .date {
  font-size: 13px;
}

.detail-content {
  margin-bottom: 30px;
}

.detail-content h3 {
  font-size: 16px;
  margin-bottom: 10px;
  color: #303133;
  font-weight: 500;
}

.content-box {
  background: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  line-height: 1.6;
  color: #606266;
  white-space: pre-line;
}

.contact-info {
  margin-top: 15px;
}

.contact-box {
  background: #f0f9eb;
}

/* Reply section */
.reply-section {
  margin-top: 30px;
}

.reply-history {
  margin-bottom: 30px;
}

.reply-box {
  background: #ecf5ff;
  padding: 15px;
  border-radius: 4px;
}

.reply-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 14px;
}

.reply-admin {
  font-weight: bold;
  color: #409eff;
}

.reply-date {
  color: #909399;
}

.reply-content {
  color: #606266;
  line-height: 1.6;
  white-space: pre-line;
}

.empty-detail {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #909399;
}

.empty-detail i {
  font-size: 64px;
  margin-bottom: 15px;
}

.empty-detail p {
  font-size: 16px;
}
</style> 