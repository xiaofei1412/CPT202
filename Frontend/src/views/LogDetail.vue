<template>
  <MainLayout>
    <div class="log-detail-page">
      <!-- 顶部操作栏 - 返回和编辑/删除按钮 -->
      <div class="action-bar" >
        <el-button @click="goBack" icon="ArrowLeft">Return</el-button>
        
        <!-- 右侧操作按钮区 - 仅在加载完成且有日志数据时显示 -->
        <div class="right-actions" v-if="!loading && log">
          <!-- 编辑和删除按钮 - 用户可以编辑和删除自己的日志 -->
          <el-button v-if="userId === logDetail.userId" type="primary" plain @click="editLog">
            <el-icon><Edit /></el-icon> Edit
          </el-button>
          <el-button v-if="userId === logDetail.userId"type="danger" plain @click="confirmDelete">
            <el-icon><Delete /></el-icon> Delete
          </el-button>
          
          <!-- 标记违规按钮 - 仅管理员可见且状态不为已拒绝 -->
          <el-button 
            v-if="userStore.isAdmin && log.status !== 'rejected'" 
            type="warning" 
            plain 
            @click="rejectDialog.visible = true"
          >
            <el-icon><Warning /></el-icon> Mark as Violation
          </el-button>
        </div>
      </div>
      
      <!-- 加载状态显示 - 骨架屏 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="15" animated />
      </div>
      
      <!-- 主要内容区域 - 仅在有日志数据时显示 -->
      <template v-else-if="log">
        <!-- 日志状态提醒 - 审核中状态 -->
        <el-alert
          v-if="log.status === 'pending'"
          title="This log is under review, it will be visible to other users after approval"
          type="warning"
          show-icon
          :closable="false"
          class="status-alert"
        />
        
        <!-- 日志状态提醒 - 被拒绝状态 -->
        <el-alert
          v-if="log.status === 'rejected'"
          :title="`This log did not pass review: ${log.rejectReason || 'Violated platform rules'}`"
          type="error"
          show-icon
          :closable="false"
          class="status-alert"
        />
        
        <!-- 日志标题和元数据区域 -->
        <div class="log-header">
          <h1 class="log-title">{{ logDetail.title }}</h1>
          
          <!-- 日志元数据信息 - 作者、日期、时长、状态 -->
          <div class="log-meta">
            <!-- 作者信息 -->
            <span class="meta-item">
              <el-icon><User /></el-icon> 
              Author: {{ logDetail.author }}
            </span>
            <!-- 阅读日期 -->
            <span class="meta-item">
              <el-icon><Calendar /></el-icon>
              Reading Date: {{ logDetail.readingDate }}
            </span>
            <!-- 阅读时长 -->
            <span class="meta-item">
              <el-icon><Timer /></el-icon>
              Reading Duration: {{ logDetail.readingTime }} minutes
            </span>
            <!-- 日志状态 -->
            <span class="meta-item">
              <el-icon><InfoFilled /></el-icon>
              Status: 
              <el-tag :type="getStatusType(logDetail.status)" size="small">
                <!-- {{ getStatusText(logDetail.status) }} -->
                  {{ logDetail.status }}
              </el-tag>
            </span>
          </div>
        </div>
        
        <!-- 日志内容卡片 - 悬停阴影效果 -->
        <el-card class="log-content-card" shadow="hover">
          <div class="log-content" v-html="logDetail.personalNotes"></div>
        </el-card>
      </template>
      
      <!-- 数据未找到提示 - 当日志不存在时显示 -->
      <el-empty 
        v-else 
        description="Reading log not found" 
        class="empty-container"
      >
        <el-button type="primary" @click="goBack">Return to Log List</el-button>
      </el-empty>
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
    
    <!-- 标记违规对话框 - 管理员功能 -->
    <el-dialog
      v-model="rejectDialog.visible"
      title="Mark as Violation"
      width="40%"
      :close-on-click-modal="false"
    >
      <el-form :model="rejectDialog.form" label-width="100px">
        <!-- 违规原因选择 -->
        <el-form-item label="Violation Reason" required>
          <el-select v-model="rejectDialog.form.reason" placeholder="Please select a violation reason" style="width: 100%">
            <el-option label="Content does not meet platform standards" value="Content does not meet platform standards" />
            <el-option label="Contains sensitive content" value="Contains sensitive content" />
            <el-option label="Suspected plagiarism/copyright infringement" value="Suspected plagiarism/copyright infringement" />
            <el-option label="Other reasons" value="Other reasons" />
          </el-select>
        </el-form-item>
        
        <!-- 详细描述文本域 -->
        <el-form-item label="Detailed Description">
          <el-input 
            v-model="rejectDialog.form.detail" 
            type="textarea" 
            rows="4" 
            placeholder="Please enter detailed description (optional)"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="rejectDialog.visible = false">Cancel</el-button>
          <el-button 
            type="danger" 
            @click="rejectLog"
            :loading="rejectDialog.loading"
          >
            Confirm Mark as Violation
          </el-button>
        </span>
      </template>
    </el-dialog>
  </MainLayout>
</template>

<script setup>
import { ref, computed, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useLogStore } from '../store/log'
import { useUserStore } from '../store/user'
import MainLayout from '../layouts/MainLayout.vue'
import { ArrowLeft, Edit, Delete, User, Calendar, Timer, Warning, InfoFilled } from '@element-plus/icons-vue'
import gsap from 'gsap'
import AOS from 'aos'
import 'aos/dist/aos.css'
import { useMotion } from '@vueuse/motion'
import { getLogDetail,deleteLog } from '../api/home'

// 路由相关
const route = useRoute()
const router = useRouter()

const logId = route.params.id

const userId = ref(JSON.parse(localStorage.getItem('user')).userId)
console.log(userId.value);

const logDetail = ref(null)
getLogDetail(logId).then(res=>{
  logDetail.value = res.data
  console.log(logDetail.value);
})

// 状态管理
const logStore = useLogStore()
const userStore = useUserStore()

// 组件状态
const loading = ref(true)   // 加载状态
const log = ref(null)       // 日志数据

// 删除对话框状态
const deleteDialog = reactive({
  visible: false,  // 对话框可见性
  loading: false   // 删除操作加载状态
})

// 标记违规对话框状态
const rejectDialog = reactive({
  visible: false,  // 对话框可见性
  loading: false,  // 标记操作加载状态
  form: {
    reason: '',    // 违规原因
    detail: ''     // 详细描述
  }
})

// 计算属性 - 格式化内容（处理换行等）
const formattedContent = computed(() => {
  if (!log.value || !log.value.content) return ''
  
  // 将换行符转换为HTML的<br>标签
  return log.value.content.replace(/\n/g, '<br>')
})

// 获取状态显示类型 - 用于标签颜色
const getStatusType = (status) => {
  const map = {
    approved: 'success', // 已批准 - 绿色
    pending: 'warning',  // 审核中 - 黄色
    rejected: 'danger'   // 已拒绝 - 红色
  }
  return map[status] || 'info' // 默认为信息类型 - 蓝色
}

// 获取状态显示文本 - 用户友好的状态描述
const getStatusText = (status) => {
  const map = {
    approved: 'Approved',     // 已批准
    pending: 'Under Review',  // 审核中
    rejected: 'Not Approved'  // 未批准
  }
  return map[status] || 'Unknown' // 默认为未知状态
}

// 返回上一页 - 导航到日志列表
const goBack = () => {
  router.push('/logs')
}

// 编辑日志 - 导航到编辑页面
const editLog = () => {
  router.push(`/logs/edit/${route.params.id}`) // 使用路由参数构建编辑URL
}

// 确认删除 - 显示删除确认对话框
const confirmDelete = () => {
  deleteDialog.visible = true
}

const deleteLogs = () => {
  deleteLog(route.params.id).then(res=>{
    console.log(res);
    if(res.code === 200){
      ElMessage.success('Log deleted successfully')
      router.push('/logs')
    }else{
      ElMessage.error(res.message)
    }
  })
}
// 删除日志 - 执行删除操作
const deleteLog1 = async () => {
  try {
    deleteDialog.loading = true // 设置加载状态
    await logStore.deleteLog(route.params.id) // 调用store中的删除方法
    
    // 显示成功消息
    ElMessage({
      type: 'success',
      message: 'Log deleted successfully'
    })
    
    // 删除成功后返回日志列表页
    router.push('/logs')
  } catch (error) {
    // 删除失败显示错误消息
    ElMessage.error('Deletion failed, please try again later')
    console.error(error)
  } finally {
    // 重置对话框状态
    deleteDialog.loading = false
    deleteDialog.visible = false
  }
}

// 标记为违规内容 - 管理员功能
const rejectLog = async () => {
  // 验证是否选择了违规原因
  if (!rejectDialog.form.reason) {
    ElMessage.warning('Please select a violation reason')
    return
  }
  
  try {
    rejectDialog.loading = true // 设置加载状态
    
    // 构建完整的违规原因描述
    const rejectReason = rejectDialog.form.detail
      ? `${rejectDialog.form.reason}: ${rejectDialog.form.detail}` // 包含详细描述
      : rejectDialog.form.reason // 仅使用选择的原因
    
    // 更新日志状态为已拒绝
    await logStore.updateLog(route.params.id, {
      status: 'rejected',
      rejectReason
    })
    
    // 重新获取日志详情，确保显示最新状态
    await fetchLogDetail()
    
    // 显示成功消息
    ElMessage({
      type: 'success',
      message: 'Marked as Violation Content'
    })
    
    // 关闭对话框
    rejectDialog.visible = false
  } catch (error) {
    // 操作失败显示错误消息
    ElMessage.error('Operation failed, please try again later')
    console.error(error)
  } finally {
    // 重置加载状态
    rejectDialog.loading = false
  }
}

// 获取日志详情数据
const fetchLogDetail = async () => {
  loading.value = true // 设置加载状态
  
  try {
    // 调用store中的获取详情方法
    const result = await logStore.fetchLogDetail(route.params.id)
    log.value = result // 更新日志数据
  } catch (error) {
    // 获取详情失败显示错误消息
    ElMessage.error('Failed to get log details')
    console.error(error)
  } finally {
    // 完成加载
    loading.value = false
  }
}

// 初始化组件
onMounted(() => {
  fetchLogDetail() // 获取日志详情数据
  
  // 初始化AOS动画库
  AOS.init({
    duration: 800, // 动画持续时间
    once: true     // 动画只执行一次
  })
})

// 内容卡片动画引用
const contentCard = ref(null)
// VueUse Motion动画配置
const { motion: contentMotion } = useMotion(contentCard, {
  initial: { opacity: 0, y: 20 },  // 初始状态：透明并向下偏移
  enter: { opacity: 1, y: 0, transition: { duration: 600 } } // 进入状态：完全不透明且回到原位
})

// GSAP动画效果
onMounted(() => {
  // 标题动画 - 从上方滑入
  gsap.from('.log-title', {
    opacity: 0,     // 初始透明
    y: -20,         // 从上方偏移
    duration: 0.8,  // 动画持续0.8秒
    ease: 'power2.out' // 缓动函数
  })

  // 元数据信息动画 - 依次从左侧滑入
  gsap.from('.meta-item', {
    opacity: 0,     // 初始透明
    x: -20,         // 从左侧偏移
    duration: 0.5,  // 动画持续0.5秒
    stagger: 0.1,   // 每项延迟0.1秒
    ease: 'power2.out', // 缓动函数
    delay: 0.3      // 整体延迟0.3秒开始
  })

  // 内容动画 - 从下方淡入
  gsap.from('.log-content', {
    opacity: 0,     // 初始透明
    y: 20,          // 从下方偏移
    duration: 0.8,  // 动画持续0.8秒
    ease: 'power2.out', // 缓动函数
    delay: 0.5      // 延迟0.5秒开始
  })
})
</script>

<style scoped>
/* 日志详情页面容器样式 */
.log-detail-page {
  padding-bottom: 40px;
}

/* 顶部操作栏 - 包含返回按钮和操作按钮 */
.action-bar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 24px;
}

/* 右侧操作按钮组 */
.right-actions {
  display: flex;
  gap: 10px;
}

/* 加载状态容器 */
.loading-container {
  padding: 20px 0;
}

/* 状态提醒警告框 */
.status-alert {
  margin-bottom: 20px;
}

/* 日志标题和元数据头部区域 */
.log-header {
  margin-bottom: 24px;
}

/* 日志标题样式 */
.log-title {
  font-size: 28px;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 16px;
}

/* 日志元数据信息区域 - 包含作者、日期等 */
.log-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  color: var(--text-secondary);
}

/* 元数据单项样式 - 图标和文本的容器 */
.meta-item {
  display: flex;
  align-items: center;
  gap: 5px;
}

/* 日志内容卡片样式 */
.log-content-card {
  margin-bottom: 30px;
}

/* 日志内容文本区域样式 */
.log-content {
  padding: 10px;
  min-height: 200px;
  line-height: 1.8;
  color: var(--text-primary);
  white-space: pre-wrap; /* 保留空白符和换行符 */
}

/* 空状态容器样式 */
.empty-container {
  padding: 60px 0;
}

/* 响应式布局调整 */
@media (max-width: 768px) {
  /* 移动设备下操作栏垂直排列 */
  .action-bar {
    flex-direction: column;
    gap: 15px;
  }
  
  /* 移动设备下右侧按钮占满宽度并右对齐 */
  .right-actions {
    width: 100%;
    justify-content: flex-end;
  }
  
  /* 移动设备下元数据信息垂直排列 */
  .log-meta {
    flex-direction: column;
    gap: 10px;
  }
}

/* 动画和交互效果 */
/* 返回按钮样式 */
.back-button {
  margin-bottom: 20px;
}

/* 元数据项过渡动画效果 */
.meta-item {
  transition: all 0.3s ease;
}

/* 元数据项悬停效果 - 轻微右移 */
.meta-item:hover {
  transform: translateX(5px);
}

/* 内容卡片过渡动画效果 */
.content-card {
  transition: all 0.3s ease;
}

/* 内容卡片悬停效果 - 向上浮动并增强阴影 */
.content-card:hover {
  transform: translateY(-5px);
  box-shadow: var(--el-box-shadow-light);
}

/* 按钮过渡动画效果 */
.el-button {
  transition: all 0.3s ease;
}

/* 按钮悬停效果 - 轻微上浮 */
.el-button:hover {
  transform: translateY(-2px);
}
</style> 