<template>
  <AdminLayout>
    <div class="admin-users">
      <div class="container">
        <h1 class="page-title">User Management</h1>
        
        <!-- 筛选/搜索区域 -->
        <div class="filter-section">
          <el-input 
            v-model="searchQuery" 
            placeholder="Search by email/username" 
            clearable
            @clear="handleSearch"
            class="search-input"
          >
            <template #append>
              <el-button @click="handleSearch">
                <i class="el-icon-search"></i>
              </el-button>
            </template>
          </el-input>
          
          <el-select 
            v-model="statusFilter" 
            placeholder="Status Filter" 
            @change="handleSearch"
            class="status-filter"
          >
            <el-option label="All" value=""></el-option>
            <el-option label="Active" value="active"></el-option>
            <el-option label="Banned" value="banned"></el-option>
          </el-select>
        </div>
        
        <!-- 用户列表表格 -->
        <div class="users-table">
          <el-table 
            :data="usersList" 
            style="width: 100%"
            v-loading="loading"
            border
            stripe
            row-key="id"
          >
            <el-table-column label="avatar" width="80">
              <template #default="scope">
                <el-avatar :src="scope.row.avatarPath" :size="40"></el-avatar>
              </template>
            </el-table-column>
            
            <el-table-column prop="email" label="email" min-width="180"></el-table-column>
            
            <el-table-column prop="nickname" label="nickname" min-width="120"></el-table-column>
            
            <el-table-column prop="createdAt" label="Registration Date" min-width="140"></el-table-column>
            
            <el-table-column prop="isActivated" label="Status" width="100">
              <template #default="scope">
                <el-tag 
                  :type="scope.row.isActivated  ? 'success' : 'danger'"
                >
                  {{ scope.row.isActivated  ? 'Active' : 'Banned' }}
                </el-tag>
              </template>
            </el-table-column>
            
            <el-table-column label="Actions" width="220">
              <template #default="scope">
                <el-button 
                  size="small" 
                  type="primary" 
                  @click="viewUserDetails(scope.row.userId)"
                >
                  Details
                </el-button>
                
                <el-button 
                  size="small" 
                  :type="scope.row.isActivated  ? 'danger' : 'success'"
                  @click="toggleUserStatus(scope.row)"
                >
                  {{ scope.row.isActivated  ? 'Ban' : 'Unban' }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          
          <!-- 分页 -->
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
        </div>
        
        <!-- 用户详情对话框 -->
        <el-dialog 
          v-model="userDetailsVisible" 
          title="User Details" 
          width="600px"
        >
          <div v-if="selectedUser" class="user-details">
            <div class="user-header">
              <el-avatar :src="selectedUser.avatarPath" :size="80"></el-avatar>
              <div class="user-info">
                <h3>{{ selectedUser.username }}</h3>
                <p>{{ selectedUser.email }}</p>
              </div>
            </div>
            
            <div class="detail-item">
              <span class="item-label">ID:</span>
              <span>{{ selectedUser.userId }}</span>
            </div>
            
            <div class="detail-item">
              <span class="item-label">Registration Date:</span>
              <span>{{ selectedUser.createdAt }}</span>
            </div>

            <div class="detail-item">
              <span class="item-label">Status:</span>
              <el-tag 
                :type="selectedUser.isActivated ? 'success' : 'danger'"
              >
                {{ selectedUser.isActivated  ? 'Active' : 'Banned' }}
              </el-tag>
            </div>
            
            <template v-if="!selectedUser.isActivated ">
              <div class="detail-item">
                <span class="item-label">Ban Reason:</span>
                <span>{{ selectedUser.banReason }}</span>
              </div>
              
              <div class="detail-item">
                <span class="item-label">Ban Date:</span>
                <span>{{ selectedUser.banDate }}</span>
              </div>
            </template>
            
            <!-- <div class="user-logs">
              <h4>Recent Reading Logs</h4>
              <el-table :data="selectedUser.recentLogs" style="width: 100%" size="small">
                <el-table-column prop="title" label="Title" min-width="150"></el-table-column>
                <el-table-column prop="date" label="Date" width="100"></el-table-column>
                <el-table-column label="Actions" width="80">
                  <template #default="scope">
                    <el-button 
                      size="mini" 
                      type="text" 
                      @click="viewLog(scope.row.id)"
                    >
                      View
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div> -->
          </div>
        </el-dialog>
        
        <!-- Ban User Confirmation Dialog -->
        <el-dialog 
          v-model="banDialogVisible" 
          title="Ban User" 
          width="500px"
        >
          <div class="ban-dialog">
            <p>Are you sure you want to ban user <strong>{{ banUser?.username }}</strong>?</p>
            
            <el-form :model="banForm" :rules="banRules" ref="banFormRef" label-width="100px">
              <el-form-item label="Ban Reason" prop="reason">
                <el-input 
                  type="textarea" 
                  v-model="banForm.reason"
                  :rows="3"
                  placeholder="Please enter the reason for the ban"
                ></el-input>
              </el-form-item>
            </el-form>
          </div>
          
          <template #footer>
            <el-button @click="banDialogVisible = false">Cancel</el-button>
            <el-button type="danger" @click="confirmBanUser">Confirm Ban</el-button>
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
import { getUserList, getUser, updateUser } from '../../api/index'

export default {
  name: 'AdminUsers',
  components: {
    AdminLayout
  },
  setup() {
    const router = useRouter()
    const loading = ref(false)
    
    // 搜索和筛选
    const searchQuery = ref('')
    const statusFilter = ref('')
    
    // 分页
    const currentPage = ref(1)
    const pageSize = ref(10)
    const totalItems = ref(0)
    
    // 用户列表
    const usersList = ref([])
    
    // 用户详情
    const userDetailsVisible = ref(false)
    const selectedUser = ref(null)
    
    // 封禁用户
    const banDialogVisible = ref(false)
    const banUser = ref(null)
    const banFormRef = ref(null)
    const banForm = reactive({
      reason: ''
    })
    const banRules = {
      reason: [
        { required: true, message: 'Please enter the reason for the ban', trigger: 'blur' },
        { min: 5, message: 'The ban reason must be at least 5 characters long', trigger: 'blur' }
      ]
    }
    
    // 加载用户列表
    const loadUsersList = () => {
      loading.value = true
      let params = {
        pageNum: currentPage.value,
        pageSize: pageSize.value,
        searchQuery: searchQuery.value,
        statusFilter: statusFilter.value
      }
      getUserList(params).then(res => {
        console.log(res);
        usersList.value = res.data.records
        totalItems.value = res.data.total
        loading.value = false
      });
      
      // 模拟API请求
      // setTimeout(() => {
      //   // Simulated data
      //   usersList.value = [
      //     {
      //       id: 1,
      //       username: 'John Smith',
      //       email: 'johnsmith@example.com',
      //       avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      //       registerDate: '2023-03-15',
      //       lastLogin: '2023-04-08 14:30:25',
      //       status: 'active',
      //       logCount: 12,
      //       recentLogs: [
      //         { id: 101, title: 'The Three-Body Problem - Review', date: '2023-04-05' },
      //         { id: 98, title: 'One Hundred Years of Solitude - Notes', date: '2023-04-01' }
      //       ]
      //     },
      //     {
      //       id: 2,
      //       username: 'Emma Johnson',
      //       email: 'emma.j@example.com',
      //       avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      //       registerDate: '2023-03-10',
      //       lastLogin: '2023-04-07 09:15:40',
      //       status: 'active',
      //       logCount: 8,
      //       recentLogs: [
      //         { id: 95, title: 'Dream of the Red Chamber - Reading Notes', date: '2023-04-02' }
      //       ]
      //     },
      //     {
      //       id: 3,
      //       username: 'Michael Wong',
      //       email: 'michael.w@example.com',
      //       avatar: 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png',
      //       registerDate: '2023-02-20',
      //       lastLogin: '2023-04-01 16:45:10',
      //       status: 'banned',
      //       banReason: 'Violation of community guidelines: posted inappropriate content',
      //       banDate: '2023-04-02 10:30:45',
      //       logCount: 5,
      //       recentLogs: []
      //     }
      //   ]
        
      //   totalItems.value = 3 // 总用户数
      //   loading.value = false
      // }, 500)
    }
    
    // 初始化
    onMounted(() => {
      loadUsersList()
    })
    
    // 搜索和筛选处理
    const handleSearch = () => {
      currentPage.value = 1 // 重置为第一页
      loadUsersList()
    }
    
    // 分页处理
    const handleSizeChange = (size) => {
      pageSize.value = size
      loadUsersList()
    }
    
    const handleCurrentChange = (page) => {
      currentPage.value = page
      loadUsersList()
    }
    
    // 查看用户详情
    const viewUserDetails = (userId) => {
      // 查找用户
      getUser(userId).then(res => {
        selectedUser.value = res.data
        userDetailsVisible.value = true
      })

    }
    
    // 切换用户状态
    const toggleUserStatus = (user) => {
      if (user.isActivated === true) {
        // 封禁用户
        banUser.value = user
        banDialogVisible.value = true
      } else {
        // 解禁用户
        ElMessageBox.confirm(
          `确定要解禁用户 ${user.nickname} 吗？`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        ).then(() => {
          // 模拟API请求
          updateUser({
            userId: user.userId,
            isActivated: true
          }).then(res => {
            ElMessage.success('用户已解禁')
            loadUsersList()
          })
      
        }).catch(() => {})
      }
    }
    
    // 确认封禁用户
    const confirmBanUser = async () => {
      if (!banFormRef.value) return
      
      await banFormRef.value.validate((valid) => {
        if (valid) {
          updateUser({
            userId: banUser.value.userId,
            isActivated: false,
            banReason: banForm.reason,
            banDate: new Date().toLocaleString('zh-CN', { 
              year: 'numeric',
              month: '2-digit',
              day: '2-digit',
              hour: '2-digit',
              minute: '2-digit',
              second: '2-digit',
              hour12: false
            }).replace(/\//g, '-')
        
          }).then(res => {
            ElMessage.success('用户已封禁')
            banDialogVisible.value = false
            loadUsersList()
          })
          // 模拟API请求
          // setTimeout(() => {
          //   // 更新用户状态
          //   if (banUser.value) {
          //     const user = usersList.value.find(u => u.id === banUser.value.id)
          //     if (user) {
          //       user.status = 'banned'
          //       user.banReason = banForm.reason
          //       user.banDate = new Date().toLocaleString()
          //     }
          //   }
            
          //   banDialogVisible.value = false
          //   banForm.reason = ''
          //   ElMessage.success('用户已被封禁')
          // }, 500)
        }
      })
    }
    
    // 查看用户日志
    const viewLog = (logId) => {
      router.push(`/logs/${logId}`)
    }
    
    return {
      loading,
      searchQuery,
      statusFilter,
      currentPage,
      pageSize,
      totalItems,
      usersList,
      userDetailsVisible,
      selectedUser,
      banDialogVisible,
      banUser,
      banFormRef,
      banForm,
      banRules,
      handleSearch,
      handleSizeChange,
      handleCurrentChange,
      viewUserDetails,
      toggleUserStatus,
      confirmBanUser,
      viewLog
    }
  }
}
</script>

<style scoped>
.admin-users {
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

/* 筛选区域 */
.filter-section {
  display: flex;
  margin-bottom: 20px;
  gap: 15px;
}

.search-input {
  max-width: 300px;
}

.status-filter {
  width: 120px;
}

/* 用户表格 */
.users-table {
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

/* 用户详情 */
.user-details {
  padding: 10px;
}

.user-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}

.user-info {
  margin-left: 20px;
}

.user-info h3 {
  margin: 0 0 5px 0;
  font-size: 18px;
  color: #303133;
}

.user-info p {
  margin: 0;
  color: #606266;
}

.detail-item {
  margin-bottom: 10px;
  line-height: 24px;
}

.item-label {
  font-weight: bold;
  color: #606266;
  display: inline-block;
  width: 100px;
}

.user-logs {
  margin-top: 20px;
}

.user-logs h4 {
  margin-bottom: 10px;
  font-size: 16px;
  color: #303133;
}

/* 封禁对话框 */
.ban-dialog p {
  margin-bottom: 15px;
}
</style> 