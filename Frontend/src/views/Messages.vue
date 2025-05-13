<template>
  <MainLayout>
    <div class="messages-page">
      <!-- 页面标题区域 - 带有淡入动画效果 -->
      <div class="page-header" data-aos="fade-down">
        <h2 class="page-title title-water-effect">
          <el-icon><ChatDotRound /></el-icon> Message Center
        </h2>
        <p class="page-description">View your system notifications and interaction messages</p>
      </div>

      <!-- 消息类型选项卡 - 未读/全部消息切换 -->
      <div class="message-tabs" data-aos="fade-up">
        <el-tabs v-model="activeTab" class="message-tabs">
          <!-- 未读消息选项卡 - 带有数量徽章 -->
          <el-tab-pane label="Unread Messages" name="unread">
            <template #label>
              <div class="tab-label">
                <el-icon><Bell /></el-icon>
                Unread Messages
                <el-badge :value="unreadCount" class="tab-badge" v-if="unreadCount > 0" />
              </div>
            </template>
          </el-tab-pane>
          <!-- 全部消息选项卡 -->
          <el-tab-pane label="All Messages" name="all">
            <template #label>
              <div class="tab-label">
                <el-icon><Message /></el-icon>
                All Messages
              </div>
            </template>
          </el-tab-pane>
        </el-tabs>
      </div>

      <!-- 消息列表区域 - 当非加载状态时显示 -->
      <div class="messages-list" v-if="!loading">
        <!-- 循环渲染消息项 - 带有动画效果 -->
        <div v-for="message in displayMessages" 
             :key="message.messageId"
             :class="['message-item', `message-item-${message.messageId}`, { 'unread': !message.isRead }]"
             v-motion="{ 
               initial: { opacity: 0, y: 50 },
               visible: { opacity: 1, y: 0 },
               hover: { scale: 1.01 }
             }">
          <!-- 消息图标 - 统一使用 Bell 图标 -->
          <div class="message-icon">
            <el-icon class="system"> <!-- 统一使用 system 类 -->
              <Bell />
            </el-icon>
          </div>
          <!-- 消息内容区域 - 标题、正文和日期 -->
          <div class="message-content">
            <h3 class="message-title" v-if="message.messageType === 'system_notification'">系统通知</h3>
            <h3 class="message-title" v-if="message.messageType === 'feedback_notification'">反馈通知</h3>
            <h3 class="message-title" v-if="message.messageType === 'appeals_notification'">申诉通知</h3>
            <p class="message-text">{{ message.messageContent }}</p>
            <span class="message-date">{{ message.createAt}}</span>
          </div>
          <!-- 消息操作按钮 - 标记已读和删除 -->
          <div class="message-actions">
            <!-- 标记已读按钮 - 仅对未读消息显示 -->
            <el-button 
              v-if="!message.isRead"
              type="primary" 
              text 
              @click="markAsRead(message)"
              v-motion="{ hover: { scale: 1.1 } }">
              <el-icon><Check /></el-icon>
              Mark as Read
            </el-button>
            <!-- 删除按钮 - 所有消息都可删除 -->
            <el-button 
              type="danger" 
              text 
              @click="deleteMessages(message.messageId)"
              v-motion="{ hover: { scale: 1.1 } }">
              <el-icon><Delete /></el-icon>
              Delete
            </el-button>
          </div>
        </div>

        <!-- 空状态提示 - 当没有消息时显示 -->
        <el-empty 
          v-if="displayMessages.length === 0"
          description="No messages"
          :image-size="200">
        </el-empty>
      </div>

      <!-- 加载状态 - 骨架屏 -->
      <div v-else class="loading-container">
        <el-skeleton :rows="3" animated />
      </div>
    </div>
  </MainLayout>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue'
import MainLayout from '../layouts/MainLayout.vue'
import { Bell, Message, Delete, Check, ChatDotRound } from '@element-plus/icons-vue'
import { useMotion } from '@vueuse/motion'
import gsap from 'gsap'
import AOS from 'aos'
import 'aos/dist/aos.css'
import { getMessageList, markRead, deleteMessage } from '../api/message'
import { ElMessage } from 'element-plus'

// 初始化AOS动画库
onMounted(() => {
  AOS.init({
    duration: 600,  // 动画持续时间
    once: true,     // 动画是否只播放一次
    offset: 50      // 触发动画的偏移量
  })
})

// 当前激活的选项卡 - 默认为未读消息
const activeTab = ref('unread')
// 加载状态标志
const loading = ref(true)

// 消息数据
const messages = ref([])

// 初始化消息列表
const initMessage = async () => {
  loading.value = true
  try {
    const res = await getMessageList()
    if (res && res.data && res.data.records) {
      messages.value = res.data.records
    } else {
      messages.value = []
      console.error('Failed to get message list', res)
    }
  } catch (error) {
    console.error('Error fetching messages:', error)
    messages.value = []
  } finally {
    loading.value = false
  }
  return messages.value
}

// 监听标签页变化，更新消息列表
watch(activeTab, (newValue) => {
  loading.value = true
  initMessage()
}, { immediate: true })

// 计算未读消息数量
const unreadCount = computed(() => {
  return messages.value.filter(message => !message.isRead).length
})

// 根据当前选择的标签页显示相应的消息
const displayMessages = computed(() => {
  if (activeTab.value === 'unread') {
    return messages.value.filter(message => !message.isRead)
  } else {
    return messages.value
  }
})

// 初始化动画和数据加载
onMounted(async () => {
  // 消息列表动画 - 使用GSAP实现滑入效果
  setTimeout(() => {
    gsap.from('.message-item', {
      opacity: 0,      // 初始透明度为0
      y: 20,           // 初始位置向下偏移20px
      duration: 0.5,   // 动画持续0.5秒
      stagger: 0.1,    // 每个项目错开0.1秒开始动画
      ease: 'power2.out', // 缓动函数
      delay: 0.2       // 整体延迟0.2秒开始
    })
  }, 600)
})

// 标记消息为已读的处理函数
const markAsRead = async (message) => {
  try {
    message.isRead = true
    const res = await markRead(message)
    if (res.code === 200) {
      ElMessage.success('Message marked as read successfully')
      
      // 直接更新本地状态
      const index = messages.value.findIndex(m => m.messageId === message.messageId)
      if (index !== -1) {
        messages.value[index].isRead = true
      }
      
      // 重新获取最新列表
      await initMessage()
    } else {
      ElMessage.error(res.message || 'Failed to mark as read')
    }
  } catch (error) {
    console.error('Error marking message as read:', error)
    ElMessage.error('An error occurred while marking the message as read')
  }
}

// 删除消息的处理函数
const deleteMessages = async (messageId) => {
  try {
    const res = await deleteMessage(messageId)
    if (res.code === 200) {
      ElMessage.success('Message deleted successfully')
      
      // 在等待网络请求的同时，先从本地删除该消息
      messages.value = messages.value.filter(message => message.messageId !== messageId)
      
      // 重新获取最新消息列表
      await initMessage()
    } else {
      ElMessage.error(res.message || 'Failed to delete message')
    }
  } catch (error) {
    console.error('Error deleting message:', error)
    ElMessage.error('An error occurred while deleting the message')
  }
}
</script>

<style scoped>
/* 消息中心页面容器样式 */
.messages-page {
  padding: 24px;
  max-width: 1000px;
  margin: 0 auto;
}

/* 页面标题区域样式 */
.page-header {
  margin-bottom: 24px;
}

/* 页面标题文本样式 */
.page-title {
  font-size: 24px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin: 0;
}

/* 页面描述文本样式 */
.page-description {
  color: var(--el-text-color-secondary);
  margin: 8px 0 0 0;
}

/* 消息选项卡样式 */
.message-tabs {
  margin-bottom: 24px;
  background: white;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.tab-label {
  display: flex;
  align-items: center;
  gap: 8px;
}

.tab-badge {
  margin-left: 4px;
}

.messages-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message-item {
  background: white;
  border-radius: 12px;
  padding: 20px;
  display: flex;
  align-items: flex-start;
  gap: 16px;
  transition: all 0.3s ease;
  border: 1px solid var(--el-border-color-lighter);
  position: relative;
  overflow: hidden;
}

.message-item.unread {
  background: var(--el-color-primary-light-9);
  border-color: var(--el-color-primary-light-7);
}

.message-item.unread::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 4px;
  height: 100%;
  background: var(--el-color-primary);
}

.message-icon {
  padding: 12px;
  border-radius: 50%;
  background: var(--el-color-primary-light-9);
}

.message-icon .el-icon {
  font-size: 24px;
  color: var(--el-color-primary);
}

.message-content {
  flex: 1;
}

.message-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--el-text-color-primary);
  margin: 0 0 8px 0;
}

.message-text {
  color: var(--el-text-color-regular);
  margin: 0 0 8px 0;
  line-height: 1.5;
}

.message-date {
  font-size: 12px;
  color: var(--el-text-color-secondary);
}

.message-actions {
  display: flex;
  gap: 12px;
}

.loading-container {
  padding: 24px;
  background: white;
  border-radius: 8px;
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .messages-page {
    padding: 16px;
  }

  .message-item {
    flex-direction: column;
  }

  .message-actions {
    margin-top: 16px;
    justify-content: flex-end;
    width: 100%;
  }
}
</style> 